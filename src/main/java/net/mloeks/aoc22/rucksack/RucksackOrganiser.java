package net.mloeks.aoc22.rucksack;

import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.data.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class RucksackOrganiser {

    private final List<Character> duplicatedItems = new LinkedList<>();

    public RucksackOrganiser(final String input) {
        try (PuzzleInputReader reader = new PuzzleInputReader(input)) { reader
                .stream(String::trim)
                .map(line -> Pair.of(
                    line.substring(0, line.length() / 2),
                    line.substring(line.length() / 2)
                ))
                .map(compartments -> findDuplicatedItem(compartments.getFirst(), compartments.getSecond()))
                .forEach(duplicatedItems::add);
        }
    }

    public List<Character> getDuplicatedItems() {
        return duplicatedItems;
    }

    private char findDuplicatedItem(String compartment1, String compartment2) {
        for (char item1 : compartment1.toCharArray()) {
            for (char item2 : compartment2.toCharArray()) {
                if (item1 == item2) return item1;
            }
        }
        throw new IllegalStateException("No duplicate found in compartmens: " + compartment1 + " | " + compartment2);
    }
}
