package strategy.typestrategy;

import base.Offer;
import base.Player;
import base.Card;
import card.SuitCard;
import strategy.AIStrategy;
import java.util.List;

public class DefensiveStrategy extends AIStrategy {

    public DefensiveStrategy() {
        super();
    }

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

    @Override
    public boolean chooseCard(Offer offer) {
        return true;
    }

    @Override
    public void evaluateOffer(Offer offer) {
    }

    private int getCardValue(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getValue();
        }
        return 0;
    }
}
