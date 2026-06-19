package org.example.save;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class SaveManager {

    private static final String SAVE_EXTENSION = ".bin";

    private SaveManager() {
    }

    public static File createSaveFile(String gameName) {
        File savesFolder = getSavesFolder();
        String baseName = sanitizeFileName(gameName);
        File saveFile = new File(savesFolder, baseName + SAVE_EXTENSION);
        int copyNumber = 2;

        while (saveFile.exists()) {
            saveFile = new File(savesFolder, baseName + "-" + copyNumber + SAVE_EXTENSION);
            copyNumber++;
        }

        return saveFile;
    }

    public static void save(GameSave gameSave, File saveFile) {
        try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            output.writeObject(gameSave);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save game: " + saveFile.getAbsolutePath(), e);
        }
    }

    public static GameSave load(File saveFile) {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (GameSave) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("Could not load game: " + saveFile.getAbsolutePath(), e);
        }
    }

    public static List<File> listSaveFiles() {
        File savesFolder = getSavesFolder();
        File[] files = savesFolder.listFiles(file -> file.isFile() && file.getName().endsWith(SAVE_EXTENSION));

        if (files == null) {
            return new ArrayList<>();
        }

        Arrays.sort(files, Comparator.comparing(File::lastModified).reversed());
        return new ArrayList<>(Arrays.asList(files));
    }

    public static File getSavesFolder() {
        File savesFolder = new File(System.getProperty("user.dir"), "saves");
        if (!savesFolder.exists() && !savesFolder.mkdirs()) {
            throw new IllegalStateException("Could not create saves folder: " + savesFolder.getAbsolutePath());
        }

        if (!savesFolder.isDirectory()) {
            throw new IllegalStateException("Save path is not a folder: " + savesFolder.getAbsolutePath());
        }

        return savesFolder;
    }

    private static String sanitizeFileName(String gameName) {
        String sanitized = gameName.trim().replaceAll("[^a-zA-Z0-9._-]", "_");
        return sanitized.isEmpty() ? "Game" : sanitized;
    }
}
