package player;

import base.Offer;
import base.Player;
import strategy.PlayStrategy;
import strategy.HumanStrategy;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name, new HumanStrategy());
    }

    public HumanPlayer(String name, PlayStrategy strategy) {
        super(name, strategy);
    }

    @Override
    public Offer makeOffer() {
        return strategy.chooseOffer(this);
    }

    public int getInput() {
        if (strategy instanceof HumanStrategy) {
            return ((HumanStrategy) strategy).getInput();
        }
        return 0;
    }
}
