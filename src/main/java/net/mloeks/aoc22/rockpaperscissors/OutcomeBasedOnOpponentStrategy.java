package net.mloeks.aoc22.rockpaperscissors;

import static net.mloeks.aoc22.rockpaperscissors.Shape.PAPER;
import static net.mloeks.aoc22.rockpaperscissors.Shape.ROCK;
import static net.mloeks.aoc22.rockpaperscissors.Shape.SCISSORS;

public class OutcomeBasedOnOpponentStrategy implements Strategy {
    @Override
    public Shape chooseShapeToPlay(String strategyCode, Shape opponentsShape) {
        if ("X".equals(strategyCode)) {
            // choose shape which makes me lose
            return switch(opponentsShape) {
                case ROCK -> SCISSORS;
                case PAPER -> ROCK;
                case SCISSORS -> PAPER;
            };
        }
        if ("Y".equals(strategyCode)) {
            // choose shape which yields a draw
            return opponentsShape;
        }
        if ("Z".equals(strategyCode)) {
            // choose shape which makes me win
            return switch(opponentsShape) {
                case ROCK -> PAPER;
                case PAPER -> SCISSORS;
                case SCISSORS -> ROCK;
            };

        }
        throw new IllegalArgumentException("Invalid code: " + strategyCode);
    }
}
