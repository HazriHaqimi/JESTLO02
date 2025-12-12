package base;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single round in the JEST game.
 * Each round consists of dealing cards, making offers, taking cards, and collecting leftovers.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class Round {
    /** List of offers made this round */
    private List<Offer> offers;
    
    /** The deck to draw from */
    private Deck deck;
    
    /** List of players in this round */
    private List<Player> players;
    
    /** Order in which players take turns */
    private List<Player> turnOrder;

    /**
     * Creates a new round.
     * 
     * @param deck The deck to draw from
     * @param players The players participating
     */
    public Round(Deck deck, List<Player> players) {
        this.deck = deck;
        this.players = players;
        this.offers = new ArrayList<>();
        this.turnOrder = new ArrayList<>();
    }

    /**
     * Deals 2 cards to each player.
     */
    public void dealCards() {
        for (Player player : players) {
            Card card1 = deck.drawCard();
            Card card2 = deck.drawCard();
            if (card1 != null) player.addCardToHand(card1);
            if (card2 != null) player.addCardToHand(card2);
        }
    }

    /**
     * Has each player make their offer.
     */
    public void makeOffers() {
        offers.clear();
        for (Player player : players) {
            Offer offer = player.makeOffer();
            if (offer != null) {
                offers.add(offer);
            }
        }
    }

    /**
     * Processes card taking phase.
     * Players take turns based on highest face-up card.
     */
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

    /**
     * Determines the order of play based on highest face-up card.
     * Ties broken by suit strength (Spade > Club > Diamond > Heart).
     */
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

    /**
     * Finds the player with the highest face-up card value.
     * 
     * @param candidatePlayers Players to consider
     * @return The player with highest face-up card
     */
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

    /**
     * Gets offers available to a player.
     * 
     * @param currentPlayer The player taking
     * @param playersWhoTookTurn Players who already took
     * @return List of available offers
     */
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

    /**
     * Gets the value of a card.
     * 
     * @param card The card to evaluate
     * @return The card's value (0 for Joker)
     */
    private int getCardValue(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getValue();
        } else if (card instanceof JokerCard) {
            return 0;
        }
        return 0;
    }

    /**
     * Gets the suit of a card.
     * 
     * @param card The card to check
     * @return The card's suit, or null for Joker
     */
    private Suit getCardSuit(Card card) {
        if (card instanceof SuitCard) {
            return ((SuitCard) card).getSuit();
        }
        return null;
    }

    /**
     * Compares two suits by strength.
     * 
     * @param suit1 First suit
     * @param suit2 Second suit
     * @return Positive if suit1 is stronger
     */
    private int compareSuitStrength(Suit suit1, Suit suit2) {
        if (suit1 == suit2) return 0;
        if (suit2 == null) return 1;
        if (suit1 == null) return -1;
        return getSuitStrength(suit1) - getSuitStrength(suit2);
    }

    /**
     * Gets the strength value of a suit.
     * 
     * @param suit The suit to evaluate
     * @return Strength (4=Spade, 3=Club, 2=Diamond, 1=Heart)
     */
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

    /**
     * Collects leftover cards into owners' Jests.
     */
    public void collectLeftoverCards() {
        for (Offer offer : offers) {
            Card remaining = offer.getRemainingCard();
            if (remaining != null && offer.getOwner() != null) {
                offer.getOwner().getJest().addCard(remaining);
                System.out.println(offer.getOwner().getName() + " added remaining card to Jest: " + remaining);
            }
        }
    }

    /**
     * Prepares for the next round by clearing state.
     */
    public void prepareNextRound() {
        offers.clear();
        turnOrder.clear();
        for (Player player : players) {
            player.clearHand();
        }
    }

    /**
     * Gets the list of offers.
     * 
     * @return List of offers
     */
    public List<Offer> getOffers() {
        return offers;
    }

    /**
     * Sets the list of offers.
     * 
     * @param offers List of offers to set
     */
    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    /**
     * Gets the turn order.
     * 
     * @return List of players in turn order
     */
    public List<Player> getTurnOrder() {
        return turnOrder;
    }
}
