package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.resource.ResourceGame;

import java.util.HashMap;

public class GamelistResponse extends BaseResponse {

    private HashMap<String, ResourceGame> games = null;
    public long lastUpdated = 0;

    public HashMap<String, ResourceGame> getGames() {

        return games == null ? new HashMap<>() : games;

    }

    public ResourceGame getGame(String game) {

        if (getGames().containsKey(game)) {

            return getGames().get(game);

        }
        return new ResourceGame();

    }

}
