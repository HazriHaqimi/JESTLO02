package base;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.ArrayList;
import java.util.List;

public class Round {
    private List<Offer> offers;
    private Deck deck;
    private List<Player> players;
    private List<Player> turnOrder;

    public Round(Deck deck, List<Player> players) {
        this.deck = deck;
        this.players = players;
        this.offers = new ArrayList<>();
        this.turnOrder = new ArrayList<>();
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
        determineTurnOrder();
        
        List<Player> playersWhoTookTurn = new ArrayList<>();
        
        for (Player currentPlayer : turnOrder) {
            List<Offer> availableOffers = getAvailableOffers(currentPlayer, playersWhoTookTurn);
            
            if (availableOffers.isEmpty()) {
                continue;
            }
            
            if (availableOffers.size() == 1 && availableOffers.get(0).getOwner() == currentPlayer) {
                Offer ownOffer = availableOffers.get(0);
                if (ownOffer.isComplete()) {
                    boolean takeFaceUp = currentPlayer.getStrategy().chooseCard(ownOffer);
                    currentPlayer.takeOffer(ownOffer, takeFaceUp);
                    System.out.println(currentPlayer.getName() + " took from their own offer (only option with 2 cards)");
                }
            } else {
                List<Offer> otherOffers = new ArrayList<>();
                for (Offer o : availableOffers) {
                    if (o.getOwner() != currentPlayer && o.isComplete()) {
                        otherOffers.add(o);
                    }
                }
                
                if (!otherOffers.isEmpty()) {
                    Offer selectedOffer = currentPlayer.getStrategy().selectOffer(otherOffers);
                    boolean takeFaceUp = currentPlayer.getStrategy().chooseCard(selectedOffer);
                    currentPlayer.takeOffer(selectedOffer, takeFaceUp);
                    System.out.println(currentPlayer.getName() + " took a card from " + selectedOffer.getOwner().getName() + "'s offer");
                }
            }
            
            playersWhoTookTurn.add(currentPlayer);
        }
    }

    private void determineTurnOrder() {
        turnOrder.clear();
        
        List<Player> remainingPlayers = new ArrayList<>(players);
        
        while (!remainingPlayers.isEmpty()) {
            Player nextPlayer = findPlayerWithHighestFaceUp(remainingPlayers);
            if (nextPlayer != null) {
                turnOrder.add(nextPlayer);
                remainingPlayers.remove(nextPlayer);
            } else {
                turnOrder.addAll(remainingPlayers);
                break;
            }
        }
    }

    private Player findPlayerWithHighestFaceUp(List<Player> candidatePlayers) {
        Player highestPlayer = null;
        int highestValue = Integer.MIN_VALUE;
        Suit strongestSuit = null;
        
        for (Offer offer : offers) {
            if (!candidatePlayers.contains(offer.getOwner())) continue;
            if (offer.getFaceUp() == null) continue;
            
            Card faceUp = offer.getFaceUp();
            int value = getCardValue(faceUp);
            Suit suit = getCardSuit(faceUp);
            
            if (value > highestValue || 
                (value == highestValue && compareSuitStrength(suit, strongestSuit) > 0)) {
                highestValue = value;
                strongestSuit = suit;
                highestPlayer = offer.getOwner();
            }
        }
        
        return highestPlayer;
    }

    private List<Offer> getAvailableOffers(Player currentPlayer, List<Player> playersWhoTookTurn) {
        List<Offer> available = new ArrayList<>();
        
        for (Offer offer : offers) {
            if (offer.hasCards()) {
                Player offerOwner = offer.getOwner();
                if (!playersWhoTookTurn.contains(offerOwner) || offerOwner == currentPlayer) {
                    available.add(offer);
                }
            }
        }
        
        return available;
    }

    private int getCardValue(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getValue();
        } else if (card instanceof JokerCard) {
            return 0;
        }
        return 0;
    }

    private Suit getCardSuit(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getSuit();
        }
        return null;
    }

    private int compareSuitStrength(Suit suit1, Suit suit2) {
        if (suit1 == suit2) return 0;
        if (suit2 == null) return 1;
        if (suit1 == null) return -1;
        return getSuitStrength(suit1) - getSuitStrength(suit2);
    }

    private int getSuitStrength(Suit suit) {
        if (suit == null) return 0;
        switch (suit) {
            case SPADE: return 4;
            case CLUB: return 3;
            case DIAMOND: return 2;
            case HEART: return 1;
            default: return 0;
        }
    }

    public void collectLeftoverCards() {
        for (Offer offer : offers) {
            Card remaining = offer.getRemainingCard();
            if (remaining != null && offer.getOwner() != null) {
                offer.getOwner().getJest().addCard(remaining);
                System.out.println(offer.getOwner().getName() + " added remaining card to Jest: " + remaining);
            }
        }
    }

    public void prepareNextRound() {
        offers.clear();
        turnOrder.clear();
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

    public List<Player> getTurnOrder() {
        return turnOrder;
    }
}
