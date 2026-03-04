package persistence;

import model.*;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestJsonReader {
    // Test taken from JsonSerializationDemo
    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("\\null\\filepath.json");
        try {
            reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    // Test taken and modified from JsonSerializationDemo
    @Test
    void testReaderEmptyJson() {
        JsonReader reader = new JsonReader(".\\src\\test\\persistence\\jsonTestData\\testReaderEmptyPlayer.json");
        try {
            Player p = reader.read();
            assertEquals(0, p.getPlaylists().size());
            assertEquals(0, p.getQueue().size());
            assertNull(p.getCurrentSong());
            assertFalse(p.isLooping());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    // Test taken and modified from JsonSerializationDemo
    @Test
    void testReaderGeneralPlayer() {
        JsonReader reader = new JsonReader(".\\src\\test\\persistence\\jsonTestData\\testReaderGeneralPlayer.json");
        try {
            Player p = reader.read();
            ArrayList<Playlist> playlists = p.getPlaylists();
            assertEquals(2, playlists.size());
            Playlist p1 = playlists.get(0);
            Playlist p2 = playlists.get(1);
            assertEquals("Rock", p1.getName());
            assertEquals(2, p1.getSongs().size());
            assertEquals("Eastside", p1.getSongs().get(0).getName());
            assertEquals("No Time To Explain", p1.getSongs().get(1).getName());
            assertEquals("Alt", p2.getName());
            assertEquals(1, p2.getSongs().size());
            assertEquals("Mimi's Delivery Service", p2.getSongs().get(0).getName());
            assertEquals(3, p.getQueue().size());
            assertEquals("Mimi's Delivery Service", p.getCurrentSong().getName());
            assertEquals(12345, p.getPausedPosition());
            assertTrue(p.isLooping());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
