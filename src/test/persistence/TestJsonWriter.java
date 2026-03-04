package persistence;

import model.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import exceptions.EmptyQueueException;

import static org.junit.jupiter.api.Assertions.*;

public class TestJsonWriter {
    // Test taken from JsonSerializationDemo
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyPlayer() {
        try {
            Player p = new Player(".\\src\\test\\persistence\\jsonTestData\\EmptyTestData");
            JsonWriter writer = new JsonWriter(".\\src\\test\\persistence\\jsonTestData\\testWriterEmptyWorkroom.json");
            writer.open();
            writer.write(p);
            writer.close();
            JsonReader reader = new JsonReader(".\\src\\test\\persistence\\jsonTestData\\testWriterEmptyWorkroom.json");
            p = reader.read();
            assertEquals(0, p.getPlaylists().size());
            assertEquals(0, p.getQueue().size());
            assertNull(p.getCurrentSong());
            assertFalse(p.isLooping());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralPlayer() {
        try {
            Player p = createGeneralPlayer();
            JsonWriter writer = new JsonWriter(".\\src\\test\\persistence\\jsonTestData\\testWriterGeneralPlayer.json");
            writer.open();
            writer.write(p);
            writer.close();
            assertCorrectPlayer();
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    // EFFECTS: Create a general player example
    private Player createGeneralPlayer() {
        Player p = new Player(".\\src\\test\\model\\test data 6\\");
        createPlaylistsAndQueue(p);
        p.setPausedPosition(12345);
        p.setLooping(true);
        return p;
    }

    // EFFECTS: Creates all playlists and the queue for an example player
    private void createPlaylistsAndQueue(Player p) {
        Playlist pl = new Playlist("Rock");
        Song s = p.getSong("Eastside");
        s.setArtist("Goodkid");
        pl.addSong(s);
        s = p.getSong("No Time To Explain");
        s.setArtist("Goodkid");
        pl.addSong(s);
        p.addPlaylist(pl);
        pl = new Playlist("Alt");
        s = p.getSong("Mimi's Delivery Service");
        s.setArtist("Goodkid");
        pl.addSong(s);
        p.addPlaylist(pl);
        try {
            p.addSongToQueue(s);
            s = p.getSong("No Time To Explain");
            p.addSongToQueue(s);
            s = p.getSong("Eastside");
            p.addSongToQueue(s);
            p.addSongToQueue(s);
            p.nextSong();
        } catch (InterruptedException e) {
            fail("Should not be interrupted");
        } catch (EmptyQueueException e) {
            fail("Queue should not be empty");
        }
    }

    // EFFECTS: Assert the Player was correctly saved using the reader to read it
    private void assertCorrectPlayer() throws IOException {
        JsonReader reader = new JsonReader(".\\src\\test\\persistence\\jsonTestData\\testWriterGeneralPlayer.json");
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
    }
}
