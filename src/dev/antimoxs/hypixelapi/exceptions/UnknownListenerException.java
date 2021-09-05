package dev.antimoxs.hypixelapi.exceptions;

import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.utilities.logger.AtmxLogType;

public class UnknownListenerException extends TBCException {

    private String reason;

    public UnknownListenerException(String reason) {

        super(AtmxLogType.WARNING, config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
