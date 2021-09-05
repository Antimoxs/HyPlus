package dev.antimoxs.hypixelapi;



import dev.antimoxs.hypixelapi.util.UnixConverter;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

public class Main {

    public static final String Key = ApiTokens.key1;

    public static void main(String[] args) {

        /*
        TBCLogger.useShorting(true);
        TBCLogger.useTimestamp(true);

        ApiBuilder builder = new ApiBuilder();
        builder.addKey(Key);
        builder.setApplicationName("app");
        TBCHypixelApi api = builder.build();

        //wait.sc(5L);
        System.out.println("request? go!");

        try {



            PlayerResponse response = api.createPlayerRequestUUID(MojangRequest.getUUID("Antimoxs"));
            if (response.success) {

                long unixTime = (System.currentTimeMillis() / 1000L - 18000L);
                QuestsResponse qs = api.createQuestsRequest();
                for (ResourceQuest q : qs.getQuests().getQuestsForMode("battleground")) {

                    PlayerQuest quest = response.getPlayer().quests.get(q.id);
                    if (unixTime-quest.getLatestCompletion() > 0) {

                        for (ResourceQuestObjective objective : q.objectives) {

                            int progress = 0;
                            String type = objective.type;
                            if (quest.getActiveChallenge().objectives.get(objective.id) == null) {}
                            else if (type.equals("BooleanObjective")) {

                                boolean b = (Boolean) quest.getActiveChallenge().objectives.get(objective.id);
                                if (b) progress = 1;

                            }
                            else if (type.equals("IntegerObjective")) {

                                progress = (int)(double) quest.getActiveChallenge().objectives.get(objective.id);

                            }
                            System.out.println(q.description + " | " + progress + "/" + objective.integer);

                        }

                    }
                    else {

                        System.out.println(q.description + " | " + "completed");

                    }

                }

            }
            


        }
        catch (ApiRequestException e) {

            e.printStackTrace();
        }

        */

        LocalDateTime resetTimeDaily = LocalDateTime.of(LocalDate.now(ZoneId.of("UTC")), LocalTime.MIDNIGHT).plusDays(1l);
        long unixTime = resetTimeDaily.toEpochSecond(ZoneOffset.of("-4"));
        long unixTimeWeek = resetTimeDaily.with(TemporalAdjusters.nextOrSame(DayOfWeek.FRIDAY)).toEpochSecond(ZoneOffset.of("-4"));
        long unixNow = System.currentTimeMillis() / 1000;

        System.out.println("ResetTime: " + UnixConverter.toDate(unixTime*1000, "MM/dd/yyyy HH:mm:ss"));
        System.out.println("ResetTimeWeekly: " + UnixConverter.toDate(unixTimeWeek*1000, "MM/dd/yyyy HH:mm:ss"));
        System.out.println("Time Now: " + UnixConverter.toDate(unixNow*1000, "MM/dd/yyyy HH:mm:ss"));

        long resetsIn = unixTime - unixNow;

        int seconds = (int)(resetsIn % 60);
        int minutes = (int)(resetsIn-seconds)/60;
        int mins = minutes % 60;
        int hours = (minutes-mins)/60;

        System.out.println("resets in: " + hours + "h " + mins + "m " + seconds + "s");


    }

}
