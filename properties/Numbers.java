package properties;

public enum Numbers {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int value;

    Numbers(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
