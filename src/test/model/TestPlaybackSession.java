package model;


import static org.junit.jupiter.api.Assertions.*;

import exceptions.EmptyQueueException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestPlaybackSession {
    private FileAccessor testAccessor;
    private Player player;
    Song[] songs;
    
    @BeforeEach
    public void setup() throws InterruptedException {
        player = new Player(".\\src\\test\\model\\test data 6");
        testAccessor = new FileAccessor(".\\src\\test\\model\\test data 6");
        songs = testAccessor.fetchAllSongs();
        player.addSongToQueue(songs[0]);
        player.addSongToQueue(songs[1]);
        player.addSongToQueue(songs[2]);
    }

    @Test
    public void playMultipleSongsTest() throws InterruptedException, EmptyQueueException {
        player.nextSong();
        player.play();
        player.getAudioThread().join();
        player.getAudioThread().join();
        player.getAudioThread().join();
        assertEquals(player.getQueue().size(), 0);
        assertEquals(player.getCurrentSong(), songs[2]);

    }
}
