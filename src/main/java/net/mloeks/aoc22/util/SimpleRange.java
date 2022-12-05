package net.mloeks.aoc22.util;

/**
 * Simple, INCLUSIVE range implementation.
 */
public class SimpleRange {

    private final int min;
    private final int max;

    private SimpleRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public static SimpleRange of(int min, int max) {
        return new SimpleRange(min, max);
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public boolean contains(final SimpleRange other) {
        return this.min <= other.getMin() && this.max >= other.getMax();
    }

    public boolean overlaps(final SimpleRange other) {
        return this.min <= other.getMax() && this.max >= other.getMin()
                || this.min <= other.getMax() && this.max >= other.getMax();
    }
}
