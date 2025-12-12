public class Player {
   
    private String name;
    private Hand hand;
    private Jest jest;
    private Strategy strategy;

    public Player(String name, Strategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.hand = new Hand();
    }

    public void makeOffer() {
        // Make an offer based on strategy
    }

    public void takeOffer(Object offer) {
        // Handle received offer
    }
    
     public void calculateFinalScore(ScoreVisitor visitor, int score) {
        visitor.visit(this, score);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public Hand getHand() {
        return hand;
    }

    public Jest getJest() {
        return jest;
    }

    public void setJest(Jest jest) {
        this.jest = jest;
    }

    public Strategy getStrategy() {
        return strategy;
    }
}