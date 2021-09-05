package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.games.Game;

public class GamesResponse {

    public boolean success = false;
    public String uuid = "";
    public Game[] games = new Game[0];
    public String cause = "";

}
