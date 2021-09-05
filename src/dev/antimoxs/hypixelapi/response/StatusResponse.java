package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.status.Session;

public class StatusResponse {

    public boolean success = false;
    public String uuid = "No data for uuid";
    private Session session = null;
    public String cause = "";

    public StatusResponse() {



    }

    public Session getSession() {

        return session == null ? new Session() : session;

    }


}
