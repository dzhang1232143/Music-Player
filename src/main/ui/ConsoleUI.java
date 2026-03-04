package ui;

import model.Player;
import model.Playlist;
import model.Song;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Scanner;

import exceptions.EmptyQueueException;

// Represents a console based version of the music player program
// will take in all user inputs through the console and all output print statements
// Has a Player to process inputs

// much of the code was taken from Lab 4 FlashCard Reviewer and modified to fit this program
public class ConsoleUI {
    private Player player;
    private Scanner scanner;
    private boolean isProgramRunning;
    private String jsonSource;

    // contructor taken from FlashCard Reviewer
    // EFFECTS: creates an instance of ConsoleUI for the music player app
    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
        this.isProgramRunning = true;
        this.jsonSource = ".\\src\\data\\json\\save.json";

        printDivider();
        System.out.println("Welcome to the Music Player app!");
        printDivider();
        handleLoadMenu();

        while (isProgramRunning) {
            handleMainMenu();
        }
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the load menu
    private void handleLoadMenu() {
        displayLoadMenu();
        String input = this.scanner.nextLine();
        processLoadMenuCommands(input);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the load menu
    private void displayLoadMenu() {
        printDivider();
        System.out.println("Would you like to load your last save? (Y/n):");
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the main menu
    private void processLoadMenuCommands(String input) {
        printDivider();
        switch (input) {
            case "y":
                load();
                break;
            case "Y":
                load();
                break;
            default:
                System.out.println("Save not loaded");
                player = new Player(".\\src\\data\\songs");
                break;
        }
        printDivider();
    }

    // MODIFIES: this
    // EFFECTS: load the json save of player
    private void load() {
        JsonReader reader = new JsonReader(jsonSource);
        try {
            player = reader.read();
            System.out.println("Save successfully loaded");
        } catch (IOException e) {
            System.out.println("IOException has occured.");
        }
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the main menu
    private void handleMainMenu() {
        try {
            displayMainMenu();
            String input = this.scanner.nextLine();
            processMainMenuCommands(input);
        } catch (InterruptedException e) {
            // Main Thread should NEVER be interrupted so this catch should never run
        }
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the main menu
    private void displayMainMenu() {
        printDivider();
        System.out.println("Please select an option:\n");
        System.out.println("p: Play/Pause");
        System.out.println("a: Add a new playlist");
        System.out.println("v: Viewing menu");
        System.out.println("c: Clear the queue");
        System.out.println("q: Exit the application");
        printDivider();
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the main menu
    private void processMainMenuCommands(String input) throws InterruptedException {
        printDivider();
        switch (input) {
            case "p":
                handlePlayPause();
                break;
            case "a":
                addNewPlaylist();
                break;
            case "v":
                handleViewingMenu();
                break;
            case "c":
                clearQueue();
                break;
            case "q":
                quitApplication();
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
        printDivider();
    }

    // MODIFIES: this
    // EFFECTS: clears all songs from the queue
    public void clearQueue() {
        player.getQueue().clear();
        System.out.println("The queue has been cleared.");
    }

    // method taken from Flashcard Reviewer
    // MODIFIES: this
    // EFFECTS: prints a closing message and marks the program as not running
    public void quitApplication() {
        System.out.println("Thanks for using the Music Player app!");
        System.out.println("Have a good day!");
        if (player.isPlaying()) {
            player.pause();
        }
        handleSaveMenu();
        this.isProgramRunning = false;
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the save menu
    private void handleSaveMenu() {
        displaySaveMenu();
        String input = this.scanner.nextLine();
        processSaveMenuCommands(input);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the save menu
    private void displaySaveMenu() {
        printDivider();
        System.out.println("Would you like to save? (Y/n):");
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the main menu
    private void processSaveMenuCommands(String input) {
        printDivider();
        switch (input) {
            case "y":
                save();
                break;
            case "Y":
                save();
                break;
            default:
                System.out.println("Instance not saved.");
                break;
        }
        printDivider();
    }

    
    private void save() {
        JsonWriter writer = new JsonWriter(jsonSource);
        try {
            writer.open();
            writer.write(player);
            writer.close();
            System.out.println("Instance successfully saved.");
        } catch (IOException e) {
            System.out.println("Save failed (IOException).");
        }
    }

    // MODIFIES: this
    // EFFECTS: pause the current song if it is playing and play if it is paused
    // If there is no current song, then pull the next song from the queue first
    // If there are no songs in the queue, then tell the user so
    private void handlePlayPause() throws InterruptedException {
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
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: Adds a playlist to the list of playlists within player
    private void addNewPlaylist() {
        System.out.println("Please enter the playlists name");
        String name = this.scanner.nextLine();

        Playlist playlist = new Playlist(name);

        player.addPlaylist(playlist);
        System.out.println("\nNew Playlist successfully created!");
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the viewing menu
    private void handleViewingMenu() throws InterruptedException {
        displayViewingMenu();
        String input = this.scanner.nextLine();
        processViewingMenuCommands(input);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the viewing menu
    private void displayViewingMenu() {
        printDivider();
        System.out.println("Please select an option:\n");
        System.out.println("s: View all songs");
        System.out.println("p: View all Playlists");
        System.out.println("v: View songs all songs in the queue");
        System.out.println("q: Return to main menu");
        printDivider();
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the viewing menu
    private void processViewingMenuCommands(String input) throws InterruptedException {
        printDivider();
        switch (input) {
            case "s":
                viewAllSongs();
                handleSongMenu();
                break;
            case "p":
                handlePlaylistViewing();
                break;
            case "v":
                viewQueue();
                break;
            case "q":
                System.out.println("Returning to main menu");
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
    }

    // EFFECTS: Displays all playlists and the playlist menu,
    // displays an error if there are no playlists
    private void handlePlaylistViewing() throws InterruptedException {
        if (player.getPlaylists().size() == 0) {
            System.out.println("Error: There are no Playlists");
        } else {
            viewAllPlaylists();
            handlePlaylistMenu();
        }
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the playlist menu
    private void handlePlaylistMenu() throws InterruptedException {
        displayPlaylistMenu();
        String input = this.scanner.nextLine();
        processPlaylistMenuCommands(input);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the playlist menu
    private void displayPlaylistMenu() {
        printDivider();
        System.out.println("Please select an option:\n");
        System.out.println("v: View all songs within a Playlist");
        System.out.println("d: Delete a playlist");
        System.out.println("r: Rename a playlist");
        System.out.println("a: Add a playlist to the queue");
        System.out.println("q: Return to main menu");
        printDivider();
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the playlist menu
    private void processPlaylistMenuCommands(String input) throws InterruptedException {
        printDivider();
        switch (input) {
            case "v":
                Playlist selectedPlaylist = handleViewSongsInPlaylist();
                viewSongs(selectedPlaylist);
                handleSongsInPlaylistMenu(selectedPlaylist);
                break;
            case "d":
                handleDeletePlaylist();
                break;
            case "r":
                handleRenamePlaylist();
                break;
            case "a":
                handleAddPlaylistToQueue();
                break;
            case "q":
                System.out.println("Returning to main menu");
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
    }

    // REQUIRES: player.getPlaylists().size() > 0
    // EFFECTS: allows user to select a playlist and print out all songs within it
    private Playlist handleViewSongsInPlaylist() {
        ArrayList<Playlist> playlists = player.getPlaylists();
        int index = userSelect(playlists.size(), "playlist");
        Playlist playlist = player.getPlaylists().get(index);
        ArrayList<Song> songs = playlist.getSongs();
        for (int i = 0; i > 0; i++) {
            System.out.println(i + 1 + ". " + songs.get(i));
        }
        return playlist;
    }

    // REQUIRES: player.getPlaylists().size() > 0
    // EFFECTS: allows user to select a playlist and delete it
    private void handleDeletePlaylist() {
        ArrayList<Playlist> playlists = player.getPlaylists();
        int index = userSelect(playlists.size(), "playlist");
        player.removePlaylist(playlists.get(index));
        System.out.println("Playlist deleted.");
    }

    // REQUIRES: player.getPlaylists().size() > 0
    // EFFECTS: allows user to select a playlist and rename it
    private void handleRenamePlaylist() throws InterruptedException {
        ArrayList<Playlist> playlists = player.getPlaylists();

        int index = userSelect(playlists.size(), "playlist");
        System.out.println("Please input a new name for this song.");
        String input = this.scanner.nextLine();
        playlists.get(index).setName(input);
        System.out.println("Playlist renamed.");
    }

    // REQUIRES: player.getPlaylists().size() > 0
    // EFFECTS: allows user to select a playlist add it to the queue
    private void handleAddPlaylistToQueue() throws InterruptedException {
        ArrayList<Playlist> playlists = player.getPlaylists();
        int index = userSelect(playlists.size(), "playlist");
        player.addPlaylistToQueue(playlists.get(index));
        System.out.println("Playlist Queued.");
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the SongsInPlaylist menu
    private void handleSongsInPlaylistMenu(Playlist playlist) {
        displaySongsInPlaylistMenu();
        String input = this.scanner.nextLine();
        processSongsInPlaylistMenuCommands(input, playlist);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the SongsInPlaylist
    // menu
    private void displaySongsInPlaylistMenu() {
        System.out.println("Please select an option:\n");
        System.out.println("a: Add a song");
        System.out.println("r: Remove a song");
        System.out.println("q: Return to main menu");
        printDivider();
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the SongsInPlaylist menu
    private void processSongsInPlaylistMenuCommands(String input, Playlist playlist) {
        printDivider();
        switch (input) {
            case "a":
                handleAddSong(playlist);
                break;
            case "r":
                handleRemoveSong(playlist);
                break;
            case "q":
                System.out.println("Returning to main menu");
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
        printDivider();
    }

    // MODIFIES: this, playlist
    // EFFECTS: Adds a song to playlist based on user selection
    private void handleAddSong(Playlist playlist) {
        System.out.println("Please select a song.");
        viewAllSongs();
        int index = userSelect(player.getSongs().length, "song");
        playlist.addSong(player.getSongs()[index]);
        System.out.println("Song Added.");
    }

    // MODIFIES: this, playlist
    // EFFECTS: removes a song to playlist based on user selection
    private void handleRemoveSong(Playlist playlist) {
        ArrayList<Song> songs = playlist.getSongs();
        int index = userSelect(songs.size(), "song");
        playlist.removeSong(songs.get(index));
        System.out.println("Song removed.");
    }

    // EFFECTS: prints out all songs within playlist
    private void viewSongs(Playlist playlist) {
        ArrayList<Song> songs = playlist.getSongs();
        for (int i = 0; i < songs.size(); i++) {
            System.out.println(i + 1 + ". " + songs.get(i).getName());
        }
    }

    // EFFECTS: print out all songs in player
    private void viewAllSongs() {
        Song[] songs = player.getSongs();
        printAllSongs(songs);
    }

    // REQUIRES: player.getPlaylists().size() > 0
    // EFFECTS: print out all playlists
    private void viewAllPlaylists() {
        ArrayList<Playlist> playlists = player.getPlaylists();
        for (int i = 0; i < playlists.size(); i++) {
            System.out.println(i + 1 + ". " + playlists.get(i).getName());
        }
    }

    // EFFECTS: print out all songs in the queue
    private void viewQueue() {
        Song[] queue = player.getQueue().toArray(new Song[player.getQueue().size()]);
        // add the current song to the front of the printed queue
        if (player.getCurrentSong() != null) {
            Song[] songs = new Song[queue.length + 1];
            songs[0] = player.getCurrentSong();
            System.arraycopy(queue, 0, songs, 1, queue.length);
            printAllSongs(songs);
        } else {
            printAllSongs(queue);
        }
    }

    // EFFECTS: print out all songs
    private void printAllSongs(Song[] songs) {
        for (int i = 0; i < songs.length; i++) {
            System.out.println(i + 1 + ". " + songs[i].getName());
        }
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays and processes inputs for the song menu
    private void handleSongMenu() throws InterruptedException {
        displaySongMenu();
        String input = this.scanner.nextLine();
        processSongMenuCommands(input);
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: displays a list of commands that can be used in the song menu
    private void displaySongMenu() {
        System.out.println("Please select an option:\n");
        System.out.println("a: Add a song to the queue");
        System.out.println("r: Rename a song");
        System.out.println("q: Return to main menu");
        printDivider();
    }

    // method taken from FlashCard Reviewer
    // EFFECTS: processes the user's input in the song menu
    private void processSongMenuCommands(String input) throws InterruptedException {
        printDivider();
        switch (input) {
            case "a":
                handleAddSongToQueue();
                break;
            case "r":
                handleRenamingSong();
                break;
            case "q":
                System.out.println("Returning to main menu.");
                break;
            default:
                System.out.println("Invalid option inputted. Please try again.");
        }
    }

    // EFFECTS: allows user to select a song and add it to the queue
    private void handleAddSongToQueue() throws InterruptedException {
        Song[] songs = player.getSongs();
        int index = userSelect(songs.length, "song");
        player.addSongToQueue(songs[index]);
    }

    // EFFECTS: allows user to select a song and rename it
    private void handleRenamingSong() throws InterruptedException {
        Song[] songs = player.getSongs();
        int index = userSelect(songs.length, "song");
        System.out.println("Please input a new name for this song.");
        String input = this.scanner.nextLine();
        try {
            songs[index].renameSong(input, player.getFileAccessor());
            System.out.println("Song renamed.");
        } catch (FileAlreadyExistsException e) {
            System.out.println("Error: File with that name already exists.");
        }
    }

    // REQUIRES: size > 0
    // EFFECTS: return an int refering to the index of an item that the user selects
    private int userSelect(int size, String type) {
        System.out.println("Please input a number in reference to select a " + type + ".");
        String input = this.scanner.nextLine();
        try {
            int selection = Integer.parseInt(input);
            if (selection > size || selection <= 0) {
                System.out.println("Invalid option inputted. Please try again.");
                return userSelect(size, type);
            } else {
                printDivider();
                return selection - 1;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid option inputted. Please try again.");
            return userSelect(size, type);
        }

    }

    // EFFECTS: prints out a line of dashes to act as a divider
    // method taken from FlashCard Reviewer
    private void printDivider() {
        System.out.println("------------------------------------");
    }
}
