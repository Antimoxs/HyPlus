package dev.antimoxs.hypixelapi.objects.resource;

import java.util.ArrayList;

public class ResourceQuest {

    public String id = "";
    public String name = "";
    public ResourceQuestReward[] rewards = new ResourceQuestReward[0];
    public ResourceQuestObjective[] objectives = new ResourceQuestObjective[0];
    public ResourceQuestRequirement[] requirements = new ResourceQuestRequirement[0];
    public String description = "";

    public ArrayList<String> getObjectiveIds() {

        ArrayList<String> re = new ArrayList<>();
        for (ResourceQuestObjective r : objectives) {

            re.add(r.id);

        }

        return re;

    }

}
