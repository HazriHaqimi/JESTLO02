import base.Game;
import base.InputHandler;

/**
 * Main entry point for the JEST card game.
 * Handles game initialization, player setup, and AI difficulty selection.
 * 
 * <p>JEST is a card game for 3-4 players where players collect cards
 * to build the highest-scoring Jest. The game uses 17 cards (16 suit cards
 * plus 1 Joker) and awards trophies based on various conditions.</p>
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method - starts the JEST game.
     * 
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
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
        
        // Get total number of players
        int totalPlayers = 0;
        while (totalPlayers < 3 || totalPlayers > 4) {
            System.out.print("Enter total number of players (3 or 4): ");
            totalPlayers = InputHandler.getInt();
            
            if (totalPlayers < 3 || totalPlayers > 4) {
                System.out.println("Invalid number. Please enter 3 or 4.");
            }
        }
        
        // Get number of human players
        int humanPlayers = 0;
        while (humanPlayers < 1 || humanPlayers > totalPlayers) {
            System.out.print("Enter number of HUMAN players (1 to " + totalPlayers + "): ");
            humanPlayers = InputHandler.getInt();
            
            if (humanPlayers < 1 || humanPlayers > totalPlayers) {
                System.out.println("Invalid number. Please enter 1 to " + totalPlayers + ".");
            }
        }
        
        int aiPlayers = totalPlayers - humanPlayers;
        
        // Get AI difficulty if there are AI players
        int aiDifficulty = 0;
        if (aiPlayers > 0) {
            System.out.println("\nAI Difficulty Options:");
            System.out.println("  1 = Defensive (AI hides high cards, takes low cards)");
            System.out.println("  2 = Offensive (AI shows high cards, takes high cards)");
            System.out.println("  3 = Mixed (random strategy per AI)");
            
            while (aiDifficulty < 1 || aiDifficulty > 3) {
                System.out.print("Choose AI difficulty (1, 2, or 3): ");
                aiDifficulty = InputHandler.getInt();
                
                if (aiDifficulty < 1 || aiDifficulty > 3) {
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
                }
            }
        }
        
        // Create and start game
        Game game = new Game(totalPlayers, humanPlayers, aiDifficulty);
        game.startGame();
        
        System.out.println("\nThank you for playing JEST!");
    }
}
