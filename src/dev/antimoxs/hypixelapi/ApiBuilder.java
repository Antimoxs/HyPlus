package dev.antimoxs.hypixelapi;

import dev.antimoxs.hypixelapi.exceptions.ApiBuildException;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class ApiBuilder {

    private ArrayList<String> API_KEYS;
    private String APP_NAME;
    private HashMap<Integer, ApiEvent> LISTENERS = new HashMap<>();
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

    // in progress...
    private ApiBuilder addListener(ApiEvent event) {

        if (LISTENERS.isEmpty()) {

            LISTENERS.put(0, event);

        } else {

            LISTENERS.put(LISTENERS.size(), event);

        }
        return this;

    }

    public TBCHypixelApi build() {

        if (this.API_KEYS == null) {



        }

        try {

            return new TBCHypixelApi(this);

        } catch (ApiBuildException e) {
            AtmxLogger.log(AtmxLogType.FAILED, config.AppName, "Failed ApiBuild.");
            AtmxLogger.log(AtmxLogType.WARNING, config.AppName, "Invalid ApiBuild, expecting NullPointers in the codeflow...");
        }


        return null;

    }

    //------------------------------------------

    public ArrayList<String> getApiKeys() { return this.API_KEYS; }
    public String getAppName() { return this.APP_NAME; }
    public HashMap getListeners() { return this.LISTENERS; }
    public boolean isUseSlothPixel() { return this.useSlothPixel; }

    public int getTIMEOUT() {
        return TIMEOUT;
    }
}
