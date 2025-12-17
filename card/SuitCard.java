package card;

import base.Card;
import properties.Suit;
import properties.Numbers;
import visitor.ScoreVisitor;

/**
 * Represents a suit card in the JEST game.
 * Suit cards have a suit (Spade, Club, Diamond, Heart) and a number (Ace, 2, 3, 4).
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class SuitCard extends Card {
    /** The suit of this card */
    private Suit suit;
    
    /** The number/value of this card */
    private Numbers number;

    /**
     * Creates a suit card with specified suit and number.
     * 
     * @param suit The card's suit
     * @param number The card's number
     */
    public SuitCard(Suit suit, Numbers number) {
        this.suit = suit;
        this.number = number;
    }

    /**
     * Checks if this card is an Ace.
     * Aces have special scoring rules.
     * 
     * @return true if this is an Ace
     */
    public boolean isAce() {
        return number == Numbers.ACE;
    }

    /**
     * Gets the face value of this card.
     * 
     * @return The card's value (1-4)
     */
    @Override
    public int getValue() {
        return number.getValue();
    }

    /**
     * Gets the suit of this card.
     * 
     * @return The card's suit
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Gets the number of this card.
     * 
     * @return The card's number
     */
    public Numbers getNumber() {
        return number;
    }

    /**
     * Accepts a visitor for score calculation.
     * 
     * @param visitor The score visitor
     */
    @Override
    public void accept(ScoreVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a string representation of this card.
     * 
     * @return String in format "NUMBER of SUIT"
     */
    @Override
    public String toString() {
        return number + " of " + suit;
    }
}
