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
 * Base deck: 16 suit cards (4 suits x 4 values) plus 1 Joker = 17 cards total.
 * With expansion: 32 suit cards (4 suits x 8 values) plus 1 Joker = 33 cards total.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Deck {
    /** The list of cards in the deck */
    private List<Card> cards;
    
    /** Whether expansion cards are included */
    private boolean hasExpansion;

    /**
     * Creates a new deck and initializes it with all cards.
     * 
     * @param hasExpansion true to include expansion cards (6, 7, 8, 9)
     */
    public Deck(boolean hasExpansion) {
        this.cards = new ArrayList<>();
        this.hasExpansion = hasExpansion;
        initializeDeck();
    }
    
    /**
     * Creates a new deck without expansion cards (default behavior).
     */
    public Deck() {
        this(false);
    }

    /**
     * Initializes the deck with suit cards and Joker.
     * Base: Ace, 2, 3, 4
     * Expansion: adds 6, 7, 8, 9
     */
    private void initializeDeck() {
        for (Suit suit : Suit.values()) {
            for (Numbers number : Numbers.values()) {
                // Skip expansion cards if expansion is disabled
                if (!hasExpansion && (number == Numbers.SIX || number == Numbers.SEVEN || 
                    number == Numbers.EIGHT || number == Numbers.NINE)) {
                    continue;
                }
                cards.add(new SuitCard(suit, number));
            }
        }
        cards.add(new JokerCard());
    }
    
    /**
     * Checks if this deck has expansion cards.
     * 
     * @return true if expansion cards are included
     */
    public boolean hasExpansion() {
        return hasExpansion;
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
