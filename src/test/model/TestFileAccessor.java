package model;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestFileAccessor {
    private FileAccessor fileAccessor1;
    private FileAccessor fileAccessor2;

    @BeforeEach
    public void setup() {
        fileAccessor1 = new FileAccessor(".\\src\\test\\model\\test data 1\\");
        fileAccessor2 = new FileAccessor(".\\src\\test\\model\\test data 2\\");
    }

    @Test
    public void testEmptyFetchAllFiles() {
        assertEquals(fileAccessor1.fetchAllSongs().length, 0);
    }

    @Test
    public void testMultipleFilesfetchAllFiles() {
        Song[] songs = fileAccessor2.fetchAllSongs();
        assertEquals(songs.length, 3);
        // the strings are taken from copying the relative path of the files
        assertEquals(songs[0].getFile().toPath().toString(), ".\\src\\test\\model\\test data 2\\Eastside.wav");
        assertEquals(songs[1].getFile().toPath().toString(),
                ".\\src\\test\\model\\test data 2\\Mimi's Delivery Service.wav");
        assertEquals(songs[2].getFile().toPath().toString(), ".\\src\\test\\model\\test data 2\\No Time To Explain.wav");
    }

    @Test
    public void testRenameSingleFile() {
        Song[] songs = fileAccessor2.fetchAllSongs();
        File testFile = songs[0].getFile();
        try {
            File renamedFile = fileAccessor2.renameFile(testFile, "ASDF");
            assertNotEquals(renamedFile.getName(), testFile.getName());
            assertFalse(testFile.exists());
            assertTrue(renamedFile.exists());

            // fetch the songs to test if the files have actually been renamed
            Song[] renamedSongs = fileAccessor2.fetchAllSongs();
            File fetchedRenamedFile = renamedSongs[0].getFile();

            assertEquals(fetchedRenamedFile.getName(), renamedFile.getName());
        } catch (FileAlreadyExistsException e) {
            fail("FileAlreadyExistsException should not have been thrown.");
        }
        // rename the file to how it was originally to not break other tests
        try {
            Song[] renamedSongs = fileAccessor2.fetchAllSongs();
            File fetchedRenamedFile = renamedSongs[0].getFile();
            fileAccessor2.renameFile(fetchedRenamedFile, "Eastside");
        } catch (FileAlreadyExistsException e) {
            fail("FileAlreadyExistsException should not have been thrown.");
        }
    }

    @Test
    public void testRenameFileToExistingFile() {
        Song[] songs = fileAccessor2.fetchAllSongs();
        File testFile = songs[0].getFile();
        String existingFileName = "Mimi's Delivery Service";

        try {
            fileAccessor2.renameFile(testFile, existingFileName);
            fail("FileAlreadyExistsException was not thrown.");
        } catch (FileAlreadyExistsException e) {
            assertEquals(e.getMessage(), existingFileName);
        }

        Song[] newlyFetchedSongs = fileAccessor2.fetchAllSongs();
        File newlyFetchedTestFile = newlyFetchedSongs[0].getFile();

        // assert the file did not change name
        assertEquals(newlyFetchedTestFile.getName(), testFile.getName());
    }
}
