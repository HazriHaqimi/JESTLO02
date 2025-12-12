package visitor;

import card.SuitCard;
import card.JokerCard;
import properties.Suit;

public class FinalScoreVisitor implements ScoreVisitor {
    private int totalScore;
    private int heartCount;
    private boolean hasJoker;
    private int aceCount;

    public FinalScoreVisitor() {
        this.totalScore = 0;
        this.heartCount = 0;
        this.hasJoker = false;
        this.aceCount = 0;
    }

    @Override
    public void visit(SuitCard suitCard) {
        Suit suit = suitCard.getSuit();
        int value = suitCard.getValue();

        switch (suit) {
            case SPADE:
            case CLUB:
                totalScore += value;
                break;
            case DIAMOND:
                totalScore -= value;
                break;
            case HEART:
                heartCount++;
                break;
        }

        if (suitCard.isAce()) {
            aceCount++;
        }
    }

    @Override
    public void visit(JokerCard jokerCard) {
        hasJoker = true;
        calculateJokerValue();
    }

    private void calculateJokerValue() {
        if (heartCount == 0) {
            totalScore += 4;
        } else if (heartCount >= 1 && heartCount <= 3) {
            totalScore -= heartCount;
        } else if (heartCount == 4) {
            totalScore += heartCount;
        }
    }

    @Override
    public int getTotalScore() {
        if (!hasJoker) {
        }
        return totalScore;
    }

    public int getHeartCount() {
        return heartCount;
    }

    public boolean hasJoker() {
        return hasJoker;
    }
}
