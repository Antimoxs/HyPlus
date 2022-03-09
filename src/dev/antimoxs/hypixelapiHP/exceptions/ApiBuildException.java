package dev.antimoxs.hypixelapiHP.exceptions;

import dev.antimoxs.hypixelapiHP.config;

public class ApiBuildException extends AtmxException {

    private String reason;

    public ApiBuildException(String reason) {

        super(config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
