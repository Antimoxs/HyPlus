package dev.antimoxs.hypixelapi.exceptions;

import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

public class TBCException extends Exception {

    public TBCException(AtmxLogType type, String AppName, String reason) {

        AtmxLogger.log(type, AppName, reason);

    }

}
