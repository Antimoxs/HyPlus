package dev.antimoxs.hypixelapi.requests;

public class valueBuilder {

    public static String build(String type, String value) {

        return "&" + type + "=" + value;

    }

}
