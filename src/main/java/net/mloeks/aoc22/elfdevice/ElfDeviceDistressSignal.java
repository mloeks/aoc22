package net.mloeks.aoc22.elfdevice;

import jakarta.annotation.Nullable;
import net.mloeks.aoc22.util.PuzzleInputReader;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.function.Function.identity;

/**
 * Pretty sure there's an easier way to do this ;-)
 */
public class ElfDeviceDistressSignal {
    private static final List<String> DIVIDER_PACKETS = List.of("[[2]]", "[[6]]");

    private final List<Pair<String, String>> packetPairs;
    private final List<String> allPairsIncludingDividers;

    public ElfDeviceDistressSignal(final String packetsInput) {
        packetPairs = PuzzleInputReader.readAllSeparatedOn(packetsInput, line -> line.trim().isEmpty(),
                        line -> Pair.of(line.get(0), line.get(1)));
        allPairsIncludingDividers = packetPairs.stream()
                .flatMap(pair -> Stream.of(pair.getFirst(), pair.getSecond()))
                .collect(Collectors.toList());
        allPairsIncludingDividers.addAll(DIVIDER_PACKETS);
    }

    public List<Integer> getPacketPairsInCorrectOrder() {
        List<Integer> packetPairsInCorrectOrder = new ArrayList<>();
        AtomicInteger packetIndex = new AtomicInteger(1);
        packetPairs.forEach(packetPair -> {
            if (isPacketPairCorrect(packetPair) > 0) {
                packetPairsInCorrectOrder.add(packetIndex.get());
            }
            packetIndex.incrementAndGet();
        });
        return packetPairsInCorrectOrder;
    }

    public int getDecoderKey() {
        allPairsIncludingDividers.sort(comparing(identity(), (String packet, String other) ->
                isPacketPairCorrect(Pair.of(packet, other))).reversed());
        return DIVIDER_PACKETS.stream()
                .mapToInt(allPairsIncludingDividers::indexOf)
                .map(i -> i + 1)
                .reduce(1, (a, b) -> a*b);
    }

    private static String stripOuterBrackets(final String inp) {
        if (inp.isEmpty() || inp.equals("[]")) return null;
        if (!inp.contains("[") || !inp.contains("]")) return inp;
        return inp.substring(1, inp.length() - 1);
    }

    private static int isPacketPairCorrect(final Pair<String, String> packetPair) {
//        System.out.println("Compare " + packetPair.getFirst() + " vs " + packetPair.getSecond());
        AtomicInteger firstPos = new AtomicInteger(0);
        AtomicInteger secondPos = new AtomicInteger(0);

        int comparisonResult;
        Optional<String> nextFirstItem;
        Optional<String> nextSecondItem;
        do {
            nextFirstItem = getNextItem(packetPair.getFirst(), firstPos);
            nextSecondItem = getNextItem(packetPair.getSecond(), secondPos);
            comparisonResult = compareTo(nextFirstItem.orElse(null), nextSecondItem.orElse(null));
        } while (nextFirstItem.isPresent() && nextSecondItem.isPresent() && comparisonResult == 0);

//        System.out.println(comparisonResult);
        return comparisonResult;
    }

    private static Optional<String> getNextItem(String subPacket, AtomicInteger pos) {
        int braceLevel = 0;
        StringBuilder buffer = new StringBuilder();
        subPacket = stripOuterBrackets(subPacket);
        if (subPacket == null) return Optional.empty();

        if (pos.get() < subPacket.length()) {
            char next;
            do {
                next = subPacket.charAt(pos.getAndIncrement());
                if (next == '[') braceLevel++;
                else if (next == ']') braceLevel--;
                else if (next == ',' && braceLevel == 0) break;
                buffer.append(next);
            } while (pos.get() < subPacket.length());
        }

        return buffer.isEmpty() ? Optional.empty() : Optional.of(buffer.toString());
    }

    private static int compareTo(@Nullable String packet, @Nullable String otherPacket) {
//        System.out.println("Compare " + packet + " vs " + otherPacket);
        if (packet == null || otherPacket == null) {
            if (packet == null && otherPacket == null) return 0;
            return packet == null ? 1 : -1;
        }
        try {
            int firstInt = Integer.parseInt(packet);
            int secondInt = Integer.parseInt(otherPacket);
            if (firstInt == secondInt) return 0;
            else return secondInt - firstInt;
        } catch (NumberFormatException e) {
            return isPacketPairCorrect(Pair.of(packet, otherPacket));
        }
    }

}
