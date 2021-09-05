package dev.antimoxs.utilities.logger;

import dev.antimoxs.utilities.AtmxUtilitiesConfig;
import dev.antimoxs.utilities.text.AtmxColors;
import dev.antimoxs.utilities.time.wait;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 *   AtmxLogger
 *   Copyright Antimoxs 2021
 *   https://antimoxs.dev
 */

public class AtmxLogger {

    public static String version = AtmxUtilitiesConfig.Logger_Version;
    private static boolean first = AtmxUtilitiesConfig.Logger_FirstLog;
    private static String AppName = AtmxUtilitiesConfig.Logger_Name;
    private static boolean useSymbols = false;
    private static boolean useSpacing = false;
    private static boolean useShorting = false;
    private static boolean useTimestamp = true;
    private static boolean useColors = true;


    // Color definitions
    private static String comp = AtmxColors.GREEN_BOLD_BRIGHT;
    private static String info = AtmxColors.WHITE;
    private static String sysm = AtmxColors.PURPLE_BOLD_BRIGHT;
    private static String comd = AtmxColors.BLUE;
    private static String wiit = AtmxColors.CONSOLE_CYAN;
    private static String warn = AtmxColors.YELLOW;
    private static String fail = AtmxColors.RED;
    private static String errr = AtmxColors.WHITE_BACKGROUND_BRIGHT + AtmxColors.BLACK_BRIGHT;

    private static String RESET = AtmxColors.RESET;



    public static void log(AtmxLogType logType, String ApplicationName, String text) {

        //ThreadGroup log = new ThreadGroup("log");
        //new Thread(log, () -> {

            // shorter var name
            String an = ApplicationName;

            if (first) {

                first = false;
                System.out.println(AtmxColors.GREEN_BOLD_BRIGHT + "---------------------------------------------------------------------------------------------------" + AtmxColors.RESET);
                send1(false, AtmxColors.GREEN_BOLD_BRIGHT + getPrefix("Logger", AppName, ""), "Using TBC#Logger " + version + ", Copyright TeamBuildingCreations 2020" + AtmxColors.RESET);
                send1(false, AtmxColors.GREEN_BOLD_BRIGHT + getPrefix("Logger", AppName, ""), "Get support here: https://discord.gg/ATdbUS4" + AtmxColors.RESET);
                System.out.println(AtmxColors.GREEN_BOLD_BRIGHT + "---------------------------------------------------------------------------------------------------" + AtmxColors.RESET);

                wait.sc(2L);

            }

            if (!useColors) {

                comp = "";
                info = "";
                sysm = "";
                comd = "";
                wiit = "";
                warn = "";
                fail = "";
                errr = "";
                RESET = "";


            }
            else {

                comp = AtmxColors.GREEN_BOLD_BRIGHT;
                info = AtmxColors.WHITE;
                sysm = AtmxColors.PURPLE_BOLD_BRIGHT;
                comd = AtmxColors.BLUE;
                wiit = AtmxColors.CONSOLE_CYAN;
                warn = AtmxColors.YELLOW;
                fail = AtmxColors.RED;
                errr = AtmxColors.WHITE_BACKGROUND_BRIGHT + AtmxColors.BLACK_BRIGHT;
                RESET = AtmxColors.RESET;

            }

            if (useSymbols) {

                switch (logType) {

                    case COMPLETED:     send1(false, comp + getPrefix("✔", an, ""), text); return;
                    case INFORMATION:   send1(false, info + getPrefix("⚑", an, ""), text); return;
                    case SYSTEM:        send1(false, sysm + getPrefix("▣", an ,""), text); return;
                    case COMMAND:       send1(false, comd + getPrefix("↦", an, ""), text); return;
                    case WAITING:       send1(true,  wiit + getPrefix("⌚", an, ""), text); return;
                    case WARNING:       send1(true,  warn + getPrefix("⚠", an, ""), text); return;
                    case FAILED:        send1(false, fail + getPrefix("✖", an, ""), text); return;
                    case ERROR:         send1(false, errr + getPrefix("⛔", an, ""), text); return;

                }

            }
            else if (useShorting) {

                switch (logType) {

                    case COMPLETED:     send1(false, comp + getPrefix("Comp", an, ""), text); return;
                    case INFORMATION:   send1(false, info + getPrefix("Info", an, ""), text); return;
                    case SYSTEM:        send1(false, sysm + getPrefix("Sysm", an ,""), text); return;
                    case COMMAND:       send1(false, comd + getPrefix("Cmnd", an, ""), text); return;
                    case WAITING:       send1(true,  wiit + getPrefix("Runs", an, ""), text); return;
                    case WARNING:       send1(true,  warn + getPrefix("Warn", an, ""), text); return;
                    case FAILED:        send1(false, fail + getPrefix("Fail", an, ""), text); return;
                    case ERROR:         send1(false, errr + getPrefix("ERR!", an, ""), text); return;

                }

            }
            else {

                switch (logType) {

                    case COMPLETED:     send1(false, comp + getPrefix("Task Completed", an, ""          ), text); return;
                    case INFORMATION:   send1(false, info + getPrefix("Information",    an, "   "       ), text); return;
                    case SYSTEM:        send1(false, sysm + getPrefix("System",         an, "        "  ), text); return;
                    case COMMAND:       send1(false, comd + getPrefix("Command",        an, "       "   ), text); return;
                    case WAITING:       send1(true , wiit + getPrefix("Task Running",   an, "  "        ), text); return;
                    case WARNING:       send1(true , warn + getPrefix("Warning",        an, "       "   ), text); return;
                    case FAILED:        send1(false, fail + getPrefix("Task Failed",    an, "   "       ), text); return;
                    case ERROR:         send1(false, errr + getPrefix("ERROR",          an, "         " ), text); return;

                }

            }

            //log.destroy();

        //}).start();



    }

    public static void log(String ApplicationName, String text) {

        log(AtmxLogType.INFORMATION, ApplicationName, text);

    }

    public static void log(AtmxLogType logType, String text) {

        log(logType, "Application", text);

    }

    public static void log(String text) {

        log(AtmxLogType.INFORMATION, "Application", text);

    }

    private static String getPrefix(String prefixString, String ApplicationName, String prefixGap) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");

        if (!useSpacing) {

            prefixGap = "";

        }

        if (!useTimestamp) {

            return "[" + ApplicationName + "] [" + prefixString + "]: " + prefixGap;

        }

        return "[" + ApplicationName + "] [" + sdf.format(cal.getTime()) + "] [" + prefixString + "]: " + prefixGap;

    }

    private static void send1(Boolean mark, String prefix, String text) {

        if (mark) {

            System.err.println(prefix + text + RESET);
            System.err.flush();

        } else {

            System.out.println(prefix + text + RESET);
            System.out.flush();

        }

    }

    public static void flush() {

        System.out.flush();
        System.err.flush();

    }

    public static void useSymbols(Boolean use) {

        useSymbols = use;

    }

    public static void useSpacing(Boolean use) {

        useSpacing = use;

    }

    public static void useShorting(Boolean use) {

        useShorting = use;

    }

    public static void useTimestamp(Boolean use) {

        useTimestamp = use;

    }




}
