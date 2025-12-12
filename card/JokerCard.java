package card;

import base.Card;
import visitor.ScoreVisitor;

public class JokerCard extends Card {
    private int baseValue;

    public JokerCard() {
        this.baseValue = 0;
    }

    public int getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public void accept(ScoreVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "Joker";
    }
}
