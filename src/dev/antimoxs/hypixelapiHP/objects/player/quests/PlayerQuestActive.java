package dev.antimoxs.hypixelapiHP.objects.player.quests;

import java.util.HashMap;

public class PlayerQuestActive {

    public HashMap<String, Object> objectives = new HashMap<>();

    public String getChallengeName() {

        String re = "";
        for (String s : objectives.keySet()) {

            re = re + s + ", ";

        }

        return re;

    }



}
