package dev.antimoxs.hyplus.modules.quests;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.objects.player.quests.PlayerQuest;
import dev.antimoxs.hypixelapiHP.objects.resource.ResourceQuest;
import dev.antimoxs.hypixelapiHP.objects.resource.ResourceQuestObjective;
import dev.antimoxs.hypixelapiHP.response.PlayerResponse;
import dev.antimoxs.hypixelapiHP.response.QuestsResponse;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.HyAdvanced;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.awt.*;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.List;

public class HyQuestTracker implements IHyPlusModule, IHyPlusEvent {

    // ChallengeTracker
    public static final HySetting<Boolean> HYPLUS_CTR_TOGGLE = new HySetting<>("HYPLUS_CTR_TOGGLE", "QuestTracker", "Toggle the quest-tracker.", true, Material.BOW);
    public static final HySetting<Boolean> HYPLUS_CTR_DAILY = new HySetting<>("HYPLUS_CTR_DAILY", "Show daily quests", "Toggle the display of daily quests.", true, Material.WATCH);
    public static final HySetting<Boolean> HYPLUS_CTR_WEEKLY = new HySetting<>("HYPLUS_CTR_WEEKLY", "Show weekly quests", "Toggle the display of weekly quests.", true, Material.PAPER);
    public static final HySetting<Boolean> HYPLUS_CTR_COMPLETED = new HySetting<>("HYPLUS_CTR_COMPLETED", "Show completed quests", "Toggle the display of daily quests.", true, Material.GOLD_INGOT);
    public static final HySetting<Boolean> HYPLUS_CTR_SORTORDER = new HySetting<>("HYPLUS_CTR_SORTORDER", "Reversed sort order", "Reverse the sort order.", true, Material.ARROW);
    public static final HySetting<Boolean> HYPLUS_CTR_DP_DAILY = new HySetting<>("HYPLUS_CTR_DP_DAILY", "Announce daily reset time", "Toggle the announcement of the daily reset time.", true, Material.EMERALD);
    public static final HySetting<Boolean> HYPLUS_CTR_DP_WEEKLY = new HySetting<>("HYPLUS_CTR_DP_WEEKLY", "Announce weekly reset time", "Toggle the announcement of the weekly reset time.", true, Material.DIAMOND);

    boolean active = true;
    public boolean updateReq = true;

    public final TreeMap<Integer, QuestData> dspc = new TreeMap<>();
    public final TreeMap<Integer, QuestData> dspc2 = new TreeMap<>();

    @Override
    public String getModuleName() {
        return "QuestTracker";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_DAILY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_WEEKLY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_COMPLETED);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_SORTORDER);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_DP_DAILY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_CTR_DP_WEEKLY);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_CTR_TOGGLE.getDisplayName(), HYPLUS_CTR_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_TOGGLE.getValue());
        toggle.setDescriptionText(HYPLUS_CTR_TOGGLE.getDescription());


        Settings ctr_sub = new Settings();
        ctr_sub.addAll(getSubSettings());


        toggle.setSubSettings(ctr_sub);

        moduleSettings.add(toggle);
        return moduleSettings;

    }

    public ArrayList<SettingsElement> getSubSettings() {

        ArrayList<SettingsElement> subSettings = new ArrayList<>();

        ButtonElement ctr_refesh = new ButtonElement(
                "Reload Quests", new ControlElement.IconData(Material.FEATHER), buttonElement -> updateChallanges(HyPlus.getInstance().hyLocationDetector.getCurrentLocation().rawloc.toLowerCase()), "Reload", "Reload current quests.", Color.CYAN
        );

        BooleanElement ctr_show_daily = new BooleanElement(HYPLUS_CTR_DAILY.getDisplayName(), HYPLUS_CTR_DAILY.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_DAILY.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_DAILY.getValue());
        ctr_show_daily.setDescriptionText(HYPLUS_CTR_DAILY.getDescription());

        BooleanElement ctr_show_weekly = new BooleanElement(HYPLUS_CTR_WEEKLY.getDisplayName(), HYPLUS_CTR_WEEKLY.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_WEEKLY.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_WEEKLY.getValue());
        ctr_show_weekly.setDescriptionText(HYPLUS_CTR_WEEKLY.getDescription());

        BooleanElement ctr_show_completed = new BooleanElement(HYPLUS_CTR_COMPLETED.getDisplayName(), HYPLUS_CTR_COMPLETED.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_COMPLETED.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_COMPLETED.getValue());
        ctr_show_completed.setDescriptionText(HYPLUS_CTR_COMPLETED.getDescription());

        BooleanElement ctr_sortorder = new BooleanElement(HYPLUS_CTR_SORTORDER.getDisplayName(), HYPLUS_CTR_SORTORDER.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_SORTORDER.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_SORTORDER.getValue());
        ctr_sortorder.setDescriptionText(HYPLUS_CTR_SORTORDER.getDescription());

        HeaderElement ctr_info = new HeaderElement("Chat announcements");

        BooleanElement ctr_display_daily = new BooleanElement(HYPLUS_CTR_DP_DAILY.getDisplayName(), HYPLUS_CTR_DP_DAILY.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_DP_DAILY.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_DP_DAILY.getValue());
        ctr_display_daily.setDescriptionText(HYPLUS_CTR_DP_DAILY.getDescription());

        BooleanElement ctr_display_weekly = new BooleanElement(HYPLUS_CTR_DP_WEEKLY.getDisplayName(), HYPLUS_CTR_DP_WEEKLY.getIcon(), (booleanElement) -> {

            HYPLUS_CTR_DP_WEEKLY.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_CTR_DP_WEEKLY.getValue());
        ctr_display_weekly.setDescriptionText(HYPLUS_CTR_WEEKLY.getDescription());




        subSettings.add(ctr_refesh);
        subSettings.add(ctr_show_daily);
        subSettings.add(ctr_show_weekly);
        subSettings.add(ctr_show_completed);
        subSettings.add(ctr_sortorder);
        subSettings.add(ctr_info);
        subSettings.add(ctr_display_daily);
        subSettings.add(ctr_display_weekly);

        return subSettings;

    }


    public void updateChallanges(String gamemode) {

        if (HYPLUS_CTR_TOGGLE.getValue()) {
            Thread t = new Thread(() -> {
                synchronized (dspc) {

                    //if (!currentGame.equals(gamemode)) {

                        if (!dspc.isEmpty()) {

                            dspc.clear();

                        }

                    //}

                    if (HyPlus.getInstance().hypixelApi == null) {

                        HyPlus.getInstance().displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
                        return;

                    }

                    try {
                        PlayerResponse response = HyPlus.getInstance().hypixelApi.createPlayerRequestUUID(LabyMod.getInstance().getPlayerUUID().toString());
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

                            if (HYPLUS_CTR_DP_DAILY.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), "Daily Quests reset in: "  + hours + "h " + mins + "m " + seconds + "s");
                            if (HYPLUS_CTR_DP_WEEKLY.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), "Weekly Quests reset in: "  + hoursW + "h " + minsW + "m " + secondsW + "s");
                            QuestsResponse qs = HyPlus.getInstance().hypixelApi.createQuestsRequest();

                            if (qs.getQuests().getQuestsForMode(gamemode).length == 0) {

                                if (!dspc.containsKey(0)) dspc.put(0, new QuestData(HyPlus.getInstance(), 0, 0, new ResourceQuestObjective()));

                            }


                            unixReset = unixReset + 21600;

                            for (ResourceQuest q : qs.getQuests().getQuestsForMode(gamemode)) {

                                PlayerQuest quest = response.getPlayer().quests.get(q.id);

                                boolean com = false;

                                //HyPlus.displayIgMessage(q.id, UnixConverter.toDate(rtime*1000,"MM/dd/yyyy HH:mm:ss"));

                                if (q.requirements[0].type.equals("DailyResetQuestRequirement")) {
                                    //HyPlus.displayIgMessage(getModuleName(), "Daily");
                                    if (!HYPLUS_CTR_DAILY.getValue()) continue;
                                    com = unixReset > quest.getLatestCompletion();
                                    //hyPlus.displayIgMessage(q.id, unixReset + " | " + quest.getLatestCompletion() + " | " + (unixReset - quest.getLatestCompletion()));
                                }
                                if (q.requirements[0].type.equals("WeeklyResetQuestRequirement")) {
                                    //HyPlus.displayIgMessage(getModuleName(), "Weekly");
                                    if (!HYPLUS_CTR_WEEKLY.getValue()) continue;
                                    com = unixTimeWeek > quest.getLatestCompletion();
                                    //hyPlus.displayIgMessage(q.id, unixTimeWeek + " | " + quest.getLatestCompletion() + " | " + (unixTimeWeek - quest.getLatestCompletion()));
                                }



                                if (com) {

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
                                        HyPlus.debugLog(q.description + " | " + progress + "/" + objective.integer);
                                        double percent = ((double) progress / (double) objective.integer) * 100;

                                        QuestData data = new QuestData(HyPlus.getInstance(), q, progress, (int) percent, objective);

                                        dspc.put(data.getL(false), data);

                                    }

                                } else {

                                    if (HYPLUS_CTR_COMPLETED.getValue()) {

                                        HyPlus.debugLog(q.description + " | " + "completed");
                                        QuestData data = new QuestData(HyPlus.getInstance(), q, 0, 100, null);

                                        dspc.put(data.getL(true), data);

                                    }
                                    else {

                                        if (HyAdvanced.HYPLUS_ADVANCED_DEBUGLOG.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), "no completed");

                                    }


                                }

                            }

                        }

                    } catch (ApiRequestException ignored) {


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
