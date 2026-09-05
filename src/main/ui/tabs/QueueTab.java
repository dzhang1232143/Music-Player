package ui.tabs;

import javax.swing.*;

import exceptions.EmptyQueueException;

import java.awt.*;

import model.Player;
import model.Song;
import ui.PlayerUI;

// Represents a tab that will display the queue of songs to be played
public class QueueTab extends Tab implements QueueListener {

    private DefaultListModel<String> queueModel;
    private JList<String> queueList;

    // EFFECTS: constructs the queue tab that displays all songs in the queue and
    // buttons to play/pause and clear queue
    public QueueTab(PlayerUI controller) {
        super(controller);
        setLayout(new BorderLayout());

        setupQueueDisplay();
        addControls();
    }

    // EFFECTS: creates the visual list of songs in queue
    private void setupQueueDisplay() {
        queueModel = new DefaultListModel<>();
        queueList = new JList<>(queueModel);

        JScrollPane scrollPane = new JScrollPane(queueList);
        add(scrollPane, BorderLayout.CENTER);
        
        refreshQueue();
    }

    // MODFIES: this
    // EFFECTS: creates play/pause and clear queue buttons
    private void addControls() {
        JButton playPauseButton = new JButton("Play/Pause");
        JButton clearButton = new JButton("Clear Queue");
        JButton nextButton = new JButton("Next Song");

        playPauseButton.addActionListener(e -> handlePlayPause());
        clearButton.addActionListener(e -> handleClearQueue());
        nextButton.addActionListener(e -> handlePlayNextSong());

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new FlowLayout());
        buttonRow.add(playPauseButton);
        buttonRow.add(clearButton);
        buttonRow.add(nextButton);

        add(buttonRow, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: updates queueModel based on Player
    public void refreshQueue() {
        queueModel.clear();
        Player player = getController().getPlayer();
        if (player.getCurrentSong() != null) {
            queueModel.addElement(player.getCurrentSong().getName());
        }
        for (Song song : player.getQueue()) {
            queueModel.addElement(song.getName());
        }
    }

    // EFFECTS: pause or play the current song depending whether it is already
    // playing or not
    private void handlePlayPause() {
        Player player = getController().getPlayer();
        try {
            if (player.isPlaying()) {
                player.pause();
            } else if (player.getCurrentSong() != null) {
                player.play();
            } else {
                try {
                    player.nextSong();
                    player.play();
                } catch (EmptyQueueException e) {
                    System.out.println("There are no songs in queue.");
                }
            }
        } catch (InterruptedException e) {
            // pass
            // This will only be triggered from a program shutdown
        }
    }

    // MODIFIES: this
    // EFFECTS: clear the queue and update queueModel
    private void handleClearQueue() {
        getController().clearQueue();
        refreshQueue();
    }

    private void handlePlayNextSong() {
        Player player = getController().getPlayer();
        try {   
            if (player.isPlaying()) {
                player.pause();
            }   
            try {
                player.nextSong();
                player.play();
            } catch (EmptyQueueException e) {
                System.out.println("There are no songs in queue.");
            }
        } catch (InterruptedException e) {
            // pass
            // This will only be triggered from a program shutdown
        }
    }

    @Override
    // EFFECTS: thread safe call on refreshQueue
    public void onQueueChange() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                refreshQueue();
            }
        });
    }
}