package dev.antimoxs.hypixelapiHP.objects.player.quests;

public class PlayerQuest {

    private PlayerQuestActive active = null;
    public dev.antimoxs.hypixelapiHP.objects.player.quests.PlayerQuestCompletion[] completions = new PlayerQuestCompletion[0];

    public boolean isChallengeActive() { return !(active == null); }

    public PlayerQuestActive getActiveChallenge() {

        if (isChallengeActive()) {

            return active;

        }
        else {

            return new PlayerQuestActive();

        }

    }

    public long getLatestCompletion() {

        if (completions == null | completions.length == 0) { return System.currentTimeMillis()*10; }
        String l = completions[completions.length-1].time + "";

        return Long.parseLong(l.substring(0,10));

    }

}
