package dev.antimoxs.hypixelapi.exceptions;

import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.utilities.logger.AtmxLogType;

public class ApiBuildException extends TBCException {

    private String reason;

    public ApiBuildException(String reason) {

        super(AtmxLogType.ERROR, config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
