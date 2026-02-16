package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import exceptions.EmptyQueueException;

// Central controller of the music app player
//
// contains and manages playlists, songs, a queue, a FileAccessor, a playbackSession, and an audioThread
// two boolean fields that determine where the queue is playing and is looping
//
// All audio is to be played by the audioThread which will play the first song in the queue 
// songs and playlists are to be added to the queue to be played
//
// This class coordinates how songs are configured and played but does not directly deal with files or audio control
// File management is done by a designated FileAccessor and audio is controlled by a designated AudioThread 
public class Player {
    private String fileDirectory = "src\\data\\";
    private ArrayList<Playlist> playlists;
    private Song[] songs; // this is an array since songs cannot be added or removed as they depends on
                          // the .wav files
    private LinkedBlockingDeque<Song> queue;
    private Song currentSong;
    private FileAccessor fileAccessor;
    private PlaybackSession playbackSession;
    private AudioThread audioThread;
    private boolean isLooping;

    // EFFECTS: setup the player by creating a FileAccessor and using it to fetch
    // all songs within the data folder
    // Creates an empty queue and empty list of playlists
    public Player() {
        fileAccessor = new FileAccessor(fileDirectory);
        songs = fileAccessor.fetchAllSongs();
        playlists = new ArrayList<Playlist>();
        queue = new LinkedBlockingDeque<Song>();
        isLooping = false;
    }

    // MODIFIES: this
    // EFFECTS: add playlist to playlists if it is not already in playlists
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
    }

    // REQUIRES: playlists.contains(playlist)
    // MODIFIES: this
    // EFFECTS: remove playlist from playlists
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
    }

    // REQUIRES: (!audioThread.isAlive() or audioThread == null) and !currentSong ==
    // null
    // MODIFIES: this
    // EFFECTS: plays the current song by creating a new audioThread and starting it
    // if there is no current song pull the next song from the queue
    public void play() throws InterruptedException {
        audioThread = new AudioThread(playbackSession);
        audioThread.start();
    }

    // MODIFIES: this
    // EFFECTS: sets the current song to the first song in the queue
    // if isLooping and currentSong != null, then put the previous currentSong at
    // the end of the queue
    // throws EmptyQueueException if the queue is empty
    public void nextSong() throws InterruptedException, EmptyQueueException {
        if (isLooping && currentSong != null) {
            queue.put(currentSong);
        }
        if (!queue.isEmpty()) {
            currentSong = queue.take();
            playbackSession = new PlaybackSession(currentSong.getFile(), this);
        } else {
            throw new EmptyQueueException();
        }
    }

    // REQUIRES: audioThread.isAlive()
    // MODIFIES: this
    // EFFECTS: pause the current playing song by interupting the audio thread
    public void pause() {
        audioThread.interrupt();
    }

    // MODIFIES: this
    // EFFECTS: add song to the end of queue
    public void addSongToQueue(Song song) throws InterruptedException {
        queue.put(song);
    }

    // MODIFIES: this
    // EFFECTS: add all songs within playlist to the end of queue in the order they
    // appear withing playlist
    public void addPlaylistToQueue(Playlist playlist) throws InterruptedException {
        for (Song s : playlist.getSongs()) {
            queue.put(s);
        }
    }

    public FileAccessor getFileAccessor() {
        return fileAccessor;
    }

    public Song[] getSongs() {
        return songs;
    }

    public BlockingQueue<Song> getQueue() {
        return queue;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public AudioThread getAudioThread() {
        return audioThread;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    // REQUIRES: !audioThread == null
    public long getPausedPosition() {
        return audioThread.getPausedPosition();
    }

    public boolean isLooping() {
        return isLooping;
    }

    // EFFECTS: returns if the player is playing anything depending on whether the
    // audioThread is alive
    public boolean isPlaying() {
        if (audioThread == null) {
            return false;
        } else {
            return audioThread.isAlive();
        }
    }

    public void setLooping(boolean isLooping) {
        this.isLooping = isLooping;
    }
}
