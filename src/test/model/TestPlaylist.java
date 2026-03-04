package model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestPlaylist {
    private Playlist playlist;
    private Song song1;
    private Song song2;
    private Song song3;

    @BeforeEach
    public void setup() {
        playlist = new Playlist("Playlist 1");
        FileAccessor fileAccessor = new FileAccessor(".\\src\\test\\model\\test data 2\\");
        Song[] songs = fileAccessor.fetchAllSongs();
        song1 = songs[0];
        song2 = songs[1];
        song3 = songs[2];
    }

    @Test
    public void constructorTest() {
        assertEquals(playlist.getName(), "Playlist 1");
        assertEquals(playlist.getSongs(), new ArrayList<Song>());
    }

    @Test
    public void addSingleSongTest() {
        assertTrue(playlist.addSong(song1));
        assertEquals(playlist.getSongs().size(), 1);
        assertEquals(playlist.getSongs().get(0), song1);
    }

    @Test
    public void addSingleSongMultipleTimesTest() {
        assertTrue(playlist.addSong(song1));
        assertFalse(playlist.addSong(song1));
        assertEquals(playlist.getSongs().size(), 1);
        assertEquals(playlist.getSongs().get(0), song1);
    }

    @Test
    public void addMultipleSongsTest() {
        assertTrue(playlist.addSong(song1));
        assertTrue(playlist.addSong(song2));
        assertTrue(playlist.addSong(song3));
        assertFalse(playlist.addSong(song1));
        assertEquals(playlist.getSongs().size(), 3);
        assertEquals(playlist.getSongs().get(0), song1);
        assertEquals(playlist.getSongs().get(1), song2);
        assertEquals(playlist.getSongs().get(2), song3);
    }

    @Test
    public void removeSong() {
        assertTrue(playlist.addSong(song1));
        assertTrue(playlist.addSong(song2));
        assertTrue(playlist.addSong(song3));

        assertTrue(playlist.removeSong(song2));

        assertEquals(playlist.getSongs().size(), 2);
        assertEquals(playlist.getSongs().get(0), song1);
        assertEquals(playlist.getSongs().get(1), song3);

        playlist.removeSong(song3);

        assertEquals(playlist.getSongs().size(), 1);
        assertEquals(playlist.getSongs().get(0), song1);

        playlist.removeSong(song1);

        assertEquals(playlist.getSongs().size(), 0);
    }

    @Test
    public void removeSongNotWithinPlaylist() {
        assertTrue(playlist.addSong(song1));
        assertTrue(playlist.addSong(song3));

        assertFalse(playlist.removeSong(song2));

        assertEquals(playlist.getSongs().size(), 2);
        assertEquals(playlist.getSongs().get(0), song1);
        assertEquals(playlist.getSongs().get(1), song3);
    }

    @Test
    public void setNameTest() {
        playlist.setName("A");
        assertEquals(playlist.getName(), "A");
        playlist.setName("A");
        assertEquals(playlist.getName(), "A");
        playlist.setName("B");
        assertEquals(playlist.getName(), "B");
    }
}
