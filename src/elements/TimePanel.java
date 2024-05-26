package elements;

import javax.swing.*;
import java.awt.*;

public class TimePanel extends JPanel {
    protected Habitat _hab;
    JLabel _timeLabel;
    TimePanel(Habitat habitat) {
        _hab = habitat;
        ////TIME LABEL////
        _timeLabel = new JLabel();
        _timeLabel.setText("Time in seconds: " + _hab.getTime());
        _timeLabel.setHorizontalTextPosition(JLabel.CENTER);
        _timeLabel.setForeground(Color.white);
        _timeLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
        _timeLabel.setHorizontalAlignment(JLabel.CENTER);

        ////TIME PANEL////
        setLayout(new BorderLayout());
        setBackground(Color.gray);
        setPreferredSize(new Dimension(1250,50));
        add(_timeLabel, BorderLayout.CENTER);
        setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.BLACK));
    }
}
