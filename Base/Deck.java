package base;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import properties.Numbers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of cards used in the JEST game.
 * Contains 16 suit cards (4 suits x 4 values) plus 1 Joker = 17 cards total.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Deck {
    /** The list of cards in the deck */
    private List<Card> cards;

    /**
     * Creates a new deck and initializes it with all cards.
     */
    public Deck() {
        this.cards = new ArrayList<>();
        initializeDeck();
    }

    /**
     * Initializes the deck with 16 suit cards and 1 Joker.
     * Suit cards: Ace, 2, 3, 4 in each of Spade, Club, Diamond, Heart.
     */
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Numbers number : Numbers.values()) {
                cards.add(new SuitCard(suit, number));
            }
        }
        cards.add(new JokerCard());
    }

    /**
     * Shuffles the deck randomly.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Draws and removes the top card from the deck.
     * 
     * @return The drawn card, or null if deck is empty
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Checks if the deck is empty.
     * 
     * @return true if no cards remain
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Gets the number of cards remaining in the deck.
     * 
     * @return Number of cards
     */
    public int size() {
        return cards.size();
    }

    /**
     * Gets the list of cards in the deck.
     * 
     * @return List of cards
     */
    public List<Card> getCards() {
        return cards;
    }
}
