package net.mloeks.aoc22;

import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.apache.groovy.parser.antlr4.util.StringUtils.isEmpty;

@Component
public class FoodService {

    public List<Integer> getCaloriesCarriedByElves(final String input) {
        final List<Integer> caloriesCarriedByElf = new LinkedList<>();
        try (final PuzzleInputReader reader = new PuzzleInputReader(input)) {
            int sumCalories = 0;
            for (String line : reader.stream().toList()) {
                if (isEmpty(line.trim())) {
                    caloriesCarriedByElf.add(sumCalories);
                    sumCalories = 0;
                } else {
                    sumCalories += Integer.parseInt(line.trim());
                }
            }
        }

        return caloriesCarriedByElf;
    }

}
