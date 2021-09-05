package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.resource.ResourceQuests;

public class QuestsResponse {

    public boolean success = false;
    private ResourceQuests quests = null;
    public long lastUpdated = 0;
    public String cause = "";

    public ResourceQuests getQuests() {

        return quests == null ? new ResourceQuests() : quests;

    }

}
