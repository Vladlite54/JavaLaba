package cars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.Serializable;

public abstract class Car implements IBehaviour, Serializable {
    Color _color;
    private  int _x;
    private  int _y;
    private int _id;
    protected int birthTime; //lifeTime;
    public void set_color(Color color) {
        _color = color;
    }

    public void set_x(int x) {
        this._x = x;
    }
    public void set_y(int y) {
        _y = y;
    }

    public Color get_color() {
        return _color;
    }
    public int get_xCoordinate() {
        return _x;
    }
    public int get_yCoordinate() {
        return _y;
    }
    public void setID(int id) {_id = id;}
    public int getID() {return _id;}
    public void setBirthTime(int time) {birthTime = time;}
    public int getBirthTime() {return birthTime;}
    //public void setLifeTime(int time) {lifeTime = time;}
    //public int getLifeTime() {return lifeTime;}

    public abstract void paintCar(Graphics g);

}
