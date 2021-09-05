package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.Key;

public class KeyResponse {

    public boolean success = false;
    private Key record = null;
    public String cause = "";

    public Key getKey() {

        return record == null ? new Key() : record;

    }

}
