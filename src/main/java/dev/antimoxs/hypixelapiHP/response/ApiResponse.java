package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.ApiKey;

public class ApiResponse {

    private String JSON;
    private String URL;
    private dev.antimoxs.hypixelapiHP.objects.ApiKey key;
    private int code;

    public ApiResponse(dev.antimoxs.hypixelapiHP.objects.ApiKey key, String JSON, String URL, int code) {

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

    public dev.antimoxs.hypixelapiHP.objects.ApiKey getApiKey() {

        if (key == null) { return new ApiKey(""); }
        return key;

    }

    public int getResponseCode() {

        return this.code;

    }

}
