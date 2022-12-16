package net.mloeks.aoc22.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.StringWriter;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class SimpleMap {

    private final Map<Long, Map<Long, Feature>> map = new HashMap<>();
    private final Bounds bounds = new Bounds(MAX_VALUE, MIN_VALUE, MAX_VALUE, MIN_VALUE);

    public void add(final Feature feature) {
        Map<Long, Feature> row = map.computeIfAbsent(feature.coordinate().x(), key -> new HashMap<>());
        row.put(feature.coordinate().y(), feature);
        extendBoundsToInclude(feature.coordinate());
    }

    public List<Feature> getFeatures() {
        return getFeatures(f -> true);
    }

    public List<Feature> getFeatures(final Predicate<Feature> filter) {
        return map.values().stream()
                .flatMap(col -> col.values().stream())
                .filter(filter)
                .toList();
    }

    public Optional<Feature> getAt(final Coordinate position) {
        return Optional.ofNullable(map.get(position.x())).map(row -> row.get(position.y()));
    }

    public List<Feature> getAllInRow(final long row) {
        return map.keySet().stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .map(col -> col.get(row))
                .filter(Objects::nonNull)
                .toList();
    }

    public Bounds getBounds() {
        return bounds;
    }


    public void buffer(final Feature feature, int distance) {
        throw new NotYetImplementedException();
    }

    public String print(final String attribute, final String noDataValue) {
        StringWriter writer = new StringWriter();

        int xCaptionsPadding = Math.max(
                String.valueOf(bounds.getMaxx()).length(),
                String.valueOf(bounds.getMinx()).length());
        int yCaptionsPadding = Math.max(
                String.valueOf(bounds.getMaxy()).length(),
                String.valueOf(bounds.getMiny()).length()) + 1;
        int xCaptionsDistance = 5;

        for (int i = 0; i < xCaptionsPadding; i++) {
            IntStream.range(0, yCaptionsPadding).forEach(s -> writer.append(" "));
            for (long x = bounds.getMinx(); x <= bounds.getMaxx(); x++) {
                if (x % xCaptionsDistance == 0) {
                    writer.append(StringUtils.leftPad(String.valueOf(x), xCaptionsPadding, " ").charAt(i));
                } else writer.append(" ");
            }
            writer.append("\n");
        }

        for (long y = bounds.getMiny(); y <= bounds.getMaxy(); y++) {
            final long row = y;
            writer.append(StringUtils.leftPad(String.valueOf(row), yCaptionsPadding - 1, ' '));
            writer.append(" ");
            for (long x = bounds.getMinx(); x <= bounds.getMaxx(); x++) {
                writer.append(Optional.ofNullable(map.get(x))
                        .map(m -> m.get(row))
                        .map(f -> f.getAttributeValue(attribute).toString())
                        .orElse(noDataValue));
            }
            writer.append("\n");
        }
        return writer.toString();
    }

    private void extendBoundsToInclude(final Coordinate coordinate) {
        if (coordinate.x() < bounds.getMinx()) bounds.setMinx(coordinate.x());
        if (coordinate.x() > bounds.getMaxx()) bounds.setMaxx(coordinate.x());
        if (coordinate.y() < bounds.getMiny()) bounds.setMiny(coordinate.y());
        if (coordinate.y() > bounds.getMaxy()) bounds.setMaxy(coordinate.y());
    }

    public static class Bounds {
        private long minx;
        private long maxx;
        private long miny;
        private long maxy;

        public Bounds(long minx, long maxx, long miny, long maxy) {
            this.minx = minx;
            this.maxx = maxx;
            this.miny = miny;
            this.maxy = maxy;
        }

        public long getMinx() {
            return minx;
        }

        public Bounds setMinx(long minx) {
            this.minx = minx;
            return this;
        }

        public long getMaxx() {
            return maxx;
        }

        public Bounds setMaxx(long maxx) {
            this.maxx = maxx;
            return this;
        }

        public long getMiny() {
            return miny;
        }

        public Bounds setMiny(long miny) {
            this.miny = miny;
            return this;
        }

        public long getMaxy() {
            return maxy;
        }

        public Bounds setMaxy(long maxy) {
            this.maxy = maxy;
            return this;
        }

        public long getWidth() {
            return maxx - minx;
        }

        public long getHeight() {
            return maxy - miny;
        }

        public boolean contain(final Coordinate coordinate) {
            return coordinate.x() >= minx && coordinate.x() <= maxx
                    && coordinate.y() >= miny && coordinate.y() <= maxy;
        }

        public String toString() {
            return "[" + minx + "," + maxx + "," + miny + "," + maxy + "]";
        }
    }
}
