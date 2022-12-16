package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.Feature;
import net.mloeks.aoc22.util.PuzzleInputReader;
import net.mloeks.aoc22.util.SimpleMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CaveProfile {
    private final SimpleMap profile;

    public CaveProfile(final String inputRocksDescription, final boolean includeFloor) {
        profile = new SimpleMap();

        try (PuzzleInputReader reader = new PuzzleInputReader(inputRocksDescription)) {
            reader.stream()
                    .map(this::parseRockCoordinates)
                    .forEach(this::drawRock);
        }

        if (includeFloor) {
            Coordinate floorLeft = new Coordinate(profile.getBounds().getMinx() - 200, profile.getBounds().getMaxy() + 2);
            Coordinate floorRight = new Coordinate(profile.getBounds().getMaxx() + 200, profile.getBounds().getMaxy() + 2);
            drawRock(List.of(floorLeft, floorRight));
        }
    }

    public void simulateDrippingSandFrom(final Coordinate start, final Supplier<Boolean> stopDrippingOn) {
        profile.add(new Feature(start, Map.of("material", Material.LEAK)));
        Optional<Coordinate> finalSandUnitPos = pour(start);
        while (finalSandUnitPos.isPresent()) {
            profile.add(new Feature(finalSandUnitPos.get(), Map.of("material", Material.SAND)));
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
                Feature feature = new Feature(new Coordinate(
                        from.x() + x*normalized.x(),
                        from.y() + y*normalized.y()
                ), Map.of("material", Material.ROCK));
                profile.add(feature);
            }
        }
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
        return drip(sandUnit, 0L, 1L)
                .or(() -> drip(sandUnit, -1L, 1L))
                .or(() -> drip(sandUnit, 1L, 1L));
    }

    private Optional<Coordinate> drip(Coordinate sandUnit, long dx, long dy) {
        Coordinate target = sandUnit.move(dx,dy);
        Optional<Feature> solidFeature = profile.getAt(target)
                .filter(f -> List.of(Material.SAND, Material.ROCK)
                        .contains((Material) f.getAttributeValue("material")));
        return solidFeature.isPresent() ? Optional.empty() : Optional.of(target);
    }

    private boolean isOutOfBounds(final Coordinate coordinate) {
        return !profile.getBounds().contain(coordinate);
    }

    public List<Coordinate> getSandUnits() {
        return profile.getFeatures(f -> f.getAttributeValue("material") == Material.SAND).stream()
                .map(Feature::coordinate).toList();
    }

    public String toString() {
        return profile.print("material", ".");
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
