package visitor;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;

public class AceRuleVisitor implements ScoreVisitor {
    private int aceValue;
    private int normalAceValue;
    private int totalScore;
    private Map<Suit, Integer> suitCounts;
    private Map<Suit, Boolean> hasAce;

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

    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        suitCounts.put(suit, suitCounts.get(suit) + 1);
        
        if (suitCard.isAce()) {
            hasAce.put(suit, true);
        }
    }

    @Override
    public void visit(JokerCard jokerCard) {
    }

    public boolean aceAlone(Suit suit) {
        return hasAce.get(suit) && suitCounts.get(suit) == 1;
    }

    public int getAceValue(Suit suit) {
        if (aceAlone(suit)) {
            return aceValue;
        }
        return normalAceValue;
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }
}
