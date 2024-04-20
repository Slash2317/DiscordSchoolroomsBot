package com.slash.schoolroomsbot.request.link;

import com.slash.schoolroomsbot.request.Command;

public enum LinkCommand {

    LEVEL(Command.LEVEL, "Level"),
    ENTITY(Command.ENTITY, "Entity"),
    OBJECT(Command.OBJECT, "Object"),
    CANON(Command.CANON, "Canon"),
    TEMPLATE(Command.TEMPLATE, "Template");

    private final Command command;
    private final String linkPrefix;

    LinkCommand(Command command, String linkPrefix) {
        this.command = command;
        this.linkPrefix = linkPrefix;
    }

    public Command getCommand() {
        return command;
    }

    public String getLinkPrefix() {
        return linkPrefix;
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
