package ai;

import cars.*;

import javax.swing.*;
import java.util.ArrayList;

public class TruckAI extends BaseAI {

    public TruckAI(ArrayList<Car> cars, JPanel gp) {
        super(cars, gp);

    }

    @Override
    synchronized void process() {
        for (Car car : cars) {
            if (car instanceof Truck) {
                car.moveCar(getRunStatus());
            }
        }
    }
}
