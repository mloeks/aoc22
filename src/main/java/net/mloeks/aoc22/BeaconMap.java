package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.Feature;
import net.mloeks.aoc22.util.PuzzleInputReader;
import net.mloeks.aoc22.util.SimpleMap;
import org.springframework.data.util.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class BeaconMap {

    private final SimpleMap map;
    private final List<Pair<Feature, Feature>> sensorsWithBeacons;

    public BeaconMap(final String sensorInput) {
        map = new SimpleMap();
        sensorsWithBeacons = new LinkedList<>();

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

    public SimpleMap getMap() {
        return map;
    }

    private void parseSensorAndBeacon(final String line) {
        Pattern p = Pattern.compile("(x=[\\-\\d]+, y=[\\-\\d]+)");
        Matcher m = p.matcher(line);
        Feature sensor;
        Feature beacon;

        if (m.find()) {
            String sensorXy = m.group(1).replaceAll("[^,\\-\\d]", "");
            sensor = new Feature(Coordinate.fromXyString(sensorXy),
                    Map.of("value", "S"));
        } else throw new IllegalStateException();

        if (m.find()) {
            String beaconXy = m.group(1).replaceAll("[^,\\-\\d]", "");
            beacon = new Feature(Coordinate.fromXyString(beaconXy), Map.of("value", "B"));
        } else throw new IllegalStateException();

        map.add(sensor);
        map.add(beacon);
        sensorsWithBeacons.add(Pair.of(sensor, beacon));
    }

    private boolean inReachOfAnySensor(final Coordinate coordinate) {
        for (Pair<Feature, Feature> swb : sensorsWithBeacons) {
            long dist = map.manhattanDistance(coordinate, swb.getFirst().coordinate());
            if (dist <= map.manhattanDistance(swb.getFirst().coordinate(), swb.getSecond().coordinate())) return true;
        }
        return false;
    }
}
