package dev.antimoxs.hypixelapi.requests;

import com.google.gson.*;

import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class MojangRequest {

    public static String getUUID(String playerName) {

        String re = "request failed.";

        try {

            URL mojang = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            URLConnection request = mojang.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String inputLine;
            re = "";

            while ((inputLine = in.readLine()) != null) {

                re = re + inputLine;

            }

            in.close();

            //GsonBuilder builder = new GsonBuilder();
            //builder.setPrettyPrinting();
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(re, JsonElement.class);

            if (element == null) {

                throw new ApiRequestException("MojangRequest returned invalid PlayerName.");

            }

            JsonObject object = element.getAsJsonObject();

            return object.get("id").getAsString();

        } catch (IOException | ApiRequestException e) {

            AtmxLogger.log(AtmxLogType.ERROR, config.AppName, "Failed Mojang UUID request.");

        }

        return re;

    }

    public static String getName(String UUID) {

        StringBuilder re = new StringBuilder("request failed.");

        try {

            URL mojang = new URL("https://api.mojang.com/user/profiles/" + UUID + "/names");
            URLConnection c = mojang.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
            String inputLine;
            re = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {

                re.append(inputLine);

            }

            in.close();

            JsonArray array = new Gson().fromJson(re.toString(), JsonElement.class).getAsJsonArray();
            return array.get(array.size()-1).getAsJsonObject().get("name").getAsString();

        } catch (IOException e) {

            AtmxLogger.log(AtmxLogType.ERROR, config.AppName, "Failed Mojang NAME request.");

        }

        return re.toString();

    }


}


