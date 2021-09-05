package dev.antimoxs.hypixelapi.response;

import com.google.gson.*;
import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.exceptions.UnknownValueException;
import dev.antimoxs.hypixelapi.objects.ApiKey;
import dev.antimoxs.hypixelapi.requests.RequestType;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

public class Parser {

    private boolean success = false;

    public static Response parse(RequestType type, String JSON, ApiKey key, String URL) {

        Response response = new Response();

        try {


            Gson gson = new Gson();


            switch (type) {

                case KEY: {

                    response.key = gson.fromJson(JSON, KeyResponse.class);
                    break;

                }
                case GUILD_UUID:
                case GUILD_NAME: {

                    response.guild = gson.fromJson(JSON, GuildResponse.class);
                    break;

                }
                case STATUS: {

                    response.status = gson.fromJson(JSON, StatusResponse.class);
                    break;

                }
                case FRIENDS: {

                    response.friends = gson.fromJson(JSON, FriendsResponse.class);
                    break;

                }
                case PLAYER_UUID:
                case PLAYER_NAME: {

                    response.player = gson.fromJson(JSON, PlayerResponse.class);
                    break;

                }
                case QUESTS: {

                    response.quests = gson.fromJson(JSON, QuestsResponse.class);
                    break;

                }
                default: {

                    throw new UnknownValueException("undefined type");

                }



            }


        } catch (JsonSyntaxException e) {

            e.printStackTrace();
            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (JsonSyntax)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            return response;

        } catch (JsonParseException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (JsonParse)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            return response;

        } catch (NullPointerException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (NullPointer)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            e.printStackTrace();
            return response;

        } catch (UnknownValueException e) {

            AtmxLogger.log(AtmxLogType.ERROR,  config.AppName, "Exception while parsing response. (NullResponseGuild)");
            AtmxLogger.log(AtmxLogType.FAILED,  config.AppName, "The request was not successful, continuing...");
            return response;

        }

        AtmxLogger.log(AtmxLogType.COMPLETED,  config.AppName, "Successfully parsed response.");
        AtmxLogger.flush();

        return response;

    }

}
