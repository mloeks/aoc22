package net.mloeks.aoc22;

public record Coordinate(int x, int y) {

    public Coordinate delta(Coordinate other) {
        return new Coordinate(other.x - x, other.y - y);
    }

    public Coordinate move(int dx, int dy) {
        return new Coordinate(x + dx, y + dy);
    }

    public boolean equals(Coordinate other) {
        return this.x == other.x && this.y == other.y;
    }

}
