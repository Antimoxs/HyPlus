package dev.antimoxs.hypixelapiHP.exceptions;

import dev.antimoxs.hypixelapiHP.config;

public class UnknownListenerException extends AtmxException {

    private String reason;

    public UnknownListenerException(String reason) {

        super(config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
