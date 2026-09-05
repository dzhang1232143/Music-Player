package model;

import java.nio.file.FileAlreadyExistsException;
import java.io.File;

public class Song {

    private File wavFile;
    private String artist;
    private String name;

    // REQUIRES: wavFile refers to a.wav file within FileAccessor.FILE_DIRECTORY
    // EFFECTS: Creates a Song instance, sets this.wavFile and name according to
    // wavFile
    public Song(File wavFile) {
        this.wavFile = wavFile;
        artist = "";
        String fileName = wavFile.getName();
        // substring to remove ".wav" at the end
        name = fileName.substring(0, fileName.length() - 4);
    }

    // MODIFIES: this
    // EFFECTS: sets name to newName and uses the FileAccessor method to rename the
    // file coresponding to this song
    public void renameSong(String newName, FileAccessor fileAccessor) throws FileAlreadyExistsException {
        EventLog.getInstance().logEvent(new Event("Renamed Song " + this + " to " + newName));
        wavFile = fileAccessor.renameFile(wavFile, newName);        
        name = newName;
    }

    public void setArtist(String artist) {
        EventLog.getInstance().logEvent(new Event("Changed Song " + this + " artist to " + artist));
        this.artist = artist;
    }

    public String getArtist() {
        return artist;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return wavFile;
    }

    public String toString() {
        return name;
    }
}
