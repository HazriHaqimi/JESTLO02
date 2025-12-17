package trophy;

import base.Player;
import base.Card;
import base.Game.TrophyType;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import visitor.FinalScoreVisitor;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a trophy in the JEST game.
 * Trophies are awarded based on specific conditions met by players' Jests.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Trophy {
    /** Description of the trophy condition */
    private String condition;
    
    /** The player who won this trophy */
    private Player winner;
    
    /** Type of trophy condition */
    private TrophyType type;
    
    /** The card that was drawn to create this trophy */
    private Card trophyCard;

    /**
     * Creates a trophy with a condition and type.
     * 
     * @param condition Description of the trophy condition
     * @param type Type of the trophy
     */
    public Trophy(String condition, TrophyType type) {
        this.condition = condition;
        this.type = type;
    }

    /**
     * Creates a trophy with a condition, type, and the card that defines it.
     * 
     * @param condition Description of the trophy condition
     * @param type Type of the trophy
     * @param trophyCard The card that was drawn as trophy
     */
    public Trophy(String condition, TrophyType type, Card trophyCard) {
        this.condition = condition;
        this.type = type;
        this.trophyCard = trophyCard;
    }

    /**
     * Determines the winner of this trophy from the list of players.
     * 
     * @param players List of players to evaluate
     * @return The winning player, or null if no winner
     */
    public Player determineWinner(List<Player> players) {
        switch (type) {
            case HIGHEST_SPADE:
                return findHighestSuitPlayer(players, Suit.SPADE);
            case LOWEST_SPADE:
                return findLowestSuitPlayer(players, Suit.SPADE);
            case HIGHEST_CLUB:
                return findHighestSuitPlayer(players, Suit.CLUB);
            case LOWEST_CLUB:
                return findLowestSuitPlayer(players, Suit.CLUB);
            case HIGHEST_DIAMOND:
                return findHighestSuitPlayer(players, Suit.DIAMOND);
            case LOWEST_DIAMOND:
                return findLowestSuitPlayer(players, Suit.DIAMOND);
            case HIGHEST_HEART:
                return findHighestSuitPlayer(players, Suit.HEART);
            case LOWEST_HEART:
                return findLowestSuitPlayer(players, Suit.HEART);
            case JOKER:
                return findPlayerWithJoker(players);
            case MAJORITY_2:
                return findMajorityPlayer(players, 2);
            case MAJORITY_3:
                return findMajorityPlayer(players, 3);
            case MAJORITY_4:
                return findMajorityPlayer(players, 4);
            default:
                return null;
        }
    }

    /**
     * Finds the player with the highest card of a specific suit.
     * Ties are broken by suit strength (Spade > Club > Diamond > Heart).
     * 
     * @param players List of players
     * @param suit The suit to check
     * @return The winning player
     */
    private Player findHighestSuitPlayer(List<Player> players, Suit suit) {
        Player winner = null;
        int highestValue = Integer.MIN_VALUE;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit) {
                        int value = suitCard.getValue();
                        if (value > highestValue) {
                            highestValue = value;
                            winner = player;
                        }
                    }
                }
            }
        }
        return winner;
    }

    /**
     * Finds the player with the lowest card of a specific suit.
     * Ties are broken by suit strength (Spade > Club > Diamond > Heart).
     * 
     * @param players List of players
     * @param suit The suit to check
     * @return The winning player
     */
    private Player findLowestSuitPlayer(List<Player> players, Suit suit) {
        Player winner = null;
        int lowestValue = Integer.MAX_VALUE;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit) {
                        int value = suitCard.getValue();
                        if (value < lowestValue) {
                            lowestValue = value;
                            winner = player;
                        }
                    }
                }
            }
        }
        return winner;
    }

    /**
     * Returns the suit strength for tie-breaking.
     * Spade = 4, Club = 3, Diamond = 2, Heart = 1.
     * 
     * @param suit The suit to evaluate
     * @return Strength value
     */
    private int getSuitStrength(Suit suit) {
        if (suit == null) return 0;
        switch (suit) {
            case SPADE: return 4;
            case CLUB: return 3;
            case DIAMOND: return 2;
            case HEART: return 1;
            default: return 0;
        }
    }

    /**
     * Finds the player who has the Joker card.
     * 
     * @param players List of players
     * @return The player with the Joker, or null
     */
    private Player findPlayerWithJoker(List<Player> players) {
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof JokerCard) {
                    return player;
                }
            }
        }
        return null;
    }

    /**
     * Finds the player with the most cards of a specific face value.
     * Ties are broken by card value in the strongest suit.
     * 
     * @param players List of players
     * @param targetValue The face value to count
     * @return The winning player
     */
    private Player findMajorityPlayer(List<Player> players, int targetValue) {
        Player winner = null;
        int highestCount = 0;
        Suit strongestSuit = null;
        
        for (Player player : players) {
            int count = 0;
            Suit bestSuit = null;
            
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard sc = (SuitCard) card;
                    if (sc.getValue() == targetValue) {
                        count++;
                        if (bestSuit == null || getSuitStrength(sc.getSuit()) > getSuitStrength(bestSuit)) {
                            bestSuit = sc.getSuit();
                        }
                    }
                }
            }
            
            if (count > highestCount ||
                (count == highestCount && count > 0 && 
                 getSuitStrength(bestSuit) > getSuitStrength(strongestSuit))) {
                highestCount = count;
                strongestSuit = bestSuit;
                winner = player;
            }
        }
        return winner;
    }

    /**
     * Gets the trophy condition description.
     * 
     * @return The condition string
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Gets the winner of this trophy.
     * 
     * @return The winning player
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Sets the winner of this trophy.
     * 
     * @param player The winning player
     */
    public void setWinner(Player player) {
        this.winner = player;
    }

    /**
     * Gets the trophy type.
     * 
     * @return The trophy type
     */
    public TrophyType getType() {
        return type;
    }

    /**
     * Gets the card that created this trophy.
     * 
     * @return The trophy card
     */
    public Card getTrophyCard() {
        return trophyCard;
    }
}
