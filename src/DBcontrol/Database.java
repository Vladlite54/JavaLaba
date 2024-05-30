package DBcontrol;


import cars.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Database {
    private Connection _connection;

    public static Database _instance;

    public Database() {
        _connection = DBConnectionManager.open();
    }

    public static Database getInstance() {
        if (_instance == null) {
            _instance = new Database();
        }
        return _instance;
    }

    public void insertToDB(int id, int birthtime, int x_coord, int y_coord, String type) {
        try {
            Statement statement = _connection.createStatement();
            String sql = String.format("INSERT INTO public.car_table(car_id,birth_time,x_coordinate,y_coordinate,car_type)" +
                    " VALUES(%d,%d,%d,%d,'%s');", id, birthtime, x_coord, y_coord, type);
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Car> getListFromDB(String type) {
        try {
            Statement statement = _connection.createStatement();
            String query = "SELECT * FROM public.car_table";
            ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Car> result = new ArrayList<>();
            while (resultSet.next()) {
                if (type.equals("Car")) {
                    if (!resultSet.getString(5).equals("Car")) continue;
                    PassengerCar carToAdd= new PassengerCar(0, 0, resultSet.getInt(2));
                    carToAdd.set_x(resultSet.getInt(3));
                    carToAdd.set_y(resultSet.getInt(4));
                    carToAdd.setID(resultSet.getInt(1));
                    result.add(carToAdd);
                } else if (type.equals("Truck")) {
                    if (!resultSet.getString(5).equals("Truck")) continue;
                    Truck truckToAdd= new Truck(0, 0, resultSet.getInt(2));
                    truckToAdd.setID(resultSet.getInt(1));
                    truckToAdd.set_x(resultSet.getInt(3));
                    truckToAdd.set_y(resultSet.getInt(4));
                    result.add(truckToAdd);
                } else {
                    if (resultSet.getString(5).equals("Car")) {
                        PassengerCar carToAdd= new PassengerCar(0, 0, resultSet.getInt(2));
                        carToAdd.set_x(resultSet.getInt(3));
                        carToAdd.set_y(resultSet.getInt(4));
                        carToAdd.setID(resultSet.getInt(1));
                        result.add(carToAdd);
                    } else {
                        Truck truckToAdd= new Truck(0, 0, resultSet.getInt(2));
                        truckToAdd.setID(resultSet.getInt(1));
                        truckToAdd.set_x(resultSet.getInt(3));
                        truckToAdd.set_y(resultSet.getInt(4));
                        result.add(truckToAdd);
                    }
                }
            }

            return result;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String showTable() {
        try {
            Statement statement = _connection.createStatement();
            String query = "SELECT * FROM public.car_table";
            ResultSet resultSet = statement.executeQuery(query);
            String result = "";
            while (resultSet.next()) {
                result += "Car ID: " + resultSet.getInt(1) + " | ";
                result += "Birth time: " + resultSet.getInt(2) + " | ";
                result += "X coordinate: " + resultSet.getInt(3) + " | ";
                result += "Y coordinate: " + resultSet.getInt(4) + " | ";
                result += "Type: " + resultSet.getString(5) + "\n";
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearTable() {
        try {
            Statement statement = _connection.createStatement();
            statement.executeUpdate("DELETE FROM public.car_table");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }



}
