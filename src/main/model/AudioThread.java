package model;

// A thread that will handle playing audio while the main thread handles the ui and use input
// All audio is played through playbackSession
// This thread ONLY calls playbackSession and allows it to play audio on a new thread to allow auto playing
public class AudioThread extends Thread {
    PlaybackSession playbackSession;

    // EFFECTS: creates a new AudioThread and sets its playbackSession
    public AudioThread(PlaybackSession playbackSession) {
        this.playbackSession = playbackSession;
    }

    // REQUIRES: playbackSession.getFile() refers to a .wav file
    // EFFECTS: calls playbackSession.play()
    @Override
    public void run() {
        try {
            playbackSession.play();
        } catch (Exception e) {
            // unrecoverable Error has Occurred
            e.printStackTrace();
        }
    }
}
