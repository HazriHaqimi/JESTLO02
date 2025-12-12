package strategy;

import base.Offer;
import base.Player;
import base.InputHandler;
import java.util.List;

/**
 * Strategy for human players using console input.
 * Prompts the user for decisions and reads their choices.
 * Uses shared InputHandler to avoid Scanner conflicts.
 * 
 * @author JEST Team
 * @version 1.0
 */
public class HumanStrategy implements PlayStrategy {

    /**
     * Creates a new human strategy.
     */
    public HumanStrategy() {
    }

    /**
     * Creates an offer by asking the user which card to place face-down.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    @Override
    public Offer chooseOffer(Player player) {
        System.out.println("\nYour hand:");
        for (int i = 0; i < player.getHand().size(); i++) {
            System.out.println("  " + i + ": " + player.getHand().get(i));
        }
        System.out.print("Choose which card to place FACE DOWN (0 or 1): ");
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
            System.out.println("  " + i + ": " + o.getOwner().getName() + "'s offer - Face-up: " + o.getFaceUp());
        }
        System.out.print("Select an offer (0 to " + (offers.size() - 1) + "): ");
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
        System.out.println("\nOffer from " + offer.getOwner().getName() + ":");
        System.out.println("  Face-up: " + offer.getFaceUp());
        System.out.println("  Face-down: [hidden]");
        System.out.print("Take face-up card? (1 = yes, 0 = no/take face-down): ");
        return getInput() == 1;
    }

    /**
     * Gets integer input from the user using shared InputHandler.
     * 
     * @return The input value
     */
    public int getInput() {
        return InputHandler.getInt();
    }
}
