package properties;

/**
 * Enumeration of card numbers/values in the JEST game.
 * Each card has a face value from 1 (Ace) to 4.
 * Extension cards can add 6, 7, 8, 9 (only with expansion enabled).
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public enum Numbers {
    /** Ace - value 1, but becomes 5 if it's the only card of its suit */
    ACE(1),
    
    /** Two - value 2 */
    TWO(2),
    
    /** Three - value 3 */
    THREE(3),
    
    /** Four - value 4 */
    FOUR(4),
    
    /** Six - value 6 (expansion card) */
    SIX(6),
    
    /** Seven - value 7 (expansion card) */
    SEVEN(7),
    
    /** Eight - value 8 (expansion card) */
    EIGHT(8),
    
    /** Nine - value 9 (expansion card) */
    NINE(9);

    /** The face value of this number */
    private final int value;

    /**
     * Creates a number with a specific value.
     * 
     * @param value The face value
     */
    Numbers(int value) {
        this.value = value;
    }

    /**
     * Gets the face value of this number.
     * 
     * @return The value (1-4)
     */
    public int getValue() {
        return value;
    }
}
