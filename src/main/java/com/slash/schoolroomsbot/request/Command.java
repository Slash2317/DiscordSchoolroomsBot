package com.slash.schoolroomsbot.request;

import java.util.Collections;
import java.util.List;

public enum Command {

    HELP("school help", "shows list of cmds"),
    LEVEL("school L", "links a level", 3),
    ENTITY("school E", "links an entity", 3),
    OBJECT("school O", "links an object", 3),
    CANON("school C", "links a canon", 3),
    TEMPLATE("school T", "links a template", 3),
    MEMBER_INFO("school member info", "provides information about a certain user", 0, List.of("user")),
    STAFF("school staff", "gets the full list of current server staff"),
    ECHO("school echo", "says the requested content", 0, List.of("message"));

//    public static final EnumSet<Command> STAFF_ONLY_COMMANDS = EnumSet.of(EVENT_START, EVENT_OVER);

    private final String commandName;
    private final String description;
    private final List<String> parameters;
    private final int suffixLength;

    Command(String commandName, String description, int suffixLength, List<String> parameters) {
        this.commandName = commandName;
        this.description = description;
        this.parameters = parameters;
        this.suffixLength = suffixLength;
    }

    Command(String commandName, String description, int suffixLength) {
        this(commandName, description, suffixLength, Collections.emptyList());
    }

    Command(String commandName, String description) {
        this(commandName, description, 0, Collections.emptyList());
    }

    public String getCommandName() {
        return commandName;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public int getSuffixLength() {
        return suffixLength;
    }

    public boolean isPrefix() {
        return suffixLength > 0;
    }

    public static Command getCommand(String message) {
        for (Command command : Command.values()) {
            if (message.equals(command.commandName) ||
                    message.startsWith(command.commandName + " ") ||
                    (command.isPrefix() && message.startsWith(command.commandName))) {
                return command;
            }
        }
        return null;
    }
}
