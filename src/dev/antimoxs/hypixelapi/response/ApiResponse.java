package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.ApiKey;

public class ApiResponse {

    private String JSON;
    private String URL;
    private ApiKey key;

    public ApiResponse(ApiKey key, String JSON, String URL) {

        this.JSON = JSON;
        this.key = key;
        this.URL = URL;

    }

    public String getJsonAsString() {

        return JSON;

    }

    public String getURL() {

        return URL;

    }

    public ApiKey getApiKey() {

        if (key == null) { return new ApiKey(""); }
        return key;

    }

}
