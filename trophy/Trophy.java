package trophy;

public class Trophy {
    private String condition;
    private Player winner;

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }
}