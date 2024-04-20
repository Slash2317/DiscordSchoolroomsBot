package com.slash.schoolroomsbot.request.link;

import com.slash.schoolroomsbot.request.RequestContext;

public record LinkRequestContext(LinkCommand linkCommand, RequestContext requestContext) {

    public static LinkRequestContext from(RequestContext requestContext) {
        return new LinkRequestContext(LinkCommand.getByCommand(requestContext.command()), requestContext);
    }
}
