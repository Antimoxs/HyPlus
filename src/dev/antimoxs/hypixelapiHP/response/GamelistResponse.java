package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.resource.ResourceGame;
import dev.antimoxs.hypixelapiHP.util.hypixelFetcher;

import java.util.HashMap;

public class GamelistResponse extends BaseResponse {

    private HashMap<String, ResourceGame> games = null;
    public long lastUpdated = 0;

    public HashMap<String, ResourceGame> getGames() {

        return games == null ? new HashMap<>() : games;

    }

    public ResourceGame getGame(String game) {

        if (getGames().containsKey(game.toUpperCase())) {

            return getGames().get(game.toUpperCase());

        }
        return new ResourceGame(hypixelFetcher.fetchGame(game));

    }

}
