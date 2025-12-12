package visitor;

import card.SuitCard;
import card.JokerCard;

public interface ScoreVisitor {
    void visit(SuitCard suitCard);
    void visit(JokerCard jokerCard);
    int getTotalScore();
}
