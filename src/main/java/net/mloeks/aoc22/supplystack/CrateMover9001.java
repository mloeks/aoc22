package net.mloeks.aoc22.supplystack;

import java.util.Stack;
import java.util.stream.IntStream;

public class CrateMover9001 implements CrateMover {
    @Override
    public <T> void moveCrates(int amountOfCrates, Stack<T> from, Stack<T> to) {
        Stack<T> movedCrates = new Stack<>();
        IntStream.rangeClosed(1, amountOfCrates).forEach(i -> movedCrates.push(from.pop()));
        while (!movedCrates.empty()) {
            to.push(movedCrates.pop());
        }
    }
}
