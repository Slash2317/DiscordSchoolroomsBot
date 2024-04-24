package com.slash.schoolroomsbot;

import com.slash.schoolroomsbot.handler.*;
import com.slash.schoolroomsbot.request.RequestContext;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SchoolroomsBotListener extends ListenerAdapter {

    private LinkRequestHandler linkRequestHandler;
    private WikiRequestHandler wikiRequestHandler;
    private InfoRequestHandler infoRequestHandler;
    private ModerationRequestHandler moderationRequestHandler;
    private MiscRequestHandler miscRequestHandler;

    public SchoolroomsBotListener() {
        this.linkRequestHandler = new LinkRequestHandler();
        this.wikiRequestHandler = new WikiRequestHandler();
        this.infoRequestHandler = new InfoRequestHandler();
        this.moderationRequestHandler = new ModerationRequestHandler();
        this.miscRequestHandler = new MiscRequestHandler();
    }

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
            case LEVEL, ENTITY, OBJECT, CANON, TEMPLATE, USER, PERSON_OF_INTEREST, PAGE -> linkRequestHandler.handleLinkCommand(requestContext);
            case MAIN -> wikiRequestHandler.handleMainCommand(requestContext);
            case USEFUL -> wikiRequestHandler.handleUsefulCommand(requestContext);
            case RANDOM -> wikiRequestHandler.handleRandomCommand(requestContext);
            case MUTE -> moderationRequestHandler.handleMuteCommand(requestContext);
            case UN_MUTE -> moderationRequestHandler.handleUnMuteCommand(requestContext);
            case KICK -> moderationRequestHandler.handleKickCommand(requestContext);
            case BAN -> moderationRequestHandler.handleBanCommand(requestContext);
            case UNBAN -> moderationRequestHandler.handleUnbanCommand(requestContext);
            case ROLE_GIVE -> moderationRequestHandler.handleRoleGiveCommand(requestContext);
            case ROLE_REMOVE -> moderationRequestHandler.handleRoleRemoveCommand(requestContext);
            case MEMBER_INFO -> infoRequestHandler.handleMemberInfoCommand(requestContext);
            case SERVER_INFO -> infoRequestHandler.handleServerInfoCommand(requestContext);
            case ECHO -> miscRequestHandler.handleEchoCommand(requestContext);
            case HELP -> miscRequestHandler.handleHelpCommand(requestContext);
        }
    }
}
