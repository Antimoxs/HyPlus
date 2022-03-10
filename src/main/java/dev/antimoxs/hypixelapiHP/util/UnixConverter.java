package dev.antimoxs.hypixelapiHP.util;

import dev.antimoxs.hypixelapiHP.HypixelApi;

import java.text.ParseException;
import java.util.Date;

public class UnixConverter {

    public static String toDate(Long unixTimestamp) {

        String re = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").format(new Date(unixTimestamp));
        return re;

    }

    public static String toDate(Long unixTimestamp, String pattern) {

        String re = new java.text.SimpleDateFormat(pattern).format(new Date(unixTimestamp));
        return re;

    }

    public static Long toUnixTimestamp(String date) {

        try {
            return new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss.SSS").parse(date).getTime() / 1000;
        } catch (ParseException e) {
            HypixelApi.log("Failed to convert date.");
        }

        return -1L;

    }


}
