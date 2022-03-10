package dev.antimoxs.hypixelapiHP;

import dev.antimoxs.hypixelapiHP.exceptions.ApiBuildException;
import dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent;

import java.util.ArrayList;

public class ApiBuilder {

    private ArrayList<String> API_KEYS;
    private String APP_NAME;
    private ArrayList<dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent> events = new ArrayList<>();
    private boolean useSlothPixel = false;
    private int TIMEOUT = 120;

    public ApiBuilder() {

        this.API_KEYS = new ArrayList<>();

    }

    public ApiBuilder addKey(String ApiKey) {

        this.API_KEYS.add(ApiKey);
        return this;

    }

    public ApiBuilder setApplicationName(String name) {

        this.APP_NAME = name;
        return this;

    }

    public ApiBuilder useSlothPixel(boolean useSlothPixel) {

        this.useSlothPixel = useSlothPixel;
        return this;

    }

    public ApiBuilder setApiTimeOut(int second) {

        this.TIMEOUT = second;
        return this;

    }

    public ApiBuilder registerEvent(dev.antimoxs.hypixelapiHP.events.IHypixelApiEvent event) {

        this.events.add(event);
        return this;

    }

    public HypixelApi build() {

        if (this.API_KEYS == null) {



        }

        try {

            return new HypixelApi(this);

        } catch (ApiBuildException e) {
            HypixelApi.log("Failed ApiBuild.");
            HypixelApi.log("Invalid ApiBuild, expecting NullPointers in the codeflow...");
        }


        return null;

    }

    //------------------------------------------

    public ArrayList<String> getApiKeys() { return this.API_KEYS; }
    public String getAppName() { return this.APP_NAME; }
    public ArrayList<IHypixelApiEvent> getEvents() { return this.events; }
    public boolean isUseSlothPixel() { return this.useSlothPixel; }

    public int getTIMEOUT() {
        return TIMEOUT;
    }
}
