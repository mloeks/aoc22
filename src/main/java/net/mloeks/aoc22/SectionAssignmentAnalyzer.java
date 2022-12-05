package net.mloeks.aoc22;

import net.mloeks.aoc22.util.PuzzleInputReader;
import net.mloeks.aoc22.util.SimpleRange;
import org.springframework.data.util.Pair;

import java.util.function.Predicate;

import static java.lang.Integer.parseInt;

public class SectionAssignmentAnalyzer {
    public static final Predicate<Pair<SimpleRange, SimpleRange>> CONTAINS = pair ->
            pair.getFirst().contains(pair.getSecond()) || pair.getSecond().contains(pair.getFirst());
    public static final Predicate<Pair<SimpleRange, SimpleRange>> OVERLAPS = pair ->
            pair.getFirst().overlaps(pair.getSecond());

    private final int redundantPairsCount;

    public SectionAssignmentAnalyzer(
            final String elfPairAssigments, final Predicate<Pair<SimpleRange, SimpleRange>> isPairRedundant
    ) {
        try (PuzzleInputReader reader = new PuzzleInputReader(elfPairAssigments)) {
            this.redundantPairsCount = (int) reader.streamSplittingBy(",")
                    .map(sectionAssignments -> Pair.of(
                            sectionAssignmentToRange(sectionAssignments.getFirst()),
                            sectionAssignmentToRange(sectionAssignments.getSecond()))
                    )
                    .filter(isPairRedundant).count();
        }
    }

    public int getRedundantPairsCount() {
        return redundantPairsCount;
    }

    private SimpleRange sectionAssignmentToRange(final String sectionAssignment) {
        String[] minMax = sectionAssignment.split("-");
        return SimpleRange.of(parseInt(minMax[0]), parseInt(minMax[1]));
    }
}
