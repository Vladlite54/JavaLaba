package cars;

import ai.BaseAI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Truck extends Car {

    private static int countTruck = 0;
    private static final Image _image = new ImageIcon("/home/vladlite/Рабочий стол/JavaProjects/JavaLaba1/src/cars/truck.png").getImage();
    public Truck(int x_position, int y_position, int time) {
        set_color(Color.GRAY);
        set_x((int) (Math.random() * x_position));
        set_y((int) (Math.random() * y_position));
        countTruck++;
        birthTime = time;
        //lifeTime = 0;

    }

    public static int getCountTruck() {
        return countTruck;
    }

    public static void setCountTruck(int value) {
        countTruck = value;
    }

    @Override
    public void paintCar(Graphics g) {
//        g.setColor(get_color());
//        g.fillRect(get_xCoordinate(), get_yCoordinate(), 100, 100);
//        g.setColor(Color.WHITE);
//        g.drawString("Truck", get_xCoordinate() + 25, get_yCoordinate() + 50);
        g.drawImage(_image, get_xCoordinate(), get_yCoordinate(), 112,112, null);
    }

    @Override
    public void moveCar(boolean isRunning) {
            int w = 850;
            int h = 900;
            if (get_xCoordinate() <= w/2 - 112 && get_yCoordinate() <= h/2 - 112) { return; }
            if (!isRunning) { return; }
        if (get_xCoordinate() <= 112) {
            set_y(get_yCoordinate() - 1);
        } else if (get_yCoordinate() == 0) {
            set_x(get_xCoordinate() - 1);
        } else {
            set_x(get_xCoordinate() - 1);
            set_y(get_yCoordinate() - 1);
        }

    }

}
