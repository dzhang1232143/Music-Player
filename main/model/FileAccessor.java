package model;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class FileAccessor {
    private String fileDirectory;

    // REQUIRES: fileDirectory is the relative path to a folder that only contains .wav files
    // EFFECT: creates FileAccessor with set fileDirectory of the folder that contains the audio files
    public FileAccessor(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    // REQUIRES: fileName is the relative path to a folder that exists
    // EFFECT: return an array of songs that represents every single file within the
    // given folder path
    public Song[] fetchAllSongs() {
        Path path = Paths.get(fileDirectory);
        File musicFolder = path.toFile();
        File[] files = musicFolder.listFiles();
        // reformats each File to Song
        Song[] songs = new Song[files.length];
        for (int i = 0; i < files.length; i++) {
            songs[i] = new Song(files[i]);
        }
        return songs;
    }

    // REQUIRES: file refers to a .wav file within the FILE_DIRECTORY
    // EFFECT: renames the file that file refers to to newName and
    //         returns the newly named file or throws a FileAlreadyExistsException if the rename fails
    public File renameFile(File file, String newName) throws FileAlreadyExistsException {
        File newFile = new File(fileDirectory + newName + ".wav");
        if (file.renameTo(newFile)) {
            return newFile;
        } else {
            throw new FileAlreadyExistsException(newName);
        }
    }
}
