package strategy.typestrategy;

import base.Offer;
import base.Player;
import base.Card;
import card.SuitCard;
import strategy.AIStrategy;
import java.util.List;

/**
 * Offensive AI strategy for virtual players.
 * Prioritizes showing high-value cards and taking high-value cards.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class OffensiveStrategy extends AIStrategy {

    /**
     * Creates an offensive strategy.
     */
    public OffensiveStrategy() {
        super();
    }

    /**
     * Creates an offer by hiding the lower value card.
     * Shows the higher value card face-up to attract opponents.
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
            
            if (value1 < value2) {
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
     * Selects the offer with the highest face-up card value.
     * 
     * @param offers List of available offers
     * @return The offer with highest visible card
     */
    @Override
    public Offer selectOffer(List<Offer> offers) {
        Offer bestOffer = null;
        int highestValue = Integer.MIN_VALUE;
        
        for (Offer offer : offers) {
            if (offer.getFaceUp() != null) {
                int value = getCardValue(offer.getFaceUp());
                if (value > highestValue) {
                    highestValue = value;
                    bestOffer = offer;
                }
            }
        }
        return bestOffer != null ? bestOffer : offers.get(random.nextInt(offers.size()));
    }

    /**
     * Always takes the face-up card (greedy approach).
     * 
     * @param offer The offer to take from
     * @return true (always takes face-up)
     */
    @Override
    public boolean chooseCard(Offer offer) {
        return true;
    }

    /**
     * Evaluates an offer (no-op for offensive strategy).
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
