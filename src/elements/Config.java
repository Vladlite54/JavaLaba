package elements;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class Config {

    private final Habitat _hab;
    public Config(Habitat habitat) {
        _hab = habitat;
    }

    public void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/configuration"));
        File file;
        FileWriter fileOut;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileOut = new FileWriter(file);
                fileOut.write(_hab.getPassengerFreq() + "\n");
                fileOut.write(_hab.getTruckFreq() + "\n");
                fileOut.write(_hab.getCarMaxLifetime() + "\n");
                fileOut.write(_hab.getTruckMaxLifetime() + "\n");
                fileOut.write(_hab.getPassengerProb() + "\n");
                fileOut.write(_hab.getTruckProb() + "\n");
                fileOut.write(_hab.userPanel.threadPriority.getSelectedItem() + "\n");
                fileOut.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void loadFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("src/configuration"));
        File file;
        BufferedReader fileIn;
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt", "txt");
        fileChooser.setFileFilter(filter);
        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            try {
                fileIn = new BufferedReader(new FileReader(file));
                _hab.setPassengerFreq(Integer.parseInt(fileIn.readLine()));
                _hab.setTruckFreq(Integer.parseInt(fileIn.readLine()));
                _hab.setCarMaxLifetime(Integer.parseInt(fileIn.readLine()));
                _hab.setTruckMaxLifetime(Integer.parseInt(fileIn.readLine()));
                _hab.setPassengerProb(Double.parseDouble(fileIn.readLine()));
                _hab.setTruckProb(Double.parseDouble(fileIn.readLine()));
                _hab.userPanel.threadPriority.setSelectedItem(fileIn.readLine());
                fileIn.close();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void saveBeforeExit() {
        try {
            FileWriter fileOut = new FileWriter("src/configuration/boot.txt");
            fileOut.write(_hab.getPassengerFreq() + "\n");
            fileOut.write(_hab.getTruckFreq() + "\n");
            fileOut.write(_hab.getCarMaxLifetime() + "\n");
            fileOut.write(_hab.getTruckMaxLifetime() + "\n");
            fileOut.write(_hab.getPassengerProb() + "\n");
            fileOut.write(_hab.getTruckProb() + "\n");
            fileOut.write(_hab.userPanel.threadPriority.getSelectedItem() + "\n");
            fileOut.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadAfterBoot() {
        try {
            BufferedReader fileIn = new BufferedReader(new FileReader("src/configuration/boot.txt"));
            _hab.setPassengerFreq(Integer.parseInt(fileIn.readLine()));
            _hab.setTruckFreq(Integer.parseInt(fileIn.readLine()));
            _hab.setCarMaxLifetime(Integer.parseInt(fileIn.readLine()));
            _hab.setTruckMaxLifetime(Integer.parseInt(fileIn.readLine()));
            _hab.setPassengerProb(Double.parseDouble(fileIn.readLine()));
            _hab.setTruckProb(Double.parseDouble(fileIn.readLine()));
            _hab.userPanel.threadPriority.setSelectedItem(fileIn.readLine());
            fileIn.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
