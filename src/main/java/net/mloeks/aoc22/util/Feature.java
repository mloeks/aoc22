package net.mloeks.aoc22.util;

import java.util.Map;

public record Feature(Coordinate coordinate, Map<String, Object> attributes) {

    public Object getAttributeValue(final String attribute) {
        return attributes.get(attribute);
    }

    public <T> void setAttribute(final String key, T value) {
        attributes.put(key, value);
    }

    public String toString() {
        return coordinate + " - " + attributes;
    }
}
