package net.mloeks.aoc22;

import net.mloeks.aoc22.elfdevice.ElfDevice;
import net.mloeks.aoc22.elfdevice.ElfDeviceDistressSignal;
import net.mloeks.aoc22.elfdevice.ElfDeviceFile;
import net.mloeks.aoc22.elfdevice.ElfDeviceFileSystem;
import net.mloeks.aoc22.hillclimbing.HeightMap;
import net.mloeks.aoc22.monkeybusiness.StuffSlingingSimianShenanigans;
import net.mloeks.aoc22.rockpaperscissors.OutcomeBasedOnOpponentStrategy;
import net.mloeks.aoc22.rockpaperscissors.RockPaperScissorsGame;
import net.mloeks.aoc22.rockpaperscissors.SimpleXyzMappingStrategy;
import net.mloeks.aoc22.rucksack.BadgeFinder;
import net.mloeks.aoc22.rucksack.RucksackOrganiser;
import net.mloeks.aoc22.rucksack.RucksackUtils;
import net.mloeks.aoc22.supplystack.CrateMover9000;
import net.mloeks.aoc22.supplystack.CrateMover9001;
import net.mloeks.aoc22.supplystack.SupplyStackOrganiser;
import net.mloeks.aoc22.util.Coordinate;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

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

    @Test
    public void day4_1() {
        SectionAssignmentAnalyzer sectionAssignmentAnalyzer =
                new SectionAssignmentAnalyzer("04.txt", SectionAssignmentAnalyzer.CONTAINS);
        assertThat(sectionAssignmentAnalyzer.getRedundantPairsCount()).isEqualTo(471);
    }

    @Test
    public void day4_2() {
        SectionAssignmentAnalyzer sectionAssignmentAnalyzer =
                new SectionAssignmentAnalyzer("04.txt", SectionAssignmentAnalyzer.OVERLAPS);
        assertThat(sectionAssignmentAnalyzer.getRedundantPairsCount()).isEqualTo(888);
    }

    @Test
    public void day5_1() {
        SupplyStackOrganiser supplyStackOrganiser = new SupplyStackOrganiser("05.txt", new CrateMover9000());
        assertThat(supplyStackOrganiser.getStacks().stream()
                .map(Stack::pop)
                .map(Object::toString)
                .collect(Collectors.joining(""))).isEqualTo("WSFTMRHPP");
    }

    @Test
    public void day5_2() {
        SupplyStackOrganiser supplyStackOrganiser = new SupplyStackOrganiser("05.txt", new CrateMover9001());
        assertThat(supplyStackOrganiser.getStacks().stream()
                .map(Stack::pop)
                .map(Object::toString)
                .collect(Collectors.joining(""))).isEqualTo("GSLCMFBRP");
    }

    @ParameterizedTest
    @CsvSource(value = { "06_example.txt,10", "06.txt,1582" })
    public void day6_1(String input, int expectedResult) {
        ElfDevice device = new ElfDevice(input);
        assertThat(device.findStartOfPackageMarker(4)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource(value = { "06_example.txt,29", "06.txt,3588" })
    public void day6_2(String input, int expectedResult) {
        ElfDevice elfDevice = new ElfDevice(input);
        assertThat(elfDevice.findStartOfPackageMarker(14)).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource(value = { "07_example.txt,95437", "07.txt,1915606" })
    public void day7_1(String input, int expectedTotalDirectorySize) {
        ElfDeviceFileSystem fileSystem = ElfDeviceFileSystem.buildFromCommandInput(input, 70_000_000);

        long dirSize = fileSystem.getDirectories().stream()
                .mapToLong(ElfDeviceFile::getTotalSize)
                .filter(s -> s <= 100_000)
                .sum();
        assertThat(dirSize).isEqualTo(expectedTotalDirectorySize);
    }

    @ParameterizedTest
    @CsvSource(value = { "07_example.txt,24933642", "07.txt,5025657" })
    public void day7_2(String input, int expectedSpaceFreedUp) {
        ElfDeviceFileSystem fileSystem = ElfDeviceFileSystem.buildFromCommandInput(input, 70_000_000);

        long freedSpace = fileSystem.freeUpSpace(30_000_000);
        assertThat(freedSpace).isEqualTo(expectedSpaceFreedUp);
    }

    @ParameterizedTest
    @CsvSource(value = { "08_example.txt,21", "08.txt,1669" })
    public void day8_1(String input, int expectedVisibleTrees) {
        TallTreeMap map = new TallTreeMap(input);
        assertThat(map.countVisibleTrees()).isEqualTo(expectedVisibleTrees);
    }

    @ParameterizedTest
    @CsvSource(value = { "08_example.txt,8", "08.txt,331344" })
    public void day8_2(String input, int expectedScenicScore) {
        TallTreeMap map = new TallTreeMap(input);
        assertThat(map.mostScenicTreeScore()).isEqualTo(expectedScenicScore);
    }

    @ParameterizedTest
    @CsvSource(value = { "09_example.txt,13", "09.txt,6470" })
    public void day9_1(String input, int expectedTailPositionCount) {
        Rope rope = new Rope(input, 2);
        assertThat(rope.getTailPositions().stream().distinct().count()).isEqualTo(expectedTailPositionCount);
    }

    @ParameterizedTest
    @CsvSource(value = { "09_example2.txt,36", "09.txt,2658" })
    public void day9_2(String input, int expectedTailPositionCount) {
        Rope rope = new Rope(input, 10);
        assertThat(rope.getTailPositions().stream().distinct().count()).isEqualTo(expectedTailPositionCount);
    }

    @ParameterizedTest
    @CsvSource(value = { "10_example.txt,13140", "10.txt,14920" })
    public void day10(String input, int expectedSumOfSignalStrengths) {
        CathodeRayTube cathodeRayTube = new CathodeRayTube(input);
        assertThat(cathodeRayTube.getSignalStrengths().stream().mapToLong(l -> l).sum())
                .isEqualTo(expectedSumOfSignalStrengths);

        System.out.println(cathodeRayTube.draw(40));
    }

    @ParameterizedTest
    @CsvSource(value = { "11_example.txt,10605", "11.txt,102399" })
    public void day11_1(String input, long expectedMonkeyBusiness) {
        StuffSlingingSimianShenanigans game =
                new StuffSlingingSimianShenanigans(input, 20, item -> BigInteger.valueOf((long) Math.floor(item.doubleValue() / 3.0)));
        game.play();

        assertThat(game.calculateMonkeyBusiness()).isEqualTo(expectedMonkeyBusiness);
    }

    @Disabled("oom")
    @ParameterizedTest
    @CsvSource(value = { "11_example.txt,2713310158", "11.txt,102399" })
    public void day11_2(String input, long expectedMonkeyBusiness) {
        StuffSlingingSimianShenanigans game =
                new StuffSlingingSimianShenanigans(input, 10_000);
        game.play();

        assertThat(game.calculateMonkeyBusiness()).isEqualTo(expectedMonkeyBusiness);
    }

    @ParameterizedTest
    @CsvSource(value = { "12_example.txt,31", "12.txt,352" })
    public void day12_1(String input, long expectedShortestPathLength) {
        HeightMap heightMap = new HeightMap(input);
        assertThat(heightMap.findShortestPath(null)).isEqualTo(expectedShortestPathLength);
    }

    @ParameterizedTest
    @CsvSource(value = { "12_example.txt,29", "12.txt,345" })
    public void day12_2(String input, long expectedShortestPathLength) {
        HeightMap heightMap = new HeightMap(input);

        int nicestShortestPath = heightMap.getGraph().getNodes().stream()
                .filter(node -> node.getHeight() == 'a')
                .map(heightMap::findShortestPath)
                .min(comparingInt(i -> i)).orElseThrow();

        assertThat(nicestShortestPath).isEqualTo(expectedShortestPathLength);
    }

    @ParameterizedTest
    @CsvSource(value = { "13_example.txt,13", "13.txt,6187" })
    public void day13_1(String input, long expectedSumOfCorrectPairs) {
        ElfDeviceDistressSignal distressSignal = new ElfDeviceDistressSignal(input);
        assertThat(distressSignal.getPacketPairsInCorrectOrder().stream()
                .mapToInt(k -> k).sum()).isEqualTo(expectedSumOfCorrectPairs);
    }

    @ParameterizedTest
    @CsvSource(value = { "13_example.txt,140", "13.txt,23520" })
    public void day13_2(String input, long expectedDecoderKey) {
        ElfDeviceDistressSignal distressSignal = new ElfDeviceDistressSignal(input);
        assertThat(distressSignal.getDecoderKey()).isEqualTo(expectedDecoderKey);
    }

    @ParameterizedTest
    @CsvSource(value = { "14_example.txt,24", "14.txt,964" })
    public void day14_1(String input, long expectedUnits) {
        CaveProfile caveProfile = new CaveProfile(input, false);
        caveProfile.simulateDrippingSandFrom(new Coordinate(500,0), () -> false);
        assertThat(caveProfile.getSandUnits().size()).isEqualTo(expectedUnits);
    }

    @ParameterizedTest
    @CsvSource(value = { "14_example.txt,93", "14.txt,32041" })
    public void day14_2(String input, long expectedUnits) {
        CaveProfile caveProfile = new CaveProfile(input, true);
        Coordinate startPouring = new Coordinate(500, 0);
        caveProfile.simulateDrippingSandFrom(startPouring,
                () -> caveProfile.getSandUnits().contains(startPouring));
        assertThat(caveProfile.getSandUnits().size()).isEqualTo(expectedUnits);
    }

    @ParameterizedTest
    @CsvSource(value = { "15_example.txt,10,26", "15.txt,2000000,5040643" })
    public void day15_1(String input, long row, int expectedPositions) {
        BeaconMap beaconMap = new BeaconMap(input);
        assertThat(beaconMap.scanRowForPositionsInReach(row)).hasSize(expectedPositions);
    }

}
