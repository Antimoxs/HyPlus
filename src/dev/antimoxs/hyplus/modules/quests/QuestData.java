package dev.antimoxs.hyplus.modules.quests;

import dev.antimoxs.hypixelapiHP.objects.resource.ResourceQuest;
import dev.antimoxs.hypixelapiHP.objects.resource.ResourceQuestObjective;
import dev.antimoxs.hyplus.HyPlus;
import net.minecraft.client.Minecraft;

public class QuestData extends ResourceQuest {

    public int progress = 0;
    public int percent = 0;
    public ResourceQuestObjective obj;

    HyPlus HyPlus;

    public QuestData(HyPlus HyPlus, ResourceQuest quest, int progress, int percent, ResourceQuestObjective obj) {

        this.HyPlus = HyPlus;
        this.id = quest.id;
        this.name = quest.name;
        this.rewards = quest.rewards;
        this.objectives = quest.objectives;
        this.requirements = quest.requirements;
        this.description = quest.description;
        this.progress = progress;
        this.percent = percent;
        this.obj = obj;

    }

    public QuestData(HyPlus HyPlus, int progress, int percent, ResourceQuestObjective obj) {

        this.HyPlus = HyPlus;
        this.progress = progress;
        this.percent = percent;
        this.id = "empty";
        this.name = "empty";
        this.description = "empty";
        this.obj = obj;

    }

    public int getL(boolean completed) {

        int len = Minecraft.getMinecraft().fontRendererObj.getStringWidth(getText(completed)) + 10;
        return HyPlus.hyQuestTracker.HYPLUS_CTR_SORTORDER.getValueBoolean() ? -len : len;

    }

    public String getText(boolean completed) {

        if (completed) {

            return this.description + " | Completed";

        }
        else {
            return this.description == null ? "error" : this.description
                    + " | "
                    + this.progress
                    + "/"
                    + this.obj.integer;

        }
    }

}
