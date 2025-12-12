package player;

import base.Offer;
import base.Player;
import strategy.PlayStrategy;
import strategy.AIStrategy;
import strategy.typestrategy.DefensiveStrategy;
import strategy.typestrategy.OffensiveStrategy;
import java.util.Random;

public class VirtualPlayer extends Player {
    private static Random random = new Random();

    public VirtualPlayer(String name) {
        super(name, createRandomStrategy());
    }

    public VirtualPlayer(String name, PlayStrategy strategy) {
        super(name, strategy);
    }

    private static PlayStrategy createRandomStrategy() {
        if (random.nextBoolean()) {
            return new OffensiveStrategy();
        } else {
            return new DefensiveStrategy();
        }
    }

    @Override
    public Offer makeOffer() {
        return strategy.chooseOffer(this);
    }

    public void evaluateOffer(Offer offer) {
        if (strategy instanceof AIStrategy) {
            ((AIStrategy) strategy).evaluateOffer(offer);
        }
    }
}
