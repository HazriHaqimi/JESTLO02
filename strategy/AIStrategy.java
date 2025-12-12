package strategy;

import base.Offer;
import base.Player;
import java.util.List;
import java.util.Random;

public abstract class AIStrategy implements PlayStrategy {
    protected Random random;

    public AIStrategy() {
        this.random = new Random();
    }

    @Override
    public abstract Offer chooseOffer(Player player);

    @Override
    public abstract Offer selectOffer(List<Offer> offers);

    @Override
    public abstract boolean chooseCard(Offer offer);

    public abstract void evaluateOffer(Offer offer);
}
