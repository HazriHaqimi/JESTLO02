package base;

import visitor.ScoreVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's Jest (collection of won cards).
 * The Jest accumulates cards throughout the game and is scored at the end.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Jest {
    /** The list of cards in this Jest */
    private List<Card> cards;

    /**
     * Creates an empty Jest.
     */
    public Jest() {
        this.cards = new ArrayList<>();
    }

    /**
     * Adds a card to this Jest.
     * 
     * @param card The card to add
     */
    public void addCard(Card card) {
        cards.add(card);
    }

    /**
     * Accepts a visitor for all cards in the Jest.
     * Used for score calculation using the Visitor pattern.
     * 
     * @param visitor The score visitor to accept
     */
    public void accept(ScoreVisitor visitor) {
        for (Card card : cards) {
            card.accept(visitor);
        }
    }

    /**
     * Gets the list of cards in this Jest.
     * 
     * @return List of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Gets the number of cards in this Jest.
     * 
     * @return Number of cards
     */
    public int size() {
        return cards.size();
    }
}
