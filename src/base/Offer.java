package base;

/**
 * Represents a player's offer during a round.
 * An offer consists of two cards: one face-up (visible) and one face-down (hidden).
 * Other players can take one card from an offer.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Offer {
    /** The face-up (visible) card */
    private Card faceUp;
    
    /** The face-down (hidden) card */
    private Card faceDown;
    
    /** The player who made this offer */
    private Player owner;

    /**
     * Creates an empty offer.
     */
    public Offer() {
    }

    /**
     * Creates an offer with specified cards and owner.
     * 
     * @param faceUp The face-up card
     * @param faceDown The face-down card
     * @param owner The player making the offer
     */
    public Offer(Card faceUp, Card faceDown, Player owner) {
        this.faceUp = faceUp;
        this.faceDown = faceDown;
        this.owner = owner;
    }

    /**
     * Checks if the offer has both cards.
     * 
     * @return true if both face-up and face-down cards are present
     */
    public boolean isComplete() {
        return faceUp != null && faceDown != null;
    }

    /**
     * Selects and removes a card from the offer.
     * 
     * @param isFaceUp true to take face-up card, false for face-down
     * @return The selected card
     */
    public Card selectCard(boolean isFaceUp) {
        Card selected;
        if (isFaceUp) {
            selected = faceUp;
            faceUp = null;
        } else {
            selected = faceDown;
            faceDown = null;
        }
        return selected;
    }

    /**
     * Gets the remaining card in the offer.
     * 
     * @return The remaining card, or null if none
     */
    public Card getRemainingCard() {
        if (faceUp != null) return faceUp;
        if (faceDown != null) return faceDown;
        return null;
    }

    /**
     * Checks if the offer has any cards.
     * 
     * @return true if at least one card remains
     */
    public boolean hasCards() {
        return faceUp != null || faceDown != null;
    }

    /**
     * Gets the face-up card.
     * 
     * @return The face-up card
     */
    public Card getFaceUp() {
        return faceUp;
    }

    /**
     * Sets the face-up card.
     * 
     * @param faceUp The card to set face-up
     */
    public void setFaceUp(Card faceUp) {
        this.faceUp = faceUp;
    }

    /**
     * Gets the face-down card.
     * 
     * @return The face-down card
     */
    public Card getFaceDown() {
        return faceDown;
    }

    /**
     * Sets the face-down card.
     * 
     * @param faceDown The card to set face-down
     */
    public void setFaceDown(Card faceDown) {
        this.faceDown = faceDown;
    }

    /**
     * Gets the owner of this offer.
     * 
     * @return The player who made this offer
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this offer.
     * 
     * @param owner The player making this offer
     */
    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
