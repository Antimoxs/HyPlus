package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.status.Session;

public class StatusResponse extends BaseResponse {

    private Session session = null;

    public StatusResponse() {



    }

    public Session getSession() {

        return session == null ? new Session() : session;

    }


}
