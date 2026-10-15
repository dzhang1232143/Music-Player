package ui;

import java.io.IOException;

import javax.swing.*;

import ui.tabs.*;
import model.EventLog;
import model.Event;
import model.Player;
import persistence.JsonReader;
import persistence.JsonWriter;

// Main GUI for Player, represents a window created through JFrame
// This GUI window will display a Player from the model folder
// All user inputs will be through this window and passed to and processed by Player
// Contains a Player that it is representing, a sidebar that represents all the tabs in this Window UI
public class PlayerUI extends JFrame {
    public static final int QUEUE_TAB_INDEX = 0;
    public static final int PLAYLISTS_TAB_INDEX = 1;
    public static final int VISUAL_TAB_INDEX = 2;

    public static final int WIDTH = 900;
    public static final int HEIGHT = 600;

    private static final String FILE_DIRECTORY = ".\\src\\data\\songs";
    private static final String JSON_SOURCE = ".\\src\\data\\json\\save.json";

    private JTabbedPane sidebar;
    private Player player;

    public static void main(String[] args) {
        new PlayerUI();
    }
    
    // MODIFIES: this
    // EFFECTS: creates an instance of PlayerUI as a representation of a Player
    public PlayerUI() {
        super("Music Player");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        
        loadPlayer();
        // handleStartupChoice();
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                savePlayer();
                dispose();
                // handleExit();
                if (player.isPlaying()) {
                    player.pause();
                }
                for (Event event : EventLog.getInstance()) {
                    System.out.println(event);
                }
            }
        });
        sidebar = new JTabbedPane();
        sidebar.setTabPlacement(JTabbedPane.LEFT);
        loadTabs();
        add(sidebar);
        setVisible(true);
    }

    public Player getPlayer() {
        return player;
    }

    // MODIFIES: this
    // EFFECTS: adds, queue tab, playlists tab, and songs tab to this UI
    private void loadTabs() {
        QueueTab queueTab = new QueueTab(this);
        PlaylistTab playlistTab = new PlaylistTab(this);
        // VisualTab visualTab = new VisualTab(this);


        sidebar.add(queueTab, QUEUE_TAB_INDEX);
        sidebar.setTitleAt(QUEUE_TAB_INDEX, "Queue");
        sidebar.add(playlistTab, PLAYLISTS_TAB_INDEX);
        sidebar.setTitleAt(PLAYLISTS_TAB_INDEX, "Playlists");
        // sidebar.add(visualTab, VISUAL_TAB_INDEX);
        // sidebar.setTitleAt(VISUAL_TAB_INDEX, "Visual Tab");

        player.setListener(queueTab);
    }

    // EFFECTS: returns sidebar of this UI
    public JTabbedPane getTabbedPane() {
        return sidebar;
    }

    // EFFECTS: Opens a window to ask the user if they want to load a save.
    // calls the load method if user selects yes
    private void handleStartupChoice() {
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Would you like to load a saved player or start new?",
                "Welcome",
                javax.swing.JOptionPane.YES_NO_OPTION);
        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            loadPlayer();
        } else {
            player = new Player(FILE_DIRECTORY);
        }

    }

    // EFFECTS: Opens a window to ask the user if they want to save thier current
    // player state.
    // calls the save method if user selects yes
    // closes the program is the user selects no
    // closes this window and continues the program is the user selects cancel
    private void handleExit() {
        int choice = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Do you want to save before exiting?",
                "Exit",
                javax.swing.JOptionPane.YES_NO_CANCEL_OPTION);

        if (choice == javax.swing.JOptionPane.YES_OPTION) {
            savePlayer();
            dispose();
        } else if (choice == javax.swing.JOptionPane.NO_OPTION) {
            dispose();
        }
        // Cancel closes the window and keeps the program running
    }

    // MODIFIES: this
    // EFFECTS: load the json save of player
    private void loadPlayer() {
        JsonReader reader = new JsonReader(JSON_SOURCE);
        try {
            player = reader.read();
            System.out.println("Save successfully loaded");
        } catch (IOException e) {
            System.out.println("IOException has occured.");
        }
    }

    // EFFECTS: save the current state of player as a json
    private void savePlayer() {
        JsonWriter writer = new JsonWriter(JSON_SOURCE);
        try {
            writer.open();
            writer.write(player);
            writer.close();
            System.out.println("Instance successfully saved.");
        } catch (IOException e) {
            System.out.println("Save failed (IOException).");
        }
    }

    // REQUIRES: player != null
    // MODIFIES: this
    // EFFECTS: clears the song queue in Player
    public void clearQueue() {
        player.clearQueue();
    }
}
