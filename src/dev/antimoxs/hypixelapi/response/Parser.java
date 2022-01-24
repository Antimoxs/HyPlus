package dev.antimoxs.hypixelapi.response;

import com.google.gson.*;
import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.events.EventHandler;
import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.objects.ApiKey;
import dev.antimoxs.hypixelapi.requests.RequestType;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

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
                case GAMES: {

                    response.games = gson.fromJson(JSON, GamesResponse.class);
                    if (!response.games.success) eventHandler.callApiRequestNotSuccessfulEvent(response.games);
                    break;

                }
                default: {

                    eventHandler.callApiParseErrorEvent("Undefined type.", ApiResponse);
                    throw new UnknownValueException("undefined type");

                }



            }



        } catch (JsonSyntaxException e) {

            e.printStackTrace();
            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (JsonSyntax)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("JsonSyntax exception.", ApiResponse);
            return response;

        } catch (JsonParseException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (JsonParse)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("JsonParse exception.", ApiResponse);
            return response;

        } catch (NullPointerException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (NullPointer)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("NullPointer exception.", ApiResponse);
            e.printStackTrace();
            return response;

        } catch (UnknownValueException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (NullResponseGuild)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            eventHandler.callApiParseErrorEvent("UnknownValueException.", ApiResponse);
            return response;

        }



        AtmxLogger.log(AtmxLogType.COMPLETED,  config.AppName, "Successfully parsed response.");
        AtmxLogger.flush();
        eventHandler.callApiResponseTypeEvent(type, response);

        return response;

    }

}
