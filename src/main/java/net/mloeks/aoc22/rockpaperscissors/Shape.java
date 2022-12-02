package net.mloeks.aoc22.rockpaperscissors;

public enum Shape {

    ROCK(1),
    PAPER(2),
    SCISSORS(3);

    private final int points;

    Shape(int points) {
        this.points = points;
    }

    public static Shape fromCode(String code) {
        if (code.equals("A")) return ROCK;
        if (code.equals("B")) return PAPER;
        if (code.equals("C")) return SCISSORS;
        throw new IllegalArgumentException("Unknown code: " + code);
    }

    public int getPoints() {
        return points;
    }

    public static int compare(Shape shape, Shape other) {
        switch (shape) {
            case ROCK:
                if (other == ROCK) return 0;
                if (other == PAPER) return -1;
                if (other == SCISSORS) return 1;
            case PAPER:
                if (other == ROCK) return 1;
                if (other == PAPER) return 0;
                if (other == SCISSORS) return -1;
            case SCISSORS:
                if (other == ROCK) return -1;
                if (other == PAPER) return 1;
                if (other == SCISSORS) return 0;
            default:
                throw new IllegalArgumentException("Invalid arguments: " + shape + ", " + other);
        }
    }
}
