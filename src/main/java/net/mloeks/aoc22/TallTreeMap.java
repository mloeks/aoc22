package net.mloeks.aoc22;

import net.mloeks.aoc22.util.Coordinate;
import net.mloeks.aoc22.util.PuzzleInputReader;

import java.io.StringWriter;
import java.util.List;

public class TallTreeMap {

    private final int[][] map;

    public TallTreeMap(final String mapInput) {
        List<String> treeLines = PuzzleInputReader.readAll(mapInput);
        map = new int[treeLines.get(0).length()][treeLines.size()];

        for (int y=0; y<treeLines.size(); y++) {
            String treeLine = treeLines.get(y);
            for (int x=0; x<treeLine.length(); x++) {
                map[x][y] = Integer.parseInt(String.valueOf(treeLine.charAt(x)));
            }
        }
    }

    public long countVisibleTrees() {
        long visible = 0;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                if (isTreeVisible(map, new Coordinate(x,y))) visible++;
            }
        }

        return visible;
    }

    public long mostScenicTreeScore() {
        long highestScenicScore = Long.MIN_VALUE;

        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[x].length; y++) {
                long score = calculateTreeScore(map, new Coordinate(x,y));
                if (score > highestScenicScore) highestScenicScore = score;
            }
        }

        return highestScenicScore;
    }

    private boolean isTreeVisible(int[][] map, Coordinate tree) {
        int treeHeight = map[tree.x()][tree.y()];
        for (ManhattanDirection direction : ManhattanDirection.values()) {
            int otherTreeHeight;
            Coordinate treeToCheck = tree;
            do {
                treeToCheck = direction.move(treeToCheck);
                if (outOfMapBounds(treeToCheck)) {
                    return true;
                }
                otherTreeHeight = map[treeToCheck.x()][treeToCheck.y()];
            } while (otherTreeHeight < treeHeight);
        }

        return false;
    }

    private long calculateTreeScore(int[][] map, Coordinate tree) {
        int treeHeight = map[tree.x()][tree.y()];
        long score = 1;
        for (ManhattanDirection direction : ManhattanDirection.values()) {
            int otherTreeHeight;
            long viewDistance = 0;
            Coordinate treeToCheck = tree;
            do {
                treeToCheck = direction.move(treeToCheck);
                if (outOfMapBounds(treeToCheck)) {
                    break;
                }
                otherTreeHeight = map[treeToCheck.x()][treeToCheck.y()];
                viewDistance++;
            } while (otherTreeHeight < treeHeight);
            score *= viewDistance;
        }

        return score;
    }

    private boolean outOfMapBounds(Coordinate tree) {
        return tree.x() < 0 || tree.x() >= map.length
                || tree.y() < 0 || tree.y() >= map[tree.x()].length;
    }

    public String toString() {
        StringWriter writer = new StringWriter();
        for (int y=0; y<map.length; y++) {
            for (int x=0; x<map[y].length; x++) {
                writer.append(String.valueOf(map[x][y]));
            }
            writer.append("\n");
        }
        return writer.toString();
    }
}
