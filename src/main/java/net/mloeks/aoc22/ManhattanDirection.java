package net.mloeks.aoc22;

public enum ManhattanDirection {
    NORTH(0,-1),
    EAST(1,0),
    SOUTH(0,1),
    WEST(-1,0);

    private final int incX;
    private final int incY;

    ManhattanDirection(final int incX, final int incY) {
        this.incX = incX;
        this.incY = incY;
    }

    public static ManhattanDirection fromUrdl(String urdl) {
        // based on right-handed system (origin in top-left corner)
        return switch(urdl) {
            case "U" -> ManhattanDirection.SOUTH;
            case "R" -> ManhattanDirection.EAST;
            case "D" -> ManhattanDirection.NORTH;
            case "L" -> ManhattanDirection.WEST;
            default -> throw new IllegalArgumentException("Unknown move instruction: " + urdl);
        };
    }

    public Coordinate move(Coordinate position) {
        return new Coordinate(
                position.x() + this.incX,
                position.y() + this.incY
        );
    }
}
