package net.mloeks.aoc22;

import net.mloeks.aoc22.rockpaperscissors.OutcomeBasedOnOpponentStrategy;
import net.mloeks.aoc22.rockpaperscissors.RockPaperScissorsGame;
import net.mloeks.aoc22.rockpaperscissors.SimpleXyzMappingStrategy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static java.util.Comparator.comparingInt;
import static org.assertj.core.api.Assertions.assertThat;

public class AocTest {

    private final FoodService foodService = new FoodService();

    @Test
    public void day1() throws IOException, URISyntaxException {
        List<Integer> caloriesCarriedByElf = foodService.getCaloriesCarriedByElves("01_food_calories.txt");
        caloriesCarriedByElf.sort(comparingInt(value -> (int) value).reversed());

        // part 1
        int maxCaloriesCarriedByElf = caloriesCarriedByElf.get(0);
        assertThat(maxCaloriesCarriedByElf).isEqualTo(71924);

        // part 2
        int topThreeElvesCaloriesSum = caloriesCarriedByElf.get(0) + caloriesCarriedByElf.get(1) + caloriesCarriedByElf.get(2);
        assertThat(topThreeElvesCaloriesSum).isEqualTo(210406);
    }

    @Test
    public void day2_1() throws IOException, URISyntaxException {
        RockPaperScissorsGame game = new RockPaperScissorsGame("02_input.txt", new SimpleXyzMappingStrategy());
        game.play();
        assertThat(game.getTotalScore()).isEqualTo(13052);
    }

    @Test
    public void day2_2() throws IOException, URISyntaxException {
        RockPaperScissorsGame game = new RockPaperScissorsGame("02_input.txt", new OutcomeBasedOnOpponentStrategy());
        game.play();
        assertThat(game.getTotalScore()).isEqualTo(13693);
    }

}
