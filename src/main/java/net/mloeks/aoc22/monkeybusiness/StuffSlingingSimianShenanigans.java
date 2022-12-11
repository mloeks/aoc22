package net.mloeks.aoc22.monkeybusiness;

import net.mloeks.aoc22.util.PuzzleInputReader;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StuffSlingingSimianShenanigans {

    private final int rounds;
    private final Map<Integer, Monkey> monkeys;
    private final Function<BigInteger, BigInteger> changeOfWorryAfterMonkeyInspection;

    public StuffSlingingSimianShenanigans(final String input, final int rounds) {
        this(input, rounds, null);
    }

    public StuffSlingingSimianShenanigans(
            final String input, final int rounds, final Function<BigInteger, BigInteger> changeOfWorryAfterMonkeyInspection
    ) {
        this.rounds = rounds;
        this.changeOfWorryAfterMonkeyInspection = changeOfWorryAfterMonkeyInspection;

        this.monkeys = PuzzleInputReader.readAllSeparatedOn(input,
                line -> line.trim().isEmpty(),
                Monkey::new).stream()
                .collect(Collectors.toMap(Monkey::getId, Function.identity()));
    }

    public void play() {
        for (int round = 1; round <= rounds; round++) {
            playRound();
            System.out.println("Inspections after round " + round);
            printInspections();
        }
    }

    public long calculateMonkeyBusiness() {
        return monkeys.values().stream()
                .map(Monkey::getNumberOfInspections)
                .sorted(Comparator.comparingLong(i -> (long) i).reversed())
                .limit(2)
                .peek(System.out::println)
                .reduce(1L, (a, b) -> a*b);

    }

    private void playRound() {
        for (int monkeyId : monkeys.keySet()) {
            Monkey monkey = monkeys.get(monkeyId);
            Iterator<BigInteger> monkeyItems = monkey.getItems().iterator();
            while (monkeyItems.hasNext()) {
                BigInteger item = monkeyItems.next();
                BigInteger afterInspection = monkey.inspect(item, changeOfWorryAfterMonkeyInspection);
                int throwTo = monkey.getFindTargetMonkey().apply(afterInspection);
                if (throwTo == monkeyId) throw new IllegalStateException("Monkey cannot throw item to itself.");

                monkey.throwItem(afterInspection, monkeys.get(throwTo));
                monkeyItems.remove();
            }
        }
    }

    public void printItems() {
        StringWriter writer = new StringWriter();
        monkeys.values().forEach(monkey -> {
            writer.append(String.format("Monkey %s: %s", monkey.getId(), monkey.getItems()));
            writer.append("\n");
        });
        System.out.println(writer);
    }

    public void printInspections() {
        StringWriter writer = new StringWriter();
        monkeys.values().forEach(monkey -> {
            writer.append(String.format("Monkey %s inspected items %s times", monkey.getId(), monkey.getNumberOfInspections()));
            writer.append("\n");
        });
        System.out.println(writer);
    }
}
