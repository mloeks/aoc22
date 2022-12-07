package net.mloeks.aoc22.elfdevice;

public class ElfDeviceCommand {

    private final String command;
    private final String arg;

    private ElfDeviceCommand(final String command, final String arg) {
        this.command = command;
        this.arg = arg;
    }

    public static ElfDeviceCommand parse(final String commandLineInput) {
        if (!commandLineInput.startsWith("$")) throw new IllegalArgumentException("Not a valid command: " + commandLineInput);

        String[] commandPlusArg = commandLineInput.replaceFirst("\\$", "").trim().split(" ");
        return new ElfDeviceCommand(commandPlusArg[0], commandPlusArg.length > 1 ? commandPlusArg[1] : null);
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }
}
