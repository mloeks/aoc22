package net.mloeks.aoc22;

import net.mloeks.aoc22.util.PuzzleInputReader;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

public class CathodeRayTube {
    private static final List<Integer> CYCLES_TO_READ = List.of(20, 60, 100, 140, 180, 220);
    private static final int SCREEN_WIDTH = 40;

    private final List<Long> signalStrengths;

    private List<Character> pixels;

    private int cycle;
    private long register;

    public CathodeRayTube(final String programInput) {
        this.signalStrengths = new LinkedList<>();
        this.cycle = 0;
        this.register = 1;
        this.pixels = new LinkedList<>();

        try (PuzzleInputReader reader = new PuzzleInputReader(programInput)) {
            reader.stream(line -> line.split(" "))
                    .forEach(command -> {
                        switch (command[0]) {
                            case "noop" -> runCycle();
                            case "addx" -> {
                                runCycle();
                                runCycle();
                                this.register += Long.parseLong(command[1]);
                            }
                        }
                    });
        }
    }

    public List<Long> getSignalStrengths() {
        return signalStrengths;
    }

    public List<Character> getPixels() {
        return pixels;
    }

    public String draw(int screenWidth) {
        StringWriter writer = new StringWriter();
        for (int i=0; i<pixels.size(); i++) {
            if (i % screenWidth == 0 && i > 0) {
                writer.append("\n");
            }
            writer.append(pixels.get(i));
        }
        return writer.toString();
    }

    private void runCycle() {
        pixels.add(getPixel(cycle, register));
        cycle++;
        if (CYCLES_TO_READ.contains(cycle)) {
            signalStrengths.add(cycle * register);
        }
    }

    private static Character getPixel(int pixelPos, long spritePos) {
        return (pixelPos % SCREEN_WIDTH) >= (spritePos - 1) && (pixelPos % SCREEN_WIDTH) <= (spritePos + 1) ? '#' : '.';
    }

}
