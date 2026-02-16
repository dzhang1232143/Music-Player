package model;


import java.io.File;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestAudioThread {
    AudioThread audioThread;

    @BeforeEach
    public void setup() {
        File file = new File("src\\test\\model\\test data 5\\Not Wav.mp3");

        PlaybackSession playbackSession = new PlaybackSession(file, new Player());
        audioThread = new AudioThread(playbackSession);
    }

    @Test
    public void unrecoverableErrorTest() throws InterruptedException {
        audioThread.start();
        audioThread.join();
        assertFalse(audioThread.isAlive());
    }

}
