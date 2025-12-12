package player;

import base.Offer;
import base.Player;
import strategy.PlayStrategy;
import strategy.AIStrategy;
import strategy.typestrategy.DefensiveStrategy;

public class VirtualPlayer extends Player {

    public VirtualPlayer(String name) {
        super(name, new DefensiveStrategy());
    }

    public VirtualPlayer(String name, PlayStrategy strategy) {
        super(name, strategy);
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
