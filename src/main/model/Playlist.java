package model;

import java.util.ArrayList;

// represents a playlist that has a list of songs and a name
public class Playlist {

    ArrayList<Song> songs;
    String name;

    // EFFECTS: Creates an empty playlist with given name and no songs in it
    public Playlist(String name) {
        songs = new ArrayList<>();
        this.name = name;
    }

    // EFFECTS: add song to the end of songs if its is not already within songs
    // returns whether the song was added
    public boolean addSong(Song song) {
        if (!songs.contains(song)) {
            songs.add(song);
            return true;
        }
        return false;
    }

    // EFFECTS: remove song from songs if it is within songs
    // returns whether the song was removed
    public boolean removeSong(Song song) {
        if (songs.contains(song)) {
            songs.remove(song);
            return true;
        }
        return false;
    }

    public void setName(String newName) {
        name = newName;
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public String getName() {
        return name;
    }
}
