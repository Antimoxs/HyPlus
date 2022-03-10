package dev.antimoxs.hypixelapiHP;

import dev.antimoxs.hypixelapiHP.exceptions.ApiBuildException;
import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.requests.ApiRequest;
import dev.antimoxs.hypixelapiHP.requests.RequestType;
import dev.antimoxs.hypixelapiHP.events.EventHandler;
import dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent;
import dev.antimoxs.hypixelapiHP.objects.ApiKey;
import dev.antimoxs.hypixelapiHP.response.ApiResponse;
import dev.antimoxs.hypixelapiHP.response.KeyResponse;

import java.util.ArrayList;

public class HypixelApi {

    private final EventHandler eventHandler;
    private ArrayList<ApiKey> apikeys;
    private boolean first = true;
    private boolean useSlothPixel = false;
    private int api_timeout = 120;

    public HypixelApi(ApiBuilder builder) throws ApiBuildException {

        if (first) {

            first = false;
            log("Welcome to the HypixelApi by Antimoxs!");
            log("You are running version: " + config.Version);
            log("If you have any questions, feel free to ask");
            log("for help on our discord: https://socials.antimoxs.dev");
            log("HypixelApi | Copyright Antimoxs 2022.");
            log("---------------------------------------------------------------------------------------------------");

        }
        first = false;
        log("Starting HypixelApi...");

        if (builder == null) {

            throw new ApiBuildException("Invalid ApiBuilder");

        }
        this.api_timeout = builder.getTIMEOUT();
        this.eventHandler = new EventHandler(this);

        if (builder.isUseSlothPixel()) {

            useSlothPixel = true;

        }



        else {

            if (builder.getApiKeys() == null) {

                throw new ApiBuildException("No API Key(s).");

            }

            this.apikeys = new ArrayList<>();

            log("Indexing API keys...");
            for (String s : builder.getApiKeys()) {

                KeyResponse response;

                try {
                    response = createKeyResponse(new dev.antimoxs.hypixelapiHP.objects.ApiKey(s));
                } catch (ApiRequestException e) {
                    log("Skipping invalid key.");
                    continue;
                }


                if (response.success) {

                    log("Added key.");
                    apikeys.add(new dev.antimoxs.hypixelapiHP.objects.ApiKey(s));

                }


            }

            if (apikeys.isEmpty() || apikeys.size() == 0) {

                throw new ApiBuildException("No listed ApiKeys.");

            }

            log("Successfully indexed " + apikeys.size() + " ApiKeys.");

        }

        if (!builder.getEvents().isEmpty()) {

            for (dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event : builder.getEvents()) {

                this.eventHandler.registerEvent(event);

            }

        }

        getRandomKey(apikeys);

        log("Successfully started the HypixelApi!");
        log("with " + apikeys.size() + " indexed api keys.");
        log("---------------------------------------------------------------------------------------------------");

    }

    // Friends request by uuid
    public dev.antimoxs.hypixelapiHP.response.FriendsResponse createFriendsRequest(String uuid) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.FRIENDS, uuid, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.FRIENDS, response, eventHandler).friends;

    }

    // Status requests by uuid
    public dev.antimoxs.hypixelapiHP.response.StatusResponse createStatusRequest(String uuid) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.STATUS, uuid, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.STATUS, response, eventHandler).status;

    }

    // Games requests by uuid
    public dev.antimoxs.hypixelapiHP.response.GamesResponse createGamesRequest(String uuid) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GAMES, uuid, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GAMES, response, eventHandler).games;

    }

    // Guild requests by guildname
    public dev.antimoxs.hypixelapiHP.response.GuildResponse createGuildRequest(String GuildName) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GUILD_NAME, GuildName, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    public dev.antimoxs.hypixelapiHP.response.GuildResponse createGuildRequest(String GuildName, dev.antimoxs.hypixelapiHP.objects.ApiKey Key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = Key;
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GUILD_NAME, GuildName, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    // Guild requests by player-uuid
    public dev.antimoxs.hypixelapiHP.response.GuildResponse createGuildRequestUUID(String PlayerUUID) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GUILD_UUID, PlayerUUID);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    public dev.antimoxs.hypixelapiHP.response.GuildResponse createGuildRequestUUID(String PlayerUUID, dev.antimoxs.hypixelapiHP.objects.ApiKey Key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = Key;
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GUILD_UUID, PlayerUUID, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }

    // Player request by name
    public dev.antimoxs.hypixelapiHP.response.PlayerResponse createPlayerRequest(String PlayerName) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.PLAYER_NAME, PlayerName);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    public dev.antimoxs.hypixelapiHP.response.PlayerResponse createPlayerRequest(String PlayerName, dev.antimoxs.hypixelapiHP.objects.ApiKey Key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = Key;
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.PLAYER_NAME, PlayerName, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    // Player request by uuid
    public dev.antimoxs.hypixelapiHP.response.PlayerResponse createPlayerRequestUUID(String PlayerUUID) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.PLAYER_UUID, PlayerUUID);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    public dev.antimoxs.hypixelapiHP.response.PlayerResponse createPlayerRequestUUID(String PlayerUUID, dev.antimoxs.hypixelapiHP.objects.ApiKey Key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = Key;
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.PLAYER_UUID, PlayerUUID, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }

    public dev.antimoxs.hypixelapiHP.response.QuestsResponse createQuestsRequest() throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.QUESTS);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.QUESTS, response, eventHandler).quests;

    }

    public dev.antimoxs.hypixelapiHP.response.GamelistResponse createGamelistRequest() throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.GAMELIST);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.GAMELIST, response, eventHandler).gamelist;

    }

    public dev.antimoxs.hypixelapiHP.response.KeyResponse createKeyRequest(String Key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = new dev.antimoxs.hypixelapiHP.objects.ApiKey(Key);
        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.KEY, Key, key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.KEY, response, eventHandler).key;

    }

    private dev.antimoxs.hypixelapiHP.response.KeyResponse createKeyResponse(dev.antimoxs.hypixelapiHP.objects.ApiKey key) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.response.ApiResponse response = createRequest(RequestType.KEY, "", key);
        return dev.antimoxs.hypixelapiHP.response.Parser.parse(RequestType.KEY, response, eventHandler).key;

    }
    private dev.antimoxs.hypixelapiHP.objects.ApiKey getRandomKey(ArrayList<dev.antimoxs.hypixelapiHP.objects.ApiKey> keys) {

        return keys.get((Integer.parseInt(Math.round(Math.random() * (keys.size() - 1)) + "")));

    }
    private dev.antimoxs.hypixelapiHP.response.ApiResponse createRequest(RequestType type) throws ApiRequestException {

        log("Making (nk) request... [" + type.name() + "]");
        return new ApiRequest(null, type, "", this.eventHandler).makeRequest(this.api_timeout);

    }
    private dev.antimoxs.hypixelapiHP.response.ApiResponse createRequest(RequestType type, String values) throws ApiRequestException {

        dev.antimoxs.hypixelapiHP.objects.ApiKey key = getRandomKey(apikeys);
        log("Making (rk) request... [" + type.name() + "]");
        return new ApiRequest(key, type, values, this.eventHandler).makeRequest(this.api_timeout);

    }
    private ApiResponse createRequest(RequestType type, String values, ApiKey key) throws ApiRequestException {

        log("Making (sk) request... [" + type.name() + "]");
        return new ApiRequest(key, type, values, this.eventHandler).makeRequest(this.api_timeout);

    }

    public void registerEvent(IHypixelApiEvent event) {

        this.eventHandler.registerEvent(event);

    }

    public static void log(String s) {

        System.out.println("[HypixelAPI] " + s);

    }

}
