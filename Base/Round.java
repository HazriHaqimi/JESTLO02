package base;

import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Offer> offers;
    private Deck deck;
    private List<Player> players;

    public Round(Deck deck, List<Player> players) {
        this.deck = deck;
        this.players = players;
        this.offers = new ArrayList<>();
    }

    public void dealCards() {
        for (Player player : players) {
            Card card1 = deck.drawCard();
            Card card2 = deck.drawCard();
            if (card1 != null) player.addCardToHand(card1);
            if (card2 != null) player.addCardToHand(card2);
        }
    }

    public void makeOffers() {
        offers.clear();
        for (Player player : players) {
            Offer offer = player.makeOffer();
            if (offer != null) {
                offers.add(offer);
            }
        }
    }

    public void takeOffers() {
        for (Player player : players) {
            if (!offers.isEmpty()) {
                Offer selectedOffer = player.getStrategy().selectOffer(offers);
                boolean takeFaceUp = player.getStrategy().chooseCard(selectedOffer);
                player.takeOffer(selectedOffer, takeFaceUp);
            }
        }
    }

    public void collectLeftoverCards() {
        for (Offer offer : offers) {
            Card remaining = offer.getRemainingCard();
            if (remaining != null && offer.getOwner() != null) {
                offer.getOwner().getJest().addCard(remaining);
            }
        }
    }

    public void prepareNextRound() {
        offers.clear();
        for (Player player : players) {
            player.clearHand();
        }
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }
}
