package card;

import base.Card;
import properties.Suit;
import properties.Numbers;
import visitor.ScoreVisitor;

public class SuitCard extends Card {
    private Suit suit;
    private Numbers number;

    public SuitCard(Suit suit, Numbers number) {
        this.suit = suit;
        this.number = number;
    }

    public boolean isAce() {
        return number == Numbers.ACE;
    }

    public int getValue() {
        return number.getValue();
    }

    public Suit getSuit() {
        return suit;
    }

    public Numbers getNumber() {
        return number;
    }

    @Override
    public void accept(ScoreVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return number + " of " + suit;
    }
}
