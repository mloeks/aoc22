package net.mloeks.aoc22.rockpaperscissors;

public interface Strategy {
    Shape chooseShapeToPlay(String strategyCode, Shape opponentsShape);
}
