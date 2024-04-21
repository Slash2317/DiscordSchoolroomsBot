package com.slash.schoolroomsbot.request;

import java.util.Collections;
import java.util.List;

public enum Command {

    LEVEL("school L", "links a level (example: school L1)", 3),
    ENTITY("school E", "links an entity (example: school E1)", 3),
    OBJECT("school O", "links an object (example: school O1)", 3),
    CANON("school C", "links a canon (example: school CSectors Canon Hub)", 3),
    TEMPLATE("school T", "links a template (example: school TBlurimage)", 3),
    MAIN("school main", "Gets the link to the main page"),
    USEFUL("school useful", "Gets the starter links"),
    RANDOM("school random", "Gets a random page in the wiki"),
    MUTE("school mute", "Mutes a user", 0, List.of("user", "duration")),
    UN_MUTE("school unmute", "Un-mutes a user", 0, List.of("user")),
    KICK("school kick", "Kicks a user", 0, List.of("user", "reason")),
    BAN("school ban", "Bans a user", 0, List.of("user", "reason")),
    ROLE_GIVE("school rolegive", "Gives a role to a user", 0, List.of("user", "role")),
    ROLE_REMOVE("school roleremove", "Removes a role from an user", 0, List.of("user", "role")),
    MEMBER_INFO("school member info", "provides information about a certain user", 0, List.of("user")),
    SERVER_INFO("school server info", "provides server information"),
    STAFF("school staff", "gets the full list of current server staff"),
    ECHO("school echo", "says the requested content", 0, List.of("message")),
    HELP("school help", "shows list of cmds");

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
