package base;

import java.util.Random;

/**
 * Configuration container for game settings.
 * Encapsulates expansion card usage, variant selection, and variant-specific parameters.
 * This class ensures modularity and extensibility of the game.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class GameConfig {
    /** Whether expansion cards (6, 7, 8, 9) are enabled */
    private boolean useExpansion;
    
    /** The selected game variant */
    private GameVariant variant;
    
    /** Threshold for NO_MERCY variant (random between 7-10) */
    private int noMercyThreshold;
    
    /** Random number generator for variant-specific values */
    private Random random;

    /**
     * Creates a default game configuration.
     * Default: No expansion, Normal mode, no threshold needed.
     */
    public GameConfig() {
        this.useExpansion = false;
        this.variant = GameVariant.NORMAL;
        this.random = new Random();
        this.noMercyThreshold = 0;
    }

    /**
     * Creates a game configuration with specified settings.
     * 
     * @param useExpansion true to include expansion cards
     * @param variant The game variant to use
     */
    public GameConfig(boolean useExpansion, GameVariant variant) {
        this.useExpansion = useExpansion;
        this.variant = variant;
        this.random = new Random();
        
        // Initialize variant-specific parameters
        if (variant == GameVariant.NO_MERCY) {
            this.noMercyThreshold = 7 + random.nextInt(4); // Random between 7-10
        }
    }

    /**
     * Gets whether expansion cards are enabled.
     * 
     * @return true if expansion is enabled
     */
    public boolean isExpansionEnabled() {
        return useExpansion;
    }

    /**
     * Sets whether to use expansion cards.
     * 
     * @param useExpansion true to enable expansion
     */
    public void setExpansionEnabled(boolean useExpansion) {
        this.useExpansion = useExpansion;
    }

    /**
     * Gets the selected game variant.
     * 
     * @return The game variant
     */
    public GameVariant getVariant() {
        return variant;
    }

    /**
     * Sets the game variant.
     * Reinitializes variant-specific parameters.
     * 
     * @param variant The variant to set
     */
    public void setVariant(GameVariant variant) {
        this.variant = variant;
        
        if (variant == GameVariant.NO_MERCY) {
            this.noMercyThreshold = 7 + random.nextInt(4);
        }
    }

    /**
     * Gets the No Mercy threshold for the NO_MERCY variant.
     * 
     * @return Threshold between 7-10, or 0 if not in NO_MERCY mode
     */
    public int getNoMercyThreshold() {
        return noMercyThreshold;
    }

    /**
     * Checks if the given score exceeds the No Mercy threshold.
     * Only meaningful in NO_MERCY variant.
     * 
     * @param score The Jest score to check
     * @return true if score exceeds threshold
     */
    public boolean exceedsNoMercyThreshold(int score) {
        if (variant != GameVariant.NO_MERCY) {
            return false;
        }
        return score > noMercyThreshold;
    }

    /**
     * Returns a summary of the configuration.
     * 
     * @return Configuration description
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Game Configuration:\n");
        sb.append("  Expansion Cards: ").append(useExpansion ? "ENABLED" : "DISABLED").append("\n");
        sb.append("  Variant: ").append(variant.getDisplayName()).append("\n");
        sb.append("  Description: ").append(variant.getDescription());
        
        if (variant == GameVariant.NO_MERCY) {
            sb.append("\n  No Mercy Threshold: ").append(noMercyThreshold);
        }
        
        return sb.toString();
    }
}
