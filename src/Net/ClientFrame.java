package Net;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ClientFrame extends JFrame {

    static JTextArea clientArea;
    JButton SwapButton;
    private static String _clients = "";
    private static String[] types = {"Cars", "Trucks"};
    private static ArrayList<String> idList;
    public static JComboBox<String> typeBox;
    public static JComboBox<String> idBox;
    public static JButton swapButton;
    public static DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

    public ClientFrame() {
        super("Client options");
        super.setSize(500, 500);
        super.setLocationRelativeTo(null);
        setLayout(new GridLayout(4,1));

        JLabel clientLabel = new JLabel("Connected clients:", JLabel.CENTER);
        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());
        clientPanel.add(clientLabel, BorderLayout.SOUTH);
        add(clientPanel);

        clientArea = new JTextArea();
        clientArea.setPreferredSize(new Dimension(150,100));
        clientArea.setMaximumSize(new Dimension(150,100));
        clientArea.setLineWrap(true);
        clientArea.setWrapStyleWord(true);
        clientArea.setFont(new Font("Tahoma", Font.PLAIN, 14));
        clientArea.setEditable(false);
        clientArea.setFocusable(false);

        JScrollPane scrollPane = new JScrollPane(clientArea);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        JPanel panePanel = new JPanel();
        panePanel.setLayout(new FlowLayout());
        panePanel.add(scrollPane);
        add(panePanel);

        JPanel swapPanel = new JPanel();
        swapPanel.setPreferredSize(new Dimension(150,50));
        swapPanel.setMaximumSize(new Dimension(150, 50));
        typeBox = new JComboBox<>(types);
        swapPanel.add(typeBox);
        JLabel toLabel = new JLabel("to", JLabel.CENTER);
        swapPanel.add(toLabel);
        idList = new ArrayList<>();
        idBox = new JComboBox<>(model);
        //idBox.addItem("-");
        swapPanel.add(idBox);
        add(swapPanel);

        JPanel butPanel = new JPanel();
        butPanel.setLayout(new FlowLayout());
        swapButton = new JButton("Swap");
        butPanel.add(swapButton);
        add(butPanel);


    }

    public static void updateList() {
        idBox = new JComboBox<>(model);
        clientArea.setText(_clients);
        if (!_clients.isEmpty()) {
            //idBox.removeAllItems();
            String[] IDs = _clients.split("\n");
            for (String elem : IDs) {
                String toAdd = elem.split("ID:")[1];
                if (model.getIndexOf(toAdd) == -1) {
                    model.addElement(toAdd);
                }
            }
        }


        //clientArea.append("a\n");
    }

    public static void updateClients(String msg) {
        _clients = msg;
        _clients = _clients.replace("clients ", "");
        _clients = _clients.replaceAll(" ", "\n");
    }

    public static void removeFromModel() {
        model.removeAllElements();
        idBox.removeAllItems();
    }
}
