package ui.tabs;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.*;

import ui.PlayerUI;

// An abstract tab that is the super class of tabs that will be be present in PlayerUI
// Contains reference to the PlayerUI that is controlling it
// ALL CODE IS EITHER DIRECTLY TAKEN OR MODIFIED from SmartHome from LongFromProblemStarters
public abstract class Tab extends JPanel {

    private final PlayerUI controller;

    // REQUIRES: PlayerUI contoller that holds this tab
    public Tab(PlayerUI controller) {
        this.controller = controller;
    }

    // EFFECTS: creates and returns row with button included
    public JPanel formatButtonRow(JButton b) {
        JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
        p.add(b);

        return p;
    }

    public PlayerUI getController() {
        return controller;
    }
}
