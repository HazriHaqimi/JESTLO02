package strategy;

import base.Offer;
import base.Player;
import java.util.List;
import java.util.Random;

/**
 * Abstract base class for AI player strategies.
 * Provides common functionality for automated decision-making.
 * 
 * @author Hazri and Sophea
 * @version 1.0
 */
public abstract class AIStrategy implements PlayStrategy {
    /** Random number generator for decisions */
    protected Random random;

    /**
     * Creates an AI strategy with a random generator.
     */
    public AIStrategy() {
        this.random = new Random();
    }

    /**
     * Creates an offer from the player's hand.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    @Override
    public abstract Offer chooseOffer(Player player);

    /**
     * Selects an offer to take from.
     * 
     * @param offers List of available offers
     * @return The selected offer
     */
    @Override
    public abstract Offer selectOffer(List<Offer> offers);

    /**
     * Chooses which card to take from an offer.
     * 
     * @param offer The offer to take from
     * @return true for face-up, false for face-down
     */
    @Override
    public abstract boolean chooseCard(Offer offer);

    /**
     * Evaluates an offer for strategic value.
     * 
     * @param offer The offer to evaluate
     */
    public abstract void evaluateOffer(Offer offer);
}
