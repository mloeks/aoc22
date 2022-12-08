package net.mloeks.aoc22.treehouse;

public enum ManhattanViewDirection {
    NORTH(0,-1),
    EAST(1,0),
    SOUTH(0,1),
    WEST(-1,0);

    private final int incX;
    private final int incY;

    ManhattanViewDirection(final int incX, final int incY) {
        this.incX = incX;
        this.incY = incY;
    }

    public Coordinate look(Coordinate position) {
        return new Coordinate(
                position.x() + this.incX,
                position.y() + this.incY
        );
    }
}
