package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.Feature;
import net.mloeks.aoc22.util.PuzzleInputReader;
import net.mloeks.aoc22.util.SimpleMap;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class TallTreeMap {

    private final SimpleMap map;

    public TallTreeMap(final String mapInput) {
        List<String> treeLines = PuzzleInputReader.readAll(mapInput);
        map = new SimpleMap();

        for (int y=0; y<treeLines.size(); y++) {
            String treeLine = treeLines.get(y);
            for (int x=0; x<treeLine.length(); x++) {
                map.add(new Feature(new Coordinate(x,y),
                        Map.of("height", Integer.parseInt(String.valueOf(treeLine.charAt(x))))));
            }
        }
    }

    public long countVisibleTrees() {
        return map.getFeatures(this::isTreeVisible).size();
    }

    public long mostScenicTreeScore() {
        AtomicLong highestScenicScore = new AtomicLong(Long.MIN_VALUE);

        map.getFeatures().stream()
                .map(this::calculateTreeScore)
                .forEach(score -> {
                    if (score > highestScenicScore.get()) highestScenicScore.set(score); });

        return highestScenicScore.get();
    }

    private boolean isTreeVisible(final Feature feature) {
        for (ManhattanDirection direction : ManhattanDirection.values()) {
            int otherTreeHeight;
            Coordinate treeToCheck = feature.coordinate();
            do {
                treeToCheck = direction.move(treeToCheck);
                if (!map.getBounds().contain(treeToCheck)) {
                    return true;
                }
                otherTreeHeight = map.getAt(new Coordinate(treeToCheck.x(), treeToCheck.y()))
                        .map(f -> (int) f.getAttributeValue("height"))
                        .orElseThrow();
            } while (otherTreeHeight < (int) feature.getAttributeValue("height"));
        }

        return false;
    }

    private long calculateTreeScore(final Feature feature) {
        long score = 1;
        for (ManhattanDirection direction : ManhattanDirection.values()) {
            int otherTreeHeight;
            long viewDistance = 0;
            Coordinate treeToCheck = feature.coordinate();
            do {
                treeToCheck = direction.move(treeToCheck);
                if (!map.getBounds().contain(treeToCheck)) {
                    break;
                }
                otherTreeHeight = map.getAt(new Coordinate(treeToCheck.x(), treeToCheck.y()))
                        .map(f -> (int) f.getAttributeValue("height"))
                        .orElseThrow();
                viewDistance++;
            } while (otherTreeHeight < (int) feature.getAttributeValue("height"));
            score *= viewDistance;
        }

        return score;
    }

    public String toString() {
        return map.print("height", "");
    }
}
