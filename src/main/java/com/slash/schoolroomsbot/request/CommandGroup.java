package com.slash.schoolroomsbot.request;

public enum CommandGroup {

    LINKING(":link: **LINKING**"),
    WIKI(":school: **WIKI**"),
    MODERATION(":tools: **MODERATION & UTILITY**"),
    SERVER(":adult: **SERVER & ACCOUNT**"),
    FUN(":smile: **FUN**"),
    MISC(":question: **MISC**");

    private final String title;

    CommandGroup(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
