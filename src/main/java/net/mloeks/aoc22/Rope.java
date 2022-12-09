package net.mloeks.aoc22;

import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.data.util.Pair;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

import static net.mloeks.aoc22.ManhattanDirection.fromUrdl;

public class Rope {

    private List<Coordinate> rope;
    private final List<Coordinate> tailPositions;

    public Rope(final String movementInstructions, final int knotCount) {
        rope = new LinkedList<>();
        IntStream.range(0, knotCount).forEach(i -> rope.add(new Coordinate(0,0)));

        tailPositions = new LinkedList<>();
        rememberTailPosition();

        try (PuzzleInputReader reader = new PuzzleInputReader(movementInstructions)) {
            reader.streamSplittingBy(" ")
                    .map(pair -> Pair.of(pair.getFirst(), Integer.parseInt(pair.getSecond())))
                    .forEach(this::moveRope);
        }
    }

    public List<Coordinate> getTailPositions() {
        return tailPositions;
    }

    private void moveRope(Pair<String, Integer> moveInstruction) {
        ManhattanDirection direction = fromUrdl(moveInstruction.getFirst());
        IntStream.range(0, moveInstruction.getSecond())
                .forEach(i -> {
                    Coordinate lastMovedKnot = null;
                    for (int knotIndex = 0; knotIndex < rope.size(); knotIndex++) {
                        lastMovedKnot = knotIndex == 0
                                ? direction.move(rope.get(knotIndex))
                                : closeUpToKnot(rope.get(knotIndex), lastMovedKnot);
                        rope.set(knotIndex, lastMovedKnot);
                    }
                    rememberTailPosition();
                });
    }

    private void rememberTailPosition() {
         tailPositions.add(rope.get(rope.size() - 1));
    }

    private Coordinate closeUpToKnot(Coordinate knot, Coordinate knotToFollow) {
          if (touches(knot, knotToFollow)) return knot;

          Coordinate delta = knot.delta(knotToFollow);
          int dx = delta.x() == 0 ? 0 : delta.x()/Math.abs(delta.x());
          int dy = delta.y() == 0 ? 0 : delta.y()/Math.abs(delta.y());

          return knot.move(dx, dy);
    }

    private static boolean touches(Coordinate head, Coordinate tail) {
        Coordinate delta = head.delta(tail);
        return Math.abs(delta.x()) <= 1 && Math.abs(delta.y()) <= 1;
    }

   public String toString() {
        StringWriter writer = new StringWriter();
        for (int y=0; y<10; y++) {
            for (int x=0; x<10; x++) {
                Coordinate pos = new Coordinate(x,y);
                int knotIndex = rope.indexOf(pos);
                if (knotIndex != -1) {
                    writer.append(knotIndex == 0 ? "H" : String.valueOf(knotIndex));
                    continue;
                }
                writer.append('.');
           }
           writer.append("\n");
       }
       return writer.toString();
   }
}
