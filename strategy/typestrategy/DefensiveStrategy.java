package strategy.typestrategy;

import base.Offer;
import base.Player;
import base.Card;
import card.SuitCard;
import strategy.AIStrategy;
import java.util.List;

/**
 * Defensive AI strategy for virtual players.
 * Prioritizes hiding high-value cards and being cautious.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class DefensiveStrategy extends AIStrategy {

    /**
     * Creates a defensive strategy.
     */
    public DefensiveStrategy() {
        super();
    }

    /**
     * Creates an offer by hiding the higher value card.
     * Shows the lower value card face-up to minimize opponent gains.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    @Override
    public Offer chooseOffer(Player player) {
        if (player.getHand().size() >= 2) {
            Offer offer = new Offer();
            Card card1 = player.getHand().get(0);
            Card card2 = player.getHand().get(1);
            
            int value1 = getCardValue(card1);
            int value2 = getCardValue(card2);
            
            if (value1 > value2) {
                offer.setFaceDown(card1);
                offer.setFaceUp(card2);
            } else {
                offer.setFaceDown(card2);
                offer.setFaceUp(card1);
            }
            offer.setOwner(player);
            return offer;
        }
        return null;
    }

    /**
     * Selects the offer with the lowest face-up card value.
     * Tries to minimize risk by taking known low-value cards.
     * 
     * @param offers List of available offers
     * @return The offer with lowest visible card
     */
    @Override
    public Offer selectOffer(List<Offer> offers) {
        Offer bestOffer = null;
        int lowestValue = Integer.MAX_VALUE;
        
        for (Offer offer : offers) {
            if (offer.getFaceUp() != null) {
                int value = getCardValue(offer.getFaceUp());
                if (value < lowestValue) {
                    lowestValue = value;
                    bestOffer = offer;
                }
            }
        }
        return bestOffer != null ? bestOffer : offers.get(random.nextInt(offers.size()));
    }

    /**
     * Takes the face-up card (predictable choice).
     * 
     * @param offer The offer to take from
     * @return true (takes face-up)
     */
    @Override
    public boolean chooseCard(Offer offer) {
        return true;
    }

    /**
     * Evaluates an offer (no-op for defensive strategy).
     * 
     * @param offer The offer to evaluate
     */
    @Override
    public void evaluateOffer(Offer offer) {
    }

    /**
     * Gets the value of a card for comparison.
     * 
     * @param card The card to evaluate
     * @return The card's value, or 0 for Joker
     */
    private int getCardValue(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getValue();
        }
        return 0;
    }
}
