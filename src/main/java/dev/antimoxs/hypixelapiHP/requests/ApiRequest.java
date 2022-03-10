package dev.antimoxs.hypixelapiHP.requests;

import dev.antimoxs.hypixelapiHP.config;
import dev.antimoxs.hypixelapiHP.events.EventHandler;
import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.objects.ApiKey;
import dev.antimoxs.hypixelapiHP.response.ApiResponse;
import dev.antimoxs.hypixelapiHP.util.kvp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequest {

    protected final dev.antimoxs.hypixelapiHP.objects.ApiKey ApiToken;
    protected dev.antimoxs.hypixelapiHP.requests.RequestType type;
    protected String values;
    protected String rawValues;
    protected String baseURL = config.BaseURL;
    protected dev.antimoxs.hypixelapiHP.events.EventHandler eventHandler;

    public ApiRequest(dev.antimoxs.hypixelapiHP.objects.ApiKey ApiToken, dev.antimoxs.hypixelapiHP.requests.RequestType type, String values, EventHandler eventHandler) {

        this.ApiToken = ApiToken;
        this.type = type;
        this.rawValues = values;
        this.eventHandler = eventHandler;

        switch (type) {

            case GUILD_NAME:
            case PLAYER_NAME:
                this.values = dev.antimoxs.hypixelapiHP.requests.valueBuilder.build("name", values);
                break;

            case GUILD_UUID:
                this.values = dev.antimoxs.hypixelapiHP.requests.valueBuilder.build("player", values);
                break;

            case PLAYER_UUID:
            case STATUS:
            case FRIENDS:
            case GAMES:
                this.values = valueBuilder.build("uuid", values);
                break;

            default:
                this.values = "";
                break;

        }

        if (type.equals(dev.antimoxs.hypixelapiHP.requests.RequestType.GAMELIST) || type.equals(dev.antimoxs.hypixelapiHP.requests.RequestType.QUESTS)) return;
        if (ApiToken != null) {
            this.values = "?key=" + ApiToken.toString() + this.values;
        }



    }

    public dev.antimoxs.hypixelapiHP.requests.RequestType getType() {

        return this.type;

    }

    public ApiKey getApiToken() {

        return this.ApiToken;

    }

    public synchronized dev.antimoxs.hypixelapiHP.response.ApiResponse makeRequest(int to) throws dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException {

        String url = buildUrl(baseURL, type, values);
        String json;
        int code = 0;

        try {
            dev.antimoxs.hypixelapiHP.util.kvp result = doApiRequest(url, to);
            json = result.string;
            code = result.i;
        } catch (IOException e) {

            e.printStackTrace();

            this.eventHandler.callApiRequestErrorEvent("Failed request with url: " + url);
            throw new ApiRequestException("Failed request with url: " + url);

        }

        return new ApiResponse(this.ApiToken, json, url, code);

    }

    private String buildUrl(String baseUrl, RequestType type, String args) {


        return baseUrl + type.getURLKey() + args;


    }

    private synchronized dev.antimoxs.hypixelapiHP.util.kvp doApiRequest(String url, int to) throws IOException {

        URL hpxl = new URL(url);
        HttpURLConnection yc = (HttpURLConnection) hpxl.openConnection();
        yc.setConnectTimeout(to);
        yc.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getResponseCode() != 200 ? yc.getErrorStream() : yc.getInputStream()));

        String inputLine;
        String re = "";

        while ((inputLine = in.readLine()) != null) {

            re = re + inputLine;

        }

        // close reader
        in.close();

        // insert url
        return new kvp(re.replaceFirst("\\{", "\\{\"url\":\"" + url + "\","), yc.getResponseCode());


    }

}
