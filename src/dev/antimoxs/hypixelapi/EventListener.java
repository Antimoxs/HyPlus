package dev.antimoxs.hypixelapi;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.requests.ApiRequest;
import dev.antimoxs.hypixelapi.response.ApiResponse;
import dev.antimoxs.utilities.time.wait;

import java.util.HashMap;

public class EventListener {

    private final String ApiKey;
    private final String BaseURL = config.BaseURL;
    private final HashMap<Integer, ApiEvent> listeners;
    private final Integer requestDelay;
    private final Integer index;

    public EventListener(String ApiKey, Integer requestDelay, HashMap<Integer, ApiEvent> listeners) {

        this.ApiKey = ApiKey;
        this.requestDelay = requestDelay;
        this.listeners = listeners;
        this.index = listeners.size() -1;

    }

    public void checkNextInQueue() throws ApiRequestException {

        ApiRequest request = listeners.get(index).getApiRequest();
        ApiResponse response = request.makeRequest(0);

        if (listeners.get(index).checkUpdate(response)) {



        }

    }

    public void delay() {

        wait.sc(Long.parseLong(requestDelay.toString()));

    }



}
