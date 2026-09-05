package model;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import exceptions.EmptyQueueException;
import ui.tabs.QueueListener;

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
    private ArrayList<Playlist> playlists;
    private Song[] songs; // this is an array since songs cannot be added or removed as they depends on
                          // the .wav files
    private LinkedBlockingDeque<Song> queue;
    private Song currentSong;
    private FileAccessor fileAccessor;
    private PlaybackSession playbackSession;
    private AudioThread audioThread;
    private boolean isLooping;
    private String fileDirectory;

    private QueueListener listener;

    // EFFECTS: setup the player by creating a FileAccessor and using it to fetch
    // all songs within the data folder
    // Creates an empty queue and empty list of playlists
    public Player(String fileDirectory) {
        this.fileDirectory = fileDirectory;
        fileAccessor = new FileAccessor(fileDirectory);
        songs = fileAccessor.fetchAllSongs();
        playlists = new ArrayList<Playlist>();
        queue = new LinkedBlockingDeque<Song>();
        isLooping = false;
    }

    // MODIFIES: this
    // EFFECTS: add playlist to playlists 
    public void addPlaylist(Playlist playlist) {
        playlists.add(playlist);
        EventLog.getInstance().logEvent(new Event("Created new playlist: " + playlist));
    }

    // REQUIRES: playlists.contains(playlist)
    // MODIFIES: this
    // EFFECTS: remove playlist from playlists
    public void removePlaylist(Playlist playlist) {
        playlists.remove(playlist);
        EventLog.getInstance().logEvent(new Event("Removed playlist: " + playlist));
    }

    // REQUIRES: (!audioThread.isAlive() or audioThread == null) and !currentSong ==
    // null
    // MODIFIES: this
    // EFFECTS: plays the current song by creating a new audioThread and starting it
    // if there is no current song pull the next song from the queue
    public void play() throws InterruptedException {
        audioThread = new AudioThread(playbackSession);
        audioThread.start();
        EventLog.getInstance().logEvent(new Event("Started playing " + currentSong));
    }

    // MODIFIES: this
    // EFFECTS: sets the current song to the first song in the queue
    // if isLooping and currentSong != null, then put the previous currentSong at
    // the end of the queue
    // throws EmptyQueueException and set currentSong to null if the queue is empty
    public void nextSong() throws InterruptedException, EmptyQueueException {
        if (isLooping && currentSong != null) {
            queue.put(currentSong);
        }
        if (!queue.isEmpty()) {
            currentSong = queue.take();
            playbackSession = new PlaybackSession(currentSong.getFile(), this);
            notifyQueueChanged();
        } else {
            currentSong = null;
            notifyQueueChanged();
            throw new EmptyQueueException();
        }
    }

    // MODIFIES: this
    // EFFECTS: clears the queue
    public void clearQueue() {
        queue.clear();
        EventLog.getInstance().logEvent(new Event("Queue cleared"));
    }

    // REQUIRES: audioThread.isAlive()
    // MODIFIES: this
    // EFFECTS: pause the current playing song by interupting the audio thread
    public void pause() {
        audioThread.interrupt();
        EventLog.getInstance().logEvent(new Event("Paused playing " + currentSong));
    }

    // MODIFIES: this
    // EFFECTS: add song to the end of queue
    public void addSongToQueue(Song song) throws InterruptedException {
        queue.put(song);
        notifyQueueChanged();
        EventLog.getInstance().logEvent(new Event("Added Song " + song + " to end of queue"));
    }

    // MODIFIES: this
    // EFFECTS: add all songs within playlist to the end of queue in the order they
    // appear within playlist
    public void addPlaylistToQueue(Playlist playlist) throws InterruptedException {
        for (Song s : playlist.getSongs()) {
            queue.put(s);
        }
        notifyQueueChanged();
        EventLog.getInstance().logEvent(new Event("Added Playlist " + playlist + " to end of queue"));
    }

    // EFFECTS: returns the song of given name and null if it doesn't exist
    public Song getSong(String name) {
        for (Song s : songs) {
            if (s.getName().equals(name)) {
                return s;
            }
        }
        return null;
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

    // EFFECT: get the paused position, return 0 if there is no playback session
    public long getPausedPosition() {
        if (playbackSession == null) {
            return 0;
        } else {
            return playbackSession.getPausedPosition();
        }
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
        EventLog.getInstance().logEvent(new Event("Set looping to " + isLooping));
    }

    // MODFIES: this
    // EFFECTS: set the paused position within playbackSession
    public void setPausedPosition(long pausedPosition) {
        playbackSession.setPausedPosition(pausedPosition);
    }

    public String getFileDirectory() {
        return fileDirectory;
    }

    public void setListener(QueueListener listener) {
        this.listener = listener;
    }

    // EFFECTS: notify the Listener that the queue has changed
    public void notifyQueueChanged() {
        if (this.listener != null) {
            listener.onQueueChange();
        }
    }
}
