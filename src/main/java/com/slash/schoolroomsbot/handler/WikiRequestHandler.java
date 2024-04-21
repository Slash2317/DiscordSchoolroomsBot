package com.slash.schoolroomsbot.handler;

import com.slash.schoolroomsbot.request.RequestContext;

public class WikiRequestHandler {

    public void handleMainCommand(RequestContext requestContext) {
        requestContext.event().getChannel().sendMessage("""
                    **Here is the link to our wiki main page!** :school:\n
                    Our main page let's you know what our wiki is based about, our content and much more!
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Main>""").queue();
    }

    public void handleUsefulCommand(RequestContext requestContext) {
        requestContext.event().getChannel().sendMessage("""
                    **Here are the most useful links!** :school:\n
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Rules> | Local wiki rules applying to all users/pages/threads on the wiki.
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Staff> | Full list of our wiki staff.
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Guides> | Full list of our wiki guides, teaching you how to write, wikitext and more!
                    <https://the-schoolrooms.fandom.com/wiki/The_Schoolrooms_Wiki:Main> | Our wiki main page to let you know what our wiki is based on and our content, etc.""").queue();
    }

    public void handleRandomCommand(RequestContext requestContext) {
        requestContext.event().getChannel().sendMessage("https://the-schoolrooms.fandom.com/wiki/Special:Random").queue();
    }
}
