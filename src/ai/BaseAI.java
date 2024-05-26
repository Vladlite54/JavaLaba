package ai;
import cars.*;

import javax.swing.*;
import java.util.ArrayList;

public abstract class BaseAI extends Thread{
    private static int $velocity = 50;
    private  boolean _isRunning = true;
    protected ArrayList<Car> cars;
    protected JPanel _graphicsPanel;

    public BaseAI(ArrayList<Car> cars, JPanel graphicsPanel) {
        this.cars = cars;
        _graphicsPanel = graphicsPanel;

    }

    public  static void setVelocity(int speed) {$velocity = speed;}
    public static int getVelocity() { return $velocity; }

    protected void setRunStatus(boolean status) {
        _isRunning = status;
    }
    public boolean getRunStatus() { return _isRunning; }

    public void pauseAI() {setRunStatus(false);}
    public void resumeAI() {
        synchronized (this ) {
            setRunStatus(true);
            notify();
        }
    }

    public void setCars(ArrayList<Car> cars){
        this.cars = cars;
    }

    abstract  void process();

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                if (!getRunStatus()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    process();
                }
            }
            _graphicsPanel.repaint();
            try {
                Thread.sleep($velocity);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
