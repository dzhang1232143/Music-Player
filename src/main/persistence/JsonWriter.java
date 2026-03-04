package persistence;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

import org.json.*;

// Represents a writer that writes JSON representation of Player to file
// All methods are taken from JsonSerializationDemo and modified
public class JsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;

    // EFFECTS: constructs writer to write to destination file
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of player to file
    public void write(Player p) {
        JSONObject json = playerToJson(p);
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }

    // EFFECTS: returns player as a JsonObject
    private JSONObject playerToJson(Player p) {
        JSONObject json = new JSONObject();
        json.put("fileDirectory", p.getFileDirectory());
        json.put("playlists", playlistsToJson(p.getPlaylists()));
        json.put("queue", queueToJson(p.getQueue(), p.getCurrentSong()));
        json.put("pausedPosition", p.getPausedPosition());
        json.put("isLooping", p.isLooping());
        return json;
    }

    // EFFECTS: returns playlists as a JsonArray
    private JSONArray playlistsToJson(ArrayList<Playlist> playlists) {
        JSONArray jsonArray = new JSONArray();

        for (Playlist p : playlists) {
            jsonArray.put(playlistToJson(p));
        }

        return jsonArray;
    }

    // EFFECTS: returns playlist as a JsonObject
    private JSONObject playlistToJson(Playlist playlist) {
        JSONObject json = new JSONObject();
        json.put("name", playlist.getName());
        json.put("songs", songsToJson(playlist.getSongs()));
        return json;
    }

    // EFFECTS: returns songs as a JsonArray
    private JSONArray songsToJson(ArrayList<Song> songs) {
        JSONArray jsonArray = new JSONArray();

        for (Song s : songs) {
            jsonArray.put(songToJson(s));
        }

        return jsonArray;
    }

    // EFFECTS: returns song as a JsonObject
    private JSONObject songToJson(Song song) {
        JSONObject json = new JSONObject();
        json.put("name", song.getName());
        json.put("artist", song.getArtist());
        return json;
    }

    // EFFECTS: returns queue as a JsonArray
    private JSONArray queueToJson(BlockingQueue<Song> queue, Song currentSong) {
        JSONArray jsonArray = new JSONArray();
        if (currentSong != null) {
            jsonArray.put(songToJson(currentSong));
        }
        for (Song s : queue) {
            jsonArray.put(songToJson(s));
        }

        return jsonArray;
    }
}
