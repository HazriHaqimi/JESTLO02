package base;

/**
 * Enumeration of game variants with different rule sets.
 * Each variant modifies how Jest scores are calculated and trophies work.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public enum GameVariant {
    /**
     * NORMAL MODE: Standard game rules
     * - Spades and Clubs add their value
     * - Diamonds subtract their value
     * - Hearts are worth nothing
     * - Trophies are awarded normally
     * - Joker value depends on Hearts count
     */
    NORMAL("Normal Mode", "Standard rules - Spades/Clubs add, Diamonds subtract, Trophies awarded"),
    
    /**
     * NO MERCY: Threshold penalty variant
     * - Random threshold between 7-10 is set at game start
     * - If player's final Jest value exceeds threshold, Jest value becomes 0
     * - All other scoring rules remain the same
     * - Trophies still awarded normally
     */
    NO_MERCY("No Mercy", "Jest value reset to 0 if it exceeds random threshold (7-10)"),
    
    /**
     * GO ALL OUT: Aggressive variant
     * - No trophies awarded
     * - All cards add to Jest (no negative cards)
     * - Joker multiplies Jest by 1.5 instead of complex Heart rules
     * - Diamonds add value (not subtract)
     * - Hearts now add value instead of being worth 0
     * - More aggressive, high-risk gameplay
     */
    GO_ALL_OUT("Go All Out", "No trophies - all cards add value, Joker multiplies by 1.5");

    /** Display name of the variant */
    private final String displayName;
    
    /** Description of the variant */
    private final String description;

    /**
     * Creates a game variant.
     * 
     * @param displayName Display name
     * @param description Rule description
     */
    GameVariant(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    /**
     * Gets the display name.
     * 
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the description.
     * 
     * @return Rule description
     */
    public String getDescription() {
        return description;
    }
}
