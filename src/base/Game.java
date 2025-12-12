package base;

import trophy.Trophy;
import visitor.FinalScoreVisitor;
import player.HumanPlayer;
import player.VirtualPlayer;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import properties.Numbers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main game controller for the JEST card game.
 * Manages players, deck, trophies, rounds, and scoring.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class Game {
    /** List of players in the game */
    private List<Player> players;
    
    /** The deck of cards */
    private Deck deck;
    
    /** List of trophies for this game (1 for 4 players, 2 for 3 players) */
    private List<Trophy> trophies;
    
    /** Number of players (3 or 4) */
    private int numberOfPlayers;
    
    /** Scanner for user input */
    private Scanner scanner;
    
    /** Current round number */
    private int roundNumber;

    /**
     * Trophy types based on card conditions.
     */
    public enum TrophyType {
        /** Player with highest card in a specific suit */
        HIGHEST_SPADE, HIGHEST_CLUB, HIGHEST_DIAMOND, HIGHEST_HEART,
        /** Player with lowest card in a specific suit */
        LOWEST_SPADE, LOWEST_CLUB, LOWEST_DIAMOND, LOWEST_HEART,
        /** Player with the Joker card */
        JOKER,
        /** Player with most cards of a specific face value */
        MAJORITY_2, MAJORITY_3, MAJORITY_4
    }

    /**
     * Creates a new game with the specified number of players.
     * 
     * @param numberOfPlayers Number of players (3 or 4)
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.roundNumber = 0;
    }

    /**
     * Starts and runs the complete game.
     */
    public void startGame() {
        System.out.println("=== JEST Card Game ===");
        System.out.println("Number of players: " + numberOfPlayers);
        
        initializePlayers();
        initializeDeck();
        setupTrophies();
        
        System.out.println("\n=== Trophies for this game ===");
        for (Trophy trophy : trophies) {
            System.out.println("- " + trophy.getCondition());
        }
        
        while (canPlayRound()) {
            startRound();
        }
        
        collectFinalCards();
        endGame();
    }

    /**
     * Checks if another round can be played.
     * 
     * @return true if deck has enough cards for all players
     */
    private boolean canPlayRound() {
        return deck.size() >= numberOfPlayers * 2;
    }

    /**
     * Initializes players based on user input.
     */
    private void initializePlayers() {
        System.out.println("\nEnter name for Human Player: ");
        String humanName = scanner.nextLine();
        if (humanName.isEmpty()) humanName = "Player 1";
        players.add(new HumanPlayer(humanName));
        
        for (int i = 2; i <= numberOfPlayers; i++) {
            players.add(new VirtualPlayer("AI Player " + i));
        }
        
        System.out.println("\nPlayers:");
        for (Player p : players) {
            System.out.println("- " + p.getName());
        }
    }

    /**
     * Initializes and shuffles the deck.
     */
    private void initializeDeck() {
        deck = new Deck();
        deck.shuffle();
        System.out.println("\nDeck shuffled. " + deck.size() + " cards ready.");
    }

    /**
     * Sets up trophies by drawing trophy cards from deck.
     * 3 players = 2 trophy cards, 4 players = 1 trophy card.
     */
    private void setupTrophies() {
        trophies.clear();
        
        int numTrophyCards = (numberOfPlayers == 3) ? 2 : 1;
        
        for (int i = 0; i < numTrophyCards; i++) {
            Card trophyCard = deck.drawCard();
            if (trophyCard != null) {
                Trophy trophy = createTrophyFromCard(trophyCard);
                if (trophy != null) {
                    trophies.add(trophy);
                    System.out.println("Trophy card drawn: " + trophyCard);
                }
            }
        }
    }

    /**
     * Creates a trophy based on the card's orange band condition.
     * Each card has a specific trophy condition printed on it.
     * 
     * @param card The trophy card
     * @return Trophy with the appropriate condition
     */
    private Trophy createTrophyFromCard(Card card) {
        if (card instanceof JokerCard) {
            return new Trophy("Lowest card value in strongest suit (1)", 
                TrophyType.JOKER, card);
        }
        
        if (card instanceof SuitCard) {
            SuitCard sc = (SuitCard) card;
            Suit suit = sc.getSuit();
            Numbers number = sc.getNumber();
            
            switch (suit) {
                case HEART:
                    return new Trophy("Joker - Player with the Joker", 
                        TrophyType.JOKER, card);
                        
                case DIAMOND:
                    if (number == Numbers.ACE) {
                        return new Trophy("Highest value 4 cards", 
                            TrophyType.MAJORITY_4, card);
                    } else if (number == Numbers.TWO) {
                        return new Trophy("Highest Heart - Player with highest Heart", 
                            TrophyType.HIGHEST_HEART, card);
                    } else if (number == Numbers.THREE) {
                        return new Trophy("Lowest Spade - Player with lowest Spade", 
                            TrophyType.LOWEST_SPADE, card);
                    } else if (number == Numbers.FOUR) {
                        return new Trophy("Joker - Player with the Joker", 
                            TrophyType.JOKER, card);
                    }
                    break;
                    
                case CLUB:
                    if (number == Numbers.ACE) {
                        return new Trophy("Highest value 4 cards", 
                            TrophyType.MAJORITY_4, card);
                    } else if (number == Numbers.TWO) {
                        return new Trophy("Lowest Heart - Player with lowest Heart", 
                            TrophyType.LOWEST_HEART, card);
                    } else if (number == Numbers.THREE) {
                        return new Trophy("Lowest Diamond - Player with lowest Diamond", 
                            TrophyType.LOWEST_DIAMOND, card);
                    } else if (number == Numbers.FOUR) {
                        return new Trophy("Lowest Spade - Player with lowest Spade", 
                            TrophyType.LOWEST_SPADE, card);
                    }
                    break;
                    
                case SPADE:
                    if (number == Numbers.ACE) {
                        return new Trophy("Highest Diamond - Player with highest Diamond", 
                            TrophyType.HIGHEST_DIAMOND, card);
                    } else if (number == Numbers.TWO) {
                        return new Trophy("Majority 3s - Player with most 3-value cards", 
                            TrophyType.MAJORITY_3, card);
                    } else if (number == Numbers.THREE) {
                        return new Trophy("Majority 2s - Player with most 2-value cards", 
                            TrophyType.MAJORITY_2, card);
                    } else if (number == Numbers.FOUR) {
                        return new Trophy("Lowest Club - Player with lowest Club", 
                            TrophyType.LOWEST_CLUB, card);
                    }
                    break;
            }
        }
        
        return new Trophy("Unknown trophy", TrophyType.JOKER, card);
    }

    /**
     * Plays a single round of the game.
     */
    public void startRound() {
        roundNumber++;
        System.out.println("\n========== ROUND " + roundNumber + " ==========");
        Round round = new Round(deck, players);
        
        round.dealCards();
        System.out.println("Cards dealt to all players.");
        
        for (Player player : players) {
            if (player instanceof HumanPlayer) {
                System.out.println(player.getName() + "'s hand: " + player.getHand());
            } else {
                System.out.println(player.getName() + "'s hand: [hidden]");
            }
        }
        
        round.makeOffers();
        System.out.println("\nOffers made by all players:");
        for (Offer offer : round.getOffers()) {
            System.out.println(offer.getOwner().getName() + ": Face-up = " + offer.getFaceUp() + ", Face-down = [hidden]");
        }
        
        round.takeOffers();
        
        round.collectLeftoverCards();
        
        System.out.println("\nCurrent Jests:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getJest().getCards());
        }
        
        round.prepareNextRound();
    }

    /**
     * Collects final remaining cards at end of game.
     */
    private void collectFinalCards() {
        System.out.println("\n=== Game Over - All rounds completed ===");
    }

    /**
     * Computes and displays scores for all players.
     */
    public void computeScore() {
        System.out.println("\n=== Final Scores ===");
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            System.out.println(player.getName() + "'s Jest: " + player.getJest().getCards());
            System.out.println(player.getName() + "'s Score: " + score);
        }
    }

    /**
     * Awards trophies to players based on conditions.
     */
    public void awardTrophy() {
        System.out.println("\n=== Awarding Trophies ===");
        for (Trophy trophy : trophies) {
            Player winner = trophy.determineWinner(players);
            if (winner != null) {
                trophy.setWinner(winner);
                System.out.println(trophy.getCondition() + " -> " + winner.getName());
            } else {
                System.out.println(trophy.getCondition() + " -> No winner");
            }
        }
    }

    /**
     * Ends the game and determines the winner.
     */
    public void endGame() {
        computeScore();
        awardTrophy();
        
        System.out.println("\n=== Final Results ===");
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            
            System.out.println(player.getName() + ": Score = " + score);
            
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        
        if (winner != null) {
            System.out.println("\n*** " + winner.getName() + " WINS with " + highestScore + " points! ***");
        }
    }

    /**
     * Gets the list of players.
     * 
     * @return List of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the deck.
     * 
     * @return The deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the list of trophies.
     * 
     * @return List of trophies
     */
    public List<Trophy> getTrophies() {
        return trophies;
    }
}
