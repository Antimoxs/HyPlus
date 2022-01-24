package dev.antimoxs.hypixelapi.events;

import dev.antimoxs.hypixelapi.HypixelApi;
import dev.antimoxs.hypixelapi.requests.RequestType;
import dev.antimoxs.hypixelapi.response.*;

import java.util.ArrayList;

public class EventHandler {

    private final HypixelApi hypixelApi;
    private ArrayList<IHypixelApiEvent> events = new ArrayList<>();

    public EventHandler(HypixelApi hypixelApi) {

        this.hypixelApi = hypixelApi;

    }

    public void registerEvent(IHypixelApiEvent event) {

        synchronized (events) {
            this.events.add(event);
        }

    }


    public void callApiResponseEvent(ApiResponse response) {

        for (IHypixelApiEvent event : events) {

            event.onApiResponse(response);

        }

        new Thread(() -> {

            for (IHypixelApiEvent event : events) {

                event.onAsyncApiResponse(response);

            }

        }).start();


    }

    public void callApiRequestErrorEvent(String errorMessage) {

        for (IHypixelApiEvent event : events) {

            event.onApiRequestError(errorMessage);

        }

        new Thread(() -> {

            for (IHypixelApiEvent event : events) {

                event.onAsyncApiRequestError(errorMessage);

            }

        }).start();

    }

    public void callApiParseErrorEvent(String errorMessage, ApiResponse apiResponse) {

        for (IHypixelApiEvent event : events) {

            event.onApiParseError(errorMessage, apiResponse);

        }

        new Thread(() -> {

            for (IHypixelApiEvent event : events) {

                event.onAsyncApiParseError(errorMessage, apiResponse);

            }

        }).start();

    }

    public void callApiRequestNotSuccessfulEvent(BaseResponse response) {

        for (IHypixelApiEvent event : events) {

            event.onApiRequestNotSuccessful(response);

        }

        new Thread(() -> {

            for (IHypixelApiEvent event : events) {

                event.onAsyncApiRequestNotSuccessful(response);

            }

        }).start();

    }

    public void callApiResponseTypeEvent(RequestType type, Response response) {

        for (IHypixelApiEvent event : events) {

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
