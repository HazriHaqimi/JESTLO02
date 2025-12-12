import java.util.Random;

package strategy.typestrategy;


public class DefensiveStrategy {
    private Random random;

    public DefensiveStrategy() {
        this.random = new Random();
    }

    public void chooseOffer(Player player) {
        // Implementation for choosing an offer based on the player's state
    }

    public void selectOffer(Offer offer) {
        // Implementation for selecting a specific offer
    }

    public void evaluateOffer(Offer offer) {
        // Implementation for evaluating the given offer
    }
}