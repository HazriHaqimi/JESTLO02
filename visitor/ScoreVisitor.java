package visitor;

public interface ScoreVisitor {
    void visit(SuitCard suitCard);
    void visit(JokerCard jokerCard);
}