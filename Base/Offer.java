public class Offer {
    private Card faceUp;
    private Card faceDown;
    private Owner owner;
    
    public Offer(Card faceUp, Card faceDown, Owner owner) {
        this.faceUp = faceUp;
        this.faceDown = faceDown;
        this.owner = owner;
    }
    
    public boolean isComplete() {
        return faceUp != null && faceDown != null && owner != null;
    }
    
    public Card selectCard(boolean isFaceUp) {
        return isFaceUp ? faceUp : faceDown;
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
    
    public Owner getOwner() {
        return owner;
    }
    
    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}