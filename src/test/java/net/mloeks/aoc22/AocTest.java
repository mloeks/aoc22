package net.mloeks.aoc22;

import net.mloeks.aoc22.rockpaperscissors.OutcomeBasedOnOpponentStrategy;
import net.mloeks.aoc22.rockpaperscissors.RockPaperScissorsGame;
import net.mloeks.aoc22.rockpaperscissors.SimpleXyzMappingStrategy;
import net.mloeks.aoc22.rucksack.BadgeFinder;
import net.mloeks.aoc22.rucksack.RucksackOrganiser;
import net.mloeks.aoc22.rucksack.RucksackUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;

public class AocTest {

    private final FoodService foodService = new FoodService();

    @Test
    public void day1() {
        List<Integer> caloriesCarriedByElf = foodService.getCaloriesCarriedByElves("01.txt");
        caloriesCarriedByElf.sort(comparingInt(value -> (int) value).reversed());

        // part 1
        int maxCaloriesCarriedByElf = caloriesCarriedByElf.get(0);
        assertThat(maxCaloriesCarriedByElf).isEqualTo(71924);

        // part 2
        int topThreeElvesCaloriesSum = caloriesCarriedByElf.get(0) + caloriesCarriedByElf.get(1) + caloriesCarriedByElf.get(2);
        assertThat(topThreeElvesCaloriesSum).isEqualTo(210406);
    }

    @Test
    public void day2_1() {
        RockPaperScissorsGame game = new RockPaperScissorsGame("02.txt", new SimpleXyzMappingStrategy());
        assertThat(game.getTotalScore()).isEqualTo(13052);
    }

    @Test
    public void day2_2() {
        RockPaperScissorsGame game = new RockPaperScissorsGame("02.txt", new OutcomeBasedOnOpponentStrategy());
        assertThat(game.getTotalScore()).isEqualTo(13693);
    }

    @Test
    public void day3_1() {
        RucksackOrganiser rucksackOrganiser = new RucksackOrganiser("03.txt");
        int sumOfPriorities = rucksackOrganiser.getDuplicatedItems().stream()
                .mapToInt(RucksackUtils::getItemPriority).sum();
        assertThat(sumOfPriorities).isEqualTo(7553);
    }

    @Test
    public void day3_2() {
        BadgeFinder badgeFinder = new BadgeFinder("03.txt");
        int sumOfBadgePriorities = badgeFinder.getBadges().stream()
                .mapToInt(RucksackUtils::getItemPriority).sum();
        assertThat(sumOfBadgePriorities).isEqualTo(2758);
    }
}
