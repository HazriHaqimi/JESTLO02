package base;

import visitor.ScoreVisitor;
import java.util.ArrayList;
import java.util.List;

public class Jest {
    private List<Card> cards;

    public Jest() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void accept(ScoreVisitor visitor) {
        for (Card card : cards) {
            card.accept(visitor);
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }
}
