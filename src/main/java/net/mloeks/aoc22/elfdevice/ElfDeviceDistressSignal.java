package net.mloeks.aoc22.elfdevice;

import net.mloeks.aoc22.util.PuzzleInputReader;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ElfDeviceDistressSignal {

    private final Map<String, Node> subPacketByIdentifier;
    private final List<Integer> correctPackets;

    public ElfDeviceDistressSignal(final String packetsInput) {
        this.subPacketByIdentifier = new HashMap<>();
        this.correctPackets = new ArrayList<>();

        try (PuzzleInputReader reader = new PuzzleInputReader(packetsInput)) {
            List<Node> packets = reader.stream(line -> !line.trim().isEmpty(),
                    this::parsePacket).toList();

            for (int i = 1; i <packets.size() / 2; i++) {
                if (isPacketPairCorrect(packets.get(2*i-2), packets.get(2*i-1))) {
                    correctPackets.add(i);
                }
            }
        }
    }

    public List<Integer> getCorrectPackets() {
        return correctPackets;
    }

    private Node parsePacket(String packetDescription) {
        while (hasSubpackets(packetDescription)) {
            packetDescription = parseSubPacketRecursively(packetDescription);
        }
        return toNode(packetDescription);
    }

    private String parseSubPacketRecursively(String subPacketDescription) {
        Pattern innermostBrackets = Pattern.compile("\\[[^\\[\\]]+\\]");
        Matcher m = innermostBrackets.matcher(subPacketDescription);
        while (m.find()) {
            Node subPacket = toNode(m.group(0));
            subPacketByIdentifier.put(getIdentifier(subPacket), subPacket);
            subPacketDescription = subPacketDescription.replace(subPacket.toString(), getIdentifier(subPacket));
        }
        return subPacketDescription;
    }

    private static boolean hasSubpackets(String packetDescription) {
        return Optional.ofNullable(stripOuterBrackets(packetDescription))
                .map(s -> s.contains("[")).orElse(false);
    }

    private static String getIdentifier(Node subPacket) {
        return Optional.ofNullable(subPacket.getChildren().toString())
                .map(s -> s.replaceAll("\\[\\],", "|"))
                .orElse("<empty>");
    }

    private static String stripOuterBrackets(final String inp) {
        if (inp.isEmpty() || inp.equals("[]")) return null;
        if (!inp.contains("[") || !inp.contains("]")) return inp;
        return inp.substring(1, inp.length() - 1);
    }

    private Node toNode(final String noBracketsPlainList) {
        Node newNode = new Node();
        Stream.of(Optional.ofNullable(stripOuterBrackets(noBracketsPlainList))
                        .map(s -> s.split(","))
                        .orElse(new String[]{}))
                .map(s -> (s.contains("|"))
                        ? subPacketByIdentifier.get(s)
                        : new Node(Integer.parseInt(s)))
                .forEach(newNode::addChild);
        return newNode;
    }

    private boolean isPacketPairCorrect(final Node firstPacket, final Node secondPacket) {
        System.out.println("Compare " + firstPacket + " vs " + secondPacket);
        return false;
    }

    private static class Node {
        private final Integer value;
        private final List<Node> children;
        private Node parent;

        public Node() {
            this(null);
        }

        public Node(Integer value) {
            this.value = value;
            this.parent = null;
            this.children = new LinkedList<>();
        }

        public boolean isLeaf() {
            return value != null && children.isEmpty();
        }

        public int getValue() {
            return value;
        }

        public Node getParent() {
            return parent;
        }

        public Node setParent(Node parent) {
            this.parent = parent;
            return this;
        }

        public List<Node> getChildren() {
            return children;
        }

        public Node addChild(Node child) {
            child.setParent(this);
            this.children.add(child);
            return this;
        }

        public String toString() {
            return isLeaf()
                    ? String.valueOf(value)
                    : getChildren().toString();
        }
    }
}
