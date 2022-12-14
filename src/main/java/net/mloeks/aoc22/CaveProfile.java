package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.data.util.Pair;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CaveProfile {
    private final Map<Coordinate, Material> profile;
    private Pair<Coordinate, Coordinate> profileBounds;

    public CaveProfile(final String inputRocksDescription, final boolean includeFloor) {
        profile = new HashMap<>();
        profileBounds = Pair.of(
                new Coordinate(Integer.MAX_VALUE, Integer.MAX_VALUE),
                new Coordinate(Integer.MIN_VALUE, Integer.MIN_VALUE)
        );

        try (PuzzleInputReader reader = new PuzzleInputReader(inputRocksDescription)) {
            reader.stream()
                    .map(this::parseRockCoordinates)
                    .forEach(this::drawRock);
        }

        if (includeFloor) {
            Coordinate floorLeft = new Coordinate(profileBounds.getFirst().x() - 200, profileBounds.getSecond().y() + 2);
            Coordinate floorRight = new Coordinate(profileBounds.getSecond().x() + 200, profileBounds.getSecond().y() + 2);
            drawRock(List.of(floorLeft, floorRight));
        }
    }

    public void simulateDrippingSandFrom(final Coordinate start, final Supplier<Boolean> stopDrippingOn) {
        expandBoundsToInclude(start);
        Optional<Coordinate> finalSandUnitPos = pour(start);
        while (finalSandUnitPos.isPresent()) {
            profile.put(finalSandUnitPos.get(), Material.SAND);
            if (stopDrippingOn.get()) break;
            finalSandUnitPos = pour(start);
        }
        System.out.println(this);
    }

    private List<Coordinate> parseRockCoordinates(final String line) {
        return Stream.of(line.split("->"))
                .map(Coordinate::fromXyString)
                .toList();
    }

    private void expandBoundsToInclude(final Coordinate coordinate) {
        int minx = Math.min(coordinate.x(), profileBounds.getFirst().x());
        int miny = Math.min(coordinate.y(), profileBounds.getFirst().y());
        int maxx = Math.max(coordinate.x(), profileBounds.getSecond().x());
        int maxy = Math.max(coordinate.y(), profileBounds.getSecond().y());
        profileBounds = Pair.of(new Coordinate(minx, miny), new Coordinate(maxx, maxy));
    }

    private void drawRock(final List<Coordinate> rockCoordinates) {
        for (int i=1; i<rockCoordinates.size(); i++) {
            fillRockLine(rockCoordinates.get(i-1), rockCoordinates.get(i));
        }
    }

    private void fillRockLine(final Coordinate from, final Coordinate to) {
        Coordinate dxdy = from.delta(to);
        Coordinate normalized = dxdy.normalize();
        for (int x=0; x<=Math.abs(dxdy.x()); x++) {
            for (int y=0; y<=Math.abs(dxdy.y()); y++) {
                profile.put(new Coordinate(
                        from.x() + x*normalized.x(),
                        from.y() + y*normalized.y()
                ), Material.ROCK);
            }
        }

        expandBoundsToInclude(from);
        expandBoundsToInclude(to);
    }

    private Optional<Coordinate> pour(Coordinate sandUnit) {
        while (true) {
            Optional<Coordinate> next = drip(sandUnit);
            if (next.isPresent()) {
                if (isOutOfBounds(next.get())) return Optional.empty();
                sandUnit = next.get();
            } else return Optional.of(sandUnit);
        }
    }

    private Optional<Coordinate> drip(Coordinate sandUnit) {
        return drip(sandUnit, 0, 1)
                .or(() -> drip(sandUnit, -1, 1))
                .or(() -> drip(sandUnit, 1, 1));
    }

    private Optional<Coordinate> drip(Coordinate sandUnit, int dx, int dy) {
        return profile.getOrDefault(sandUnit.move(dx, dy), Material.AIR) == Material.AIR
            ? Optional.of(sandUnit.move(dx, dy)) : Optional.empty();
    }

    private boolean isOutOfBounds(final Coordinate coordinate) {
        return coordinate.x() < profileBounds.getFirst().x()
                || coordinate.x() > profileBounds.getSecond().x()
                || coordinate.y() < profileBounds.getFirst().y()
                || coordinate.y() > profileBounds.getSecond().y();
    }

    public List<Coordinate> getSandUnits() {
        return profile.entrySet().stream()
                .filter(entry -> entry.getValue() == Material.SAND)
                .map(Map.Entry::getKey).toList();
    }

    public String toString() {
        StringWriter writer = new StringWriter();
        writer.append(String.valueOf(profileBounds.getFirst())).append(" .......\n");
        for (int y = profileBounds.getFirst().y(); y <= profileBounds.getSecond().y(); y++) {
            for (int x = profileBounds.getFirst().x(); x <= profileBounds.getSecond().x(); x++) {
                writer.append(profile.getOrDefault(new Coordinate(x, y), Material.AIR).getSymbol());
            }
            writer.append("\n");
        }
        writer.append("....... ").append(String.valueOf(profileBounds.getSecond()));
        return writer.toString();
    }

    private enum Material {
        AIR('.'),
        ROCK('#'),
        SAND('o'),
        LEAK('+');

        private final Character symbol;

        Material(final Character symbol) {
            this.symbol = symbol;
        }

        public char getSymbol() {
            return symbol;
        }

    }


}
