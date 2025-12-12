package base;

import trophy.Trophy;
import visitor.FinalScoreVisitor;
import player.HumanPlayer;
import player.VirtualPlayer;
import card.SuitCard;
import card.JokerCard;
import properties.Suit;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private Deck deck;
    private List<Trophy> trophies;
    private int numberOfPlayers;
    private Scanner scanner;

    public enum TrophyType {
        HIGHEST_SPADE,
        LOWEST_SPADE,
        HIGHEST_CLUB,
        LOWEST_CLUB,
        HIGHEST_DIAMOND,
        LOWEST_DIAMOND,
        HIGHEST_HEART,
        LOWEST_HEART,
        HIGHEST_SUIT,
        LOWEST_SUIT,
        JOKER,
        BEST_JEST,
        BEST_JEST_NO_JOKE,
        MAJORITY
    }

    public Game(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.players = new ArrayList<>();
        this.trophies = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    public void startGame() {
        System.out.println("=== JEST Card Game ===");
        System.out.println("Number of players: " + numberOfPlayers);
        
        initializePlayers();
        initializeDeck();
        initializeTrophies();
        
        System.out.println("\n=== Trophies for this game ===");
        for (Trophy trophy : trophies) {
            System.out.println("- " + trophy.getCondition());
        }
        
        while (!deck.isEmpty()) {
            startRound();
        }
        
        collectFinalCards();
        endGame();
    }

    private void initializePlayers() {
        System.out.println("\nEnter name for Human Player: ");
        String humanName = scanner.nextLine();
        if (humanName.isEmpty()) humanName = "Player 1";
        players.add(new HumanPlayer(humanName));
        
        for (int i = 2; i <= numberOfPlayers; i++) {
            players.add(new VirtualPlayer("AI Player " + i));
        }
        
        System.out.println("\nPlayers:");
        for (Player p : players) {
            System.out.println("- " + p.getName());
        }
    }

    private void initializeDeck() {
        deck = new Deck();
        deck.shuffle();
        System.out.println("\nDeck shuffled. " + deck.size() + " cards ready.");
    }

    private void initializeTrophies() {
        trophies.clear();
        
        Card trophy1Card = deck.drawCard();
        Card trophy2Card = numberOfPlayers == 3 ? deck.drawCard() : null;
        
        if (trophy1Card != null) {
            Trophy t1 = createTrophyFromCard(trophy1Card);
            if (t1 != null) trophies.add(t1);
        }
        
        if (trophy2Card != null) {
            Trophy t2 = createTrophyFromCard(trophy2Card);
            if (t2 != null) trophies.add(t2);
        }
        
        trophies.add(new Trophy("Best Jest - Player with highest value Jest", TrophyType.BEST_JEST));
        trophies.add(new Trophy("Best Jest, No Joke - Player with highest value Jest, but without the Joker", TrophyType.BEST_JEST_NO_JOKE));
        trophies.add(new Trophy("Joker - Player with the Joker", TrophyType.JOKER));
        trophies.add(new Trophy("Majority - Player with most cards of a face value", TrophyType.MAJORITY));
    }

    private Trophy createTrophyFromCard(Card card) {
        if (card instanceof SuitCard) {
            SuitCard sc = (SuitCard) card;
            Suit suit = sc.getSuit();
            int value = sc.getValue();
            
            if (value >= 3) {
                return new Trophy("Highest " + suit + " - Player with highest " + suit + " card", 
                    getTrophyTypeForHighest(suit));
            } else {
                return new Trophy("Lowest " + suit + " - Player with lowest " + suit + " card",
                    getTrophyTypeForLowest(suit));
            }
        } else if (card instanceof JokerCard) {
            return new Trophy("Joker Trophy", TrophyType.JOKER);
        }
        return null;
    }

    private TrophyType getTrophyTypeForHighest(Suit suit) {
        switch (suit) {
            case SPADE: return TrophyType.HIGHEST_SPADE;
            case CLUB: return TrophyType.HIGHEST_CLUB;
            case DIAMOND: return TrophyType.HIGHEST_DIAMOND;
            case HEART: return TrophyType.HIGHEST_HEART;
            default: return TrophyType.HIGHEST_SUIT;
        }
    }

    private TrophyType getTrophyTypeForLowest(Suit suit) {
        switch (suit) {
            case SPADE: return TrophyType.LOWEST_SPADE;
            case CLUB: return TrophyType.LOWEST_CLUB;
            case DIAMOND: return TrophyType.LOWEST_DIAMOND;
            case HEART: return TrophyType.LOWEST_HEART;
            default: return TrophyType.LOWEST_SUIT;
        }
    }

    public void startRound() {
        System.out.println("\n=== New Round ===");
        Round round = new Round(deck, players);
        
        round.dealCards();
        System.out.println("Cards dealt to all players.");
        
        for (Player player : players) {
            System.out.println(player.getName() + "'s hand: " + player.getHand());
        }
        
        round.makeOffers();
        System.out.println("\nOffers made by all players:");
        for (Offer offer : round.getOffers()) {
            System.out.println(offer.getOwner().getName() + ": Face-up = " + offer.getFaceUp() + ", Face-down = [hidden]");
        }
        
        round.takeOffers();
        
        round.collectLeftoverCards();
        
        round.prepareNextRound();
    }

    private void collectFinalCards() {
        System.out.println("\n=== Final Round ===");
        System.out.println("Each player adds their remaining offer card to their Jest.");
    }

    public void computeScore() {
        System.out.println("\n=== Computing Scores ===");
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            System.out.println(player.getName() + "'s Jest: " + player.getJest().getCards());
            System.out.println(player.getName() + "'s Score: " + score);
        }
    }

    public void awardTrophy() {
        System.out.println("\n=== Awarding Trophies ===");
        for (Trophy trophy : trophies) {
            Player winner = trophy.determineWinner(players);
            if (winner != null) {
                trophy.setWinner(winner);
                System.out.println(trophy.getCondition() + " -> " + winner.getName());
            } else {
                System.out.println(trophy.getCondition() + " -> No winner");
            }
        }
    }

    public void endGame() {
        computeScore();
        awardTrophy();
        
        System.out.println("\n=== Final Results ===");
        Player winner = null;
        int highestScore = Integer.MIN_VALUE;
        
        for (Player player : players) {
            FinalScoreVisitor visitor = new FinalScoreVisitor(player.getJest());
            int score = player.calculateFinalScore(visitor);
            
            int trophyBonus = countTrophiesWon(player);
            int finalScore = score + trophyBonus;
            
            System.out.println(player.getName() + ": Base Score = " + score + 
                ", Trophies Won = " + trophyBonus + ", Final Score = " + finalScore);
            
            if (finalScore > highestScore) {
                highestScore = finalScore;
                winner = player;
            }
        }
        
        if (winner != null) {
            System.out.println("\n*** " + winner.getName() + " WINS with " + highestScore + " points! ***");
        }
    }

    private int countTrophiesWon(Player player) {
        int count = 0;
        for (Trophy trophy : trophies) {
            if (trophy.getWinner() == player) {
                count++;
            }
        }
        return count;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public List<Trophy> getTrophies() {
        return trophies;
    }
}
