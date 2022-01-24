package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.resource.ResourceQuests;

public class QuestsResponse extends BaseResponse {

    private ResourceQuests quests = null;
    public long lastUpdated = 0;

    public ResourceQuests getQuests() {

        return quests == null ? new ResourceQuests() : quests;

    }

}
