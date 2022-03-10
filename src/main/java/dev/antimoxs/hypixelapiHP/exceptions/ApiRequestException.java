package dev.antimoxs.hypixelapiHP.exceptions;

import dev.antimoxs.hypixelapiHP.config;

public class ApiRequestException extends AtmxException {

    private String reason;

    public ApiRequestException(String reason) {

        super(config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
