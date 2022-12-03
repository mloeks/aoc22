package net.mloeks.aoc22.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class PuzzleInputReader implements AutoCloseable {

    private final Stream<String> puzzleInputStream;

    public PuzzleInputReader(final String resource) {
        try {
            final URL resourceUrl = Objects.requireNonNull(getClass().getClassLoader().getResource(resource));
            this.puzzleInputStream = Files.lines(Paths.get(resourceUrl.toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("Error reading input.", e);
        }
    }

    public static List<String> readAll(final String resource) {
        try (PuzzleInputReader reader = new PuzzleInputReader(resource)) {
            return reader.puzzleInputStream.toList();
        }
    }

    public Stream<String> stream() {
        return this.puzzleInputStream;
    }

    public <T> Stream<T> stream(final Function<String, T> lineMapper) {
        return this.puzzleInputStream.map(lineMapper);
    }

    @Override
    public void close() {
        this.puzzleInputStream.close();
    }
}
