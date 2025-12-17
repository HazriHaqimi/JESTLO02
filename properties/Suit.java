package properties;

/**
 * Enumeration of card suits in the JEST game.
 * Suits have a hierarchy for tie-breaking: Spade > Club > Diamond > Heart.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public enum Suit {
    /** Spade suit - strongest, always adds to score */
    SPADE,
    
    /** Club suit - second strongest, always adds to score */
    CLUB,
    
    /** Diamond suit - third strongest, always subtracts from score */
    DIAMOND,
    
    /** Heart suit - weakest, worth nothing unless Joker rules apply */
    HEART
}
