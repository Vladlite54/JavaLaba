package elements;

import javax.swing.*;
import java.awt.*;

public class UserPanel extends JPanel {
    private final Habitat _hab;
    JCheckBox checkBox;
    JButton startButton, stopButton, submit, pauseButton;
    JTextField carBirth, truckBirth, carLifeTime, truckLifeTime;
    JComboBox carProbBox, truckProbBox, threadPriority;
    String buttonSelected;
    private boolean isPaused;
    UserPanel(Habitat habitat) {
        _hab = habitat;
        isPaused = false;
        setLayout(new GridLayout(13,1,0,25));
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(400, 100));
        setBorder(BorderFactory.createMatteBorder(0,1,0,0,Color.BLACK));

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
        menuLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        menuLabel.setHorizontalAlignment(JLabel.CENTER);
        add(menuLabel);

        ////START STOP BUTTONS////
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,3,25,0));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        startButton = new JButton("START");
        startButton.setBackground(Color.GREEN);
        startButton.setForeground(Color.WHITE);
        startButton.setFont(new Font("Ubuntu mono", Font.BOLD, 20));
        startButton.setFocusable(false);
        stopButton = new JButton("STOP");
        stopButton.setBackground(Color.RED);
        stopButton.setForeground(Color.WHITE);
        stopButton.setFont(new Font("Ubuntu mono", Font.BOLD, 20));
        stopButton.setFocusable(false);
        stopButton.setEnabled(false);
        pauseButton = new JButton("PAUSE");
        pauseButton.setBackground(Color.GRAY);
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setFont(new Font("Ubuntu mono", Font.BOLD, 20));
        pauseButton.setFocusable(false);
        startButton.addActionListener(actionEvent -> {
            buttonSelected = "Start";
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            submit.setEnabled(false);
            _hab.startSimulation();
        });
        stopButton.addActionListener(actionEvent -> {
            buttonSelected = "Stop";
            stopButton.setEnabled(false);
            startButton.setEnabled(true);
            submit.setEnabled(true);
            _hab.stopSimulation();
        });

        pauseButton.addActionListener(actionEvent -> {
            if(!isPaused) {
                _hab.pauseSimulation();
                isPaused = true;
            } else {
                _hab.resumeSimulation();
                isPaused = false;
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(pauseButton);
        buttonPanel.add(stopButton);
        add(buttonPanel);

        ////ALLOW MESSAGE BOX////
        checkBox = new JCheckBox("Allow message window?");
        checkBox.setFocusable(false);
        checkBox.setBackground(Color.LIGHT_GRAY);
        checkBox.setHorizontalTextPosition(JCheckBox.LEFT);
        checkBox.setFont(new Font("Tahoma", Font.BOLD, 18));
        add(checkBox);

        ////TIMER TOGGLE////
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new GridLayout());
        radioPanel.setBackground(Color.LIGHT_GRAY);
        JRadioButton showTimer = new JRadioButton("Show time");
        showTimer.setFont(new Font("Tahoma", Font.BOLD, 18));
        showTimer.setBackground(Color.LIGHT_GRAY);
        showTimer.setFocusable(false);
        showTimer.setSelected(true);
        JRadioButton hideTimer = new JRadioButton("Hide time");
        hideTimer.setFont(new Font("Tahoma", Font.BOLD, 18));
        hideTimer.setBackground(Color.LIGHT_GRAY);
        hideTimer.setFocusable(false);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(showTimer);
        buttonGroup.add(hideTimer);
        showTimer.addActionListener(actionEvent -> _hab.timePanel.setVisible(true));
        hideTimer.addActionListener(actionEvent -> _hab.timePanel.setVisible(false));
        radioPanel.add(showTimer);
        radioPanel.add(hideTimer);
        add(radioPanel);

        ////BIRTH TIME FIELDS////
        JPanel inputPanel1 = new JPanel();
        inputPanel1.setLayout(new BorderLayout());
        JLabel carBirthLabel = new JLabel("Car birth");
        carBirthLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel1.add(carBirthLabel, BorderLayout.WEST);
        carBirth = new JTextField("2");
        carBirth.setPreferredSize(new Dimension(250,20));
        carBirth.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel1.add(carBirth, BorderLayout.EAST);
        add(inputPanel1);

        JPanel inputPanel2 = new JPanel();
        inputPanel2.setLayout(new BorderLayout());
        JLabel truckBirthLabel = new JLabel("Truck birth");
        truckBirthLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel2.add(truckBirthLabel, BorderLayout.WEST);
        truckBirth = new JTextField("3");
        truckBirth.setPreferredSize(new Dimension(250,20));
        truckBirth.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel2.add(truckBirth, BorderLayout.EAST);
        add(inputPanel2);

        ////LIFETIME FIELDS////
        JPanel inputPanel3 = new JPanel();
        inputPanel3.setLayout(new BorderLayout());
        JLabel carLifeTimeLabel = new JLabel("Car lifetime");
        carLifeTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel3.add(carLifeTimeLabel, BorderLayout.WEST);
        carLifeTime = new JTextField("10");
        carLifeTime.setPreferredSize(new Dimension(250,20));
        carLifeTime.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel3.add(carLifeTime, BorderLayout.EAST);
        add(inputPanel3);

        JPanel inputPanel4 = new JPanel();
        inputPanel4.setLayout(new BorderLayout());
        JLabel truckLifeTimeLabel = new JLabel("Truck lifetime");
        truckLifeTimeLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel4.add(truckLifeTimeLabel, BorderLayout.WEST);
        truckLifeTime = new JTextField("12");
        truckLifeTime.setPreferredSize(new Dimension(250,20));
        truckLifeTime.setFont(new Font("Tahoma", Font.BOLD, 18));
        inputPanel4.add(truckLifeTime, BorderLayout.EAST);
        add(inputPanel4);

        ////SUBMIT BUTTON////
        submit = new JButton("Submit");
        submit.setFont(new Font("Tahoma", Font.BOLD, 18));
        submit.setFocusable(false);
        submit.addActionListener(actionEvent -> {
            try{
                _hab.setFrequencies(Integer.parseInt(carBirth.getText()), Integer.parseInt(truckBirth.getText()));
                _hab.setLifetimes(Integer.parseInt(carLifeTime.getText()), Integer.parseInt(truckLifeTime.getText()));
            } catch (NumberFormatException e) {
                _hab.setFrequencies(2, 3);
                carBirth.setText("2");
                truckBirth.setText("3");
                _hab.setLifetimes(10,12);
                carLifeTime.setText("10");
                truckLifeTime.setText("12");
                JOptionPane.showMessageDialog(null,
                        "Incorrect values!\nThe default values are set",
                        "ERROR", JOptionPane.WARNING_MESSAGE);
            }
            _hab.requestFocus();

        });
        add(submit);

        ////PROBABILITY BOXES////
        String[] Probs = {"0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0"};
        JPanel probPanel1 = new JPanel();
        probPanel1.setLayout(new BorderLayout());
        JLabel carProbLabel = new JLabel("Car birth chance");
        carProbLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        probPanel1.add(carProbLabel, BorderLayout.WEST);
        carProbBox = new JComboBox(Probs);
        carProbBox.setFocusable(false);
        carProbBox.setPreferredSize(new Dimension(200, 20));
        carProbBox.setSelectedIndex(5);
        probPanel1.add(carProbBox, BorderLayout.EAST);
        add(probPanel1);

        JPanel probPanel2 = new JPanel();
        probPanel2.setLayout(new BorderLayout());
        JLabel truckProbLabel = new JLabel("Truck birth chance");
        truckProbLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        probPanel2.add(truckProbLabel, BorderLayout.WEST);
        truckProbBox = new JComboBox(Probs);
        truckProbBox.setFocusable(false);
        truckProbBox.setPreferredSize(new Dimension(200, 20));
        truckProbBox.setSelectedIndex(3);
        probPanel2.add(truckProbBox, BorderLayout.EAST);
        add(probPanel2);

        String[] threads = {"Car", "Truck"};
        JPanel threadPanel = new JPanel();
        threadPanel.setLayout(new BorderLayout());
        JLabel threadLabel = new JLabel("Thread priority");
        threadLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
        threadPanel.add(threadLabel, BorderLayout.WEST);
        threadPriority = new JComboBox(threads);
        threadPriority.setFocusable(false);
        threadPriority.setPreferredSize(new Dimension(200, 20));
        threadPriority.setSelectedIndex(0);
        threadPanel.add(threadPriority, BorderLayout.EAST);
        add(threadPanel);


    }

}
