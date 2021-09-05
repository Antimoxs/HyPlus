package dev.antimoxs.hypixelapi;

import dev.antimoxs.hypixelapi.requests.ApiRequest;
import dev.antimoxs.hypixelapi.response.ApiResponse;

public class ApiEvent {

    private String lastJsonResponse;
    private ApiRequest request;

    public ApiEvent(String lastJsonResponse) {

        this.lastJsonResponse = lastJsonResponse;

    }

    public void onEvent() {}

    public boolean checkUpdate(ApiResponse response) {

        return response.getJsonAsString().equals(lastJsonResponse);

    }

    public ApiRequest getApiRequest() {

        return request;

    }
}
