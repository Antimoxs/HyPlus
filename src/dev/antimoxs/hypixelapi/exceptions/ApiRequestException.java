package dev.antimoxs.hypixelapi.exceptions;

import dev.antimoxs.hypixelapi.config;
import dev.antimoxs.utilities.logger.AtmxLogType;

public class ApiRequestException extends TBCException {

    private String reason;

    public ApiRequestException(String reason) {

        super(AtmxLogType.FAILED, config.AppName, reason);
        this.reason = reason;

    }

    public String getReason() {

        return reason;

    }

}
