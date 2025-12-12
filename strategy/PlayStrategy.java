package strategy;

import base.Offer;
import base.Player;
import java.util.List;

public interface PlayStrategy {
    Offer chooseOffer(Player player);
    Offer selectOffer(List<Offer> offers);
    boolean chooseCard(Offer offer);
}
