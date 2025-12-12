package strategy;

import base.Offer;
import base.Player;
import java.util.List;

/**
 * Interface for player decision-making strategies.
 * Implements the Strategy design pattern for flexible player behavior.
 * 
 * @author JEST Team
 * @version 1.0
 */
public interface PlayStrategy {
    
    /**
     * Creates an offer from the player's hand.
     * Decides which card to place face-up and which face-down.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    Offer chooseOffer(Player player);
    
    /**
     * Selects an offer to take a card from.
     * 
     * @param offers List of available offers
     * @return The selected offer
     */
    Offer selectOffer(List<Offer> offers);
    
    /**
     * Chooses which card to take from an offer.
     * 
     * @param offer The offer to take from
     * @return true to take face-up card, false for face-down
     */
    boolean chooseCard(Offer offer);
}
