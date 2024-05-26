package elements;

import cars.*;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AliveMessage extends JFrame {
    protected HashMap<Integer,Car> _carHashMap;
    protected JTextArea textArea;
    JButton OkButton;

    AliveMessage(HashMap<Integer, Car> carHashMap) {
        super("Current objects");
        super.setSize(500, 500);
        super.setLocationRelativeTo(null);
        super.setLayout(new BorderLayout());
        super.setResizable(false);
        super.setVisible(true);

        _carHashMap = carHashMap;

        JPanel messagePanel = new JPanel();

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Tahoma", Font.PLAIN, 28));
        textArea.setEditable(false);

        for (Map.Entry<Integer, Car> e : _carHashMap.entrySet()) {
            String toAppend = "Birth time: " + e.getKey() + "\tID: " + e.getValue().getID() + "\n";
            textArea.append(toAppend);
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        messagePanel.add(scrollPane);

        OkButton = new JButton("OK");
        OkButton.setFocusable(false);
        OkButton.setPreferredSize(new Dimension(100,50));


        super.add(OkButton, BorderLayout.SOUTH);
        super.add(messagePanel, BorderLayout.CENTER);
    }
}
