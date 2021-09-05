package dev.antimoxs.hypixelapi.requests;

public enum RequestType {



    GUILD_NAME("guild"),
    GUILD_UUID("guild"),
    PLAYER_NAME("player"),
    PLAYER_UUID("player"),
    KEY("key"),
    STATUS("status"),
    FRIENDS("friends"),
    QUESTS("resources/quests");

    private String URLKey;

    RequestType(String URLkey) {

        this.URLKey = URLkey;

    }

    String getURLKey() {

        return this.URLKey;

    }

}


