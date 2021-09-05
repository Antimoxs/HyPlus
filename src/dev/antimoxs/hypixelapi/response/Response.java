package dev.antimoxs.hypixelapi.response;

public class Response {

    public GuildResponse guild = new GuildResponse();
    public StatusResponse status = new StatusResponse();
    public FriendsResponse friends = new FriendsResponse();
    public PlayerResponse player = new PlayerResponse();
    public KeyResponse key = new KeyResponse();
    public GamesResponse games = new GamesResponse();
    public QuestsResponse quests = new QuestsResponse();


    public void setGuild(GuildResponse response) {

        guild = response;

    }

    public void setStatus(StatusResponse response) {

        status = response;

    }

    public void setFriends(FriendsResponse response) {

        friends = response;

    }

    public void setPlayer(PlayerResponse response) {

        player = response;

    }

    public void setQuests(QuestsResponse response) {

        quests = response;

    }

}
