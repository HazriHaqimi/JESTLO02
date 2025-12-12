package trophy;

import base.Player;
import base.Card;
import base.Game.TrophyType;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;
import visitor.FinalScoreVisitor;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

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
            case HIGHEST_SPADE:
                return findHighestSuitPlayer(players, Suit.SPADE);
            case LOWEST_SPADE:
                return findLowestSuitPlayer(players, Suit.SPADE);
            case HIGHEST_CLUB:
                return findHighestSuitPlayer(players, Suit.CLUB);
            case LOWEST_CLUB:
                return findLowestSuitPlayer(players, Suit.CLUB);
            case HIGHEST_DIAMOND:
                return findHighestSuitPlayer(players, Suit.DIAMOND);
            case LOWEST_DIAMOND:
                return findLowestSuitPlayer(players, Suit.DIAMOND);
            case HIGHEST_HEART:
                return findHighestSuitPlayer(players, Suit.HEART);
            case LOWEST_HEART:
                return findLowestSuitPlayer(players, Suit.HEART);
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
        Suit winningSuit = null;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit) {
                        int value = suitCard.getValue();
                        if (value > highestValue || 
                            (value == highestValue && compareSuitStrength(suitCard.getSuit(), winningSuit) > 0)) {
                            highestValue = value;
                            winningSuit = suitCard.getSuit();
                            winner = player;
                        }
                    }
                }
            }
        }
        return winner;
    }

    private Player findLowestSuitPlayer(List<Player> players, Suit suit) {
        Player winner = null;
        int lowestValue = Integer.MAX_VALUE;
        Suit winningSuit = null;
        
        for (Player player : players) {
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard suitCard = (SuitCard) card;
                    if (suitCard.getSuit() == suit) {
                        int value = suitCard.getValue();
                        if (value < lowestValue ||
                            (value == lowestValue && compareSuitStrength(suitCard.getSuit(), winningSuit) > 0)) {
                            lowestValue = value;
                            winningSuit = suitCard.getSuit();
                            winner = player;
                        }
                    }
                }
            }
        }
        return winner;
    }

    private int compareSuitStrength(Suit suit1, Suit suit2) {
        if (suit1 == suit2) return 0;
        if (suit2 == null) return 1;
        return getSuitStrength(suit1) - getSuitStrength(suit2);
    }

    private int getSuitStrength(Suit suit) {
        switch (suit) {
            case SPADE: return 4;
            case CLUB: return 3;
            case DIAMOND: return 2;
            case HEART: return 1;
            default: return 0;
        }
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
        int highestCardValue = Integer.MIN_VALUE;
        Suit strongestSuit = null;
        
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            
            if (score > highestScore) {
                highestScore = score;
                winner = player;
                int[] cardInfo = getHighestCardInfo(player);
                highestCardValue = cardInfo[0];
                strongestSuit = Suit.values()[cardInfo[1]];
            } else if (score == highestScore) {
                int[] cardInfo = getHighestCardInfo(player);
                if (cardInfo[0] > highestCardValue ||
                    (cardInfo[0] == highestCardValue && cardInfo[1] > getSuitStrength(strongestSuit))) {
                    highestCardValue = cardInfo[0];
                    strongestSuit = Suit.values()[cardInfo[1]];
                    winner = player;
                }
            }
        }
        return winner;
    }

    private int[] getHighestCardInfo(Player player) {
        int highestValue = 0;
        int strongestSuitIndex = 0;
        
        for (Card card : player.getJest().getCards()) {
            if (card instanceof SuitCard) {
                SuitCard sc = (SuitCard) card;
                int strength = getSuitStrength(sc.getSuit());
                if (sc.getValue() > highestValue ||
                    (sc.getValue() == highestValue && strength > strongestSuitIndex)) {
                    highestValue = sc.getValue();
                    strongestSuitIndex = strength;
                }
            }
        }
        return new int[]{highestValue, strongestSuitIndex};
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
                FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
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
        Player winner = null;
        int highestCount = 0;
        int highestFaceValue = 0;
        Suit strongestSuit = null;
        
        for (Player player : players) {
            Map<Integer, Integer> valueCounts = new HashMap<>();
            Map<Integer, Suit> valueSuits = new HashMap<>();
            
            for (Card card : player.getJest().getCards()) {
                if (card instanceof SuitCard) {
                    SuitCard sc = (SuitCard) card;
                    int value = sc.getValue();
                    valueCounts.put(value, valueCounts.getOrDefault(value, 0) + 1);
                    
                    Suit currentSuit = valueSuits.get(value);
                    if (currentSuit == null || getSuitStrength(sc.getSuit()) > getSuitStrength(currentSuit)) {
                        valueSuits.put(value, sc.getSuit());
                    }
                }
            }
            
            for (Map.Entry<Integer, Integer> entry : valueCounts.entrySet()) {
                int value = entry.getKey();
                int count = entry.getValue();
                Suit suit = valueSuits.get(value);
                
                if (count > highestCount ||
                    (count == highestCount && value > highestFaceValue) ||
                    (count == highestCount && value == highestFaceValue && 
                     getSuitStrength(suit) > getSuitStrength(strongestSuit))) {
                    highestCount = count;
                    highestFaceValue = value;
                    strongestSuit = suit;
                    winner = player;
                }
            }
        }
        return winner;
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
