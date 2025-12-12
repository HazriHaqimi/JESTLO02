import base.Game;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║           JEST CARD GAME             ║");
        System.out.println("║       By Brett J. Gilbert            ║");
        System.out.println("╚══════════════════════════════════════╝");
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
        
        scanner.close();
    }
}
