import base.Game;
import java.util.Scanner;

/**
 * Main entry point for the JEST card game.
 * Handles game initialization and player setup.
 * 
 * <p>JEST is a card game for 3-4 players where players collect cards
 * to build the highest-scoring Jest. The game uses 17 cards (16 suit cards
 * plus 1 Joker) and awards trophies based on various conditions.</p>
 * 
 * @author JEST Team
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method - starts the JEST game.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("======================================");
        System.out.println("         JEST CARD GAME");
        System.out.println("       By Brett J. Gilbert");
        System.out.println("======================================");
        System.out.println();
        System.out.println("Game Rules:");
        System.out.println("- 3 players: 2 trophy cards");
        System.out.println("- 4 players: 1 trophy card");
        System.out.println("- Each round: receive 2 cards, make an offer");
        System.out.println("- Take 1 card from another player's offer");
        System.out.println("- Build the highest-scoring Jest!");
        System.out.println();
        
        int numberOfPlayers = 0;
        while (numberOfPlayers < 3 || numberOfPlayers > 4) {
            System.out.print("Enter number of players (3 or 4): ");
            if (scanner.hasNextInt()) {
                numberOfPlayers = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
            }
            
            if (numberOfPlayers < 3 || numberOfPlayers > 4) {
                System.out.println("Invalid number. Please enter 3 or 4.");
            }
        }
        
        Game game = new Game(numberOfPlayers);
        game.startGame();
        
        System.out.println("\nThank you for playing JEST!");
        scanner.close();
    }
}
