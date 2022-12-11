package net.mloeks.aoc22.monkeybusiness;

import net.mloeks.aoc22.util.PoorMansEvaluator;

import java.math.BigInteger;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Monkey {
    private final int id;
    private final List<BigInteger> items;
    private final int testDivisibleBy;
    private final BiFunction<BigInteger, BigInteger, Integer> findTargetMonkey;

    private final String operation;

    private long numberOfInspections;

    public Monkey(final List<String> description) {
        this.id = extractSingleNumberOnLine(description.get(0));
        this.numberOfInspections = 0;

        this.items = Stream.of(description.get(1).replaceAll("[^\\d|,]", "").split(","))
                .map(BigInteger::new)
                .collect(Collectors.toList());

        this.operation = description.get(2).split("=")[1];

        this.testDivisibleBy = extractSingleNumberOnLine(description.get(3));
        final int truthyTargetMonkey = extractSingleNumberOnLine(description.get(4));
        final int falsyTargetMonkey = extractSingleNumberOnLine(description.get(5));
        this.findTargetMonkey = (input, superMod) -> input.remainder(superMod).equals(BigInteger.ZERO)
                || input.remainder(BigInteger.valueOf(testDivisibleBy)).equals(BigInteger.ZERO)
                        ? truthyTargetMonkey : falsyTargetMonkey;
    }

    public BigInteger inspect(final BigInteger item, final Function<BigInteger, BigInteger> itemChangeAfterInspection) {
        numberOfInspections++;
        BigInteger result = PoorMansEvaluator.eval(this.operation.replaceAll("old", item.toString()));
        return itemChangeAfterInspection != null
                ? itemChangeAfterInspection.apply(result)
                : result;
    }

    public void catchItem(final BigInteger item) {
        items.add(item);
    }

    public void throwItem(final BigInteger item, final Monkey targetMonkey) {
//        System.out.println("Monkey " + id + " throws item " + item + " to monkey " + targetMonkey.getId());
        targetMonkey.catchItem(item);
    }

    private static int extractSingleNumberOnLine(final String line) {
        return Integer.parseInt(line.replaceAll("[^\\d]", ""));
    }

    public int getId() {
        return id;
    }

    public List<BigInteger> getItems() {
        return items;
    }

    public int getTestDivisibleBy() {
        return testDivisibleBy;
    }

    public BiFunction<BigInteger, BigInteger, Integer> getFindTargetMonkey() {
        return findTargetMonkey;
    }

    public Long getNumberOfInspections() {
        return numberOfInspections;
    }

    @Override
    public String toString() {
        return "Monkey{" +
                "id=" + id +
                ", items=" + items +
                '}';
    }
}
