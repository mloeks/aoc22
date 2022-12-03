package net.mloeks.aoc22.rucksack;

import net.mloeks.aoc22.util.PuzzleInputReader;

import java.util.LinkedList;
import java.util.List;

public class BadgeFinder {

    private final List<Character> badges = new LinkedList<>();

    public BadgeFinder(final String input) {
        List<String> rucksackList = PuzzleInputReader.readAll(input);
        for (int i = 0; i < rucksackList.size(); i += 3) {
            badges.add(findCommonItem(List.of(
                    rucksackList.get(i),
                    rucksackList.get(i+1),
                    rucksackList.get(i+2)
            )));
        }
    }

    public List<Character> getBadges() {
        return badges;
    }

    private char findCommonItem(List<String> groupRucksacks) {
        for (char item1 : groupRucksacks.get(0).toCharArray()) {
            for (char item2 : groupRucksacks.get(1).toCharArray()) {
                if (item1 == item2) {
                    for (char item3 : groupRucksacks.get(2).toCharArray()) {
                        if (item1 == item3) {
                            return item1;
                        }
                    }
                }
            }
        }
        throw new IllegalStateException("No badge found in this group: " + groupRucksacks);
    }
}
