package model;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import exceptions.EmptyQueueException;

import javax.sound.sampled.AudioInputStream;
import java.io.File;

// Represents the current playback session
// has a Clip to play audio, AudioInputStream to know where to play audio from, 
// wavFile of the file it is playing, and pausedPosition of what point it was last paused at
//
// PlaybackSession is responsible for playing and pausing playing audio of .wav files
// a new PlaybackSession each time a new song is going to be played and the last one will be discarded
public class PlaybackSession {

    private Clip clip;
    private AudioInputStream audioInputStream;
    private File wavFile;
    private long pausedPosition;
    private Player player;

    // REQUIRES: wavFile correctly refers to one of the .wav files within the data
    // folder
    // EFFECTS: create a PlaybackSession with a wavFile that it will play and sets
    // paused position to 0
    public PlaybackSession(File wavFile, Player player) {
        this.wavFile = wavFile;
        pausedPosition = 0;
        this.player = player;
    }

    // REQUIRES: The file type of wavFile is .wav
    // EFFECTS: Plays the wavFile from pausedPosition, if interuppted, save the
    // current pausedPosition
    // always close clip and audiostream
    public void play() throws Exception {
        try {
            clip = AudioSystem.getClip();
            audioInputStream = AudioSystem.getAudioInputStream(wavFile);
            clip.open(audioInputStream);
            clip.setMicrosecondPosition(pausedPosition);
            clip.start();
            // sleep for the length of the .wav file
            Thread.sleep(clip.getMicrosecondLength() / 1000);
            playNext();
        } catch (InterruptedException e) {
            pausedPosition = clip.getMicrosecondPosition();
        } finally {
            // close clip and audioInputStream to prevent data leak
            clip.close();
            audioInputStream.close();
        }
    }

    public long getPausedPosition() {
        return pausedPosition;
    }

    private void playNext() throws InterruptedException {
        try { 
            player.nextSong();
            player.play();
        } catch (EmptyQueueException e) {
            // This exception means that we are done playing everything in the queue
            // All that needs to be done is the thread needs to stop evaluating so we can leave this blank
        } 
    }
}
