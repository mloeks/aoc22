package net.mloeks.aoc22.supplystack;

import java.util.Stack;
import java.util.stream.IntStream;

public class CrateMover9000 implements CrateMover {
    @Override
    public <T> void moveCrates(int amountOfCrates, Stack<T> from, Stack<T> to) {
        IntStream.rangeClosed(1, amountOfCrates).forEach(i -> to.push(from.pop()));
    }
}
