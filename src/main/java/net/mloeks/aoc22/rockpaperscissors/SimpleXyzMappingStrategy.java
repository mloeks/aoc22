package net.mloeks.aoc22.rockpaperscissors;

import static net.mloeks.aoc22.rockpaperscissors.Shape.PAPER;
import static net.mloeks.aoc22.rockpaperscissors.Shape.ROCK;
import static net.mloeks.aoc22.rockpaperscissors.Shape.SCISSORS;

public class SimpleXyzMappingStrategy implements Strategy {
    @Override
    public Shape chooseShapeToPlay(String strategyCode, Shape opponentsShape) {
        return switch (strategyCode) {
            case "X" -> ROCK;
            case "Y" -> PAPER;
            case "Z" -> SCISSORS;
            default -> throw new IllegalArgumentException("Invalid code: " + strategyCode);
        };
    }
}
