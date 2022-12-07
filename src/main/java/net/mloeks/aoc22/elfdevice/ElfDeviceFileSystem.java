package net.mloeks.aoc22.elfdevice;


import net.mloeks.aoc22.util.PuzzleInputReader;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ElfDeviceFileSystem {

    private final long size;
    private final ElfDeviceFile root;
    private ElfDeviceFile currentDir;

    private ElfDeviceFileSystem(final long size) {
        this.size = size;
        this.root = new ElfDeviceFile("/", true, 0);
        this.currentDir = root;
    }

    public static ElfDeviceFileSystem buildFromCommandInput(final String commandInput, final long size) {
        ElfDeviceFileSystem fileSystem = new ElfDeviceFileSystem(size);

        try (PuzzleInputReader reader = new PuzzleInputReader(commandInput)) {
            reader.stream(ElfDeviceFileSystem::mapCommandInput)
                    .forEach(inp -> {
                        if (inp instanceof ElfDeviceCommand command) {
                            if ("cd".equals(command.getCommand())) {
                                fileSystem.changeDir(command.getArg());
                            } else if ("ls".equals(command.getCommand())) {
                                // Nothing to do.
                            } else {
                                throw new IllegalArgumentException("Unknown command: " + command.getCommand());
                            }
                        } else if (inp instanceof ElfDeviceFile file) {
                            fileSystem.addFile(file);
                        }
                    });
        }
        return fileSystem;
    }

    private static Object mapCommandInput(String command) {
        if (command.startsWith("$")) return ElfDeviceCommand.parse(command);
        return ElfDeviceFile.parse(command);
    }

    public ElfDeviceFile getRoot() {
        return root;
    }

    public ElfDeviceFile getCurrentDir() {
        return currentDir;
    }

    public List<ElfDeviceFile> getDirectories() {
        List<ElfDeviceFile> dirs = new LinkedList<>();
        dirs.add(root);
        dirs.addAll(root.findAllRecursively(ElfDeviceFile::isDirectory));
        return dirs;
    }

    public long freeUpSpace(long requiredSpace) {
        List<ElfDeviceFile> allDirs = getDirectories();
        long free = size - root.getTotalSize();
        allDirs.sort(Comparator.comparingLong(ElfDeviceFile::getTotalSize));
        for (ElfDeviceFile dir : allDirs) {
            if (free + dir.getTotalSize() >= requiredSpace) return dir.getTotalSize();
        }
        throw new IllegalStateException("Cannot free up space");
    }

    public void changeDir(final String dir) {
        if ("..".equals(dir)) {
            if (currentDir.getName().equals(root.getName())) throw new IllegalArgumentException("Already in root.");
            currentDir = currentDir.getParent();
        } else if (currentDir.getName().equals(dir)) {
            // No change required.
        } else {
            currentDir = currentDir.getChildren().stream()
                    .filter(ElfDeviceFile::isDirectory)
                    .filter(child -> dir.equals(child.getName()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("cd: " + dir + ": No such directory."));
        }
    }

    public void addFile(final ElfDeviceFile file) {
        currentDir.addChild(file);
    }
}
