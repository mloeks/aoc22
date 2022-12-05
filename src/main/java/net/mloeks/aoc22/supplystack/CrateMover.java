package net.mloeks.aoc22.supplystack;

import java.util.Stack;

public interface CrateMover {
    <T> void moveCrates(int amountOfCrates, Stack<T> from, Stack<T> to);
}
