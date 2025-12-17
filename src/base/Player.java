package base;

import strategy.PlayStrategy;
import visitor.ScoreVisitor;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class representing a player in the JEST game.
 * Players have a hand of cards, a Jest (collection of won cards), and a strategy.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public abstract class Player {
    /** The player's name */
    protected String name;
    
    /** The player's current hand of cards */
    protected List<Card> hand;
    
    /** The player's Jest (won cards collection) */
    protected Jest jest;
    
    /** The player's strategy for making decisions */
    protected PlayStrategy strategy;

    /**
     * Creates a player with a name and strategy.
     * 
     * @param name The player's name
     * @param strategy The player's decision-making strategy
     */
    public Player(String name, PlayStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.hand = new ArrayList<>();
        this.jest = new Jest();
    }

    /**
     * Creates an offer from the player's hand.
     * Must be implemented by subclasses.
     * 
     * @return The offer created by the player
     */
    public abstract Offer makeOffer();

    /**
     * Takes a card from an offer and adds it to the Jest.
     * 
     * @param offer The offer to take from
     * @param takeFaceUp true to take face-up card, false for face-down
     */
    public void takeOffer(Offer offer, boolean takeFaceUp) {
        Card card = offer.selectCard(takeFaceUp);
        if (card != null) {
            jest.addCard(card);
        }
    }

    /**
     * Calculates the final score using a visitor.
     * 
     * @param visitor The score visitor to use
     * @return The calculated score
     */
    public int calculateFinalScore(ScoreVisitor visitor) {
        jest.accept(visitor);
        return visitor.getTotalScore();
    }

    /**
     * Adds a card to the player's hand.
     * 
     * @param card The card to add
     */
    public void addCardToHand(Card card) {
        hand.add(card);
    }

    /**
     * Clears all cards from the player's hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Gets the player's name.
     * 
     * @return The player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the player's hand.
     * 
     * @return List of cards in hand
     */
    public List<Card> getHand() {
        return hand;
    }

    /**
     * Gets the player's Jest.
     * 
     * @return The player's Jest
     */
    public Jest getJest() {
        return jest;
    }

    /**
     * Sets the player's Jest.
     * 
     * @param jest The Jest to set
     */
    public void setJest(Jest jest) {
        this.jest = jest;
    }

    /**
     * Gets the player's strategy.
     * 
     * @return The player's strategy
     */
    public PlayStrategy getStrategy() {
        return strategy;
    }

    /**
     * Sets the player's strategy.
     * 
     * @param strategy The strategy to set
     */
    public void setStrategy(PlayStrategy strategy) {
        this.strategy = strategy;
    }
}
