package trophy;

import base.Player;
import base.Card;
import base.Game.TrophyType;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import visitor.FinalScoreVisitor;
import java.util.List;

public class Trophy {
    private String condition;
    private Player winner;
    private TrophyType type;

    public Trophy(String condition, TrophyType type) {
        this.condition = condition;
        this.type = type;
    }

    public Player determineWinner(List<Player> players) {
        switch (type) {
            case HIGHEST_SUIT:
                return findHighestSuitPlayer(players, Suit.SPADE);
            case LOWEST_SUIT:
                return findLowestSuitPlayer(players, Suit.CLUB);
            case JOKER:
                return findPlayerWithJoker(players);
            case BEST_JEST:
                return findBestJestPlayer(players);
            case BEST_JEST_NO_JOKE:
                return findBestJestNoJokerPlayer(players);
            case MAJORITY:
                return findMajorityPlayer(players);
            default:
                return null;
        }
    }

    private Player findHighestSuitPlayer(List<Player> players, Suit suit) {
        Player winner = null;
        int highestValue = Integer.MIN_VALUE;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit && suitCard.getValue() > highestValue) {
                        highestValue = suitCard.getValue();
                        winner = player;
                    }
                }
            }
        }
        return winner;
    }

    private Player findLowestSuitPlayer(List<Player> players, Suit suit) {
        Player winner = null;
        int lowestValue = Integer.MAX_VALUE;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit && suitCard.getValue() < lowestValue) {
                        lowestValue = suitCard.getValue();
                        winner = player;
                    }
                }
            }
        }
        return winner;
    }

    private Player findPlayerWithJoker(List<Player> players) {
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof JokerCard) {
                    return player;
                }
            }
        }
        return null;
    }

    private Player findBestJestPlayer(List<Player> players) {
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor();
            int score = player.calculateFinalScore(visitor);
            if (score > highestScore) {
                highestScore = score;
                winner = player;
            }
        }
        return winner;
    }

    private Player findBestJestNoJokerPlayer(List<Player> players) {
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            boolean hasJoker = false;
            for (Card card : player.getJest().getCards()) {
                if (card instanceof JokerCard) {
                    hasJoker = true;
                    break;
                }
            }
            if (!hasJoker) {
                FinalScoreVisitor visitor = new FinalScoreVisitor();
                int score = player.calculateFinalScore(visitor);
                if (score > highestScore) {
                    highestScore = score;
                    winner = player;
                }
            }
        }
        return winner;
    }

    private Player findMajorityPlayer(List<Player> players) {
        return null;
    }

    public String getCondition() {
        return condition;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player player) {
        this.winner = player;
    }

    public TrophyType getType() {
        return type;
    }
}
