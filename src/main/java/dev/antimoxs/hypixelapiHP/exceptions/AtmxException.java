package dev.antimoxs.hypixelapiHP.exceptions;


import dev.antimoxs.hypixelapiHP.HypixelApi;

public class AtmxException extends Exception {

    public AtmxException(String AppName, String reason) {

        HypixelApi.log(AppName + " " + reason);

    }

}
