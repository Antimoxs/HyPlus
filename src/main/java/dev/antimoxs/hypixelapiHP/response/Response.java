package dev.antimoxs.hypixelapiHP.response;

public class Response {

    public dev.antimoxs.hypixelapiHP.response.GuildResponse guild = new dev.antimoxs.hypixelapiHP.response.GuildResponse();
    public dev.antimoxs.hypixelapiHP.response.StatusResponse status = new dev.antimoxs.hypixelapiHP.response.StatusResponse();
    public dev.antimoxs.hypixelapiHP.response.FriendsResponse friends = new dev.antimoxs.hypixelapiHP.response.FriendsResponse();
    public dev.antimoxs.hypixelapiHP.response.PlayerResponse player = new dev.antimoxs.hypixelapiHP.response.PlayerResponse();
    public KeyResponse key = new KeyResponse();
    public dev.antimoxs.hypixelapiHP.response.GamesResponse games = new GamesResponse();
    public dev.antimoxs.hypixelapiHP.response.QuestsResponse quests = new dev.antimoxs.hypixelapiHP.response.QuestsResponse();
    public dev.antimoxs.hypixelapiHP.response.GamelistResponse gamelist = new dev.antimoxs.hypixelapiHP.response.GamelistResponse();


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

    public void setGamelist(GamelistResponse response) {

        gamelist = response;

    }

}
