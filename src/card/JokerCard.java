package card;

import base.Card;
import visitor.ScoreVisitor;

/**
 * Represents the Joker card in the JEST game.
 * The Joker has special scoring rules that depend on the number of Hearts in the Jest.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class JokerCard extends Card {
    /** Base value of the Joker (0 for face-up comparison) */
    private int baseValue;

    /**
     * Creates a new Joker card.
     */
    public JokerCard() {
        this.baseValue = 0;
    }

    /**
     * Gets the base value for comparisons.
     * 
     * @return The base value (0)
     */
    public int getBaseValue() {
        return baseValue;
    }

    /**
     * Sets the base value.
     * 
     * @param baseValue The value to set
     */
    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    /**
     * Gets the value for comparisons.
     * The Joker has value 0 for determining turn order.
     * 
     * @return 0
     */
    @Override
    public int getValue() {
        return baseValue;
    }

    /**
     * Accepts a visitor for score calculation.
     * 
     * @param visitor The score visitor
     */
    @Override
    public void accept(ScoreVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a string representation of the Joker.
     * 
     * @return "Joker"
     */
    @Override
    public String toString() {
        return "Joker";
    }
}
