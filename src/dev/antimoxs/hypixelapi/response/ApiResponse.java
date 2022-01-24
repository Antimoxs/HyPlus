package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.ApiKey;

public class ApiResponse {

    private String JSON;
    private String URL;
    private ApiKey key;
    private int code;

    public ApiResponse(ApiKey key, String JSON, String URL, int code) {

        this.JSON = JSON;
        this.key = key;
        this.URL = URL;
        this.code = code;

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

    public int getResponseCode() {

        return this.code;

    }

}
