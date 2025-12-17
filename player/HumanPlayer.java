package player;

import base.Offer;
import base.Player;
import strategy.PlayStrategy;
import strategy.HumanStrategy;

/**
 * Represents a human player in the JEST game.
 * Human players make decisions through console input.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class HumanPlayer extends Player {

    /**
     * Creates a human player with the default HumanStrategy.
     * 
     * @param name The player's name
     */
    public HumanPlayer(String name) {
        super(name, new HumanStrategy());
    }

    /**
     * Creates a human player with a custom strategy.
     * 
     * @param name The player's name
     * @param strategy The strategy to use
     */
    public HumanPlayer(String name, PlayStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Creates an offer using the player's strategy.
     * 
     * @return The offer created
     */
    @Override
    public Offer makeOffer() {
        return strategy.chooseOffer(this);
    }

    /**
     * Gets input from the human player.
     * 
     * @return The input value
     */
    public int getInput() {
        if (strategy instanceof HumanStrategy) {
            return ((HumanStrategy) strategy).getInput();
        }
        return 0;
    }
}
