package net.mloeks.aoc22.elfdevice;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ElfDeviceFile {

    private final String name;
    private final long size;
    private final boolean directory;

    private ElfDeviceFile parent;
    private final List<ElfDeviceFile> children;

    public ElfDeviceFile(String name, long size) {
        this(name, false, size);
    }

    public ElfDeviceFile(String name, boolean directory, long size) {
        this.name = name;
        this.directory = directory;
        this.size = size;

        this.parent = null;
        this.children = new LinkedList<>();

        if (directory && size > 0) throw new IllegalArgumentException("Directories cannot have a size.");
    }

    public static ElfDeviceFile parse(final String commandLineInput) {
        String[] inputParts = commandLineInput.split(" ");
        if (commandLineInput.startsWith("dir")) {
            return new ElfDeviceFile(inputParts[1], true, 0);
        }
        return new ElfDeviceFile(inputParts[1], false, Long.parseLong(inputParts[0]));
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return size;
    }

    public boolean isDirectory() {
        return directory;
    }

    public ElfDeviceFile getParent() {
        return parent;
    }

    public ElfDeviceFile setParent(final ElfDeviceFile parent) {
        this.parent = parent;
        return this;
    }

    public List<ElfDeviceFile> getChildren() {
        return children;
    }

    public ElfDeviceFile addChild(final ElfDeviceFile file) {
        file.setParent(this);
        this.children.add(file);
        return this;
    }

    public long getTotalSize() {
        if (!isDirectory()) return size;
        return findAllRecursively(file -> !file.isDirectory()).stream()
                .mapToLong(ElfDeviceFile::getSize)
                .sum();
    }

    public List<ElfDeviceFile> findAllRecursively(Predicate<ElfDeviceFile> filter) {
        List<ElfDeviceFile> files = new LinkedList<>();
        files.add(this);
        for (ElfDeviceFile child : children) {
            files.addAll(child.findAllRecursively(filter));
        }
        return files.stream().filter(filter).toList();
    }

    public String toString() {
        return name + " (" + size + ")";
    }
}
