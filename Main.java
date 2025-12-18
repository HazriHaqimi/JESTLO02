import base.Game;
import base.InputHandler;
import base.GameConfig;
import base.GameVariant;

/**
 * Main entry point for the JEST card game.
 * Handles game initialization, player setup, AI difficulty, expansion cards, and variant selection.
 * 
 * <p>JEST is a card game for 3-4 players where players collect cards
 * to build the highest-scoring Jest. The game supports multiple variants
 * (Normal, No Mercy, Go All Out) and optional expansion cards (6, 7, 8, 9).</p>
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Main {
    
    /**
     * Main method - starts the JEST game.
     * Prompts for all configuration options before starting.
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
        
        // Get expansion card preference
        System.out.println("\n========================================");
        System.out.println("     EXPANSION CARDS");
        System.out.println("========================================");
        System.out.println("Base deck: Ace, 2, 3, 4 in each suit (16 cards + Joker = 17 total)");
        System.out.println("Expansion: Adds 6, 7, 8, 9 in each suit (32 cards + Joker = 33 total)");
        
        int expansionChoice = 0;
        while (expansionChoice < 1 || expansionChoice > 2) {
            System.out.print("Use expansion cards? (1 = No, 2 = Yes): ");
            expansionChoice = InputHandler.getInt();
            
            if (expansionChoice < 1 || expansionChoice > 2) {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
        boolean useExpansion = (expansionChoice == 2);
        
        // Get game variant
        System.out.println("\n========================================");
        System.out.println("     GAME VARIANTS");
        System.out.println("========================================");
        System.out.println("1 = NORMAL MODE");
        System.out.println("    Standard rules: Spades/Clubs add, Diamonds subtract");
        System.out.println();
        System.out.println("2 = NO MERCY");
        System.out.println("    Jest value reset to 0 if exceeds random threshold (7-10)");
        System.out.println();
        System.out.println("3 = GO ALL OUT");
        System.out.println("    No trophies! All cards add value. Joker multiplies by 1.5");
        System.out.println();
        
        int variantChoice = 0;
        while (variantChoice < 1 || variantChoice > 3) {
            System.out.print("Choose a variant (1, 2, or 3): ");
            variantChoice = InputHandler.getInt();
            
            if (variantChoice < 1 || variantChoice > 3) {
                System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
        
        GameVariant selectedVariant;
        switch (variantChoice) {
            case 1:
                selectedVariant = GameVariant.NORMAL;
                break;
            case 2:
                selectedVariant = GameVariant.NO_MERCY;
                break;
            case 3:
                selectedVariant = GameVariant.GO_ALL_OUT;
                break;
            default:
                selectedVariant = GameVariant.NORMAL;
        }
        
        // Create game configuration
        GameConfig gameConfig = new GameConfig(useExpansion, selectedVariant);
        
        // Create and start game
        Game game = new Game(totalPlayers, humanPlayers, aiDifficulty, gameConfig);
        game.startGame();
        
        System.out.println("\nThank you for playing JEST!");
    }
}
