package base;

import trophy.Trophy;
import visitor.ScoreVisitor;
import visitor.FinalScoreVisitor;
import visitor.NoMercyScoreVisitor;
import visitor.GoAllOutScoreVisitor;
import player.HumanPlayer;
import player.VirtualPlayer;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import properties.Numbers;
import strategy.typestrategy.DefensiveStrategy;
import strategy.typestrategy.OffensiveStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Main game controller for the JEST card game.
 * Manages players, deck, trophies, rounds, scoring, and game variants.
 * 
 * <p>Game flow:</p>
 * <ol>
 *   <li>Setup: Initialize players (human and AI), deck, draw trophy cards</li>
 *   <li>Rounds: Deal 2 cards, make offers, take cards (repeat until deck empty)</li>
 *   <li>Final: Collect remaining offer cards, award trophies to Jests, calculate scores</li>
 * </ol>
 * 
 * <p>Supports game variants:</p>
 * <ul>
 *   <li>NORMAL: Standard rules</li>
 *   <li>NO_MERCY: Jest value reset to 0 if exceeds random threshold (7-10)</li>
 *   <li>GO_ALL_OUT: No trophies, all cards add value, Joker multiplies by 1.5</li>
 * </ul>
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Game {
    /** List of players in the game */
    private List<Player> players;
    
    /** The deck of cards */
    private Deck deck;
    
    /** List of trophies for this game (1 for 4 players, 2 for 3 players) */
    private List<Trophy> trophies;
    
    /** Game configuration (expansion cards, variant, etc.) */
    private GameConfig gameConfig;
    
    /** Total number of players (3 or 4) */
    private int numberOfPlayers;
    
    /** Number of human players */
    private int numberOfHumans;
    
    /** AI difficulty setting (1=Defensive, 2=Offensive, 3=Mixed) */
    private int aiDifficulty;
    
    /** Current round number */
    private int roundNumber;
    
    /** Cards remaining from previous round's offers */
    private List<Card> previousRoundLeftovers;

    /**
     * Trophy types based on card conditions.
     */
    public enum TrophyType {
        HIGHEST_SPADE, HIGHEST_CLUB, HIGHEST_DIAMOND, HIGHEST_HEART,
        LOWEST_SPADE, LOWEST_CLUB, LOWEST_DIAMOND, LOWEST_HEART,
        JOKER,
        MAJORITY_2, MAJORITY_3, MAJORITY_4
    }

    /**
     * Creates a new game with the specified player configuration and game config.
     * 
     * @param numberOfPlayers Total number of players (3 or 4)
     * @param numberOfHumans Number of human players (1 to numberOfPlayers)
     * @param aiDifficulty AI difficulty (1=Defensive, 2=Offensive, 3=Mixed)
     * @param gameConfig Configuration for expansion and variant settings
     */
    public Game(int numberOfPlayers, int numberOfHumans, int aiDifficulty, GameConfig gameConfig) {
        this.numberOfPlayers = numberOfPlayers;
        this.numberOfHumans = numberOfHumans;
        this.aiDifficulty = aiDifficulty;
        this.gameConfig = gameConfig;
        this.players = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.roundNumber = 0;
        this.previousRoundLeftovers = new ArrayList<>();
    }
    
    /**
     * Creates a new game with default game config (no expansion, normal variant).
     * 
     * @param numberOfPlayers Total number of players (3 or 4)
     * @param numberOfHumans Number of human players (1 to numberOfPlayers)
     * @param aiDifficulty AI difficulty (1=Defensive, 2=Offensive, 3=Mixed)
     */
    public Game(int numberOfPlayers, int numberOfHumans, int aiDifficulty) {
        this(numberOfPlayers, numberOfHumans, aiDifficulty, new GameConfig());
    }

    /**
     * Starts and runs the complete game.
     */
    public void startGame() {
        System.out.println("\n=== JEST Card Game ===");
        System.out.println("Total players: " + numberOfPlayers);
        System.out.println("Human players: " + numberOfHumans);
        System.out.println("AI players: " + (numberOfPlayers - numberOfHumans));
        System.out.println();
        System.out.println(gameConfig.toString());
        System.out.println();
        
        initializePlayers();
        initializeDeck();
        
        // Skip trophies in GO_ALL_OUT variant
        if (gameConfig.getVariant() != GameVariant.GO_ALL_OUT) {
            setupTrophies();
        }
        
        System.out.println("\n========================================");
        System.out.println("         TROPHIES FOR THIS GAME");
        System.out.println("========================================");
        for (Trophy trophy : trophies) {
            Card card = trophy.getTrophyCard();
            System.out.println("Trophy Card: " + card);
            System.out.println("  Condition: " + trophy.getCondition());
            System.out.println("  (This card will be added to the winner's Jest)");
            System.out.println();
        }
        
        // Play rounds until deck is empty
        while (canPlayRound()) {
            playRound();
        }
        
        endGame();
    }

    /**
     * Checks if another round can be played.
     * 
     * @return true if deck has enough cards for all players
     */
    private boolean canPlayRound() {
        int availableCards = deck.size() + previousRoundLeftovers.size();
        return availableCards >= numberOfPlayers * 2;
    }

    /**
     * Initializes players - humans first, then AIs with chosen difficulty.
     */
    private void initializePlayers() {
        // Create human players
        for (int i = 1; i <= numberOfHumans; i++) {
            System.out.print("\nEnter name for Human Player " + i + ": ");
            String name = InputHandler.getString();
            if (name.isEmpty()) name = "Human " + i;
            players.add(new HumanPlayer(name));
        }
        
        // Create AI players with selected difficulty
        int aiCount = numberOfPlayers - numberOfHumans;
        for (int i = 1; i <= aiCount; i++) {
            String aiName = "AI Player " + i;
            VirtualPlayer ai;
            
            switch (aiDifficulty) {
                case 1: // Defensive
                    ai = new VirtualPlayer(aiName, new DefensiveStrategy());
                    System.out.println("Created " + aiName + " (Defensive)");
                    break;
                case 2: // Offensive
                    ai = new VirtualPlayer(aiName, new OffensiveStrategy());
                    System.out.println("Created " + aiName + " (Offensive)");
                    break;
                case 3: // Mixed - alternate or random
                default:
                    if (i % 2 == 0) {
                        ai = new VirtualPlayer(aiName, new OffensiveStrategy());
                        System.out.println("Created " + aiName + " (Offensive)");
                    } else {
                        ai = new VirtualPlayer(aiName, new DefensiveStrategy());
                        System.out.println("Created " + aiName + " (Defensive)");
                    }
                    break;
            }
            players.add(ai);
        }
        
        System.out.println("\nPlayers in game:");
        for (Player p : players) {
            String type = (p instanceof HumanPlayer) ? "Human" : "AI";
            System.out.println("  - " + p.getName() + " (" + type + ")");
        }
    }

    /**
     * Initializes and shuffles the deck.
     * Uses expansion cards if enabled in game config.
     */
    private void initializeDeck() {
        deck = new Deck(gameConfig.isExpansionEnabled());
        deck.shuffle();
        System.out.println("Deck shuffled. " + deck.size() + " cards ready.");
        if (gameConfig.isExpansionEnabled()) {
            System.out.println("  (Expansion cards enabled: 6, 7, 8, 9)");
        }
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
                }
            }
        }
    }

    /**
     * Creates a trophy based on the card's orange band condition.
     * 
     * @param card The trophy card
     * @return Trophy with the appropriate condition
     */
    private Trophy createTrophyFromCard(Card card) {
        if (card instanceof JokerCard) {
            return new Trophy("Lowest value 1 card in strongest suit", 
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
                        return new Trophy("Majority 4s - Player with most 4-value cards", 
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
                        return new Trophy("Majority 4s - Player with most 4-value cards", 
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
    private void playRound() {
        roundNumber++;
        System.out.println("\n========================================");
        System.out.println("            ROUND " + roundNumber);
        System.out.println("========================================");
        
        // Prepare cards for this round
        List<Card> roundCards = new ArrayList<>();
        roundCards.addAll(previousRoundLeftovers);
        previousRoundLeftovers.clear();
        
        int cardsNeeded = (numberOfPlayers * 2) - roundCards.size();
        for (int i = 0; i < cardsNeeded && !deck.isEmpty(); i++) {
            roundCards.add(deck.drawCard());
        }
        
        Collections.shuffle(roundCards);
        
        for (Player player : players) {
            player.clearHand();
            if (roundCards.size() >= 2) {
                player.addCardToHand(roundCards.remove(0));
                player.addCardToHand(roundCards.remove(0));
            }
        }
        
        System.out.println("Cards dealt to all players.");
        System.out.println("Remaining in deck: " + deck.size());
        
        Round round = new Round(deck, players);
        round.makeOffers();
        
        System.out.println("\n--- Offers Made ---");
        for (Offer offer : round.getOffers()) {
            System.out.println("  " + offer.getOwner().getName() + ": Face-up = " + offer.getFaceUp());
        }
        
        System.out.println("\n--- Taking Cards ---");
        round.takeOffers();
        
        System.out.println("\n--- Current Jests ---");
        for (Player player : players) {
            System.out.println("  " + player.getName() + " (" + player.getJest().size() + " cards): " + 
                player.getJest().getCards());
        }
        
        previousRoundLeftovers = round.getLeftoverCards();
        System.out.println("\nCards remaining on table: " + previousRoundLeftovers);
        
        if (deck.isEmpty()) {
            System.out.println("\n=== DECK EMPTY - FINAL COLLECTION ===");
            for (Offer offer : round.getOffers()) {
                Card remaining = offer.getRemainingCard();
                if (remaining != null && offer.getOwner() != null) {
                    offer.getOwner().getJest().addCard(remaining);
                    System.out.println(offer.getOwner().getName() + " added final card: " + remaining);
                }
            }
            previousRoundLeftovers.clear();
        }
    }

    /**
     * Awards trophies to winners and adds trophy cards to their Jests.
     */
    public void awardTrophies() {
        System.out.println("\n========================================");
        System.out.println("         AWARDING TROPHIES");
        System.out.println("========================================");
        System.out.println("(Trophy cards are added to winners' Jests)\n");
        
        for (Trophy trophy : trophies) {
            Player winner = trophy.determineWinner(players);
            if (winner != null) {
                trophy.setWinner(winner);
                Card trophyCard = trophy.getTrophyCard();
                winner.getJest().addCard(trophyCard);
                
                System.out.println("Trophy: " + trophy.getCondition());
                System.out.println("  Winner: " + winner.getName());
                System.out.println("  Card added to Jest: " + trophyCard);
                System.out.println();
            } else {
                System.out.println("Trophy: " + trophy.getCondition());
                System.out.println("  No winner (condition not met)");
                System.out.println();
            }
        }
    }

    /**
     * Computes and displays final scores using the appropriate visitor for the variant.
     */
    public void computeFinalScores() {
        System.out.println("\n========================================");
        System.out.println("         FINAL JESTS & SCORES");
        System.out.println("========================================");
        
        if (gameConfig.getVariant() == GameVariant.NO_MERCY) {
            System.out.println("NO MERCY Variant - Threshold: " + gameConfig.getNoMercyThreshold());
            System.out.println("Jest values exceeding threshold will be reset to 0!\n");
        } else if (gameConfig.getVariant() == GameVariant.GO_ALL_OUT) {
            System.out.println("GO ALL OUT Variant - All cards add value, Joker multiplies by 1.5\n");
        }
        
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s Jest:");
            System.out.println("  Cards: " + player.getJest().getCards());
            
            ScoreVisitor visitor = createScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            
            if (gameConfig.getVariant() == GameVariant.NO_MERCY && 
                gameConfig.exceedsNoMercyThreshold(score)) {
                System.out.println("  Score EXCEEDED threshold! -> 0 points (NO MERCY!)");
            }
            System.out.println("  Total Score: " + score + " points");
        }
    }
    
    /**
     * Creates the appropriate score visitor based on the game variant.
     * 
     * @param jest The Jest to score
     * @return The appropriate ScoreVisitor for the current variant
     */
    private ScoreVisitor createScoreVisitor(Jest jest) {
        switch (gameConfig.getVariant()) {
            case NO_MERCY:
                return new NoMercyScoreVisitor(jest, gameConfig);
            case GO_ALL_OUT:
                return new GoAllOutScoreVisitor(jest);
            case NORMAL:
            default:
                return new FinalScoreVisitor(jest);
        }
    }

    /**
     * Ends the game - awards trophies (if applicable), calculates scores, determines winner.
     */
    public void endGame() {
        System.out.println("\n========================================");
        System.out.println("           GAME OVER");
        System.out.println("========================================");
        
        System.out.println("\nJests before trophy awards:");
        for (Player player : players) {
            System.out.println("  " + player.getName() + ": " + player.getJest().getCards());
        }
        
        // Award trophies only in NORMAL and NO_MERCY variants
        if (gameConfig.getVariant() != GameVariant.GO_ALL_OUT) {
            awardTrophies();
        } else {
            System.out.println("\n(No trophies awarded in GO ALL OUT variant)");
        }
        
        computeFinalScores();
        
        System.out.println("\n========================================");
        System.out.println("           FINAL RESULTS");
        System.out.println("========================================");
        
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            ScoreVisitor visitor = createScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            
            System.out.println(player.getName() + ": " + score + " points");
            
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        
        if (winner != null) {
            System.out.println("\n*** " + winner.getName() + " WINS with " + highestScore + " points! ***");
        }
        
        InputHandler.close();
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }

    /**
     * Gets the game configuration.
     * 
     * @return The game configuration
     */
    public GameConfig getGameConfig() {
        return gameConfig;
    }
}
