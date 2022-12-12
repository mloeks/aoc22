package net.mloeks.aoc22.hillclimbing;

import net.mloeks.aoc22.Coordinate;
import net.mloeks.aoc22.ManhattanDirection;

import java.util.List;
import java.util.Optional;

import static net.mloeks.aoc22.util.PuzzleInputReader.*;

public class HeightMap {

    private final Node[][] map;
    private final Graph graph;
    private Node start;
    private Node destination;

    public HeightMap(final String inputMap) {
        List<String> lines = readAll(inputMap);
        map = new Node[lines.get(0).length()][lines.size()];
        graph = new Graph();

        for (int y = 0; y < lines.size(); y++) {
            char[] line = lines.get(y).trim().toCharArray();
            for (int x = 0; x < line.length; x++) {
                Node node = new Node(line[x]);

                if (node.getHeight() == 'S') {
                    node = new Node('a');
                    start = node;
                } else if (node.getHeight() == 'E') {
                    node = new Node('z');
                    destination = node;
                }

                map[x][y] = node;
            }
        }

        buildGraph();
    }

    public int findShortestPath(final Node start) {
        Graph.calculateShortestPathsFromSource(Optional.ofNullable(start).orElse(this.start));
        return destination.getDistance();
    }

    private void buildGraph() {
        for (int x = 0; x < map.length; x++) {
            Node[] line = map[x];
            for (int y = 0; y < line.length; y++) {
                Node currentNode = map[x][y];
                for (ManhattanDirection direction : ManhattanDirection.values()) {
                    try {
                        Coordinate adjacentCoordinate = direction.move(new Coordinate(x, y));
                        Node adjacentNode = map[adjacentCoordinate.x()][adjacentCoordinate.y()];
                        if (canClimbThere(currentNode, adjacentNode)) {
                            currentNode.addDestination(adjacentNode, 1);
                        }
                    } catch (IndexOutOfBoundsException ignore) {}
                }
                graph.addNode(currentNode);
            }
        }
    }

    private static boolean canClimbThere(Node me, Node other) {
        return other.getHeight() - me.getHeight() <= 1;
    }

    public Graph getGraph() {
        return graph;
    }
}
