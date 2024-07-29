package com.github.romanqed.nem;

public enum IC2Tier {
    LOW(1, 0, 32),
    MEDIUM(2, 33, 128),
    HIGH(3, 129, 512),
    EXTREME(4, 513, 2048),
    INSANE(5, 2049, 8192);

    private final int value;
    private final int min;
    private final int max;

    IC2Tier(int value, int min, int max) {
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public int getValue() {
        return value;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
