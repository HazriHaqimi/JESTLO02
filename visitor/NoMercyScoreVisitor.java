package visitor;

import base.Card;
import base.Jest;
import base.GameConfig;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * Score visitor for NO MERCY variant.
 * Uses normal scoring rules but applies threshold penalty:
 * If final Jest score exceeds the threshold (7-10), Jest value becomes 0.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class NoMercyScoreVisitor extends FinalScoreVisitor {
    /** Game configuration with threshold */
    private GameConfig config;
    
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
     * Creates a No Mercy score visitor.
     * 
     * @param jest The Jest to score
     * @param config Game configuration with threshold
     */
    public NoMercyScoreVisitor(Jest jest, GameConfig config) {
        super(jest);
        this.config = config;
        this.jest = jest;
        this.totalScore = 0;
        this.heartCount = 0;
        this.hasJoker = false;
        this.suitCounts = new HashMap<>();
        this.countedBlackPairs = new HashSet<>();
        
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
     * Gets the final score after applying No Mercy threshold penalty.
     * If score > threshold, returns 0. Otherwise returns normal score.
     * 
     * @return Final score (or 0 if threshold exceeded)
     */
    @Override
    public int getTotalScore() {
        int baseScore = super.getTotalScore();
        
        if (config.exceedsNoMercyThreshold(baseScore)) {
            return 0;
        }
        return baseScore;
    }
}
