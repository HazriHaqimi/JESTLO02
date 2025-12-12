package visitor;

public class TrophyScoreVisitor {
    private int trophy;

    public TrophyScoreVisitor() {
        this.trophy = 0;
    }

    public void visit(SuitCard suitCard) {
        // Implementation for visiting a SuitCard
    }

    public void applyTrophy(Player player) {
        // Implementation for applying trophy to a player
    }

    public int calculateTrophyBonus(int trophy) {
        // Implementation for calculating trophy bonus
        return trophy * 10; // Example calculation
}