package com.slash.schoolroomsbot;

import com.slash.schoolroomsbot.request.Command;
import com.slash.schoolroomsbot.request.RequestContext;
import com.slash.schoolroomsbot.request.link.LinkRequestContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SchoolroomsBotListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        RequestContext requestContext = RequestContext.from(event);

        if (requestContext.command() == null) {
            return;
        }

        switch (requestContext.command()) {
            case HELP -> handleHelpCommand(requestContext);
            case LEVEL, ENTITY, OBJECT, CANON, TEMPLATE -> handleLinkCommand(requestContext);
            case MEMBER_INFO -> handleMemberInfoCommand(requestContext);
//            case TIZILAND -> event.getChannel().sendMessage("""
//                    The only links to join Tiziland and invite people are:
//                    :link: Discord Invite Link: https://discord.gg/9XTkWVbycs
//                    :link: Bit.ly Invite Link: http://bit.ly/tiziland""").queue();
//            case RULES -> event.getChannel().sendMessage(":scroll: You can read our rules here: https://discord.com/channels/1108179404137447484/1108181346033094736").queue();
//            case TIZIPAGES -> event.getChannel().sendMessage("""
//                    Here are all the pages of Tizi:
//                    -bit.ly/tiziabout
//                    -bit.ly/tizisocial
//                    -bit.ly/tizi-links""").queue();
            case STAFF -> event.getChannel().sendMessage("""
                    :crown: Owner: Tizi!! (tiziandfrodo)
                    :tools: Community Manager: Xanth (._.xanth._.)
                    :sparkles: Admins: N/A
                    :star2: Mods: Nathan (natxer43) | TireCuzWhyNot (tirecuzwhynot) | Ajax (arsunal)""").queue();
//            case EVENT_START -> handleEventStartCommand(requestContext);
//            case EVENT_OVER -> handleEventOverCommand(requestContext);
            case ECHO -> handleEchoCommand(requestContext);
        }
    }

    private void handleHelpCommand(RequestContext requestContext) {
        List<String> regularCommandDisplays = new ArrayList<>();
        List<String> staffCommandDisplays = new ArrayList<>();
        for (Command command : Command.values()) {
            StringBuilder sb = new StringBuilder();
            sb.append(command.getCommandName());
            if (command.isPrefix()) {
                sb.append("#".repeat(command.getSuffixLength()));
            }

            if (!command.getParameters().isEmpty()) {
                sb.append(" [" + String.join(", ", command.getParameters()) + "]");
            }
            sb.append(" (" + command.getDescription() + ")");

//            if (Command.STAFF_ONLY_COMMANDS.contains(command)) {
//                staffCommandDisplays.add(sb.toString());
//            }
//            else {
                regularCommandDisplays.add(sb.toString());
//            }
        }

        requestContext.event().getChannel().sendMessage(String.join("\n", regularCommandDisplays)).queue();
    }

    private void handleMemberInfoCommand(RequestContext requestContext) {
        List<Member> mentions = requestContext.event().getMessage().getMentions().getMembers();

        for (Member member : mentions) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x0099FF)
//                    .setTitle(member.getEffectiveName())
//                    .setUrl("https://discord.js.org/")
                    .setAuthor(member.getUser().getGlobalName(), null, member.getEffectiveAvatarUrl())
                    .addField("Discord name", member.getUser().getName(), false);
//                    .setDescription(member.getFlags().)
//                    .setThumbnail(requestContext.event().getGuild().getIconUrl());

            if (member.getNickname() != null) {
                embedBuilder.addField("Nickname", member.getNickname(), false);
            }

            String rolesString;
            if (member.getRoles().size() > 4) {
                rolesString = getRolesDisplayString(member.getRoles().subList(0, 4)) + "\n... " + (member.getRoles().size() - 4) + " more";
            }
            else {
                rolesString = getRolesDisplayString(member.getRoles());
            }

            MessageEmbed embed = embedBuilder.addField("Roles", rolesString, true)
                    .addField("Created at", String.join("\n", getAllTimeDisplays(member.getTimeCreated())), true)
                    .addField("Joined at", String.join("\n", getAllTimeDisplays(member.getTimeJoined())), true)
                    .setImage(member.getEffectiveAvatarUrl())
                    .setTimestamp(member.getTimeCreated())
                    .setFooter("ID: " + member.getId(), member.getEffectiveAvatarUrl())
                    .build();
            requestContext.event().getChannel().sendMessageEmbeds(embed).setAllowedMentions(Collections.emptyList()).queue();
        }
    }

    private String getRolesDisplayString(List<Role> roles) {
        return roles.stream().map(r -> "<@&" + r.getId() + ">").collect(Collectors.joining("\n"));
    }

    private List<String> getAllTimeDisplays(OffsetDateTime offsetDateTime) {
        return List.of(
                getTimeDisplay(offsetDateTime, "d"),
                getTimeDisplay(offsetDateTime, "f"),
                getTimeDisplay(offsetDateTime, "D"),
                getTimeDisplay(offsetDateTime, "R")
        );
    }

    private String getTimeDisplay(OffsetDateTime offsetDateTime, String format) {
        return "<t:" + offsetDateTime.toEpochSecond()+ ":" + format + ">";
    }

//    private void handleEventStartCommand(RequestContext requestContext) {
//        if (isStaff(requestContext.event())) {
//            requestContext.event().getChannel().sendMessage("The event just started, come join and participate! :star2:").queue();
//        }
//    }
//
//    private void handleEventOverCommand(RequestContext requestContext) {
//        if (isStaff(requestContext.event())) {
//            requestContext.event().getChannel().sendMessage("This event has been closed. Thanks for participating! :heart:").queue();
//        }
//    }

    private void handleLinkCommand(RequestContext requestContext) {
        try {
            LinkRequestContext linkRequestContext = LinkRequestContext.from(requestContext);
            int id = Integer.parseInt(linkRequestContext.requestContext().arguments().split(" ")[0]);

            String url = "https://the-schoolrooms.fandom.com/wiki/" + linkRequestContext.linkCommand().getLinkPrefix() + "_" + id;
            requestContext.event().getChannel().sendMessage(url).queue();
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandName() + "###`").queue();
        }
    }

    private void handleEchoCommand(RequestContext requestContext) {
        if (requestContext.arguments() != null && !requestContext.arguments().isBlank()) {
            requestContext.event().getChannel().sendMessage(requestContext.arguments()).queue();
        }
    }

    private static boolean isStaff(MessageReceivedEvent event) {
        if (event.getMember() == null) {
            return false;
        }
        return event.getMember().getRoles().stream().anyMatch(r -> r.getName().contains("Staff"));
    }
}
