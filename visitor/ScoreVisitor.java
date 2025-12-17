package visitor;

import card.SuitCard;
import card.JokerCard;

/**
 * Visitor interface for calculating scores.
 * Implements the Visitor design pattern for card scoring.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public interface ScoreVisitor {
    
    /**
     * Visits a suit card for scoring.
     * 
     * @param suitCard The suit card to score
     */
    void visit(SuitCard suitCard);
    
    /**
     * Visits a Joker card for scoring.
     * 
     * @param jokerCard The Joker card to score
     */
    void visit(JokerCard jokerCard);
    
    /**
     * Gets the total calculated score.
     * 
     * @return The total score
     */
    int getTotalScore();
}
