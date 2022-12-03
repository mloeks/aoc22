package net.mloeks.aoc22.rucksack;

public abstract class RucksackUtils {

    public static int getItemPriority(char item) {
        if (item > 96) {
            // lower case, starting at 97
            return item - 96;
        }
        // upper case, starting at 65
        return item - 38;
    }
}
