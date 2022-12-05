package net.mloeks.aoc22.supplystack;

import net.mloeks.aoc22.util.PuzzleInputReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.stream.IntStream;

public class SupplyStackOrganiser {

    private final List<String> crateRows;
    private final List<Stack<Character>> stacks;
    private final CrateMover crateMover;

    public SupplyStackOrganiser(final String input, final CrateMover crateMover) {
        this.crateMover = crateMover;

        crateRows = new LinkedList<>();
        stacks = new LinkedList<>();

        try (PuzzleInputReader reader = new PuzzleInputReader(input)) {
            reader.stream().forEach(line -> {
                        if (line.contains("[")) crateRows.add(line);
                        else if (line.startsWith("move")) {
                            if (stacks.isEmpty()) readStacksDefinition();
                            moveCrates(line);
                        }
                    });
        }
    }

    public List<Stack<Character>> getStacks() {
        return stacks;
    }

    private void readStacksDefinition() {
        int stackPos = 0;

        while(true) {
            Stack<Character> newCrateStack = new Stack<>();
            for (int i = crateRows.size(); i > 0; i--) {
                try {
                    Character crate = crateRows.get(i-1).charAt(stackPos + 1);
                    if (notBlank(crate)) newCrateStack.push(crate);
                } catch (IndexOutOfBoundsException ignore) {}
            }

            if (newCrateStack.empty()) return;

            stacks.add(newCrateStack);

            stackPos += 4;
        }
    }

    private void moveCrates(String procedure) {
        Scanner scanner = new Scanner(procedure).useDelimiter("[\\D]+");

        int numCratesToMove = scanner.nextInt();
        int fromStack = scanner.nextInt() - 1;
        int toStack = scanner.nextInt() - 1;

        crateMover.moveCrates(numCratesToMove, stacks.get(fromStack), stacks.get(toStack));
    }

    private static boolean notBlank(Character c) {
        return c != null && c != 32;  // empty char
    }
}
