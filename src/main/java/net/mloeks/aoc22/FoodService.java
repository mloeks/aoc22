package net.mloeks.aoc22;

import org.apache.groovy.parser.antlr4.util.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.apache.groovy.parser.antlr4.util.StringUtils.isEmpty;

@Component
public class FoodService {

    public List<Integer> getCaloriesCarriedByElves(final String input) throws URISyntaxException, IOException {
        List<Integer> caloriesCarriedByElf = new LinkedList<>();
        Path path = Paths.get(getClass().getClassLoader().getResource(input).toURI());

        int sumCalories = 0;
        try (Stream<String> lines = Files.lines(path)) {
            for (String line : lines.toList()) {
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
