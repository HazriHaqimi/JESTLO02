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
 * @author Hazri and Sophea
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
     * Validates that the choice is 0 or 1.
     * 
     * @param player The player making the offer
     * @return The created offer
     */
    @Override
    public Offer chooseOffer(Player player) {
        System.out.println("\n========== " + player.getName().toUpperCase() + "'S TURN ==========");
        System.out.println(player.getName() + "'s hand:");
        for (int i = 0; i < player.getHand().size(); i++) {
            System.out.println("  " + i + ": " + player.getHand().get(i));
        }
        
        // Get valid input (only 0 or 1)
        int choice = -1;
        while (choice != 0 && choice != 1) {
            System.out.print("Choose which card to place FACE DOWN (0 or 1): ");
            choice = getInput();
            
            if (choice != 0 && choice != 1) {
                System.out.println("Invalid choice! Please enter 0 or 1.");
            }
        }
        
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
            System.out.println(player.getName() + " placed " + offer.getFaceUp() + " face-up.");
            return offer;
        }
        return null;
    }

    /**
     * Asks the user to select an offer from available options.
     * Validates that the choice is within valid range.
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
        
        // Get valid input
        int choice = -1;
        int maxChoice = offers.size() - 1;
        while (choice < 0 || choice > maxChoice) {
            System.out.print("Select an offer (0 to " + maxChoice + "): ");
            choice = getInput();
            
            if (choice < 0 || choice > maxChoice) {
                System.out.println("Invalid choice! Please enter a number between 0 and " + maxChoice + ".");
            }
        }
        
        return offers.get(choice);
    }

    /**
     * Asks the user whether to take the face-up card.
     * Validates that the choice is 0 or 1.
     * 
     * @param offer The offer to take from
     * @return true if user wants face-up card
     */
    @Override
    public boolean chooseCard(Offer offer) {
        System.out.println("\nOffer from " + offer.getOwner().getName() + ":");
        System.out.println("  Face-up: " + offer.getFaceUp());
        System.out.println("  Face-down: [hidden]");
        
        // Get valid input (only 0 or 1)
        int choice = -1;
        while (choice != 0 && choice != 1) {
            System.out.print("Take face-up card? (1 = yes, 0 = no/take face-down): ");
            choice = getInput();
            
            if (choice != 0 && choice != 1) {
                System.out.println("Invalid choice! Please enter 0 or 1.");
            }
        }
        
        return choice == 1;
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
