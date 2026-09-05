package model;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class TestSong {
    private Song song;
    private FileAccessor fileAccessor;

    @BeforeEach
    public void setup() {
        fileAccessor = new FileAccessor(".\\src\\test\\model\\test data 3\\");
        Song[] songs = fileAccessor.fetchAllSongs();
        song = songs[0];
    }

    @Test
    public void constructorTest() {
        assertEquals(song.getFile().toPath().toString(), ".\\src\\test\\model\\test data 3\\Eastside.wav");
        assertEquals(song.getName(), "Eastside");
        assertEquals(song.toString(), "Eastside");
        assertEquals(song.getArtist(), "");
    }

    @Test
    public void renameSongTest() {
        try {
            song.renameSong("eastside", fileAccessor);
            assertEquals(song.getFile().toPath().toString(), ".\\src\\test\\model\\test data 3\\eastside.wav");
            assertEquals(song.getName(), "eastside");

            // rename it back to its original name to prevent other test from failing
            song.renameSong("Eastside", fileAccessor);
        } catch (FileAlreadyExistsException e) {
            fail("File should not already exist");
        }
    }

    @Test
    public void renameSongAlreadyExistsTest() {
        try {
            song.renameSong("Mimi's Delivery Service", fileAccessor);
            fail("File should already exist");
        } catch (FileAlreadyExistsException e) {
            // Exception should be caught
        }
        assertEquals(song.getName(), "Eastside");
        File compareFile = new File(".\\src\\test\\model\\test data 3\\Eastside.wav");
        assertEquals(compareFile, song.getFile());
    }

    @Test 
    public void setArtistTest() {
        song.setArtist("GoodKid");
        assertEquals("GoodKid", song.getArtist());
        song.setArtist("GoodKid");
        assertEquals("GoodKid", song.getArtist());
        song.setArtist("Goodkid");
        assertEquals("Goodkid", song.getArtist());
    }
}
