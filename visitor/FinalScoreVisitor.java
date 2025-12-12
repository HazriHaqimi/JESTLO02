package visitor;

import base.Card;
import base.Jest;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import java.util.HashMap;
import java.util.Map;

public class FinalScoreVisitor implements ScoreVisitor {
    private int totalScore;
    private int heartCount;
    private boolean hasJoker;
    private Map<Suit, Integer> suitCounts;
    private Map<Suit, Boolean> hasAceInSuit;
    private Map<Suit, Integer> suitCardValues;
    private Jest jest;

    public FinalScoreVisitor() {
        this.totalScore = 0;
        this.heartCount = 0;
        this.hasJoker = false;
        this.suitCounts = new HashMap<>();
        this.hasAceInSuit = new HashMap<>();
        this.suitCardValues = new HashMap<>();
        
        for (Suit suit : Suit.values()) {
            suitCounts.put(suit, 0);
            hasAceInSuit.put(suit, false);
            suitCardValues.put(suit, 0);
        }
    }

    public FinalScoreVisitor(Jest jest) {
        this();
        this.jest = jest;
        
        for (Card card : jest.getCards()) {
            if (card instanceof SuitCard) {
                SuitCard sc = (SuitCard) card;
                suitCounts.put(sc.getSuit(), suitCounts.get(sc.getSuit()) + 1);
                if (sc.isAce()) {
                    hasAceInSuit.put(sc.getSuit(), true);
                }
            } else if (card instanceof JokerCard) {
                hasJoker = true;
            }
            if (card instanceof SuitCard && ((SuitCard) card).getSuit() == Suit.HEART) {
                heartCount++;
            }
        }
    }

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
                break;
            case DIAMOND:
                totalScore -= value;
                break;
            case HEART:
                break;
        }

        checkBlackPair(suitCard);
    }

    private int getAceValue(Suit suit) {
        if (suitCounts.get(suit) == 1) {
            return 5;
        }
        return 1;
    }

    private void checkBlackPair(SuitCard card) {
        if (card.getSuit() != Suit.SPADE && card.getSuit() != Suit.CLUB) {
            return;
        }
        
        if (jest == null) return;
        
        Suit otherSuit = (card.getSuit() == Suit.SPADE) ? Suit.CLUB : Suit.SPADE;
        
        for (Card c : jest.getCards()) {
            if (c instanceof SuitCard) {
                SuitCard sc = (SuitCard) c;
                if (sc.getSuit() == otherSuit && sc.getValue() == card.getValue() && sc != card) {
                    if (!suitCardValues.containsKey(card.getSuit()) || 
                        suitCardValues.get(card.getSuit()) != card.getValue()) {
                        totalScore += 2;
                        suitCardValues.put(card.getSuit(), card.getValue());
                    }
                    break;
                }
            }
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
            for (int i = 0; i < heartCount; i++) {
            }
        } else if (heartCount == 4) {
            totalScore += heartCount;
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

    @Override
    public int getTotalScore() {
        if (!hasJoker && jest != null) {
            for (Card c : jest.getCards()) {
                if (c instanceof SuitCard) {
                    SuitCard sc = (SuitCard) c;
                    if (sc.getSuit() == Suit.HEART) {
                    }
                }
            }
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
