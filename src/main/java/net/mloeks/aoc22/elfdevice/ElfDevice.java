package net.mloeks.aoc22.elfdevice;

import net.mloeks.aoc22.util.PuzzleInputReader;

public class ElfDevice {

    private final String datastream;

    public ElfDevice(final String input) {
        this.datastream = PuzzleInputReader.readAll(input).get(0);
    }

    public int findStartOfPackageMarker(int bufferSize) {
        String buffer;
        for (int i = 0; i < datastream.length() - bufferSize; i++) {
            buffer = datastream.substring(i, i + bufferSize);
            if (hasUniqueCharacters(buffer)) return i + bufferSize;
        }
        throw new IllegalStateException("Could not find start of package marker.");
    }

    private boolean hasUniqueCharacters(final String buffer) {
        return buffer.chars().distinct().count() == buffer.length();
    }
}
