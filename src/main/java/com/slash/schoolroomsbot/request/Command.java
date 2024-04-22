package com.slash.schoolroomsbot.request;

import java.util.Collections;
import java.util.List;

import static com.slash.schoolroomsbot.request.CommandGroup.*;

public enum Command {

    LEVEL("school L", LINKING, "Links a level (e.g. school L1)", 3),
    ENTITY("school E", LINKING, "Links an entity (e.g. school E1)", 3),
    OBJECT("school O", LINKING, "Links an object (e.g. school O1)", 3),
    CANON("school C", LINKING, "Links a canon (e.g. school CSectors Canon Hub)", 5),
    TEMPLATE("school T", LINKING, "links a template (e.g. school TBlurimage)", 5),
    MAIN("school main", WIKI, "Gets the link to the main page"),
    USEFUL("school useful", WIKI, "Gets the starter links"),
    RANDOM("school random", WIKI, "Gets a random page in the wiki"),
    MUTE("school mute", MODERATION, "Mutes a user (seconds)", 0, List.of("user", "duration")),
    UN_MUTE("school unmute", MODERATION, "Un-mutes a user", 0, List.of("user")),
    KICK("school kick", MODERATION, "Kicks a user", 0, List.of("user", "reason")),
    BAN("school ban", MODERATION, "Bans a user", 0, List.of("user", "reason")),
    UNBAN("school unban", MODERATION, "Unbans a user", 0, List.of("user")),
    ROLE_GIVE("school rolegive", MODERATION, "Adds a role to a user", 0, List.of("user", "role")),
    ROLE_REMOVE("school roleremove", MODERATION, "Removes a role from a user", 0, List.of("user", "role")),
    MEMBER_INFO("school member info", SERVER, "Provides information about a certain user", 0, List.of("user")),
    SERVER_INFO("school server info", SERVER, "Provides server information"),
    ECHO("school echo", FUN, "Says the requested content", 0, List.of("message")),
    HELP("school help", MISC, "Shows list of commands");

    private final String commandName;
    private final String description;
    private final List<String> parameters;
    private final int suffixLength;
    private final CommandGroup commandGroup;

    Command(String commandName, CommandGroup commandGroup, String description, int suffixLength, List<String> parameters) {
        this.commandName = commandName;
        this.commandGroup = commandGroup;
        this.description = description;
        this.parameters = parameters;
        this.suffixLength = suffixLength;
    }

    Command(String commandName, CommandGroup commandGroup, String description, int suffixLength) {
        this(commandName, commandGroup, description, suffixLength, Collections.emptyList());
    }

    Command(String commandName, CommandGroup commandGroup, String description) {
        this(commandName, commandGroup, description, 0, Collections.emptyList());
    }

    public String getCommandName() {
        return commandName;
    }

    public CommandGroup getCommandGroup() {
        return commandGroup;
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

    public String getCommandFormat() {
        StringBuilder sb = new StringBuilder(commandName);

        if (suffixLength > 0) {
            sb.append("#".repeat(suffixLength));
        }
        if (!parameters.isEmpty()) {
            sb.append(" [" + String.join("] [", parameters) + "]");
        }
        return sb.toString();
    }

    public String getFullDescription() {
        return getCommandFormat() + " | " + description;
    }
}
