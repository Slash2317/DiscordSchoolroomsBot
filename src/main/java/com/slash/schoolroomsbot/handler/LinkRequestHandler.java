package com.slash.schoolroomsbot.handler;

import com.slash.schoolroomsbot.request.RequestContext;
import com.slash.schoolroomsbot.request.link.LinkRequestContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LinkRequestHandler {

    public void handleLinkCommand(RequestContext requestContext) {
        try {
            if (requestContext.arguments() == null || requestContext.arguments().isBlank()) {
                throw new IllegalArgumentException("No arguments supplied");
            }

            LinkRequestContext linkRequestContext = LinkRequestContext.from(requestContext);

            String suffix;

            if (linkRequestContext.linkCommand().isUsesId()) {
                suffix = String.valueOf(Integer.valueOf(linkRequestContext.requestContext().arguments().split(" ")[0]));
            }
            else {
                suffix = toPascalCamelCase(linkRequestContext.requestContext().arguments());
            }

            String url = "https://the-schoolrooms.fandom.com/wiki/" + linkRequestContext.linkCommand().getLinkPrefix() + suffix;
            requestContext.event().getChannel().sendMessage(url).queue();
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandFormat() + "`").queue();
        }
    }

    private String toPascalCamelCase(String text) {
        if (text.isBlank()) {
            return text;
        }

        List<String> words = Arrays.stream(text.split(" ")).flatMap(s -> Arrays.stream(s.split("_"))).toList();

        return words.stream().map(w -> {
            if (w.equalsIgnoreCase("of")) {
                return w;
            }
            char firstChar = w.charAt(0);
            return Character.toString(firstChar).toUpperCase() + w.substring(1);
        }).collect(Collectors.joining("_"));
    }
}
