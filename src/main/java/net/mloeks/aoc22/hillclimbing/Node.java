package net.mloeks.aoc22.hillclimbing;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Taken and adjusted from
 * <a href="https://www.baeldung.com/java-dijkstra">baeldung.com</a>
 */
public class Node {
    private final char height;
    private Integer distance;
    private List<Node> shortestPath;
    private final Map<Node, Integer> adjacentNodes;

    public Node(final char height) {
        this.height = height;
        this.distance = Integer.MAX_VALUE;
        this.shortestPath = new LinkedList<>();
        this.adjacentNodes = new HashMap<>();
    }

    public void addDestination(final Node destination, final int distance) {
        adjacentNodes.put(destination, distance);
    }

    public char getHeight() {
        return height;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }
}
