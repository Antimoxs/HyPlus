package dev.antimoxs.hypixelapi.objects.player.quests;

public class PlayerQuest {

    private PlayerQuestActive active = null;
    public PlayerQuestCompletion[] completions = new PlayerQuestCompletion[0];

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

        // TODO: change in hypixelapi
        if (completions == null | completions.length == 0) { return 0; }
        String l = completions[completions.length-1].time + "";

        return Long.parseLong(l.substring(0,10));

    }

}
