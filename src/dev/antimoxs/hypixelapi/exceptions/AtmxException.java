package dev.antimoxs.hypixelapi.exceptions;

import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

public class AtmxException extends Exception {

    public AtmxException(AtmxLogType type, String AppName, String reason) {

        AtmxLogger.log(type, AppName, reason);

    }

}
