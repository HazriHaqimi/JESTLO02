package properties;

public enum Numbers {
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    ACE(14);

    private final int value;

    Numbers(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
