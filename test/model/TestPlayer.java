package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.EmptyQueueException;

public class TestPlayer {
    private Player player;
    private FileAccessor testAccessor;
    private Song[] testSongs;
    private Playlist playlist1;
    private Playlist playlist2;
    private Playlist playlist3;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    public void setup() {
        player = new Player();
        testAccessor = new FileAccessor("src\\test\\model\\test data 4\\");
        testSongs = testAccessor.fetchAllSongs();
        playlist1 = new Playlist("Playlist 1");
        playlist2 = new Playlist("Empty");
        playlist3 = new Playlist("GoodKid");
        song1 = testSongs[0];
        song2 = testSongs[1];
        song3 = testSongs[2];
        playlist1.addSong(song1);
        playlist3.addSong(song1);
        playlist3.addSong(song2);
        playlist3.addSong(song3);

    }

    @Test
    public void constructorTest() {
        assertNotNull(player.getFileAccessor());
        assertNotNull(player.getSongs());
        assertNull(player.getAudioThread());
        assertEquals(new ArrayList<Playlist>(), player.getPlaylists());
        assertEquals(0, player.getQueue().size());
        assertFalse(player.isLooping());
        assertFalse(player.isPlaying());
    }

    @Test
    public void addSinglePlaylistTest() {
        player.addPlaylist(playlist1);
        assertEquals(player.getPlaylists().size(), 1);
        assertTrue(player.getPlaylists().contains(playlist1));
    }

    @Test
    public void addMultiplePlaylistsTest() {
        player.addPlaylist(playlist1);
        player.addPlaylist(playlist2);
        player.addPlaylist(playlist3);
        assertEquals(player.getPlaylists().size(), 3);
        assertEquals(player.getPlaylists().get(0), playlist1);
        assertEquals(player.getPlaylists().get(1), playlist2);
        assertEquals(player.getPlaylists().get(2), playlist3);
    }

    @Test
    public void removeMultiplePlaylistsTest() {
        player.addPlaylist(playlist1);
        player.addPlaylist(playlist2);
        player.addPlaylist(playlist3);
        player.removePlaylist(playlist2);
        assertEquals(player.getPlaylists().size(), 2);
        assertEquals(player.getPlaylists().get(0), playlist1);
        assertEquals(player.getPlaylists().get(1), playlist3);
        player.removePlaylist(playlist3);
        assertEquals(player.getPlaylists().size(), 1);
        assertEquals(player.getPlaylists().get(0), playlist1);
        player.removePlaylist(playlist1);
        assertEquals(player.getPlaylists().size(), 0);
    }

    @Test
    public void playTest() throws InterruptedException {
        player.addSongToQueue(song1);
        player.addSongToQueue(song2);
        try {
            player.nextSong();
            player.play();
            assertTrue(player.getAudioThread().isAlive());
            player.pause();
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }

    }

    @Test
    public void playPauseTest() throws InterruptedException {
        player.addSongToQueue(song1);
        player.addSongToQueue(song2);
        try {
            player.nextSong();
            player.play();
            Thread.sleep(1000);
            player.pause();
            player.getAudioThread().join();
            assertFalse(player.getAudioThread().isAlive());
            assertFalse(player.isPlaying());
            // BELOW TEST CAN FAIL IF JAVA RUNS EXTREMELY SLOWLY
            // INCREASE THE LENGTH THAT Thread SLEEPS FOR IF JAVA IS EXTREMELY SLOW
            assertTrue(player.getPausedPosition() != 0);
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }

    }

    @Test
    public void nextSongNotLoopingTest() throws InterruptedException {
        player.addSongToQueue(song1);
        player.addSongToQueue(song2);
        try {
            player.nextSong();
            assertEquals(player.getCurrentSong(), song1);
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }
        assertEquals(player.getQueue().size(), 1);
        assertTrue(player.getQueue().contains(song2));
        try {
            player.nextSong();
            assertEquals(player.getCurrentSong(), song2);
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }
        assertEquals(player.getQueue().size(), 0);
        try {
            player.nextSong();
            fail("Queue should be empty");
        } catch (EmptyQueueException e) {
            // Exception should be caught
        }
    }

    @Test
    public void nextSongIsLoopingEmptyQueueTest() throws InterruptedException {
        player.setLooping(true);
        try {
            player.nextSong();
            fail("Queue should be empty");
        } catch (EmptyQueueException e) {
            // Exception should be caught
        }
    }

    @Test
    public void newSongIsLoopingSingleSongTest() throws InterruptedException {
        player.setLooping(true);
        player.addSongToQueue(song1);
        try {
            player.nextSong();
            assertEquals(player.getQueue().size(), 0);
            assertEquals(player.getCurrentSong(), song1);
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }
    }

    @Test
    public void newSongIsLoopingMultipleSongsTest() throws InterruptedException {
        player.setLooping(true);
        player.addSongToQueue(song1);
        player.addSongToQueue(song2);
        player.addSongToQueue(song3);
        try {
            player.nextSong();
            assertEquals(player.getQueue().size(), 2);
            assertEquals(player.getCurrentSong(), song1);
            Object[] testQueue = player.getQueue().toArray();
            assertEquals(testQueue[0], song2);
            assertEquals(testQueue[1], song3);

            player.nextSong();
            assertEquals(player.getQueue().size(), 2);
            assertEquals(player.getCurrentSong(), song2);
            testQueue = player.getQueue().toArray();
            assertEquals(testQueue[0], song3);
            assertEquals(testQueue[1], song1);
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }
    }

    @Test
    public void addSongToQueueTest() throws InterruptedException {
        player.addSongToQueue(song1);
        Object[] testQueue = player.getQueue().toArray();
        assertEquals(testQueue.length, 1);
        assertEquals(testQueue[0], song1);
        player.addSongToQueue(song2);
        player.addSongToQueue(song1);
        player.addSongToQueue(song3);
        testQueue = player.getQueue().toArray();
        assertEquals(testQueue.length, 4);
        assertEquals(testQueue[0], song1);
        assertEquals(testQueue[1], song2);
        assertEquals(testQueue[2], song1);
        assertEquals(testQueue[3], song3);
    }

    @Test
    public void addEmptyPlaylistToQueue() throws InterruptedException {
        player.addPlaylistToQueue(playlist2);
        assertEquals(player.getQueue().size(), 0);
    }

    @Test
    public void addMultiplePlaylistsToQueue() throws InterruptedException {
        player.addPlaylistToQueue(playlist1);
        Object[] testQueue = player.getQueue().toArray();
        assertEquals(testQueue.length, 1);
        assertEquals(testQueue[0], song1);

        player.addPlaylistToQueue(playlist3);
        testQueue = player.getQueue().toArray();
        assertEquals(testQueue.length, 4);
        assertEquals(testQueue[0], song1);
        assertEquals(testQueue[1], song1);
        assertEquals(testQueue[2], song2);
        assertEquals(testQueue[3], song3);
        player.addPlaylistToQueue(playlist3);
        testQueue = player.getQueue().toArray();
        assertEquals(player.getQueue().size(), 7);
        assertEquals(testQueue[0], song1);
        assertEquals(testQueue[1], song1);
        assertEquals(testQueue[2], song2);
        assertEquals(testQueue[3], song3);
        assertEquals(testQueue[4], song1);
        assertEquals(testQueue[5], song2);
        assertEquals(testQueue[6], song3);
    }
}
