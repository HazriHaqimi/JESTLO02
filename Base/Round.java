package base;

public class Round {
    private Offer offer;
    
    public Round() {
        this.offer = new Offer();
    }
    
    public void dealCard() {
        // Deal a card to each player
    }
    
    public void makeOffer() {
        // Create and manage an offer
    }
    
    public void takeOffer() {
        // Accept the current offer
    }
    
    public void collectLeftoverCard() {
        // Collect cards that were not taken
    }
    
    public void prepareNextRound() {
        // Reset state for the next round
    }
    
    public Offer getOffer() {
        return offer;
    }
    
    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}