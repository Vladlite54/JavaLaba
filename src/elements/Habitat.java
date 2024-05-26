package elements;

import Net.ClientFrame;
import cars.*;
import ai.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeSet;
import Net.*;

public class Habitat extends JFrame {
    // variables
    private final double P1, P2; // probabilities
    private final int N1, N2; // frequency
    private double _passengerProb, _truckProb;
    private  int _passengerFreq, _truckFreq, _carMaxLifetime, _truckMaxLifetime;
    private ArrayList<Car> _cars; // car list
    private TreeSet<Integer> _idSet;
    private HashMap<Integer, Car> _carMap;
    private final Timer _timer; // program timer
    PassengerAI _passengerAI;
    TruckAI _truckAI;
    private int _time, messageAnswer;
    TimePanel timePanel;
    MainPanel mainPanel;
    JPanel mainMenu, workspace, panelCont;
    UserPanel userPanel;
    private boolean _isDeserialized, _isRunning, _configLoaded;
    boolean _timeShown;
    public static String ipAddr = "localhost";
    public static int port = 8080;
    public static Client client;

    // initialisation
    {
        P1 = 0.6;
        P2 = 0.4;
        N1 = 2;
        N2 = 3;
        _cars = new ArrayList<Car>();
        _idSet = new TreeSet<Integer>();
        _carMap = new HashMap<Integer,Car>();

    }
    public Habitat() {
        // frame
        super("Simulation");
        super.setSize(1250, 1000);
        super.setLocationRelativeTo(null);
        super.getContentPane().setBackground(Color.BLACK);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());

        MenuBar menu = new MenuBar(this);
        super.setJMenuBar(menu);
        menu.setVisible(false);
        _passengerProb = P1;
        _truckProb = P2;
        _passengerFreq = N1;
        _truckFreq = N2;
        _carMaxLifetime = 10;
        _truckMaxLifetime = 12;
        client = new Client(ipAddr, port, this);

        ////CARD LAYOUT////
        panelCont = new JPanel();
        CardLayout cl = new CardLayout();
        panelCont.setLayout(cl);

        mainMenu = new JPanel();
        mainMenu.setLayout(null);
        JLabel welcome = new JLabel("Simulation");
        welcome.setFont(new Font("Tahoma", Font.BOLD, 72));
        welcome.setBounds(425, 10, 500, 100);
        mainMenu.add(welcome);
        JButton butt = new JButton("PLAY");
        butt.setFocusable(false);
        butt.setBounds(500, 150, 300, 100);
        butt.setFont(new Font("Tahoma", Font.BOLD, 36));
        mainMenu.add(butt);

        JButton exitButt = new JButton("EXIT");
        exitButt.setFocusable(false);
        exitButt.setBounds(500, 300, 300, 100);
        exitButt.setFont(new Font("Tahoma", Font.BOLD, 36));
        mainMenu.add(exitButt);

        workspace = new JPanel();
        workspace.setLayout(new BorderLayout());

        panelCont.add(mainMenu, "1");
        panelCont.add(workspace, "2");

        cl.show(panelCont, "1");

        butt.addActionListener(actionEvent -> {
            cl.show(panelCont, "2");
            menu.setVisible(true);
            loadBootConfig();
        });

        exitButt.addActionListener(actionEvent -> {
            super.dispose();
            System.exit(0);
        });

        super.add(panelCont);

        ////TIME PANEL////
        timePanel = new TimePanel(this);
        timePanel.setVisible(true);
        workspace.add(timePanel, BorderLayout.NORTH);

        ////MAIN PANEL////
        mainPanel = new MainPanel(this);
        workspace.add(mainPanel, BorderLayout.CENTER);

        ////USER PANEL///
        userPanel = new UserPanel(this);
        workspace.add(userPanel, BorderLayout.EAST);

        JButton back = new JButton("Back to main menu");
        back.setFocusable(false);
        back.setFont(new Font("Tahoma", Font.BOLD, 18));
        back.addActionListener(actionEvent -> {
            stopSimulation();
            saveBootConfig();
            menu.setVisible(false);
            cl.show(panelCont, "1");
            userPanel.startButton.setEnabled(true);
            userPanel.stopButton.setEnabled(false);
            mainPanel.repaint();
        });
        userPanel.add(back);
        _passengerAI = new PassengerAI(_cars, mainPanel);
        _truckAI = new TruckAI(_cars, mainPanel);
        _passengerAI.start();
        _truckAI.start();
        pauseAI();


        _timer = new Timer(1000, actionEvent -> {
            _time++;
            timePanel._timeLabel.setText("Time in seconds: " + _time);
            Update(_time);
            repaint();
            setProbabilities();
            setPriority();
            checkLife();
        });

        JPanel pn = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Car car : _cars) {
                    car.paintCar(g);
                    //car.setLifeTime(car.getLifeTime() + 1);
                }
            }
        };


        mainPanel.add(pn, BorderLayout.CENTER);

        super.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_B) {
                    startSimulation();
                    userPanel.startButton.setEnabled(false);
                    userPanel.stopButton.setEnabled(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_E) {
                    stopSimulation();
                    userPanel.stopButton.setEnabled(false);
                    userPanel.startButton.setEnabled(true);
                }
                if (e.getKeyCode() == KeyEvent.VK_T) {
                    timePanel.setVisible(_timeShown);
                    _timeShown = !_timeShown;
                }
            }
        });

        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveBootConfig();
                client.disableClient();
            }
        });

    }

    ////METHODS////

    public int getTime() { return _time; }

    ////CONFIGURATION////
    public double getPassengerProb() { return _passengerProb; }
    public double getTruckProb() { return _truckProb; }
    public void setPassengerProb(double prob) {
        if (prob < 0 || prob > 1) {
            _passengerProb = P1;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _passengerProb = prob;
        }
    }
    public void setTruckProb(double prob) {
        if (prob < 0 || prob > 1) {
            _truckProb = P2;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _truckProb = prob;
        }
    }
    public void setProbabilities() {
        _passengerProb = Double.parseDouble((String) Objects.requireNonNull(userPanel.carProbBox.getSelectedItem()));
        _truckProb = Double.parseDouble((String) Objects.requireNonNull(userPanel.truckProbBox.getSelectedItem()));
    }

    public int getPassengerFreq() { return _passengerFreq; }
    public int getTruckFreq() { return _truckFreq;}
    public void setPassengerFreq(int freq) {
        if (freq <= 0) {
            _passengerFreq = N1;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _passengerFreq = freq;
        }
    }
    public void setTruckFreq(int freq) {
        if (freq <= 0) {
            _truckFreq = N2;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _truckFreq = freq;
        }
    }
    public void setFrequencies(int n1, int n2) {
        if (n1 <= 0 || n2 <= 0) {
            _passengerFreq = N1;
            _truckFreq = N2;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        else {
            _passengerFreq = n1;
            _truckFreq = n2;
        }

    }

    public int getCarMaxLifetime() { return _carMaxLifetime; }
    public int getTruckMaxLifetime() { return _truckMaxLifetime; }
    public void setCarMaxLifetime(int lifetime) {
        if (lifetime <= 0) {
            _carMaxLifetime = 10;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _carMaxLifetime = lifetime;
        }
    }
    public void setTruckMaxLifetime(int lifetime) {
        if (lifetime <= 0) {
            _truckMaxLifetime = 12;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        } else {
            _truckMaxLifetime = lifetime;
        }
    }
    public void setLifetimes(int lifetime1, int lifetime2) {
        if (lifetime1 <= 0 || lifetime2 <= 0) {
            _carMaxLifetime = 10;
            _truckMaxLifetime = 12;
            JOptionPane.showMessageDialog(null,
                    "Incorrect values!\nThe default values are set",
                    "ERROR", JOptionPane.WARNING_MESSAGE);
        }
        else {
            _carMaxLifetime = lifetime1;
            _truckMaxLifetime = lifetime2;
        }
    }


    ////ACTION////
    public void startSimulation() {
        _isRunning = true;
        if (!_cars.isEmpty() && !_isDeserialized && !_configLoaded) {
            _cars.clear();
            _time = 0;
            PassengerCar.setCountPassenger(0);
            Truck.setCountTruck(0);
            _carMap.clear();
            _idSet.clear();
            _timer.start();
            _truckAI.resumeAI();
            _passengerAI.resumeAI();
        } else {
            _isDeserialized = false;
            _configLoaded = false;
            //_carMap.clear();
            //_idSet.clear();
            _timer.start();
            resumeAI();
            _truckAI.resumeAI();
            _passengerAI.resumeAI();
        }
//        _carMap.clear();
//        _idSet.clear();
//        _timer.start();
//        _truckAI.resumeAI();
//        _passengerAI.resumeAI();

    }

    public void stopSimulation() {
        _isRunning = false;
        if (userPanel.checkBox.isSelected()) {
            _timer.stop();
            pauseAI();
            showInfo();
            if (messageAnswer == 2 || messageAnswer == -1) {
                userPanel.stopButton.setEnabled(true);
                userPanel.startButton.setEnabled(false);
                _timer.start();
                resumeAI();
                _isRunning = true;

            }
        }
        else {
            _timer.stop();
            pauseAI();
            //_time = 0;
            //_cars.clear();
            //PassengerCar.setCountPassenger(0);
            //Truck.setCountTruck(0);
        }

    }

    public void pauseSimulation() {
        _timer.stop();
        pauseAI();
    }

    public void resumeSimulation() {
        _timer.start();
        resumeAI();
    }

    public int get_totalTruck() {
        return Truck.getCountTruck();
    }

    public  int get_totalPassenger() {
        return PassengerCar.getCountPassenger();
    }

    public void showInfo() {
        String info = "Total objects: " + Integer.toString(get_totalTruck() + get_totalPassenger()) +
                "\nTotal trucks: " + get_totalTruck() + "\nTotal Passenger cars: " + get_totalPassenger();
        messageAnswer = JOptionPane.showOptionDialog(null, info, "Information",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.INFORMATION_MESSAGE, null, null, 0);

    }

    public void showAliveObjects() {
        _timer.stop();
        AliveMessage currentObjects = new AliveMessage(_carMap);
        currentObjects.OkButton.addActionListener(actionEvent -> {
            if (userPanel.buttonSelected.equals("Stop")) {
                currentObjects.setVisible(false);
            } else {
                _timer.start();
                currentObjects.setVisible(false);
            }
        });
        currentObjects.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        currentObjects.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (userPanel.buttonSelected.equals("Stop")) {
                    currentObjects.setVisible(false);
                } else {
                    _timer.start();
                    currentObjects.setVisible(false);
                }
            }
        });
    }

    public void showConsole() {
        ConsoleFrame console = new ConsoleFrame(this);
        console.setVisible(true);
    }

    public void showClients() {
        Client.clientFrame.setVisible(true);
    }

    ////GENERATION////
    private void generateTruck() {
        double rand = Math.random();
        if (rand <= _truckProb) {
            Truck newCar = new Truck(750,800,_time);
            int id = (int)(Math.random() * 1000 + 1);
            newCar.setID(id);
            _cars.add(newCar);
            _idSet.add(id);
            _carMap.put(_time, newCar);
        }
    }

    private void generatePassengerCar() {
        double rand = Math.random();
        if (rand <= _passengerProb) {
            PassengerCar newCar = new PassengerCar(750,800,_time);
            int id = (int)(Math.random() * 1000 + 1);
            newCar.setID(id);
            _cars.add(newCar);
            _idSet.add(id);
            _carMap.put(_time, newCar);
        }
    }

    private void Update(int time) {
        if (time % _truckFreq == 0) {
            generateTruck();
        }
        if (time % _passengerFreq == 0) {
            generatePassengerCar();
        }
    }

    private void checkLife() {

        if (_carMap.containsKey(_time-_carMaxLifetime) && _carMap.get(_time-_carMaxLifetime) instanceof PassengerCar) {
            _idSet.remove(_carMap.get(_time-_carMaxLifetime).getID());
            _cars.remove(_carMap.get(_time-_carMaxLifetime));
            _carMap.remove(_time-_carMaxLifetime);
        }
        if(_carMap.containsKey(_time-_truckMaxLifetime) && _carMap.get(_time-_truckMaxLifetime) instanceof Truck) {
            _idSet.remove(_carMap.get(_time-_truckMaxLifetime).getID());
            _cars.remove(_carMap.get(_time-_truckMaxLifetime));
            _carMap.remove(_time-_truckMaxLifetime);
        }

    }

    ////AI CONTROL////
    public void pauseAI() { _passengerAI.pauseAI(); _truckAI.pauseAI(); }
    public void resumeAI() { _passengerAI.resumeAI(); _truckAI.resumeAI(); }
    private void setPriority() {
        if (userPanel.threadPriority.getSelectedItem() == "Car") {
            _passengerAI.setPriority(Thread.MAX_PRIORITY);
            _truckAI.setPriority(Thread.MIN_PRIORITY);
        } else if (userPanel.threadPriority.getSelectedItem() == "Truck") {
            _truckAI.setPriority(Thread.MAX_PRIORITY);
            _passengerAI.setPriority(Thread.MIN_PRIORITY);
        }
    }

    ////SERIALIZATION////
    public void serialize() {
        Serializer toSerialize = new Serializer(_cars, _idSet, _carMap, _time);
        try {
            toSerialize.saveFile(toSerialize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deserialize() {
        try {
            if (_isRunning) {
                stopSimulation();
                userPanel.startButton.setEnabled(true);
                userPanel.stopButton.setEnabled(false);
            }
            Serializer toDeserialize = (Serializer)Serializer.loadFile();
            if (toDeserialize == null) { return; }
            _isDeserialized = true;
            _cars.clear();
            _cars = toDeserialize.get_carList();
            _idSet = toDeserialize.get_idSet();
            _carMap = toDeserialize.get_carMap();
            _time = toDeserialize.get_time();
            _passengerAI.setCars(_cars);
            _truckAI.setCars(_cars);
            timePanel._timeLabel.setText("Time in seconds: " + _time);
            repaint();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    ////CONFIG CONTROL////
    public void saveConfig() {
        Config config = new Config(this);
        config.saveFile();
    }

    public void loadConfig() {
        if (_isRunning) {
            stopSimulation();
            userPanel.startButton.setEnabled(true);
            userPanel.stopButton.setEnabled(false);
        }
        Config config = new Config(this);
        config.loadFile();
        _configLoaded = true;
        userPanel.carBirth.setText(Integer.toString(_passengerFreq));
        userPanel.truckBirth.setText(Integer.toString(_truckFreq));
        userPanel.carLifeTime.setText(Integer.toString(_carMaxLifetime));
        userPanel.truckLifeTime.setText(Integer.toString(_truckMaxLifetime));
        userPanel.carProbBox.setSelectedItem(Double.toString(_passengerProb));
        userPanel.truckProbBox.setSelectedItem(Double.toString(_truckProb));
    }

    public void saveBootConfig() {
        setProbabilities();
        userPanel.submit.doClick();
        Config config = new Config(this);
        config.saveBeforeExit();
    }

    public void loadBootConfig() {
        if (_isRunning) {
            stopSimulation();
            userPanel.startButton.setEnabled(true);
            userPanel.stopButton.setEnabled(false);
        }
        Config config = new Config(this);
        config.loadAfterBoot();
        _configLoaded = true;
        userPanel.carBirth.setText(Integer.toString(_passengerFreq));
        userPanel.truckBirth.setText(Integer.toString(_truckFreq));
        userPanel.carLifeTime.setText(Integer.toString(_carMaxLifetime));
        userPanel.truckLifeTime.setText(Integer.toString(_truckMaxLifetime));
        userPanel.carProbBox.setSelectedItem(Double.toString(_passengerProb));
        userPanel.truckProbBox.setSelectedItem(Double.toString(_truckProb));
    }

    public ArrayList<Car> getCarList() { return _cars; }

    public void updateCarListP(ArrayList<Car> newCars) {
        _cars.removeIf(car -> car instanceof PassengerCar);
        _cars.addAll(newCars);
    }
    public void updateCarListT(ArrayList<Car> newCars) {
        _cars.removeIf(car -> car instanceof Truck);
        _cars.addAll(newCars);
    }
}
