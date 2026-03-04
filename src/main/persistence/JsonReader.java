package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

import exceptions.EmptyQueueException;

// Represents a reader that reads player from JSON data stored in data folder
public class JsonReader {

    private String source;

    // EFFECTS: constructs a reader to read from the source file
    public JsonReader(String source) {
        this.source = source;
    }

    // method taken from JsonSerializationDemo
    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Player read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parsePlayer(jsonObject);
    }

    // method taken from JsonSerializationDemo
    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }
        return contentBuilder.toString();
    }

    // method taken and modified from JsonSerializationDemo
    // EFFECTS: parses player from JSON object and returns it
    private Player parsePlayer(JSONObject jsonObject) {
        String fileDirectory = jsonObject.getString("fileDirectory");        
        Player p = new Player(fileDirectory);
        addPlaylists(p, jsonObject);
        p.setLooping(jsonObject.getBoolean("isLooping"));
        try {
            addQueue(p, jsonObject);
            p.nextSong();
            p.setPausedPosition(jsonObject.getLong("pausedPosition"));
        } catch (InterruptedException | EmptyQueueException e) {
            // can never be interrupted as this will be on the main thread blocking before user can play
            // Pass setting a position is Queue is empty
        }
        return p;
    }

    // method taken from JsonSerializationDemo
    // MODIFIES: p
    // EFFECTS: parses playlists from JSON object and adds them to player
    private void addPlaylists(Player p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("playlists");
        for (Object json : jsonArray) {
            JSONObject nextPlaylist = (JSONObject) json;
            addPlaylist(p, nextPlaylist);
        }
    }

    // method taken and modified from JsonSerializationDemo
    // MODIFIES: p
    // EFFECTS: parses playlist from JSON object and adds it to player
    private void addPlaylist(Player p, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Playlist playlist = new Playlist(name);
        p.addPlaylist(playlist);
        addSongs(playlist, p, jsonObject);
    }

    // method taken from JsonSerializationDemo
    // MODIFIES: p
    // EFFECTS: parses queue from JSON object and adds it to player
    private void addQueue(Player p, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("queue");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSongToQueue(p, nextSong);
        }
    }

    // method taken from JsonSerializationDemo
    // MODIFIES: p
    // EFFECTS: parses song from JSON object and adds it to queue
    private void addSongToQueue(Player p, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Song song = p.getSong(name);
        song.setArtist(jsonObject.getString("artist"));
        try {
            p.addSongToQueue(song);
        } catch (InterruptedException e) {
            // Can not be interrupted
        }
    }

    // method taken and modified from JsonSerializationDemo
    // MODIFIES: playlist
    // EFFECTS: parses songs from JSON object and adds them to playlist
    private void addSongs(Playlist playlist, Player player, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("songs");
        for (Object json : jsonArray) {
            JSONObject nextSong = (JSONObject) json;
            addSong(playlist, player, nextSong);
        }
    }

    // MODIFIES: playlist
    // EFFECTS: parses song from JSON object and adds it to playlist
    private void addSong(Playlist playlist, Player player, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        Song song = player.getSong(name);
        song.setArtist(jsonObject.getString("artist"));
        playlist.addSong(song);
    }

}
