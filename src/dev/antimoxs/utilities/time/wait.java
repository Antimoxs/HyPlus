package dev.antimoxs.utilities.time;

import dev.antimoxs.utilities.AtmxUtilitiesConfig;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;

public class wait {

    /*
     *   TimeUtils for the AtmxUtilities library
     *   Copyright Antimoxs 2021
     *   https://antimoxs.dev
     */

    /*  Wait a specific amount of time easily
    *   without having to worry about exceptions
    *
    * */


    public static boolean ms(Long ms) {

        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            AtmxLogger.log(AtmxLogType.WARNING, "Atmx#System", "Failed to wait in thread.");
            return false;
        }

        return true;

    }

    public static boolean sc(Long sec) {

        Long ms = sec * 1000;
        return ms(ms);

    }

    public static String info() {

        return AtmxUtilitiesConfig.Time_Name + " : " + AtmxUtilitiesConfig.Time_Version;

    }

}