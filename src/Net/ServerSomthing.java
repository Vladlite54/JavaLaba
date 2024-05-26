package Net;

import cars.Car;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;

class ServerSomthing extends Thread {

    private Socket socket; // сокет, через который сервер общается с клиентом,
    // кроме него - клиент и сервер никак не связаны
    private int socketID;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток завписи в сокет


    /**
     * для общения с клиентом необходим сокет (адресные данные)
     * @param socket
     * @throws IOException
     */

    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        // если потоку ввода/вывода приведут к генерированию искдючения, оно проброситься дальше
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start(); // вызываем run()
    }
    @Override
    public void run() {
        String word;
        try {
            // первое сообщение отправленное сюда - это никнейм
            socketID = Server.getNewID();
            try {
                out.write(socketID + "\n");
                out.flush(); // flush() нужен для выталкивания оставшихся данных
                // если такие есть, и очистки потока для дьнейших нужд
            } catch (IOException ignored) {}
            try {
                while (true) {
                    word = in.readLine();
                    if (word == null) {
                        Server.removeSocket(socket);
                        break;
                    }
                    if(word.equals("stop")) {
                        this.downService(); // харакири
                        break; // если пришла пустая строка - выходим из цикла прослушки
                    } else if (word.equals("get_connections")) {
                        System.out.println(getStringConnectionList());
                        sendToClientFrame();
                    } else if (word.split(" ")[0].equals("send")) {
                        for (ServerSomthing vr : Server.serverList) {
                            if (Integer.toString(vr.socketID).equals(word.split(" ")[3])) {
                                vr.send("swap " + word.split(" ")[1] + " " + word.split(" ")[2] + " "
                                + word.split(" ")[4]);
                            }
                        }
                    } else if (word.split(" ")[0].equals("swapback")) {
                        for (ServerSomthing vr : Server.serverList) {
                            if (Integer.toString(vr.socketID).equals(word.split(" ")[2])) {
                                vr.send("getback " + word.split(" ")[1] + " " + word.split(" ")[3]);
                            }
                        }
                    }
                    System.out.println("Echoing: " + word);
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }
                }
            } catch (NullPointerException ignored) {

            }


        } catch (IOException e) {
            this.downService();
        }
    }

    public Socket getSocket() { return socket; }
    public int getSocketID() { return socketID; }


    private void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }



    private void downService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }

    @Override
    public String toString(){
        return "" + socket.getRemoteSocketAddress() + "ID:" + socketID;
    }

    public static String getStringConnectionList() {
        String list = "";
        for (ServerSomthing vr : Server.serverList) {
            list += vr.toString() + " ";
        }
        return list;
    }

    private void sendToClientFrame() {
        send("clients " + getStringConnectionList());
    }

}



