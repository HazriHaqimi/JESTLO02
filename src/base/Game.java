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
import java.util.Collections;
import java.util.List;

/**
 * Main game controller for the JEST card game.
 * Manages players, deck, trophies, rounds, and scoring.
 * 
 * Game flow:
 * 1. Setup: Initialize players, deck, draw trophy cards
 * 2. Rounds: Deal 2 cards, make offers, take cards (repeat until deck empty)
 * 3. Final: Collect remaining offer cards, award trophies to Jests, calculate scores
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
     * Creates a new game with the specified number of players.
     * 
     * @param numberOfPlayers Number of players (3 or 4)
     */
    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.roundNumber = 0;
        this.previousRoundLeftovers = new ArrayList<>();
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
     * Initializes players based on user input.
     */
    private void initializePlayers() {
        System.out.print("\nEnter name for Human Player: ");
        String humanName = InputHandler.getString();
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
     * This happens BEFORE final scoring.
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
                
                // Add the trophy card to the winner's Jest
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
     * Computes and displays final scores (after trophies are added).
     */
    public void computeFinalScores() {
        System.out.println("\n========================================");
        System.out.println("         FINAL JESTS & SCORES");
        System.out.println("========================================");
        
        for (Player player : players) {
            System.out.println("\n" + player.getName() + "'s Jest:");
            System.out.println("  Cards: " + player.getJest().getCards());
            
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            System.out.println("  Total Score: " + score + " points");
        }
    }

    /**
     * Ends the game - awards trophies, calculates scores, determines winner.
     */
    public void endGame() {
        System.out.println("\n========================================");
        System.out.println("           GAME OVER");
        System.out.println("========================================");
        
        // Show Jests before trophies
        System.out.println("\nJests before trophy awards:");
        for (Player player : players) {
            System.out.println("  " + player.getName() + ": " + player.getJest().getCards());
        }
        
        // Award trophies (adds trophy cards to Jests)
        awardTrophies();
        
        // Calculate final scores (after trophies added)
        computeFinalScores();
        
        // Determine winner
        System.out.println("\n========================================");
        System.out.println("           FINAL RESULTS");
        System.out.println("========================================");
        
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
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
}
