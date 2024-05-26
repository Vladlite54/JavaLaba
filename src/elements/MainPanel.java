package elements;

import javax.swing.*;
import java.awt.*;

public class MainPanel extends JPanel {
    protected Habitat _hab;
    MainPanel(Habitat habitat) {
        _hab = habitat;
        setLayout(new BorderLayout());
        setBackground(Color.white);
        setPreferredSize(new Dimension(850,100));
    }

    
}

