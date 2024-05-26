package elements;

import cars.Car;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Serializer implements Serializable {
    private final ArrayList<Car> _carList;
    private int _time;
    private TreeSet<Integer> _idSet;
    private HashMap<Integer, Car> _carMap;

    public Serializer(ArrayList<Car> carList, TreeSet<Integer> idSet, HashMap<Integer, Car> carMap, int time) {
        _carList = carList;
        _time = time;
        _idSet = idSet;
        _carMap = carMap;
    }

    public void saveFile(Serializer object) throws IOException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/data"));
        File file;
        FileOutputStream fileOut;
        ObjectOutputStream out;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin", "bin");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new FileOutputStream(file);
                out = new ObjectOutputStream(fileOut);
                out.writeObject(object);
                out.close();
                fileOut.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Serializer loadFile() throws IOException, ClassNotFoundException {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/data"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin", "bin");
        fileChooser.setFileFilter(filter);
        File file;
        FileInputStream fileIn;
        ObjectInputStream in;
        Serializer res = null;
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            fileIn = new FileInputStream(file);
            in = new ObjectInputStream(fileIn);
            res = (Serializer)in.readObject();
            in.close();
            fileIn.close();
            return res;
        } else {
            return null;
        }
    }

    public  ArrayList<Car> get_carList() { return _carList;}
    public TreeSet<Integer> get_idSet() { return _idSet; }
    public HashMap<Integer, Car> get_carMap() { return _carMap; }
    public  int get_time() { return _time; }
}
