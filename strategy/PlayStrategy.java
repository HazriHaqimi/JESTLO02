public interface PlayStrategy {
    Offer chooseOffer(Player player);
    Offer selectOffer(List<Offer> offers);
}
