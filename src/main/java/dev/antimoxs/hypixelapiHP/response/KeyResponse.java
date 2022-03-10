package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.Key;

public class KeyResponse extends BaseResponse {

    private dev.antimoxs.hypixelapiHP.objects.Key record = null;

    public dev.antimoxs.hypixelapiHP.objects.Key getKey() {

        return record == null ? new Key() : record;

    }

}
