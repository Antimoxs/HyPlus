package dev.antimoxs.hypixelapiHP.exceptions;

import dev.antimoxs.hypixelapiHP.config;

public class UnknownValueException extends AtmxException {

    private String reason;

    public UnknownValueException(String reason) {

        super(config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
