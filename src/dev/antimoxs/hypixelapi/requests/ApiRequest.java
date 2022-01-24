package dev.antimoxs.hypixelapi.requests;

import dev.antimoxs.hypixelapi.HypixelApi;
import dev.antimoxs.hypixelapi.events.EventHandler;
import dev.antimoxs.hypixelapi.response.ApiResponse;
import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.ApiKey;
import dev.antimoxs.hypixelapi.util.kvp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class ApiRequest {

    protected final ApiKey ApiToken;
    protected RequestType type;
    protected String values;
    protected String rawValues;
    protected String baseURL = config.BaseURL;
    protected EventHandler eventHandler;

    public ApiRequest(ApiKey ApiToken, RequestType type, String values, EventHandler eventHandler) {

        this.ApiToken = ApiToken;
        this.type = type;
        this.rawValues = values;
        this.eventHandler = eventHandler;

        switch (type) {

            case GUILD_NAME:
            case PLAYER_NAME:
                this.values = valueBuilder.build("name", values);
                break;

            case GUILD_UUID:
                this.values = valueBuilder.build("player", values);
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

        if (ApiToken != null) {
            this.values = "?key=" + ApiToken.toString() + this.values;
        }



    }

    public RequestType getType() {

        return this.type;

    }

    public ApiKey getApiToken() {

        return this.ApiToken;

    }

    public synchronized ApiResponse makeRequest(int to) throws ApiRequestException {

        String url = buildUrl(baseURL, type, values);
        String json;
        int code = 0;

        try {
            kvp result = doApiRequest(url, to);
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

    private synchronized kvp doApiRequest(String url, int to) throws IOException {

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
