package base;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Manages saving and loading game state.
 * Provides functionality to save current game progress and resume later.
 * Uses serialization-based approach with file I/O.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class GameSaveManager {
    /** Directory for saving games */
    private static final String SAVE_DIRECTORY = "saves";
    
    /** File extension for saved games */
    private static final String SAVE_EXTENSION = ".jest";
    
    /** Date format for save filenames */
    private static final DateTimeFormatter dateFormat = 
        DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    /**
     * Creates the save directory if it doesn't exist.
     */
    static {
        try {
            Files.createDirectories(Paths.get(SAVE_DIRECTORY));
        } catch (IOException e) {
            System.err.println("Warning: Could not create save directory: " + e.getMessage());
        }
    }

    /**
     * Saves the current game state to a file.
     * Filename format: JEST_VARIANT_TIMESTAMP.jest
     * 
     * @param gameState A map of game data to save
     * @param variant The current game variant
     * @return true if save was successful
     */
    public static boolean saveGame(Map<String, Object> gameState, GameVariant variant) {
        try {
            String timestamp = LocalDateTime.now().format(dateFormat);
            String filename = String.format("JEST_%s_%s%s", 
                variant.name(), timestamp, SAVE_EXTENSION);
            Path filepath = Paths.get(SAVE_DIRECTORY, filename);
            
            // Serialize game state
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(gameState);
            oos.close();
            
            // Write to file
            Files.write(filepath, bos.toByteArray());
            System.out.println("Game saved successfully: " + filename);
            return true;
            
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lists all available saved games.
     * 
     * @return List of save file names
     */
    public static List<String> listSavedGames() {
        List<String> saves = new ArrayList<>();
        try {
            DirectoryStream<Path> stream = Files.newDirectoryStream(
                Paths.get(SAVE_DIRECTORY), "*" + SAVE_EXTENSION);
            
            for (Path file : stream) {
                saves.add(file.getFileName().toString());
            }
            Collections.sort(saves, Collections.reverseOrder()); // Most recent first
        } catch (IOException e) {
            System.err.println("Error listing saved games: " + e.getMessage());
        }
        return saves;
    }

    /**
     * Loads a game from a save file.
     * 
     * @param filename The name of the save file to load
     * @return Map of game data, or null if load fails
     */
    public static Map<String, Object> loadGame(String filename) {
        try {
            Path filepath = Paths.get(SAVE_DIRECTORY, filename);
            
            if (!Files.exists(filepath)) {
                System.err.println("Save file not found: " + filename);
                return null;
            }
            
            // Read from file
            byte[] data = Files.readAllBytes(filepath);
            
            // Deserialize game state
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bis);
            @SuppressWarnings("unchecked")
            Map<String, Object> gameState = (Map<String, Object>) ois.readObject();
            ois.close();
            
            System.out.println("Game loaded successfully: " + filename);
            return gameState;
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a saved game file.
     * 
     * @param filename The save file to delete
     * @return true if deletion was successful
     */
    public static boolean deleteSave(String filename) {
        try {
            Path filepath = Paths.get(SAVE_DIRECTORY, filename);
            Files.delete(filepath);
            System.out.println("Save deleted: " + filename);
            return true;
        } catch (IOException e) {
            System.err.println("Error deleting save: " + e.getMessage());
            return false;
        }
    }

    /**
     * Gets the save directory path.
     * 
     * @return Path to the saves directory
     */
    public static String getSaveDirectory() {
        return SAVE_DIRECTORY;
    }

    /**
     * Formats a save filename for display.
     * Extracts variant and timestamp from filename.
     * 
     * @param filename The save filename
     * @return Formatted display string
     */
    public static String formatSaveInfo(String filename) {
        // Remove extension
        String base = filename.replace(SAVE_EXTENSION, "");
        // Replace underscores and extract info
        String[] parts = base.split("_");
        if (parts.length >= 2) {
            StringBuilder display = new StringBuilder();
            display.append("Variant: ").append(parts[1]);
            if (parts.length > 2) {
                display.append(" | Time: ").append(parts[2]).append(" ").append(parts[3]);
            }
            return display.toString();
        }
        return filename;
    }
}
