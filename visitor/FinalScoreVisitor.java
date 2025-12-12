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
 * Visitor for calculating final Jest scores.
 * Implements all JEST scoring rules including:
 * - Spades and Clubs add their face value
 * - Diamonds subtract their face value
 * - Hearts are worth nothing (unless Joker rules apply)
 * - Aces become 5 if only card of their suit
 * - Black pairs (same value Spade + Club) add 2 bonus
 * - Joker value depends on number of Hearts
 * 
 * @author JEST Team
 * @version 1.0
 */
public class FinalScoreVisitor implements ScoreVisitor {
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
     * Creates a score visitor without a Jest reference.
     */
    public FinalScoreVisitor() {
        this.totalScore = 0;
        this.heartCount = 0;
        this.hasJoker = false;
        this.suitCounts = new HashMap<>();
        this.countedBlackPairs = new HashSet<>();
        
        for (Suit suit : Suit.values()) {
            suitCounts.put(suit, 0);
        }
    }

    /**
     * Creates a score visitor with a Jest reference.
     * Pre-analyzes the Jest for Ace and Joker rules.
     * 
     * @param jest The Jest to score
     */
    public FinalScoreVisitor(Jest jest) {
        this();
        this.jest = jest;
        
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
     * Visits a suit card and adds its contribution to the score.
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

        switch (suit) {
            case SPADE:
            case CLUB:
                totalScore += value;
                checkBlackPair(suitCard);
                break;
            case DIAMOND:
                totalScore -= value;
                break;
            case HEART:
                break;
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
     * Visits the Joker and calculates its value based on Hearts.
     * - 0 Hearts: +4 points
     * - 1-3 Hearts: Joker worth 0, each Heart reduces score
     * - 4 Hearts: Each Heart adds its value
     * 
     * @param jokerCard The Joker card
     */
    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
        
        if (heartCount == 0) {
            totalScore += 4;
        } else if (heartCount >= 1 && heartCount <= 3) {
        } else if (heartCount == 4) {
            if (jest != null) {
                for (Card c : jest.getCards()) {
                    if (c instanceof SuitCard) {
                        SuitCard sc = (SuitCard) c;
                        if (sc.getSuit() == Suit.HEART) {
                            totalScore += sc.getValue();
                        }
                    }
                }
            }
        }
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
