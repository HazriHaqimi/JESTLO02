package player;

import base.Offer;
import base.Player;
import strategy.PlayStrategy;
import strategy.AIStrategy;
import strategy.typestrategy.DefensiveStrategy;
import strategy.typestrategy.OffensiveStrategy;
import java.util.Random;

/**
 * Represents an AI-controlled player in the JEST game.
 * Virtual players make automatic decisions using AI strategies.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public class VirtualPlayer extends Player {
    /** Random generator for strategy selection */
    private static Random random = new Random();

    /**
     * Creates a virtual player with a randomly chosen strategy.
     * 
     * @param name The player's name
     */
    public VirtualPlayer(String name) {
        super(name, createRandomStrategy());
    }

    /**
     * Creates a virtual player with a specific strategy.
     * 
     * @param name The player's name
     * @param strategy The strategy to use
     */
    public VirtualPlayer(String name, PlayStrategy strategy) {
        super(name, strategy);
    }

    /**
     * Creates a random AI strategy (Offensive or Defensive).
     * 
     * @return A randomly selected strategy
     */
    private static PlayStrategy createRandomStrategy() {
        if (random.nextBoolean()) {
            return new OffensiveStrategy();
        } else {
            return new DefensiveStrategy();
        }
    }

    /**
     * Creates an offer using the AI strategy.
     * 
     * @return The offer created
     */
    @Override
    public Offer makeOffer() {
        return strategy.chooseOffer(this);
    }

    /**
     * Evaluates an offer using the AI strategy.
     * 
     * @param offer The offer to evaluate
     */
    public void evaluateOffer(Offer offer) {
        if (strategy instanceof AIStrategy) {
            ((AIStrategy) strategy).evaluateOffer(offer);
        }
    }
}
