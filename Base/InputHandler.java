package base;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Singleton class to handle all console input.
 * Uses BufferedReader for reliable input handling.
 * hello
 * @author Hazri and Sophea
 * @version 1.0
 */
public class InputHandler {
    /** BufferedReader for reliable input */
    private static BufferedReader reader = null;
    
    /**
     * Gets or creates the BufferedReader.
     */
    private static BufferedReader getReader() {
        if (reader == null) {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }
        return reader;
    }
    
    /**
     * Private constructor to prevent instantiation.
     */
    private InputHandler() {
    }
    
    /**
     * Gets integer input from the user.
     * Blocks until valid integer input is received.
     * 
     * @return The integer entered by user
     */
    public static int getInt() {
        System.out.flush();
        try {
            while (true) {
                String line = getReader().readLine();
                
                if (line == null) {
                    System.out.println("[DEBUG] readLine returned null");
                    return 0;
                }
                
                line = line.trim();
                
                if (line.isEmpty()) {
                    System.out.println("[DEBUG] Empty line received, waiting for input...");
                    continue;
                }
                
                try {
                    int value = Integer.parseInt(line);
                    return value;
                } catch (NumberFormatException e) {
                    System.out.print("Invalid number. Please enter a number: ");
                    System.out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("[DEBUG] IOException: " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Gets string input from the user.
     * 
     * @return The line entered by user
     */
    public static String getString() {
        System.out.flush();
        try {
            String line = getReader().readLine();
            return (line != null) ? line : "";
        } catch (IOException e) {
            return "";
        }
    }
    
    /**
     * Closes the reader (call at end of program).
     */
    public static void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                // Ignore
            }
        }
    }
}
