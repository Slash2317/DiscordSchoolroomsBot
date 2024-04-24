package com.slash.schoolroomsbot.request.link;

import com.slash.schoolroomsbot.request.Command;

public enum LinkCommand {

    LEVEL(Command.LEVEL, "Level_", true),
    ENTITY(Command.ENTITY, "Entity_", true),
    OBJECT(Command.OBJECT, "Object_", true),
    CANON(Command.CANON, "", false),
    TEMPLATE(Command.TEMPLATE, "Template:", false),
    USER(Command.USER, "User:", false),
    PERSON_OF_INTEREST(Command.PERSON_OF_INTEREST, "", false),
    PAGE(Command.PAGE, "", false);

    private final Command command;
    private final String linkPrefix;
    private final boolean usesId;

    LinkCommand(Command command, String linkPrefix, boolean usesId) {
        this.command = command;
        this.linkPrefix = linkPrefix;
        this.usesId = usesId;
    }

    public Command getCommand() {
        return command;
    }

    public String getLinkPrefix() {
        return linkPrefix;
    }

    public boolean isUsesId() {
        return usesId;
    }

    public static LinkCommand getByCommand(Command command) {
        for (LinkCommand linkCommand : values()) {
            if (linkCommand.getCommand() == command) {
                return linkCommand;
            }
        }
        throw new IllegalArgumentException("No link command found for command " + command);
    }
}
