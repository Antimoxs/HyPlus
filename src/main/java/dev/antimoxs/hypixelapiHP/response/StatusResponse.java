package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.status.Session;

public class StatusResponse extends BaseResponse {

    private Session session = null;

    public StatusResponse() {



    }

    public Session getSession() {

        return session == null ? new Session() : session;

    }


}
