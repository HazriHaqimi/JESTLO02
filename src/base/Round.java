package base;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single round in the JEST game.
 * Each round: deal 2 cards, make offers, take turns taking 1 card each.
 * At end of round, each player has taken exactly 1 card to their Jest.
 * Remaining offer cards stay on table until next round or final collection.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class Round {
    /** List of offers made this round */
    private List<Offer> offers;
    
    /** The deck to draw from */
    private Deck deck;
    
    /** List of players in this round */
    private List<Player> players;
    
    /** Players who have already taken a card this round */
    private List<Player> playersWhoTook;

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
        this.playersWhoTook = new ArrayList<>();
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
     * Has each player make their offer (1 face-up, 1 face-down).
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
     * Processes card taking phase according to JEST rules:
     * 1. Player with highest face-up card goes first
     * 2. Must take from ANOTHER player's complete offer
     * 3. The player whose offer was taken from goes next
     * 4. If that player already took, highest remaining face-up goes
     * 5. Final player can take from own offer if it's the only complete one
     */
    public void takeOffers() {
        playersWhoTook.clear();
        
        // Find first player (highest face-up card)
        Player currentPlayer = findPlayerWithHighestFaceUp(getPlayersWithCompleteOffers());
        
        while (playersWhoTook.size() < players.size()) {
            if (currentPlayer == null) {
                break;
            }
            
            System.out.println("\n" + currentPlayer.getName() + "'s turn to take a card.");
            
            // Get available offers (complete offers from other players, or own if last)
            List<Offer> availableOffers = getAvailableOffersFor(currentPlayer);
            
            if (availableOffers.isEmpty()) {
                System.out.println(currentPlayer.getName() + " has no available offers to take from.");
                playersWhoTook.add(currentPlayer);
                currentPlayer = findNextPlayer(null);
                continue;
            }
            
            // Select and take from an offer
            Offer selectedOffer;
            if (availableOffers.size() == 1) {
                selectedOffer = availableOffers.get(0);
                if (selectedOffer.getOwner() == currentPlayer) {
                    System.out.println(currentPlayer.getName() + " must take from their own offer (only complete offer).");
                }
            } else {
                selectedOffer = currentPlayer.getStrategy().selectOffer(availableOffers);
            }
            
            // Take a card from the selected offer
            boolean takeFaceUp = currentPlayer.getStrategy().chooseCard(selectedOffer);
            Card takenCard = selectedOffer.selectCard(takeFaceUp);
            
            if (takenCard != null) {
                currentPlayer.getJest().addCard(takenCard);
                System.out.println(currentPlayer.getName() + " took " + takenCard + 
                    " from " + selectedOffer.getOwner().getName() + "'s offer.");
            }
            
            playersWhoTook.add(currentPlayer);
            
            // Determine next player
            Player offerOwner = selectedOffer.getOwner();
            currentPlayer = findNextPlayer(offerOwner);
        }
    }

    /**
     * Gets available offers for a player to take from.
     * - Must be complete (2 cards)
     * - Must be from another player, UNLESS this is the final player and own offer is only complete one
     * 
     * @param player The player who wants to take
     * @return List of available offers
     */
    private List<Offer> getAvailableOffersFor(Player player) {
        List<Offer> available = new ArrayList<>();
        List<Offer> completeOffers = new ArrayList<>();
        
        // Find all complete offers from players who haven't taken yet
        for (Offer offer : offers) {
            if (offer.isComplete()) {
                completeOffers.add(offer);
            }
        }
        
        // Check if this is the final player scenario
        boolean isFinalPlayer = (playersWhoTook.size() == players.size() - 1);
        boolean ownOfferIsOnlyComplete = false;
        
        if (isFinalPlayer && completeOffers.size() == 1 && 
            completeOffers.get(0).getOwner() == player) {
            ownOfferIsOnlyComplete = true;
        }
        
        // Add offers from other players first
        for (Offer offer : completeOffers) {
            if (offer.getOwner() != player) {
                available.add(offer);
            }
        }
        
        // If final player and own offer is the only complete one, add it
        if (ownOfferIsOnlyComplete) {
            for (Offer offer : completeOffers) {
                if (offer.getOwner() == player) {
                    available.add(offer);
                }
            }
        }
        
        return available;
    }

    /**
     * Finds the next player to take a card.
     * Priority: The player whose offer was taken from (if they haven't taken yet)
     * Otherwise: Player with highest face-up card among remaining
     * 
     * @param offerOwner The player whose offer was just taken from
     * @return The next player, or null if all have taken
     */
    private Player findNextPlayer(Player offerOwner) {
        // If the offer owner hasn't taken yet, they go next
        if (offerOwner != null && !playersWhoTook.contains(offerOwner)) {
            return offerOwner;
        }
        
        // Otherwise, find player with highest face-up among those who haven't taken
        List<Player> remaining = new ArrayList<>();
        for (Player p : players) {
            if (!playersWhoTook.contains(p)) {
                remaining.add(p);
            }
        }
        
        if (remaining.isEmpty()) {
            return null;
        }
        
        return findPlayerWithHighestFaceUp(remaining);
    }

    /**
     * Finds players who still have complete offers.
     * 
     * @return List of players with complete offers
     */
    private List<Player> getPlayersWithCompleteOffers() {
        List<Player> result = new ArrayList<>();
        for (Offer offer : offers) {
            if (offer.isComplete()) {
                result.add(offer.getOwner());
            }
        }
        return result;
    }

    /**
     * Finds the player with the highest face-up card value among candidates.
     * Ties broken by suit strength (Spade > Club > Diamond > Heart).
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
     * Gets the remaining cards from all offers (for next round preparation).
     * Does NOT add them to Jest - they stay on table.
     * 
     * @return List of remaining cards from offers
     */
    public List<Card> getLeftoverCards() {
        List<Card> leftovers = new ArrayList<>();
        for (Offer offer : offers) {
            Card remaining = offer.getRemainingCard();
            if (remaining != null) {
                leftovers.add(remaining);
            }
        }
        return leftovers;
    }

    /**
     * Collects leftover cards into owners' Jests.
     * Only called in the FINAL round when deck is empty.
     */
    public void collectLeftoverCardsToJest() {
        for (Offer offer : offers) {
            Card remaining = offer.getRemainingCard();
            if (remaining != null && offer.getOwner() != null) {
                offer.getOwner().getJest().addCard(remaining);
                System.out.println(offer.getOwner().getName() + " added final card to Jest: " + remaining);
            }
        }
    }

    /**
     * Prepares for the next round by clearing hands.
     * Note: Offers are NOT cleared here - leftover cards handled by Game.
     */
    public void prepareNextRound() {
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
}
