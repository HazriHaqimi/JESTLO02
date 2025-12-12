package strategy;

import base.Offer;
import base.Player;
import java.util.List;
import java.util.Scanner;

public class HumanStrategy implements PlayStrategy {
    private Scanner scanner;

    public HumanStrategy() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Offer chooseOffer(Player player) {
        System.out.println("Choose which card to place face down (0 or 1): ");
        int choice = getInput();
        
        if (player.getHand().size() >= 2) {
            Offer offer = new Offer();
            if (choice == 0) {
                offer.setFaceDown(player.getHand().get(0));
                offer.setFaceUp(player.getHand().get(1));
            } else {
                offer.setFaceDown(player.getHand().get(1));
                offer.setFaceUp(player.getHand().get(0));
            }
            offer.setOwner(player);
            return offer;
        }
        return null;
    }

    @Override
    public Offer selectOffer(List<Offer> offers) {
        System.out.println("Select an offer (0 to " + (offers.size() - 1) + "): ");
        int choice = getInput();
        if (choice >= 0 && choice < offers.size()) {
            return offers.get(choice);
        }
        return offers.get(0);
    }

    @Override
    public boolean chooseCard(Offer offer) {
        System.out.println("Take face up card? (1 = yes, 0 = no): ");
        return getInput() == 1;
    }

    public int getInput() {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        return 0;
    }
}
