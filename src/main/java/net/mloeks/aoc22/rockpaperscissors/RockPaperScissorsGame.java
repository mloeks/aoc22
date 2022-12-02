package net.mloeks.aoc22.rockpaperscissors;

import org.springframework.data.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

import static net.mloeks.aoc22.rockpaperscissors.Shape.fromCode;

public class RockPaperScissorsGame {

    private final List<Pair<Shape, Shape>> strategyGuide;
    private int totalScore;

    public RockPaperScissorsGame(final String strategyResource, final Strategy strategy) throws IOException, URISyntaxException {
        Path path = Paths.get(getClass().getClassLoader().getResource(strategyResource).toURI());

        this.totalScore = 0;
        try (Stream<String> lines = Files.lines(path)) {
                this.strategyGuide = lines
                        .map(line -> {
                            String[] shapes = line.trim().split(" ");
                            Shape opponent = fromCode(shapes[0]);
                            Shape me = strategy.chooseShapeToPlay(shapes[1], opponent);
                            return Pair.of(opponent, me);
                        })
                        .toList();
        }
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void play() {
        this.strategyGuide.forEach(round -> {
            totalScore += round.getSecond().getPoints();

            int result = Shape.compare(round.getFirst(), round.getSecond());
            if (result < 0) totalScore += 6;
            if (result == 0) totalScore += 3;
        });
    }
}
