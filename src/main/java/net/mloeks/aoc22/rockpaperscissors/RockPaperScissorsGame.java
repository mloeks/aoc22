package net.mloeks.aoc22.rockpaperscissors;

import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.data.util.Pair;

import java.util.function.Function;

import static net.mloeks.aoc22.rockpaperscissors.Shape.fromCode;

public class RockPaperScissorsGame {

    private int totalScore;
    private final Strategy playingStrategy;

    public RockPaperScissorsGame(final String inputStrategyGuide, final Strategy playingStrategy) {
        this.totalScore = 0;
        this.playingStrategy = playingStrategy;

        try (PuzzleInputReader reader = new PuzzleInputReader(inputStrategyGuide)) {
            reader.stream(mapRounds())
                    .forEach(this::playRound);
        }
    }

    public int getTotalScore() {
        return totalScore;
    }

    private Function<String, Pair<Shape, Shape>> mapRounds() {
        return line -> {
            String[] shapes = line.split(" ");
            Shape opponent = fromCode(shapes[0]);
            Shape me = playingStrategy.chooseShapeToPlay(shapes[1], opponent);
            return Pair.of(opponent, me);
        };
    }

    private void playRound(final Pair<Shape, Shape> round) {
        totalScore += round.getSecond().getPoints();

        int result = Shape.compare(round.getFirst(), round.getSecond());
        if (result < 0) totalScore += 6;
        if (result == 0) totalScore += 3;
    }
}
