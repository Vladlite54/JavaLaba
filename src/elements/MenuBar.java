package elements;

import javax.swing.*;

public class MenuBar extends JMenuBar {
    private final Habitat _hab;
    private JMenu actionMenu, threadMenu, fileMenu;
    private JMenuItem start, stop, showTime, hideTime, currentObjects, saveItem, loadItem, consoleItem, clientItem;
    private JMenuItem waitPassenger, waitTruck, notifyPassenger, notifyTruck;
    private JMenuItem saveConfig, loadConfig;
    MenuBar(Habitat habitat) {
        _hab = habitat;

        fileMenu = new JMenu("File");
        add(fileMenu);
        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(actionEvent -> _hab.serialize());
        loadItem = new JMenuItem("Load");
        loadItem.addActionListener(actionEvent -> _hab.deserialize());
        saveConfig = new JMenuItem("Save config");
        saveConfig.addActionListener(actionEvent -> _hab.saveConfig());
        loadConfig = new JMenuItem("Load config");
        loadConfig.addActionListener(actionEvent -> _hab.loadConfig());
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.add(saveConfig);
        fileMenu.add(loadConfig);

        actionMenu = new JMenu("Action");
        add(actionMenu);

        start = new JMenuItem("Start");
        start.addActionListener(actionEvent -> {
            _hab.startSimulation();
            _hab.userPanel.startButton.setEnabled(false);
            _hab.userPanel.stopButton.setEnabled(true);
        });
        stop = new JMenuItem("Stop");
        stop.addActionListener(actionEvent -> {
            _hab.stopSimulation();
            _hab.userPanel.stopButton.setEnabled(false);
            _hab.userPanel.startButton.setEnabled(true);
        });
        showTime = new JMenuItem("Show time");
        showTime.addActionListener(actionEvent -> _hab.timePanel.setVisible(true));
        hideTime = new JMenuItem("Hide time");
        hideTime.addActionListener(actionEvent -> _hab.timePanel.setVisible(false));

        currentObjects = new JMenuItem("Current objects");
        currentObjects.addActionListener(actionEvent -> _hab.showAliveObjects());

        consoleItem = new JMenuItem("Console");
        consoleItem.addActionListener(actionEvent -> _hab.showConsole());

        clientItem = new JMenuItem("Client options");
        clientItem.addActionListener(actionEvent -> _hab.showClients());


        actionMenu.add(start);
        actionMenu.add(stop);
        actionMenu.add(showTime);
        actionMenu.add(hideTime);
        actionMenu.add(currentObjects);
        actionMenu.add(consoleItem);
        actionMenu.add(clientItem);

        threadMenu = new JMenu("Threads");
        add(threadMenu);

        waitPassenger = new JMenuItem("Pause Car");
        waitPassenger.addActionListener(actionEvent -> _hab._passengerAI.pauseAI());
        waitTruck = new JMenuItem("Pause Truck");
        waitTruck.addActionListener(actionEvent -> _hab._truckAI.pauseAI());
        notifyPassenger = new JMenuItem("Resume Car");
        notifyPassenger.addActionListener(actionEvent -> _hab._passengerAI.resumeAI());
        notifyTruck = new JMenuItem("Resume Truck");
        notifyTruck.addActionListener(actionEvent -> _hab._truckAI.resumeAI());

        threadMenu.add(waitPassenger);
        threadMenu.add(waitTruck);
        threadMenu.add(notifyPassenger);
        threadMenu.add(notifyTruck);


    }
}
