package model;

// A thread that will handle playing audio while the main thread handles the ui and use input
// All audio is played through playbackSession
// This thread just calls playbackSession and does not handle any audio playing itself
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
