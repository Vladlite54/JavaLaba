package ai;

import cars.*;
import javax.swing.*;
import java.util.ArrayList;

public class PassengerAI extends BaseAI{

    public PassengerAI(ArrayList<Car> cars, JPanel gp) {
        super(cars, gp);

    }

    @Override
    synchronized void process() {
        for (Car car : cars) {
            if (car instanceof PassengerCar) {
                car.moveCar(getRunStatus());
            }
        }
    }
}
