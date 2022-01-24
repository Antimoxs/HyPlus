package dev.antimoxs.hypixelapi;

import dev.antimoxs.hypixelapi.events.EventHandler;
import dev.antimoxs.hypixelapi.events.IHypixelApiEvent;
import dev.antimoxs.hypixelapi.exceptions.ApiBuildException;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.ApiKey;
import dev.antimoxs.hypixelapi.requests.ApiRequest;
import dev.antimoxs.hypixelapi.requests.RequestType;
import dev.antimoxs.hypixelapi.response.*;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;
import dev.antimoxs.utilities.text.AtmxColors;

import java.util.ArrayList;
import java.util.HashMap;

public class HypixelApi {

    private final EventHandler eventHandler;
    private ArrayList<ApiKey> apikeys;
    private boolean first = true;
    private boolean useSlothPixel = false;
    private int api_timeout = 120;

    public HypixelApi(ApiBuilder builder) throws ApiBuildException {

        if (first) {

            first = false;
            AtmxLogger.log(AtmxLogType.SYSTEM, config.AppName, "Welcome to the HypixelApi by Antimoxs!");
            AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "You are running version: " + config.Version);
            AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "If you have any questions, feel free to ask");
            AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "for help on our discord: https://socials.antimoxs.dev");
            AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "HypixelApi | Copyright Antimoxs 2022.");
            System.out.println(AtmxColors.GREEN_BOLD_BRIGHT + "---------------------------------------------------------------------------------------------------" + AtmxColors.RESET);

        }
        first = false;
        AtmxLogger.log(AtmxLogType.WAITING, config.AppName, "Starting HypixelApi...");

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

            AtmxLogger.log(AtmxLogType.WAITING, config.AppName, "Indexing API keys...");
            for (String s : builder.getApiKeys()) {

                KeyResponse response;

                try {
                    response = createKeyResponse(new ApiKey(s));
                } catch (ApiRequestException e) {
                    AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "Skipping invalid key.");
                    continue;
                }


                if (response.success) {

                    AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "Added key.");
                    apikeys.add(new ApiKey(s));

                }


            }

            if (apikeys.isEmpty() || apikeys.size() == 0) {

                throw new ApiBuildException("No listed ApiKeys.");

            }

            AtmxLogger.log(AtmxLogType.COMPLETED, config.AppName, "Successfully indexed " + apikeys.size() + " ApiKeys.");

        }

        if (!builder.getEvents().isEmpty()) {

            for (IHypixelApiEvent event : builder.getEvents()) {

                this.eventHandler.registerEvent(event);

            }

        }

        getRandomKey(apikeys);

        AtmxLogger.log(AtmxLogType.SYSTEM, config.AppName, "Successfully started the HypixelApi!");
        AtmxLogger.log(AtmxLogType.INFORMATION, config.AppName, "with " + apikeys.size() + " indexed api keys.");
        System.out.println(AtmxColors.GREEN_BOLD_BRIGHT + "---------------------------------------------------------------------------------------------------" + AtmxColors.RESET);

    }

    // Friends request by uuid
    public FriendsResponse createFriendsRequest(String uuid) throws ApiRequestException {

        ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.FRIENDS, uuid, key);
        return Parser.parse(RequestType.FRIENDS, response, eventHandler).friends;

    }

    // Status requests by uuid
    public StatusResponse createStatusRequest(String uuid) throws ApiRequestException {

        ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.STATUS, uuid, key);
        return Parser.parse(RequestType.STATUS, response, eventHandler).status;

    }

    // Games requests by uuid
    public GamesResponse createGamesRequest(String uuid) throws ApiRequestException {

        ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.GAMES, uuid, key);
        return Parser.parse(RequestType.GAMES, response, eventHandler).games;

    }

    // Guild requests by guildname
    public GuildResponse createGuildRequest(String GuildName) throws ApiRequestException {

        ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.GUILD_NAME, GuildName, key);
        return Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    public GuildResponse createGuildRequest(String GuildName, ApiKey Key) throws ApiRequestException {

        ApiKey key = Key;
        ApiResponse response = createRequest(RequestType.GUILD_NAME, GuildName, key);
        return Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    // Guild requests by player-uuid
    public GuildResponse createGuildRequestUUID(String PlayerUUID) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.GUILD_UUID, PlayerUUID);
        return Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }
    public GuildResponse createGuildRequestUUID(String PlayerUUID, ApiKey Key) throws ApiRequestException {

        ApiKey key = Key;
        ApiResponse response = createRequest(RequestType.GUILD_UUID, PlayerUUID, key);
        return Parser.parse(RequestType.GUILD_NAME, response, eventHandler).guild;

    }

    // Player request by name
    public PlayerResponse createPlayerRequest(String PlayerName) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.PLAYER_NAME, PlayerName);
        return Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    public PlayerResponse createPlayerRequest(String PlayerName, ApiKey Key) throws ApiRequestException {

        ApiKey key = Key;
        ApiResponse response = createRequest(RequestType.PLAYER_NAME, PlayerName, key);
        return Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    // Player request by uuid
    public PlayerResponse createPlayerRequestUUID(String PlayerUUID) throws ApiRequestException {

        //ApiKey key = getRandomKey(apikeys);
        ApiResponse response = createRequest(RequestType.PLAYER_UUID, PlayerUUID);
        return Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }
    public PlayerResponse createPlayerRequestUUID(String PlayerUUID, ApiKey Key) throws ApiRequestException {

        ApiKey key = Key;
        ApiResponse response = createRequest(RequestType.PLAYER_UUID, PlayerUUID, key);
        return Parser.parse(RequestType.PLAYER_NAME, response, eventHandler).player;

    }

    public QuestsResponse createQuestsRequest() throws ApiRequestException {

        ApiResponse response = createRequest(RequestType.QUESTS);
        return Parser.parse(RequestType.QUESTS, response, eventHandler).quests;

    }

    public KeyResponse createKeyRequest(String Key) throws ApiRequestException {

        ApiKey key = new ApiKey(Key);
        ApiResponse response = createRequest(RequestType.KEY, Key, key);
        return Parser.parse(RequestType.KEY, response, eventHandler).key;

    }

    private KeyResponse createKeyResponse(ApiKey key) throws ApiRequestException {

        ApiResponse response = createRequest(RequestType.KEY, "", key);
        return Parser.parse(RequestType.KEY, response, eventHandler).key;

    }
    private ApiKey getRandomKey(ArrayList<ApiKey> keys) {

        return keys.get((Integer.parseInt(Math.round(Math.random() * (keys.size() - 1)) + "")));

    }
    private ApiResponse createRequest(RequestType type) throws ApiRequestException {

        AtmxLogger.log(AtmxLogType.WAITING, config.AppName, "Making (nk) request... [" + type.name() + "]");
        return new ApiRequest(null, type, "", this.eventHandler).makeRequest(this.api_timeout);

    }
    private ApiResponse createRequest(RequestType type, String values) throws ApiRequestException {

        ApiKey key = getRandomKey(apikeys);
        AtmxLogger.log(AtmxLogType.WAITING, config.AppName, "Making (rk) request... [" + type.name() + "]");
        return new ApiRequest(key, type, values, this.eventHandler).makeRequest(this.api_timeout);

    }
    private ApiResponse createRequest(RequestType type, String values, ApiKey key) throws ApiRequestException {

        AtmxLogger.log(AtmxLogType.WAITING, config.AppName, "Making (sk) request... [" + type.name() + "]");
        return new ApiRequest(key, type, values, this.eventHandler).makeRequest(this.api_timeout);

    }

    public void registerEvent(IHypixelApiEvent event) {

        this.eventHandler.registerEvent(event);

    }

}
