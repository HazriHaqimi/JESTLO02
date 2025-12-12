package visitor;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;

/**
 * Visitor for applying the Ace scoring rule.
 * An Ace becomes worth 5 points if it's the only card of its suit in the Jest.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class AceRuleVisitor implements ScoreVisitor {
    /** Value of Ace when alone in its suit */
    private int aceValue;
    
    /** Normal value of Ace when not alone */
    private int normalAceValue;
    
    /** Running total score */
    private int totalScore;
    
    /** Count of cards per suit */
    private Map<Suit, Integer> suitCounts;
    
    /** Whether each suit has an Ace */
    private Map<Suit, Boolean> hasAce;

    /**
     * Creates an Ace rule visitor.
     */
    public AceRuleVisitor() {
        this.aceValue = 5;
        this.normalAceValue = 1;
        this.totalScore = 0;
        this.suitCounts = new HashMap<>();
        this.hasAce = new HashMap<>();
        
        for (Suit suit : Suit.values()) {
            suitCounts.put(suit, 0);
            hasAce.put(suit, false);
        }
    }

    /**
     * Visits a suit card and tracks suit counts and Aces.
     * 
     * @param suitCard The suit card to process
     */
    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        suitCounts.put(suit, suitCounts.get(suit) + 1);
        
        if (suitCard.isAce()) {
            hasAce.put(suit, true);
        }
    }

    /**
     * Visits the Joker (no effect on Ace rule).
     * 
     * @param jokerCard The Joker card
     */
    @Override
    public void visit(JokerCard jokerCard) {
    }

    /**
     * Checks if an Ace is alone in its suit.
     * 
     * @param suit The suit to check
     * @return true if Ace is the only card of that suit
     */
    public boolean aceAlone(Suit suit) {
        return hasAce.get(suit) && suitCounts.get(suit) == 1;
    }

    /**
     * Gets the value of an Ace in a specific suit.
     * 
     * @param suit The suit to check
     * @return 5 if alone, 1 otherwise
     */
    public int getAceValue(Suit suit) {
        if (aceAlone(suit)) {
            return aceValue;
        }
        return normalAceValue;
    }

    /**
     * Gets the total score (not used for this visitor).
     * 
     * @return The total score
     */
    @Override
    public int getTotalScore() {
        return totalScore;
    }
}
