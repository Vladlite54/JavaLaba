package Net;

import cars.Car;
import cars.PassengerCar;
import cars.Truck;
import elements.Habitat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Client {
    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private String addr; // ip адрес клиента
    private int port; // порт соединения
    private Date time;
    private String dtime;
    private SimpleDateFormat dt1;
    private int clientID;
    private static Timer _updater;
    public static ClientFrame clientFrame;
    private Habitat hab;
    private boolean isRemoved = false;

    /**
     * для создания необходимо принять адрес и номер порта
     *
     * @param addr
     * @param port
     */

    public Client(String addr, int port, Habitat habitat) {
        this.addr = addr;
        this.port = port;
        try {
            this.socket = new Socket(addr, port);
        } catch (IOException e) {
            System.err.println("Socket failed");
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            //this.pressNickname(); // перед началом необходимо спросит имя
            setID();
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
            clientFrame = new ClientFrame();
            hab = habitat;
            //clientFrame.setVisible(true);
            _updater = new Timer(1000, new ActionListener() {
                @Override
                synchronized public void actionPerformed(ActionEvent actionEvent) {
                    sendMessage("get_connections\n");
                    if(isRemoved) {
                        ClientFrame.updateList();
                        ClientFrame.removeFromModel();
                        isRemoved = false;
                    } else ClientFrame.updateList();

                }
            });
            _updater.start();
            ClientFrame.swapButton.addActionListener(actionEvent -> sendObjects());

        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            Client.this.downService();
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    public void setID() {
        try {
            clientID = Integer.parseInt(in.readLine());
            System.out.println("Your ID: " + clientID + "\n");
        } catch (IOException ignored){
        }
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                Server.removeSocket(socket);
            }
        } catch (IOException ignored) {}
    }

    public void sendMessage(String message) {
//        System.out.println(message);
//        ClientFrame.clientArea.append(message);
        try {
            out.write(message);
            out.flush();
        } catch (IOException ignore) {
        }
    }

    public void sendObjects() {
        String type = ClientFrame.typeBox.getSelectedItem().toString();
        String to = ClientFrame.idBox.getSelectedItem().toString();
        String strCars = makeSendList(type);
        sendMessage("send " + type + " " + clientID + " " + to + " " + strCars + "\n");


    }

    public void getCars(String str) {
        System.out.println(str.split(" ")[1] + " aaa " + str.split(" ")[3]);
        String sendList = makeSendList(str.split(" ")[1]);
        updateHabitat(str.split(" ")[1], str.split(" ")[3]);
        sendMessage("swapback " + str.split(" ")[1] + " " + str.split(" ")[2] + " " + sendList + "\n");
    }

    public String makeSendList(String type) {
        ArrayList<Car> cars = hab.getCarList();
        ArrayList<Car> sendCars = new ArrayList<>();
        if (type.equals("Cars")) {
            for (Car elem : cars) {
                if (elem instanceof PassengerCar) {
                    sendCars.add(elem);
                }
            }
        } else if (type.equals("Trucks")) {
            for (Car elem : cars) {
                if (elem instanceof Truck) {
                    sendCars.add(elem);
                }
            }
        }
        return listToString(sendCars);
    }

    public void updateHabitat(String type, String sList) {
        ArrayList<Car> list = stringToList(sList);
        if (type.equals("Cars")) {
            hab.updateCarListP(list);
        } else {
            hab.updateCarListT(list);
        }
    }

    public String listToString(ArrayList<Car> cars) {
        StringBuilder str = new StringBuilder();
        for (Car elem : cars) {
            str.append(elem.getID() + ":" + elem.getBirthTime() + ":" + elem.get_xCoordinate() + ":" + elem.get_yCoordinate() + ":");
            if (elem instanceof PassengerCar) {
                str.append("P");
            } else {
                str.append("T");
            }
        }
        return str.toString();
    }

    public ArrayList<Car> stringToList(String str) {
        ArrayList<Car> carList = new ArrayList<>();
        int i = 0;
        String sID, sBirthTime, sX, sY;
        int ID, BirthTime, X, Y;
        while (i < str.length()) {
            sID = sBirthTime = sX = sY = "";
            while (str.charAt(i) != ':') sID += str.charAt(i++);
            ID = Integer.parseInt(sID);
            i++;
            while (str.charAt(i) != ':') sBirthTime += str.charAt(i++);
            BirthTime = Integer.parseInt(sBirthTime);
            i++;
            while (str.charAt(i) != ':') sX += str.charAt(i++);
            X = Integer.parseInt(sX);
            i++;
            while (str.charAt(i) != ':') sY += str.charAt(i++);
            Y = Integer.parseInt(sY);
            i++;
            if (str.charAt(i) == 'P') {
                PassengerCar pCar = new PassengerCar(0, 0, BirthTime);
                pCar.set_x(X);
                pCar.set_y(Y);
                pCar.setID(ID);
                carList.add(pCar);
            } else {
                Truck truck = new Truck(0, 0, BirthTime);
                truck.set_x(X);
                truck.set_y(Y);
                truck.setID(ID);
                carList.add(truck);
            }
            i++;
        }
        return carList;
    }

    public void disableClient() {
        sendMessage("remove " + clientID + " from list\n");

    }

    // нить чтения сообщений с сервера
    private class ReadMsg extends Thread {
        @Override
        public void run() {

            String str;
            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера
                    if (str.equals("stop")) {
                        Client.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    } else if (str.split(" ")[0].equals("clients")) {
                        ClientFrame.updateClients(str);
                    } else if(str.equals("aboba")) {
                        System.out.println("wtf");
                    } else if(str.split(" ")[0].equals("remove")) {
                        //ClientFrame.updateList();
                        //ClientFrame.removeFromModel();
                        isRemoved = true;
                    } else if(str.split(" ")[0].equals("swap")) {
                        getCars(str);
                    } else if (str.split(" ")[0].equals("getback")) {
                        System.out.println( str.split(" ")[1] + " BBB " + str.split(" ")[2]);
                        updateHabitat(str.split(" ")[1], str.split(" ")[2]);
                    }
                    System.out.println(str); // пишем сообщение с сервера на консоль
                }
            } catch (IOException e) {
                Client.this.downService();
            }
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {

        @Override
        public void run() {
            while (true) {
                String userWord;
                try {
                    time = new Date(); // текущая дата
                    dt1 = new SimpleDateFormat("HH:mm:ss"); // берем только время до секунд
                    dtime = dt1.format(time); // время
                    userWord = inputUser.readLine(); // сообщения с консоли
                    if (userWord.equals("stop")) {
                        out.write("stop" + "\n");
                        Client.this.downService(); // харакири
                        break; // выходим из цикла если пришло "stop"
                    } else {
                        out.write(userWord + "\n"); // отправляем на сервер
                    }
                    out.flush(); // чистим
                } catch (IOException e) {
                    Client.this.downService(); // в случае исключения тоже харакири

                }

            }
        }
    }
}

