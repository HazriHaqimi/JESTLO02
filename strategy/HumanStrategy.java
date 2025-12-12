package strategy;

import base.Offer;
import base.Player;
import java.util.List;
import java.util.Scanner;

/**
 * Strategy for human players using console input.
 * Prompts the user for decisions and reads their choices.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class HumanStrategy implements PlayStrategy {
    /** Scanner for reading user input */
    private Scanner scanner;

    /**
     * Creates a new human strategy with a scanner.
     */
    public HumanStrategy() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Creates an offer by asking the user which card to place face-down.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    @Override
    public Offer chooseOffer(Player player) {
        System.out.println("\nYour hand: " + player.getHand());
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

    /**
     * Asks the user to select an offer from available options.
     * 
     * @param offers List of available offers
     * @return The selected offer
     */
    @Override
    public Offer selectOffer(List<Offer> offers) {
        System.out.println("\nAvailable offers:");
        for (int i = 0; i < offers.size(); i++) {
            Offer o = offers.get(i);
            System.out.println(i + ": " + o.getOwner().getName() + "'s offer - Face-up: " + o.getFaceUp());
        }
        System.out.println("Select an offer (0 to " + (offers.size() - 1) + "): ");
        int choice = getInput();
        if (choice >= 0 && choice < offers.size()) {
            return offers.get(choice);
        }
        return offers.get(0);
    }

    /**
     * Asks the user whether to take the face-up card.
     * 
     * @param offer The offer to take from
     * @return true if user wants face-up card
     */
    @Override
    public boolean chooseCard(Offer offer) {
        System.out.println("Take face up card (" + offer.getFaceUp() + ")? (1 = yes, 0 = no): ");
        return getInput() == 1;
    }

    /**
     * Gets integer input from the user.
     * 
     * @return The input value, or 0 if invalid
     */
    public int getInput() {
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        scanner.nextLine();
        return 0;
    }
}
