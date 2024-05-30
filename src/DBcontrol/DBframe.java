package DBcontrol;

import cars.Car;
import cars.PassengerCar;
import cars.Truck;
import elements.Habitat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class DBframe extends JFrame {
    private Habitat _habitat;
    private JTextArea _table;
    private JComboBox<String> _typeBox;
    private JButton setButton, getButton, showButton, clearButton;


    public DBframe (Habitat habitat) {
        super("Database control");
        super.setSize(700, 400);
        super.setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel panePanel = new JPanel();
        _table = new JTextArea();
        _table.setLineWrap(true);
        _table.setWrapStyleWord(true);
        _table.setFont(new Font("Tahoma", Font.PLAIN, 16));
        _table.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(_table);
        scrollPane.setPreferredSize(new Dimension(650, 225));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        panePanel.add(scrollPane);
        add(panePanel);

        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new GridLayout(1,5,50,50));
        actionPanel.setPreferredSize(new Dimension(650,50));
        String[] _types = {"All", "Car", "Truck"};
        _typeBox = new JComboBox<>(_types);
        _typeBox.setPreferredSize(new Dimension(50,50));
        actionPanel.add(_typeBox);

        setButton = new JButton("Set");
        setButton.setPreferredSize(new Dimension(50,50));
        actionPanel.add(setButton);

        getButton = new JButton("Get");
        getButton.setPreferredSize(new Dimension(50,50));
        actionPanel.add(getButton);

        showButton = new JButton("Show");
        showButton.setPreferredSize(new Dimension(50,50));
        actionPanel.add(showButton);

        clearButton = new JButton("Clear");
        clearButton.setPreferredSize(new Dimension(50,50));
        actionPanel.add(clearButton);

        add(actionPanel);

        _habitat = habitat;

        setButton.addActionListener(actionEvent -> {
            Database.getInstance().clearTable();
            ArrayList<Car> cars = _habitat.getCarList();
            if (_typeBox.getSelectedItem() == "All") {
                for (Car elem : cars) {
                    String type;
                    int id = elem.getID();
                    int birth_time = elem.getBirthTime();
                    int x_coord = elem.get_xCoordinate();
                    int y_coord = elem.get_yCoordinate();
                    if (elem instanceof PassengerCar) {
                        type = "Car";
                    } else {
                        type = "Truck";
                    }
                    Database.getInstance().insertToDB(id, birth_time, x_coord, y_coord, type);
                }
            } else if(_typeBox.getSelectedItem() == "Car") {
                for (Car elem : cars) {
                    String type = "Car";
                    int id = elem.getID();
                    int birth_time = elem.getBirthTime();
                    int x_coord = elem.get_xCoordinate();
                    int y_coord = elem.get_yCoordinate();
                    if (elem instanceof PassengerCar) {
                        Database.getInstance().insertToDB(id, birth_time, x_coord, y_coord, type);
                    }
                }
            } else if (_typeBox.getSelectedItem() == "Truck") {
                for (Car elem : cars) {
                    String type = "Truck";
                    int id = elem.getID();
                    int birth_time = elem.getBirthTime();
                    int x_coord = elem.get_xCoordinate();
                    int y_coord = elem.get_yCoordinate();
                    if (elem instanceof Truck) {
                        Database.getInstance().insertToDB(id, birth_time, x_coord, y_coord, type);
                    }
                }
            }
            _table.setText(Database.getInstance().showTable());
        });

        getButton.addActionListener(actionEvent -> {
            ArrayList<Car> newCars;
            if (_typeBox.getSelectedItem() == "All") {
                newCars = Database.getInstance().getListFromDB("All");
                _habitat.updateCarList(newCars);
            } else if (_typeBox.getSelectedItem() == "Car") {
                newCars = Database.getInstance().getListFromDB("Car");
                _habitat.updateCarListP(newCars);
            } else if (_typeBox.getSelectedItem() == "Truck") {
                newCars = Database.getInstance().getListFromDB("Truck");
                _habitat.updateCarListT(newCars);
            }
        });

        showButton.addActionListener(actionEvent -> {
            _table.setText(Database.getInstance().showTable());
        });

        clearButton.addActionListener(actionEvent -> {
            Database.getInstance().clearTable();
            _table.setText(Database.getInstance().showTable());
        });

    }

}
