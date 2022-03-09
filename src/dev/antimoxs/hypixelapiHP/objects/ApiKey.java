package dev.antimoxs.hypixelapiHP.objects;

public class ApiKey {

    private final String key;
    private final String ownerUUID = "No data for OwnerUUID";
    private final Integer requests = -1;

    public ApiKey(String key) {

        this.key = key;


    }

    @Override
    public String toString() {
        return key;
    }

    public String getOwnerUUID() {

        return ownerUUID;

    }

    public Integer getRequestAmount() {

        return requests;

    }

}
