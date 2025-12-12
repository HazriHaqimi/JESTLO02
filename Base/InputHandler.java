package base;

import java.util.Scanner;

/**
 * Singleton class to handle all console input.
 * Prevents multiple Scanner instances on System.in which causes conflicts.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class InputHandler {
    /** Single shared Scanner instance */
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Private constructor to prevent instantiation.
     */
    private InputHandler() {
    }
    
    /**
     * Gets integer input from the user.
     * Waits for valid integer input.
     * 
     * @return The integer entered by user
     */
    public static int getInt() {
        while (true) {
            try {
                if (scanner.hasNextLine()) {
                    String line = scanner.nextLine().trim();
                    if (!line.isEmpty()) {
                        return Integer.parseInt(line);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    /**
     * Gets string input from the user.
     * 
     * @return The line entered by user
     */
    public static String getString() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        }
        return "";
    }
    
    /**
     * Closes the scanner (call at end of program).
     */
    public static void close() {
        scanner.close();
    }
}
