package base;

import visitor.ScoreVisitor;

/**
 * Abstract base class representing a card in the JEST game.
 * Cards can be either suit cards (Spade, Club, Diamond, Heart) or the Joker.
 * 
 * @author JEST Team
 * @version 1.0
 */
public abstract class Card {
    
    /**
     * Accepts a visitor for score calculation using the Visitor pattern.
     * 
     * @param visitor The score visitor to accept
     */
    public abstract void accept(ScoreVisitor visitor);
    
    /**
     * Gets the face value of the card.
     * 
     * @return The card's value
     */
    public abstract int getValue();
    
    /**
     * Returns a string representation of the card.
     * 
     * @return String representation
     */
    @Override
    public abstract String toString();
}
