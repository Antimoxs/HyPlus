package dev.antimoxs.hyplus.modules.challengeTracker;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.player.quests.PlayerQuest;
import dev.antimoxs.hypixelapi.objects.resource.ResourceQuest;
import dev.antimoxs.hypixelapi.objects.resource.ResourceQuestObjective;
import dev.antimoxs.hypixelapi.response.PlayerResponse;
import dev.antimoxs.hypixelapi.response.QuestsResponse;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.awt.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;

public class HyQuestTracker implements IHyPlusModule, IHyPlusEvent {

    boolean active = true;
    public boolean updateReq = true;

    public TreeMap<Integer, QuestData> dspc = new TreeMap<>();
    public TreeMap<Integer, QuestData> dspc2 = new TreeMap<>();


    // ChallengeTracker
    public boolean HYPLUS_CTR_TOGGLE = true;
    public boolean HYPLUS_CTR_DAILY = true;
    public boolean HYPLUS_CTR_WEEKLY = true;
    public boolean HYPLUS_CTR_COMPLETED = true;
    public boolean HYPLUS_CTR_SORTORDER = true;
    public boolean HYPLUS_CTR_DP_DAILY = true;
    public boolean HYPLUS_CTR_DP_WEEKLY = true;

    HyPlus hyPlus;

    public HyQuestTracker(HyPlus HyPlus) {

        this.hyPlus = HyPlus;

    }

    @Override
    public String getModuleName() {
        return "QuestTracker";
    }

    @Override
    public void checkConfig(boolean reset) {

        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_DAILY", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_WEEKLY", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_COMPLETED", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_SORTORDER", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_DP_DAILY", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_CTR_DP_WEEKLY", true);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("QuestTracker", hyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_CTR_TOGGLE", true);

        ButtonElement ctr_refesh = new ButtonElement("Reload Quests", new ControlElement.IconData(Material.FEATHER), new Consumer<ButtonElement>() {
            @Override
            public void accept(ButtonElement buttonElement) {
                updateChallanges(hyPlus.hyLocationDetector.getCurrentLocation().rawloc.toLowerCase());
            }
        }, "Reload", "Reload current quests.", Color.CYAN);
        SettingsElement ctr_show_daily = new BooleanElement("Show Daily", hyPlus, new ControlElement.IconData(Material.WATCH), "HYPLUS_CTR_DAILY", true);
        SettingsElement ctr_show_weekly = new BooleanElement("Show Weekly", hyPlus, new ControlElement.IconData(Material.PAPER), "HYPLUS_CTR_WEEKLY", true);
        SettingsElement ctr_show_completed = new BooleanElement("Show Completed", hyPlus, new ControlElement.IconData(Material.GOLD_INGOT), "HYPLUS_CTR_COMPLETED", true);
        HeaderElement ctr_info = new HeaderElement("Chat announcements");
        BooleanElement ctr_display_daily = new BooleanElement("Display daily reset time", hyPlus, new ControlElement.IconData(Material.EMERALD), "HYPLUS_CTR_DP_DAILY", true);
        BooleanElement ctr_display_weekly = new BooleanElement("Display weekly reset time", hyPlus, new ControlElement.IconData(Material.DIAMOND), "HYPLUS_CTR_DP_WEEKLY", true);



        Settings ctr_sub = new Settings();
        ctr_sub.add(ctr_refesh);
        ctr_sub.add(ctr_show_daily);
        ctr_sub.add(ctr_show_weekly);
        ctr_sub.add(ctr_show_completed);
        ctr_sub.add(hyPlus.hyTrackboxGUI.getBooleanElement());
        ctr_sub.add(ctr_info);
        ctr_sub.add(ctr_display_daily);
        ctr_sub.add(ctr_display_weekly);

        toggle.setSubSettings(ctr_sub);

        moduleSettings.add(toggle);
        return moduleSettings;

    }


    public void updateChallanges(String gamemode) {

        if (HYPLUS_CTR_TOGGLE) {
            Thread t = new Thread(() -> {
                synchronized (dspc) {

                    //if (!currentGame.equals(gamemode)) {

                        if (!dspc.isEmpty()) {

                            dspc.clear();

                        }

                    //}

                    if (hyPlus.tbcHypixelApi == null) {

                        hyPlus.displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
                        return;

                    }

                    try {
                        PlayerResponse response = hyPlus.tbcHypixelApi.createPlayerRequestUUID(LabyMod.getInstance().getPlayerUUID().toString());
                        if (response.success) {


                            LocalDateTime resetTimeDaily = LocalDateTime.of(LocalDate.now(ZoneId.of("UTC-4")), LocalTime.MIDNIGHT);
                            LocalDateTime timeNow = LocalDateTime.now(ZoneId.of("UTC-4"));
                            long unixReset = resetTimeDaily.toEpochSecond(ZoneOffset.UTC);
                            long unixNow = timeNow.toEpochSecond(ZoneOffset.UTC);
                            long unixTimeWeek = resetTimeDaily.with(TemporalAdjusters.previousOrSame(DayOfWeek.FRIDAY)).toEpochSecond(ZoneOffset.UTC);


                            //System.out.println("ResetTime: " + UnixConverter.toDate(unixTime*1000, "MM/dd/yyyy HH:mm:ss"));
                            //System.out.println("ResetTimeWeekly: " + UnixConverter.toDate(unixTimeWeek*1000, "MM/dd/yyyy HH:mm:ss"));
                            //System.out.println("Time Now: " + UnixConverter.toDate(unixNow*1000, "MM/dd/yyyy HH:mm:ss"));

                            long resetsIn = unixReset - unixNow + 86400;
                            int seconds = (int)(resetsIn % 60);
                            int minutes = (int)(resetsIn-seconds)/60;
                            int mins = minutes % 60;
                            int hours = (minutes-mins)/60;

                            long resetsInW = unixTimeWeek - unixNow + 604800;
                            int secondsW = (int)(resetsInW % 60);
                            int minutesW = (int)(resetsInW-secondsW)/60;
                            int minsW = minutesW % 60;
                            int hoursW = (minutesW-minsW)/60;


                            //System.out.println("resets in: " + hours + "h " + mins + "m " + seconds + "s");
                            //HyPlus.displayIgMessage("ResetTime", UnixConverter.toDate(unixTime*1000, "MM/dd/yyyy HH:mm:ss"));
                            //HyPlus.displayIgMessage("ResetTimeWeekly", UnixConverter.toDate(unixTimeWeek*1000, "MM/dd/yyyy HH:mm:ss"));
                            //HyPlus.displayIgMessage("Time Now:", UnixConverter.toDate(unixNow*1000, "MM/dd/yyyy HH:mm:ss"));

                            if (HYPLUS_CTR_DP_DAILY) hyPlus.displayIgMessage(getModuleName(), "Daily Quests reset in: "  + hours + "h " + mins + "m " + seconds + "s");
                            if (HYPLUS_CTR_DP_WEEKLY) hyPlus.displayIgMessage(getModuleName(), "Weekly Quests reset in: "  + hoursW + "h " + minsW + "m " + secondsW + "s");
                            QuestsResponse qs = hyPlus.tbcHypixelApi.createQuestsRequest();

                            if (qs.getQuests().getQuestsForMode(gamemode).length == 0) {

                                if (!dspc.containsKey(0)) dspc.put(0, new QuestData(hyPlus, 0, 0, new ResourceQuestObjective()));

                            }


                            for (ResourceQuest q : qs.getQuests().getQuestsForMode(gamemode)) {

                                PlayerQuest quest = response.getPlayer().quests.get(q.id);

                                long rtime = unixReset;

                                //HyPlus.displayIgMessage(q.id, UnixConverter.toDate(rtime*1000,"MM/dd/yyyy HH:mm:ss"));

                                if (q.requirements[0].type.equals("DailyResetQuestRequirement")) {
                                    //HyPlus.displayIgMessage(getModuleName(), "Daily");
                                    if (!HYPLUS_CTR_DAILY) continue;
                                    rtime = unixReset;
                                }
                                if (q.requirements[0].type.equals("WeeklyResetQuestRequirement")) {
                                    //HyPlus.displayIgMessage(getModuleName(), "Weekly");
                                    if (!HYPLUS_CTR_WEEKLY) continue;
                                    rtime = unixTimeWeek;
                                }

                                // Debug message lol ---
                                //HyPlus.displayIgMessage(q.id, rtime + " | " + quest.getLatestCompletion() + " | " + (rtime - quest.getLatestCompletion()));

                                if (rtime > quest.getLatestCompletion()) {

                                    for (ResourceQuestObjective objective : q.objectives) {

                                        int progress = 0;
                                        String type = objective.type;
                                        if (quest.getActiveChallenge().objectives.get(objective.id) == null) {
                                        } else if (type.equals("BooleanObjective")) {

                                            boolean b = (Boolean) quest.getActiveChallenge().objectives.get(objective.id);
                                            if (b) progress = 1;

                                        } else if (type.equals("IntegerObjective")) {

                                            progress = (int) (double) quest.getActiveChallenge().objectives.get(objective.id);

                                        }
                                        System.out.println(q.description + " | " + progress + "/" + objective.integer);
                                        double percent = ((double) progress / (double) objective.integer) * 100;

                                        QuestData data = new QuestData(hyPlus, q, progress, (int) percent, objective);

                                        dspc.put(data.getL(false), data);

                                    }

                                } else {

                                    if (HYPLUS_CTR_COMPLETED) {

                                        System.out.println(q.description + " | " + "completed");
                                        QuestData data = new QuestData(hyPlus, q, 0, 100, null);

                                        dspc.put(data.getL(true), data);

                                    }
                                    else {

                                        hyPlus.displayIgMessage(getModuleName(), "no completed"); // remove before release

                                    }


                                }

                            }

                        }

                    } catch (ApiRequestException e) {


                    }

                    updateReq = true;


                }

                synchronized (dspc2) {

                    if (!dspc2.isEmpty()) dspc2.clear();
                    dspc2.putAll(dspc);


                }

            });
            Runtime.getRuntime().addShutdownHook(t);
            t.start();
        }
        else {

            if (!dspc.isEmpty()) {

                dspc.clear();

            }

        }

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        updateChallanges(location.rawloc.toLowerCase());

    }
}
