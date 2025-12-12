package visitor;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;

/**
 * Visitor for applying the Joker scoring rule.
 * The Joker's value depends on the number of Hearts in the Jest:
 * - 0 Hearts: Joker is worth +4
 * - 1-3 Hearts: Joker is worth 0, Hearts reduce score
 * - 4 Hearts: Joker is worth 0, but Hearts add their values
 * 
 * @author JEST Team
 * @version 1.0
 */
public class JokerRuleVisitor implements ScoreVisitor {
    /** Calculated Joker value */
    private int jokerValue;
    
    /** Count of Heart cards */
    private int heartCount;
    
    /** Running total score */
    private int totalScore;
    
    /** Whether Jest has the Joker */
    private boolean hasJoker;

    /**
     * Creates a Joker rule visitor.
     */
    public JokerRuleVisitor() {
        this.jokerValue = 0;
        this.heartCount = 0;
        this.totalScore = 0;
        this.hasJoker = false;
    }

    /**
     * Visits a suit card and counts Hearts.
     * 
     * @param suitCard The suit card to process
     */
    @Override
    public void visit(SuitCard suitCard) {
        if (suitCard.getSuit() == Suit.HEART) {
            heartCount++;
        }
    }

    /**
     * Visits the Joker and calculates its value.
     * 
     * @param jokerCard The Joker card
     */
    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
        adjustJokerValue(heartCount);
    }

    /**
     * Gets the Heart count.
     * 
     * @return Number of Hearts
     */
    public int countHearts() {
        return heartCount;
    }

    /**
     * Adjusts Joker value based on Hearts.
     * 
     * @param numHearts Number of Heart cards
     */
    public void adjustJokerValue(int numHearts) {
        if (numHearts == 0) {
            jokerValue = 4;
        } else if (numHearts >= 1 && numHearts <= 3) {
            jokerValue = 0;
        } else if (numHearts == 4) {
            jokerValue = 0;
        }
        totalScore += jokerValue;
    }

    /**
     * Gets the calculated Joker value.
     * 
     * @return The Joker's value
     */
    public int getJokerValue() {
        return jokerValue;
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

    /**
     * Checks if Jest has the Joker.
     * 
     * @return true if Joker is present
     */
    public boolean hasJoker() {
        return hasJoker;
    }
}
