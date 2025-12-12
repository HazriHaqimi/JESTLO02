package base;

import visitor.ScoreVisitor;

public abstract class Card {
    
    public abstract void accept(ScoreVisitor visitor);
    
    public abstract int getValue();
    
    @Override
    public abstract String toString();
}
