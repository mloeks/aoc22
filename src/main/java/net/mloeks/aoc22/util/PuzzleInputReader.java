package net.mloeks.aoc22.util;

import org.springframework.data.util.Pair;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.function.Function.identity;

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
            return reader.stream().toList();
        }
    }

    public Stream<String> stream() {
        return stream(identity());
    }

    public <T> Stream<T> stream(final Function<String, T> lineMapper) {
        return stream(l -> true, lineMapper);
    }

    public <T> Stream<T> stream(final Predicate<String> lineFilter, final Function<String, T> lineMapper) {
        return this.puzzleInputStream
                .filter(lineFilter)
                .map(lineMapper);
    }

    public Stream<Pair<String, String>> streamSplittingBy(final String regex) {
        return stream(line -> line.trim().split(regex)).map(arr -> Pair.of(arr[0], arr[1]));
    }

    @Override
    public void close() {
        this.puzzleInputStream.close();
    }
}
