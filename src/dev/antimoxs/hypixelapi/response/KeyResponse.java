package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.Key;

public class KeyResponse extends BaseResponse {

    private Key record = null;

    public Key getKey() {

        return record == null ? new Key() : record;

    }

}
