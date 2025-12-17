package visitor;

import base.Player;
import card.SuitCard;
import card.JokerCard;
import trophy.Trophy;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;

/**
 * Visitor for tracking card information relevant to trophy conditions.
 * Tracks highest/lowest cards per suit and Joker presence.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class TrophyScoreVisitor implements ScoreVisitor {
    /** The trophy being evaluated */
    private Trophy trophy;
    
    /** Running total score */
    private int totalScore;
    
    /** Highest value card per suit */
    private Map<Suit, Integer> highestPerSuit;
    
    /** Lowest value card per suit */
    private Map<Suit, Integer> lowestPerSuit;
    
    /** Whether Jest has the Joker */
    private boolean hasJoker;

    /**
     * Creates a trophy score visitor for a specific trophy.
     * 
     * @param trophy The trophy to evaluate
     */
    public TrophyScoreVisitor(Trophy trophy) {
        this.trophy = trophy;
        this.totalScore = 0;
        this.highestPerSuit = new HashMap<>();
        this.lowestPerSuit = new HashMap<>();
        this.hasJoker = false;
        
        for (Suit suit : Suit.values()) {
            highestPerSuit.put(suit, Integer.MIN_VALUE);
            lowestPerSuit.put(suit, Integer.MAX_VALUE);
        }
    }

    /**
     * Visits a suit card and tracks highest/lowest per suit.
     * 
     * @param suitCard The suit card to process
     */
    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        int value = suitCard.getValue();
        
        if (value > highestPerSuit.get(suit)) {
            highestPerSuit.put(suit, value);
        }
        if (value < lowestPerSuit.get(suit)) {
            lowestPerSuit.put(suit, value);
        }
    }

    /**
     * Visits the Joker and marks its presence.
     * 
     * @param jokerCard The Joker card
     */
    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
    }

    /**
     * Applies the trophy bonus to a player's score.
     * 
     * @param player The player to apply bonus to
     */
    public void applyTrophy(Player player) {
        int bonus = calculateTrophyBonus();
        totalScore += bonus;
    }

    /**
     * Calculates the trophy bonus value.
     * 
     * @return The bonus points
     */
    public int calculateTrophyBonus() {
        return 0;
    }

    /**
     * Gets the highest card value in a suit.
     * 
     * @param suit The suit to check
     * @return The highest value
     */
    public int getHighestInSuit(Suit suit) {
        return highestPerSuit.get(suit);
    }

    /**
     * Gets the lowest card value in a suit.
     * 
     * @param suit The suit to check
     * @return The lowest value
     */
    public int getLowestInSuit(Suit suit) {
        return lowestPerSuit.get(suit);
    }

    /**
     * Checks if Jest has the Joker.
     * 
     * @return true if Joker is present
     */
    public boolean hasJoker() {
        return hasJoker;
    }

    /**
     * Gets the total score.
     * 
     * @return The total score
     */
    @Override
    public int getTotalScore() {
        return totalScore;
    }
}
