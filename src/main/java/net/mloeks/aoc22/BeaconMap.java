package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.Feature;
import net.mloeks.aoc22.util.PuzzleInputReader;
import net.mloeks.aoc22.util.SimpleMap;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.LongStream;

public class BeaconMap {

    private final SimpleMap map;
    private final List<Feature> sensors;

    public BeaconMap(final String sensorInput) {
        map = new SimpleMap();
        sensors = new LinkedList<>();

        try (PuzzleInputReader reader = new PuzzleInputReader(sensorInput)) {
            reader.stream().forEach(this::parseSensorAndBeacon);
        }
    }

    public List<Coordinate> scanRowForPositionsInReach(long row) {
        return LongStream.rangeClosed(map.getBounds().getMinx(), map.getBounds().getMaxx())
                .mapToObj(l -> new Coordinate(l, row))
                .filter(this::inReachOfAnySensor)
                .filter(co -> map.getAt(co).isEmpty())
                .toList();
    }

    public Optional<Coordinate> findOnlyPossibleBeaconPositionWithinBounds(final SimpleMap.Bounds bounds) {
        for (long row = bounds.getMiny(); row <= bounds.getMaxy(); row++) {
            System.out.println("Checking row " + row);
            Optional<Coordinate> notReachedByAnySensor = getPositionOutOfReach(row, bounds);
            if (notReachedByAnySensor.isPresent()) return notReachedByAnySensor;
        }
        return Optional.empty();
    }

    public long getTuningFrequency(final Coordinate coordinate) {
        return coordinate.x() * 4_000_000 + coordinate.y();
    }

    public SimpleMap getMap() {
        return map;
    }

    private void parseSensorAndBeacon(final String line) {
        Pattern p = Pattern.compile("(x=[\\-\\d]+, y=[\\-\\d]+)");
        Matcher m = p.matcher(line);
        Coordinate sensorPos;
        Coordinate beaconPos;

        if (m.find()) {
            sensorPos = Coordinate.fromXyString(m.group(1).replaceAll("[^,\\-\\d]", ""));
        } else throw new IllegalStateException();

        if (m.find()) {
            beaconPos = Coordinate.fromXyString(m.group(1).replaceAll("[^,\\-\\d]", ""));
        } else throw new IllegalStateException();

        Feature beacon = new Feature(beaconPos, Map.of("value", "B"));
        Feature sensor = new Feature(sensorPos, Map.of("value", "S",
                "reach", map.manhattanDistance(sensorPos, beacon.coordinate())));

        map.add(sensor);
        map.add(beacon);
        sensors.add(sensor);
    }

    private Optional<Coordinate> getPositionOutOfReach(long row, SimpleMap.Bounds bounds) {
        return LongStream.rangeClosed(bounds.getMinx(), bounds.getMaxx())
                .mapToObj(l -> new Coordinate(l, row))
                .filter(c -> !inReachOfAnySensor(c))
                .filter(co -> map.getAt(co).isEmpty())
                .findAny();
    }

    private boolean inReachOfAnySensor(final Coordinate coordinate) {
        for (Feature sensor : sensors) {
            long dist = map.manhattanDistance(coordinate, sensor.coordinate());
            if (dist <= (long) sensor.getAttributeValue("reach")) return true;
        }
        return false;
    }
}
