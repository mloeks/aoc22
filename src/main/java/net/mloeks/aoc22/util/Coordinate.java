package net.mloeks.aoc22.util;

public record Coordinate(int x, int y) {

    public static Coordinate fromXyString(final String commaSeparatedString)
            throws IndexOutOfBoundsException, NumberFormatException {
        String[] splitted = commaSeparatedString.split(",");
        return new Coordinate(Integer.parseInt(splitted[0].trim()), Integer.parseInt(splitted[1].trim()));
    }

    public Coordinate delta(Coordinate other) {
        return new Coordinate(other.x - x, other.y - y);
    }

    public Coordinate move(int dx, int dy) {
        return new Coordinate(x + dx, y + dy);
    }

    public Coordinate normalize() {
        return new Coordinate(x != 0 ? x / Math.abs(x) : x, y != 0 ? y / Math.abs(y) : y);
    }

    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
