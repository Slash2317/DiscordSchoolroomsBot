package com.slash.schoolroomsbot.handler;

import com.slash.schoolroomsbot.request.Command;
import com.slash.schoolroomsbot.request.CommandGroup;
import com.slash.schoolroomsbot.request.RequestContext;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiscRequestHandler {

    public void handleHelpCommand(RequestContext requestContext) {
        Map<CommandGroup, List<Command>> groupToCommands = Arrays.stream(Command.values()).collect(Collectors.groupingBy(Command::getCommandGroup));

        StringBuilder sb = new StringBuilder();
        sb.append("""
                **SCHOOLROOMS | COMMANDS**
                ------------------------------------------------""");

        for (CommandGroup group : CommandGroup.values()) {
            sb.append("\n\n" + group.getTitle() + "\n");
            sb.append(groupToCommands.get(group).stream().map(c -> c.getFullDescription()).collect(Collectors.joining("\n")));
        }

        requestContext.event().getChannel().sendMessage(sb.toString()).queue();
    }

    public void handleEchoCommand(RequestContext requestContext) {
        if (requestContext.arguments() != null && !requestContext.arguments().isBlank()) {
            requestContext.event().getChannel().sendMessage(requestContext.arguments()).queue();
        }
    }
}
