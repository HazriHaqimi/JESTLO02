package base;

public class Offer {
    private Card faceUp;
    private Card faceDown;
    private Player owner;

    public Offer() {
    }

    public Offer(Card faceUp, Card faceDown, Player owner) {
        this.faceUp = faceUp;
        this.faceDown = faceDown;
        this.owner = owner;
    }

    public boolean isComplete() {
        return faceUp != null && faceDown != null;
    }

    public Card selectCard(boolean isFaceUp) {
        Card selected;
        if (isFaceUp) {
            selected = faceUp;
            faceUp = null;
        } else {
            selected = faceDown;
            faceDown = null;
        }
        return selected;
    }

    public Card getRemainingCard() {
        if (faceUp != null) return faceUp;
        if (faceDown != null) return faceDown;
        return null;
    }

    public boolean hasCards() {
        return faceUp != null || faceDown != null;
    }

    public Card getFaceUp() {
        return faceUp;
    }

    public void setFaceUp(Card faceUp) {
        this.faceUp = faceUp;
    }

    public Card getFaceDown() {
        return faceDown;
    }

    public void setFaceDown(Card faceDown) {
        this.faceDown = faceDown;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
