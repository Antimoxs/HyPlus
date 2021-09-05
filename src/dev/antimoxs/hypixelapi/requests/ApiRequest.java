package dev.antimoxs.hypixelapi.requests;

import dev.antimoxs.hypixelapi.response.ApiResponse;
import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.ApiKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ApiRequest {

    protected final ApiKey ApiToken;
    protected RequestType type;
    protected String values;
    protected String rawValues;
    protected String baseURL = config.BaseURL;

    public ApiRequest(ApiKey ApiToken, RequestType type, String values) {

        this.ApiToken = ApiToken;
        this.type = type;
        this.rawValues = values;

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
                this.values = valueBuilder.build("uuid", values);
                break;

            case KEY:
            case QUESTS:
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

        try {
            json = doApiRequest(url, to);
        } catch (IOException e) {
            throw new ApiRequestException("Failed request with url: " + url);
        }

        switch (this.type) {

            case QUESTS:
            case GUILD_NAME:
            case GUILD_UUID:
            case PLAYER_NAME:
            case PLAYER_UUID:
            case KEY:
            case STATUS:
            case FRIENDS:
                return new ApiResponse(this.ApiToken, json, url);
            default: return null;


        }

    }

    private String buildUrl(String baseUrl, RequestType type, String args) {


        return baseUrl + type.getURLKey() + args;


    }

    private synchronized String doApiRequest(String url, int to) throws IOException {

        URL hpxl = new URL(url);
        URLConnection yc = hpxl.openConnection();
        yc.setConnectTimeout(to);
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        String re = "";

        while ((inputLine = in.readLine()) != null) {

            re = re + inputLine;

        }

        in.close();

        return re.replaceFirst("\\{", "\\{\"url\":\"" + url + "\",");


    }

}
