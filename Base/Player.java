package base;

import strategy.PlayStrategy;
import visitor.ScoreVisitor;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected String name;
    protected List<Card> hand;
    protected Jest jest;
    protected PlayStrategy strategy;

    public Player(String name, PlayStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.hand = new ArrayList<>();
        this.jest = new Jest();
    }

    public abstract Offer makeOffer();

    public void takeOffer(Offer offer, boolean takeFaceUp) {
        Card card = offer.selectCard(takeFaceUp);
        if (card != null) {
            jest.addCard(card);
        }
    }

    public int calculateFinalScore(ScoreVisitor visitor) {
        jest.accept(visitor);
        return visitor.getTotalScore();
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void clearHand() {
        hand.clear();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public Jest getJest() {
        return jest;
    }

    public void setJest(Jest jest) {
        this.jest = jest;
    }

    public PlayStrategy getStrategy() {
        return strategy;
    }

    public void setStrategy(PlayStrategy strategy) {
        this.strategy = strategy;
    }
}
