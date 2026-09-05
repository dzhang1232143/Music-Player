package ui.tabs;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;

import model.FileAccessor;
import model.Player;
import model.Playlist;
import model.Song;
import ui.PlayerUI;
// Represents a tab that will display all saved playlists and the songs within them 
// This tab will allow users to:
//  - add new playlists
//  - delete playlists
//  - rename playlists
//  - add songs to playlists
//  - remove songs from playlists
//  - queue playlists

public class PlaylistTab extends Tab {

    private JTree modelPlaylists;
    private DefaultMutableTreeNode rootPlaylists;
    private DefaultMutableTreeNode selectedNode;
    private Song[] songs;
    // private DefaultListModel<String> modelSongs;
    // private JList<String> songs;

    // EFFECTS: constructs the playlists tab
    public PlaylistTab(PlayerUI controller) {
        super(controller);
        songs = controller.getPlayer().getSongs();
        setLayout(new BorderLayout());

        setupDisplay();
        addControls();
    }

    // EFFECTS: creates a visual list of all the playlists
    private void setupDisplay() {
        rootPlaylists = new DefaultMutableTreeNode("Playlists");
        modelPlaylists = new JTree(rootPlaylists);

        modelPlaylists.addTreeSelectionListener(e -> updateSelectedNode());

        JScrollPane scrollPane = new JScrollPane(modelPlaylists);
        add(scrollPane, BorderLayout.CENTER);

        pullPlaylistFromSave();
    }

    // MODIFIES: this
    // EFFECTS: update the selectedNode
    private void updateSelectedNode() {
        selectedNode = (DefaultMutableTreeNode) modelPlaylists.getLastSelectedPathComponent();
    }

    // MODIFIES: this
    // EFFECTS: creates and sets all the functionality for playlist tab buttons
    private void addControls() {
        JButton addQueueButton = new JButton("Add to Queue");
        JButton removeButton = new JButton("Delete");
        JButton renameButton = new JButton("Rename");
        JButton newPlaylistButton = new JButton("Create New Playlist");
        JButton newSongButton = new JButton("Add Song to Playlist");

        addQueueButton.addActionListener(e -> handleAddToQueue());
        removeButton.addActionListener(e -> handleRemove());
        renameButton.addActionListener(e -> handleRename());
        newPlaylistButton.addActionListener(e -> handleNewPlaylist());
        newSongButton.addActionListener(e -> handleAddSong());

        JPanel buttonRow = new JPanel();
        buttonRow.setLayout(new FlowLayout());
        buttonRow.add(addQueueButton);
        buttonRow.add(removeButton);
        buttonRow.add(renameButton);
        buttonRow.add(newPlaylistButton);
        buttonRow.add(newSongButton);

        add(buttonRow, BorderLayout.SOUTH);
    }

    // EFFECTS: if selectedNode is a playlist, open a window to allow user to select
    // song to be added to selectedNode
    // otherwise do nothing
    private void handleAddSong() {
        if (selectedNode != null && selectedNode != rootPlaylists && selectedNode.getParent().equals(rootPlaylists)) {
            Song selectedSong = handleUserSelectAddSong();
            if (selectedSong != null) {
                Playlist playlist = (Playlist) selectedNode.getUserObject();
                if (playlist.addSong(selectedSong)) {
                    selectedNode.add(new DefaultMutableTreeNode(selectedSong));
                    updateNode(selectedNode);
                }
            }
        }
    }

    // EFFECTS: creates a popup window that asks the user to select the song they
    // want to add and returns the selected song
    private Song handleUserSelectAddSong() {
        Song selectedSong = (Song) JOptionPane.showInputDialog(
                null,
                "Select a song:",
                "Add Song",
                JOptionPane.PLAIN_MESSAGE,
                null,
                songs,
                songs[0]);
        return selectedSong;
    }

    // EFFECTS: asks the user for new playlist name and creates and adds a new
    // playlist with that name to player
    private void handleNewPlaylist() {
        String name = handleGetNewName();
        if (name != null) {
            Player player = getController().getPlayer();
            Playlist newPlaylist = new Playlist(name);
            player.addPlaylist(newPlaylist);
            rootPlaylists.add(new DefaultMutableTreeNode(newPlaylist));
            updateNode(rootPlaylists);
        }
    }

    // EFFECTS: renames the object represented by selectedNode unless it is
    // rootPlaylists
    private void handleRename() {
        if (selectedNode != null && selectedNode != rootPlaylists) {
            String newName = handleGetNewName();
            if (newName != null) {
                if (selectedNode.getParent().equals(rootPlaylists)) {
                    Playlist playlist = (Playlist) selectedNode.getUserObject();
                    playlist.setName(newName);
                } else {
                    Song song = (Song) selectedNode.getUserObject();
                    FileAccessor fileAccessor = getController().getPlayer().getFileAccessor();
                    try {
                        song.renameSong(newName, fileAccessor);
                    } catch (Exception e) {
                        alreadyExistsErrorDisplay();
                    }
                }
                updateNode(rootPlaylists);
            }
        }
    }

    // EFFECTS: display an error message to the user that the name already exists
    private void alreadyExistsErrorDisplay() {
        JOptionPane.showMessageDialog(
                null,
                "Song name already exists",
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    // EFFECTS: If selected node represents a song, remove it from its playlist,
    // If selectedNode represents a playlsit, remove it from Player
    private void handleRemove() {
        if (selectedNode != null && selectedNode != rootPlaylists) {
            if (selectedNode.getParent().equals(rootPlaylists)) {
                removePlaylist();
            } else {
                removeSong();
            }
            updateNode(rootPlaylists);
        }
    }

    // EFFECTS: queue the the object that selectedNode is reperesenting if it is a song or playlist
    //          otherwise do nothing
    private void handleAddToQueue() {
        if (selectedNode != null && selectedNode != rootPlaylists) {
            if (selectedNode.getParent().equals(rootPlaylists)) {
                queuePlaylist();
            } else {
                queueSong();
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes the selected song from playlist
    private void removeSong() {
        Song song = (Song) selectedNode.getUserObject();
        DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
        Playlist parentPlaylist = (Playlist) parent.getUserObject();
        parentPlaylist.removeSong(song);
        parent.remove(selectedNode);
    }

    // EFFECTS: remove the selected playlist from Player
    private void removePlaylist() {
        Playlist playlist = (Playlist) selectedNode.getUserObject();
        Player player = getController().getPlayer();
        player.removePlaylist(playlist);
        rootPlaylists.remove(selectedNode);
    }

    // EFFECTS: Creates a popup window to ask to the user to input a new name
    private String handleGetNewName() {
        String name = (String) JOptionPane.showInputDialog("Enter new name:");
        return name;
    }

    // REQUIRES: selectedNode represents a Playlist
    // EFFECTS: queue the selected playlist
    private void queuePlaylist() {
        Playlist playlist = (Playlist) selectedNode.getUserObject();
        Player player = getController().getPlayer();
        try {
            player.addPlaylistToQueue(playlist);
        } catch (Exception e) {
            // pass
        }   
    }

    // REQUIRES: selectedNode represents a Song
    // EFFECTS: queue the selected song
    private void queueSong() {
        Song song = (Song) selectedNode.getUserObject();
        Player player = getController().getPlayer();
        try {
            player.addSongToQueue(song);
        } catch (Exception e) {
            // pass
        }   
    }

    // MODIFIES: this
    // EFFECTS: updates the modelPlaylists based on Player
    private void pullPlaylistFromSave() {
        Player player = getController().getPlayer();
        ArrayList<Playlist> playlists = player.getPlaylists();

        for (Playlist p : playlists) {
            DefaultMutableTreeNode playlistNode = new DefaultMutableTreeNode(p);
            rootPlaylists.add(playlistNode);

            ArrayList<Song> songs = p.getSongs();
            for (Song s : songs) {
                DefaultMutableTreeNode songNode = new DefaultMutableTreeNode(s);
                playlistNode.add(songNode);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: reloads the the display of modelPlaylist of the given node
    private void updateNode(MutableTreeNode node) {
        ((DefaultTreeModel) modelPlaylists.getModel()).reload(node);
    }
}
