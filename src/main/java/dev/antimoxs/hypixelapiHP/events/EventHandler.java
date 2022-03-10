package dev.antimoxs.hypixelapiHP.events;

import dev.antimoxs.hypixelapiHP.HypixelApi;
import dev.antimoxs.hypixelapiHP.requests.RequestType;
import dev.antimoxs.hypixelapiHP.response.Response;

import java.util.ArrayList;

public class EventHandler {

    private final HypixelApi hypixelApi;
    private ArrayList<dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent> events = new ArrayList<>();

    public EventHandler(HypixelApi hypixelApi) {

        this.hypixelApi = hypixelApi;

    }

    public void registerEvent(dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event) {

        synchronized (events) {
            this.events.add(event);
        }

    }


    public void callApiResponseEvent(dev.antimoxs.hypixelapiHP.response.ApiResponse response) {

        for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

            event.onApiResponse(response);

        }

        new Thread(() -> {

            for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

                event.onAsyncApiResponse(response);

            }

        }).start();


    }

    public void callApiRequestErrorEvent(String errorMessage) {

        for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

            event.onApiRequestError(errorMessage);

        }

        new Thread(() -> {

            for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

                event.onAsyncApiRequestError(errorMessage);

            }

        }).start();

    }

    public void callApiParseErrorEvent(String errorMessage, dev.antimoxs.hypixelapiHP.response.ApiResponse apiResponse) {

        for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

            event.onApiParseError(errorMessage, apiResponse);

        }

        new Thread(() -> {

            for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

                event.onAsyncApiParseError(errorMessage, apiResponse);

            }

        }).start();

    }

    public void callApiRequestNotSuccessfulEvent(dev.antimoxs.hypixelapiHP.response.BaseResponse response) {

        for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

            event.onApiRequestNotSuccessful(response);

        }

        new Thread(() -> {

            for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

                event.onAsyncApiRequestNotSuccessful(response);

            }

        }).start();

    }

    public void callApiResponseTypeEvent(RequestType type, Response response) {

        for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : events) {

            switch (type) {

                case KEY: {event.onApiKeyResponse(response.key); break;}
                case GUILD_UUID:
                case GUILD_NAME: {event.onApiGuildResponse(response.guild); break;}
                case STATUS: {event.onApiStatusResponse(response.status); break;}
                case FRIENDS: {event.onApiFriendsResponse(response.friends); break;}
                case PLAYER_UUID:
                case PLAYER_NAME: {event.onApiPlayerResponse(response.player); break;}
                case QUESTS: {event.onApiQuestsResponse(response.quests); break;}
                case GAMES: {event.onApiGamesResponse(response.games); break;}
                default: {}

            }

        }

        new Thread(() -> {

            for (IHypixelApiEvent event : events) {

                switch (type) {

                    case KEY: {event.onAsyncApiKeyResponse(response.key); break;}
                    case GUILD_UUID:
                    case GUILD_NAME: {event.onAsyncApiGuildResponse(response.guild); break;}
                    case STATUS: {event.onAsyncApiStatusResponse(response.status); break;}
                    case FRIENDS: {event.onAsyncApiFriendsResponse(response.friends); break;}
                    case PLAYER_UUID:
                    case PLAYER_NAME: {event.onAsyncApiPlayerResponse(response.player); break;}
                    case QUESTS: {event.onAsyncApiQuestsResponse(response.quests); break;}
                    case GAMES: {event.onAsyncApiGamesResponse(response.games); break;}
                    default: {}

                }

            }

        }).start();

    }

}
