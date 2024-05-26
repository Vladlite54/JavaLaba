package cars;

import ai.BaseAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PassengerCar extends Car {

    private static int countPassenger = 0;
    private static final Image _image = new ImageIcon("/home/vladlite/Рабочий стол/JavaProjects/JavaLaba1/src/cars/car.png").getImage();
    public PassengerCar(int x_position, int y_position, int time) {
        set_color(Color.RED);
        set_x((int) (Math.random() * x_position));
        set_y((int) (Math.random() * y_position));
        countPassenger++;
        birthTime = time;
        //lifeTime = 0;
    }

    public static int getCountPassenger() {
        return countPassenger;
    }

    public static void setCountPassenger(int value) {
        countPassenger = value;
    }

    @Override
    public void paintCar(Graphics g) {
//        g.setColor(get_co public abstract Object move(boolean runStatus);lor());
//        g.fillRect(get_xCoordinate(), get_yCoordinate(), 100, 50);
//        g.setColor(Color.WHITE);
//        g.drawString("Passenger", get_xCoordinate() + 10, get_yCoordinate() + 25);
        g.drawImage(_image, get_xCoordinate(), get_yCoordinate(), 112, 112,  null);
    }

    @Override
    public void moveCar(boolean isRunning) {
        int w = 850;
        int h = 900;
        if (!isRunning) { return; }
        if (get_xCoordinate() > w/2 - 112 && get_yCoordinate() > h/2 + 112) { return; }
        if (get_xCoordinate() == w - 112) {
            set_y(get_yCoordinate() + 1);
        } else if (get_yCoordinate() == h + 112) {
            set_x(get_xCoordinate() + 1);
        } else {
            set_x(get_xCoordinate() + 1);
            set_y(get_yCoordinate() + 1);
        }

    }

}
