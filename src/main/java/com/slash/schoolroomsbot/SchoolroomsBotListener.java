package com.slash.schoolroomsbot;

import com.slash.schoolroomsbot.request.Command;
import com.slash.schoolroomsbot.request.RequestContext;
import com.slash.schoolroomsbot.request.link.LinkRequestContext;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.time.temporal.ChronoUnit;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
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
            case RANDOM -> event.getChannel().sendMessage("https://the-schoolrooms.fandom.com/wiki/Special:Random").queue();
            case MUTE -> handleMuteCommand(requestContext);
            case ROLE_GIVE -> handleRoleGiveCommand(requestContext);
            case ROLE_REMOVE -> handleRoleRemoveCommand(requestContext);
            case MAIN -> event.getChannel().sendMessage("""
                    **Here is the link to our wiki main page!** :school:\n
                    Our main page let's you know what our wiki is based about, our content and much more!
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Main>""").queue();
            case USEFUL -> event.getChannel().sendMessage("""
                    **Here are the most useful links!** :school:\n
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Rules> | Local wiki rules applying to all users/pages/threads on the wiki.
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Staff> | Full list of our wiki staff.
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Guides> | Full list of our wiki guides, teaching you how to write, wikitext and more!
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Main> | Our wiki main page to let you know what our wiki is based on and our content, etc.""").queue();
//            case RULES -> event.getChannel().sendMessage(":scroll: You can read our rules here: https://discord.com/channels/1108179404137447484/1108181346033094736").queue();
            case MEMBER_INFO -> handleMemberInfoCommand(requestContext);
            case SERVER_INFO -> handleServerInfoCommand(requestContext);
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
//            case STAFF -> event.getChannel().sendMessage("""
//                    :crown: Owner: Tizi!! (tiziandfrodo)
//                    :tools: Community Manager: Xanth (._.xanth._.)
//                    :sparkles: Admins: N/A
//                    :star2: Mods: Nathan (natxer43) | TireCuzWhyNot (tirecuzwhynot) | Ajax (arsunal)""").queue();
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
                sb.append(" [" + String.join("] [ ", command.getParameters()) + "]");
            }
            sb.append(" (" + command.getDescription() + ")");

//            if (Command.STAFF_ONLY_COMMANDS.contains(command)) {
//                staffCommandDisplays.add(sb.toString());
//            }
//            else {
                regularCommandDisplays.add(sb.toString());
//            }
        }

        requestContext.event().getChannel().sendMessage("**COMMANDS**\n\n" + String.join("\n", regularCommandDisplays)).queue();
    }

    private void handleMuteCommand(RequestContext requestContext) {
        try {
            if (requestContext.arguments() == null) {
                throw new IllegalArgumentException("No arguments supplied");
            }

            int firstSpaceIndex = requestContext.arguments().indexOf(" ");

            if (firstSpaceIndex == -1) {
                throw new IllegalArgumentException("Only 1 argument supplied");
            }

            String username = requestContext.arguments().substring(0, firstSpaceIndex);
            int duration = Integer.parseInt(requestContext.arguments().substring(firstSpaceIndex + 1).trim());

            requestContext.event().getGuild().findMembers(m -> username.equals(m.getUser().getName().toLowerCase())).onSuccess(members -> {
                if (members.isEmpty()) {
                    return;
                }

                for (Member member : members) {
                    requestContext.event().getGuild().timeoutFor(member, Duration.of(duration, ChronoUnit.SECONDS)).queue();
                }
            });
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandName() + " " + " [" + String.join("] [ ", requestContext.command().getParameters()) + "]" + "`").queue();
        }
        catch (Exception e) {

        }
    }

    private void handleRoleGiveCommand(RequestContext requestContext) {
        try {
            if (requestContext.arguments() == null) {
                throw new IllegalArgumentException("No arguments supplied");
            }

            int firstSpaceIndex = requestContext.arguments().indexOf(" ");

            if (firstSpaceIndex == -1) {
                throw new IllegalArgumentException("Only 1 argument supplied");
            }

            String username = requestContext.arguments().substring(0, firstSpaceIndex);
            String roleName = requestContext.arguments().substring(firstSpaceIndex + 1).trim();

            List<Role> roles = requestContext.event().getGuild().getRolesByName(roleName, true);

            if (roles.isEmpty()) {
                return;
            }

            requestContext.event().getGuild().findMembers(m -> username.equals(m.getUser().getName().toLowerCase())).onSuccess(members -> {
                if (members.isEmpty()) {
                    return;
                }

                for (Member member : members) {
                    for (Role role : roles) {
                        requestContext.event().getGuild().addRoleToMember(member, role).queue();
                    }
                }
            });
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandName() + " " + " [" + String.join("] [ ", requestContext.command().getParameters()) + "]" + "`").queue();
        }
    }

    private void handleRoleRemoveCommand(RequestContext requestContext) {
        try {
            if (requestContext.arguments() == null) {
                throw new IllegalArgumentException("No arguments supplied");
            }

            int firstSpaceIndex = requestContext.arguments().indexOf(" ");

            if (firstSpaceIndex == -1) {
                throw new IllegalArgumentException("Only 1 argument supplied");
            }

            String username = requestContext.arguments().substring(0, firstSpaceIndex);
            String roleName = requestContext.arguments().substring(firstSpaceIndex + 1).trim();

            List<Role> roles = requestContext.event().getGuild().getRolesByName(roleName, true);

            if (roles.isEmpty()) {
                return;
            }

            requestContext.event().getGuild().findMembers(m -> username.equals(m.getUser().getName().toLowerCase())).onSuccess(members -> {
                if (members.isEmpty()) {
                    return;
                }

                for (Member member : members) {
                    for (Role role : roles) {
                        requestContext.event().getGuild().removeRoleFromMember(member, role).queue();
                    }
                }
            });
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandName() + " " + " [" + String.join("] [ ", requestContext.command().getParameters()) + "]" + "`").queue();
        }
    }

    private void handleServerInfoCommand(RequestContext requestContext) {
        Guild guild = requestContext.event().getGuild();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x0099FF)
                    .setTitle(guild.getName());
//                    .setUrl("https://discord.js.org/")
//                .setAuthor(member.getUser().getGlobalName(), null, member.getEffectiveAvatarUrl())
//                .addField("Server name", member.getUser().getName(), false);
//                    .setDescription(guild.getOwner().getUser().getName());
//                    .setThumbnail(requestContext.event().getGuild().getIconUrl());

        if (guild.getOwner() != null) {
            embedBuilder.setDescription("**Owner:** " + guild.getOwner().getEffectiveName() + " (" + guild.getOwner().getUser().getName() + ")");
        }

        MessageEmbed embed = embedBuilder.addField("Member count", String.valueOf(guild.getMemberCount()), true)
                .addField("Created at", getTimeDisplay(guild.getTimeCreated()), true)
                .addBlankField(false)
                .addField("Boost count", String.valueOf(guild.getBoostCount()), true)
                .addField("Boost tier", getBoostTierDisplay(guild.getBoostTier()), true)
                .setImage(guild.getIconUrl())
                .setTimestamp(guild.getTimeCreated())
                .setFooter("ID: " + guild.getId(), guild.getIconUrl())
                .build();
        requestContext.event().getChannel().sendMessageEmbeds(embed).setAllowedMentions(Collections.emptyList()).queue();
    }

    private String getBoostTierDisplay(Guild.BoostTier boostTier) {
        return switch (boostTier) {
            case NONE -> "None";
            case TIER_1 -> "Tier 1";
            case TIER_2 -> "Tier 2";
            case TIER_3 -> "Tier 3";
            case UNKNOWN -> "Unknown";
        };
    }

    private void handleMemberInfoCommand(RequestContext requestContext) {
        List<Member> mentions = requestContext.event().getMessage().getMentions().getMembers();

        if (mentions.isEmpty()) {
            List<String> names = Arrays.stream(requestContext.arguments().split(" ")).map(String::trim).map(String::toLowerCase).collect(Collectors.toList());

            requestContext.event().getGuild().findMembers(m -> names.contains(m.getUser().getName().toLowerCase())).onSuccess(members -> {
                for (Member member : members) {
                    sendMemberInfo(member, requestContext);
                }
            });
        }
        else {
            for (Member member : mentions) {
                sendMemberInfo(member, requestContext);
            }
        }
    }

    private void sendMemberInfo(Member member, RequestContext requestContext) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x0099FF)
//                    .setTitle(member.getEffectiveName())
//                    .setUrl("https://discord.js.org/")
                .setAuthor(member.getUser().getGlobalName(), null, member.getEffectiveAvatarUrl())
                .addField("Discord name", member.getUser().getName(), false);
//                    .setDescription(member.getFlags().)
//                    .setThumbnail(requestContext.event().getGuild().getIconUrl());

        boolean isStaff = member.getRoles().stream().anyMatch(r -> r.getName().toLowerCase().contains("staff"));
        if (isStaff) {
            embedBuilder.setDescription(":white_check_mark:***Staff member***");
        }
        else {
            embedBuilder.setDescription(":x:***Common member***");
        }

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
                .addField("Created at", getTimeDisplay(member.getTimeCreated()), true)
                .addField("Joined at", getTimeDisplay(member.getTimeJoined()), true)
                .setImage(member.getEffectiveAvatarUrl())
                .setTimestamp(member.getTimeCreated())
                .setFooter("ID: " + member.getId(), member.getEffectiveAvatarUrl())
                .build();
        requestContext.event().getChannel().sendMessageEmbeds(embed).setAllowedMentions(Collections.emptyList()).queue();
    }

    private String getRolesDisplayString(List<Role> roles) {
        return roles.stream().map(r -> "<@&" + r.getId() + ">").collect(Collectors.joining("\n"));
    }

    private String getTimeDisplay(OffsetDateTime offsetDateTime) {
        return getFormattedTime(offsetDateTime, "f") + "\n(" + getFormattedTime(offsetDateTime, "R") + ")";
    }

    private String getFormattedTime(OffsetDateTime offsetDateTime, String format) {
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
            if (requestContext.arguments() == null || requestContext.arguments().isBlank()) {
                throw new IllegalArgumentException("No argument provided");
            }

            LinkRequestContext linkRequestContext = LinkRequestContext.from(requestContext);

            String suffix;

            if (linkRequestContext.linkCommand().isUsesId()) {
                suffix = String.valueOf(Integer.valueOf(linkRequestContext.requestContext().arguments().split(" ")[0]));
            }
            else {
                suffix = toCamelCase(linkRequestContext.requestContext().arguments());
            }

            String url = "https://the-schoolrooms.fandom.com/wiki/" + linkRequestContext.linkCommand().getLinkPrefix() + suffix;
            requestContext.event().getChannel().sendMessage(url).queue();
        }
        catch (IllegalArgumentException e) {
            requestContext.event().getChannel().sendMessage("The command must follow this format `" + requestContext.command().getCommandName() + "#".repeat(requestContext.command().getSuffixLength()) + "`").queue();
        }
    }

    private String toCamelCase(String text) {
        if (text.isBlank()) {
            return text;
        }

        List<String> words = Arrays.stream(text.split(" ")).flatMap(s -> Arrays.stream(s.split("_"))).collect(Collectors.toList());

        return words.stream().map(w -> {
            if (w.equalsIgnoreCase("of")) {
                return w;
            }
            Character firstChar = w.charAt(0);
            return firstChar.toString().toUpperCase() + w.substring(1).toLowerCase();
        }).collect(Collectors.joining("_"));
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
