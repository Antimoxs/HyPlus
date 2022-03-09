package dev.antimoxs.hypixelapiHP.response;

import com.google.gson.*;
import dev.antimoxs.hypixelapiHP.requests.RequestType;
import dev.antimoxs.hypixelapiHP.HypixelApi;
import dev.antimoxs.hypixelapiHP.events.EventHandler;
import dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException;

public class Parser {

    public static Response parse(RequestType type, ApiResponse ApiResponse, EventHandler eventHandler) {

        Response response = new Response();
        String JSON = ApiResponse.getJsonAsString();
        eventHandler.callApiResponseEvent(ApiResponse);

        try {


            Gson gson = new Gson();


            switch (type) {

                case KEY: {

                    response.key = gson.fromJson(JSON, KeyResponse.class);
                    if (!response.key.success) eventHandler.callApiRequestNotSuccessfulEvent(response.key);
                    break;

                }
                case GUILD_UUID:
                case GUILD_NAME: {

                    response.guild = gson.fromJson(JSON, GuildResponse.class);
                    if (!response.guild.success) eventHandler.callApiRequestNotSuccessfulEvent(response.guild);
                    break;

                }
                case STATUS: {

                    response.status = gson.fromJson(JSON, StatusResponse.class);
                    if (!response.status.success) eventHandler.callApiRequestNotSuccessfulEvent(response.status);
                    break;

                }
                case FRIENDS: {

                    response.friends = gson.fromJson(JSON, FriendsResponse.class);
                    if (!response.friends.success) eventHandler.callApiRequestNotSuccessfulEvent(response.friends);
                    break;

                }
                case PLAYER_UUID:
                case PLAYER_NAME: {

                    response.player = gson.fromJson(JSON, PlayerResponse.class);
                    if (!response.player.success) eventHandler.callApiRequestNotSuccessfulEvent(response.player);
                    break;

                }
                case QUESTS: {

                    response.quests = gson.fromJson(JSON, QuestsResponse.class);
                    if (!response.quests.success) eventHandler.callApiRequestNotSuccessfulEvent(response.quests);
                    break;

                }
                case GAMELIST: {

                    response.gamelist = gson.fromJson(JSON, GamelistResponse.class);
                    if (!response.gamelist.success) eventHandler.callApiRequestNotSuccessfulEvent(response.gamelist);
                    break;

                }
                case GAMES: {

                    response.games = gson.fromJson(JSON, GamesResponse.class);
                    if (!response.games.success) eventHandler.callApiRequestNotSuccessfulEvent(response.games);
                    break;

                }
                default: {

                    eventHandler.callApiParseErrorEvent("Undefined type.", ApiResponse);
                    throw new dev.antimoxs.hypixelapiHP.exceptions.UnknownValueException("undefined type");

                }



            }



        } catch (JsonSyntaxException e) {

            e.printStackTrace();
            dev.antimoxs.hypixelapiHP.HypixelApi.log("Exception while parsing response. (JsonSyntax)");
            dev.antimoxs.hypixelapiHP.HypixelApi.log("The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("JsonSyntax exception.", ApiResponse);
            return response;

        } catch (JsonParseException e) {

            dev.antimoxs.hypixelapiHP.HypixelApi.log("Exception while parsing response. (JsonParse)");
            dev.antimoxs.hypixelapiHP.HypixelApi.log("The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("JsonParse exception.", ApiResponse);
            return response;

        } catch (NullPointerException e) {

            dev.antimoxs.hypixelapiHP.HypixelApi.log("Exception while parsing response. (NullPointer)");
            dev.antimoxs.hypixelapiHP.HypixelApi.log("The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("NullPointer exception.", ApiResponse);
            e.printStackTrace();
            return response;

        } catch (UnknownValueException e) {

            dev.antimoxs.hypixelapiHP.HypixelApi.log("Exception while parsing response. (NullResponseGuild)");
            dev.antimoxs.hypixelapiHP.HypixelApi.log("The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("UnknownValueException.", ApiResponse);
            return response;

        }



        HypixelApi.log("Successfully parsed response.");
        eventHandler.callApiResponseTypeEvent(type, response);

        return response;

    }

}
