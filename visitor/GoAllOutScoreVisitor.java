package visitor;

import base.Card;
import base.Jest;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * Score visitor for GO ALL OUT variant.
 * Modified scoring rules:
 * - All cards add value (no subtracting)
 * - Diamonds and Hearts now add their face value
 * - Joker multiplies Jest by 1.5 instead of complex Heart rules
 * - More aggressive, high-reward gameplay
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class GoAllOutScoreVisitor implements ScoreVisitor {
    /** Running total score */
    private int totalScore;
    
    /** Count of Heart cards */
    private int heartCount;
    
    /** Whether the Jest contains the Joker */
    private boolean hasJoker;
    
    /** Count of cards per suit */
    private Map<Suit, Integer> suitCounts;
    
    /** Reference to the Jest being scored */
    private Jest jest;
    
    /** Set of black pair values already counted */
    private Set<Integer> countedBlackPairs;

    /**
     * Creates a Go All Out score visitor.
     * 
     * @param jest The Jest to score
     */
    public GoAllOutScoreVisitor(Jest jest) {
        this.totalScore = 0;
        this.heartCount = 0;
        this.hasJoker = false;
        this.suitCounts = new HashMap<>();
        this.countedBlackPairs = new HashSet<>();
        this.jest = jest;
        
        for (Suit suit : Suit.values()) {
            suitCounts.put(suit, 0);
        }
        
        // Pre-analyze Jest
        for (Card card : jest.getCards()) {
            if (card instanceof SuitCard) {
                SuitCard sc = (SuitCard) card;
                suitCounts.put(sc.getSuit(), suitCounts.get(sc.getSuit()) + 1);
                if (sc.getSuit() == Suit.HEART) {
                    heartCount++;
                }
            } else if (card instanceof JokerCard) {
                hasJoker = true;
            }
        }
    }

    /**
     * Visits a suit card and adds its value (no subtracting in Go All Out).
     * GO ALL OUT rules:
     * - Spades and Clubs: add value
     * - Diamonds: add value (not subtract)
     * - Hearts: add value (not 0)
     * - Black pairs: still add 2 bonus
     * 
     * @param suitCard The suit card to score
     */
    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        int value = suitCard.getValue();

        if (suitCard.isAce()) {
            value = getAceValue(suit);
        }

        // In Go All Out, all cards add value
        totalScore += value;
        
        // Black pairs still add bonus
        if (suit == Suit.SPADE || suit == Suit.CLUB) {
            checkBlackPair(suitCard);
        }
    }

    /**
     * Gets the value of an Ace based on whether it's alone in its suit.
     * 
     * @param suit The Ace's suit
     * @return 5 if alone, 1 otherwise
     */
    private int getAceValue(Suit suit) {
        if (suitCounts.get(suit) == 1) {
            return 5;
        }
        return 1;
    }

    /**
     * Checks for black pair bonus (Spade + Club of same value).
     * 
     * @param card The card to check for pairing
     */
    private void checkBlackPair(SuitCard card) {
        if (card.getSuit() != Suit.SPADE && card.getSuit() != Suit.CLUB) {
            return;
        }
        
        if (jest == null) return;
        if (countedBlackPairs.contains(card.getValue())) return;
        
        Suit otherSuit = (card.getSuit() == Suit.SPADE) ? Suit.CLUB : Suit.SPADE;
        
        for (Card c : jest.getCards()) {
            if (c instanceof SuitCard && c != card) {
                SuitCard sc = (SuitCard) c;
                if (sc.getSuit() == otherSuit && sc.getValue() == card.getValue()) {
                    totalScore += 2;
                    countedBlackPairs.add(card.getValue());
                    break;
                }
            }
        }
    }

    /**
     * Visits the Joker and multiplies Jest by 1.5 (Go All Out rule).
     * In Go All Out, Joker simply multiplies the current score by 1.5.
     * 
     * @param jokerCard The Joker card
     */
    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
        
        // Joker multiplies total Jest by 1.5
        int baseScore = totalScore;
        totalScore = (int) (baseScore * 1.5);
    }

    /**
     * Gets the total calculated score.
     * 
     * @return The final score
     */
    @Override
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Gets the Heart count.
     * 
     * @return Number of Hearts in Jest
     */
    public int getHeartCount() {
        return heartCount;
    }

    /**
     * Checks if Jest has the Joker.
     * 
     * @return true if Joker is present
     */
    public boolean hasJoker() {
        return hasJoker;
    }
}
