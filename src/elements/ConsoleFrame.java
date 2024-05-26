package elements;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ConsoleFrame extends JFrame {
    private final Habitat _hab;
    private final JTextArea output;
    private final JTextField input;
    protected JScrollPane scrollPane;
    private final String[] commandList = {"-stop_ai", "-start_ai"};

    public  ConsoleFrame(Habitat hab) {
        super("Console");
        super.setSize(1000, 1000);
        super.setLocationRelativeTo(null);
        super.setLayout(new BorderLayout());
        super.setBackground(Color.GRAY);
        super.setVisible(true);
        super.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        _hab = hab;
        output = new JTextArea("Welcome to console\nType \"-help\" for view command\n");
        output.setEditable(false);
        output.setBackground(Color.GRAY);
        output.setFont(new Font("Tahoma", Font.PLAIN, 24));
        output.setForeground(Color.WHITE);

        input = new JTextField();
        input.setPreferredSize(new Dimension(50,50));
        input.setBackground(Color.GRAY);
        input.setFont(new Font("Tahoma", Font.PLAIN, 24));
        input.setForeground(Color.WHITE);
        input.setCaretColor(Color.WHITE);
        input.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String command = input.getText();
                    output.append(command + "\n");
                    input.setText(null);
                    getCommand(command);
                }
            }
        });

        scrollPane = new JScrollPane(output);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        super.add(scrollPane, BorderLayout.CENTER);
        super.add(input, BorderLayout.SOUTH);


    }

    void getCommand(String command) {
        switch (command) {
            case "-help":
                output.append("Command list:\n");
                for (String com : commandList) {
                    output.append(com + "\n");
                }
                break;
            case "-stop_ai":
                output.append("AI has been stopped\n");
                _hab.pauseAI();
                break;
            case "-start_ai":
                output.append("AI has been started\n");
                _hab.resumeAI();
                break;
            default:
                output.append("Error! Unknown command!\n");
        }
    }
}
