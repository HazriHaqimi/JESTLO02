package visitor;

import base.Player;
import card.SuitCard;
import card.JokerCard;
import trophy.Trophy;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;

public class TrophyScoreVisitor implements ScoreVisitor {
    private Trophy trophy;
    private int totalScore;
    private Map<Suit, Integer> highestPerSuit;
    private Map<Suit, Integer> lowestPerSuit;
    private boolean hasJoker;

    public TrophyScoreVisitor(Trophy trophy) {
        this.trophy = trophy;
        this.totalScore = 0;
        this.highestPerSuit = new HashMap<>();
        this.lowestPerSuit = new HashMap<>();
        this.hasJoker = false;
        
        for (Suit suit : Suit.values()) {
            highestPerSuit.put(suit, Integer.MIN_VALUE);
            lowestPerSuit.put(suit, Integer.MAX_VALUE);
        }
    }

    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        int value = suitCard.getValue();
        
        if (value > highestPerSuit.get(suit)) {
            highestPerSuit.put(suit, value);
        }
        if (value < lowestPerSuit.get(suit)) {
            lowestPerSuit.put(suit, value);
        }
    }

    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
    }

    public void applyTrophy(Player player) {
        int bonus = calculateTrophyBonus();
        totalScore += bonus;
    }

    public int calculateTrophyBonus() {
        return 0;
    }

    public int getHighestInSuit(Suit suit) {
        return highestPerSuit.get(suit);
    }

    public int getLowestInSuit(Suit suit) {
        return lowestPerSuit.get(suit);
    }

    public boolean hasJoker() {
        return hasJoker;
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }
}
