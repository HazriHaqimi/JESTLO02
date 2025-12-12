package visitor;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;

public class JokerRuleVisitor implements ScoreVisitor {
    private int jokerValue;
    private int heartCount;
    private int totalScore;
    private boolean hasJoker;

    public JokerRuleVisitor() {
        this.jokerValue = 0;
        this.heartCount = 0;
        this.totalScore = 0;
        this.hasJoker = false;
    }

    @Override
    public void visit(SuitCard suitCard) {
        if (suitCard.getSuit() == Suit.HEART) {
            heartCount++;
        }
    }

    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
        adjustJokerValue(heartCount);
    }

    public int countHearts() {
        return heartCount;
    }

    public void adjustJokerValue(int numHearts) {
        if (numHearts == 0) {
            jokerValue = 4;
        } else if (numHearts >= 1 && numHearts <= 3) {
            jokerValue = -numHearts;
        } else if (numHearts == 4) {
            jokerValue = numHearts;
        }
        totalScore += jokerValue;
    }

    public int getJokerValue() {
        return jokerValue;
    }

    @Override
    public int getTotalScore() {
        return totalScore;
    }

    public boolean hasJoker() {
        return hasJoker;
    }
}
