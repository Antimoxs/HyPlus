package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.modules.HyModuleManager;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsgType;

public class HyConfigManager {

    private final HyPlus hyPlus;
    private boolean updateCheck = false;

    public HyConfigManager(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    public void loadConfig(int retry, boolean reset) {

        if (retry >= 10) {

            hyPlus.onDisable();
            System.out.println("--- Unable to properly read HyPlus config file. ---");
            return;

        }

        // Check if all needed config entries are set to prevent errors
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_GENERAL_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_GENERAL_LOOP_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_GENERAL_APIKEY", "");
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_API_TOGGLE", false);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_ABOUT_UPDATE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_ABOUT", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_VERSION", "-");
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_STATUS_INTERVAL", 5);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_STATUS_ENABLE", true);

        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_DUELS_ANTIBLUR", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_QUICKPLAY_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_QUICKPLAY_GUIKEY", -1);





        if (updateCheck) {

            updateCheck = false;
            System.out.println("Some properties updated, reloading!");
            loadConfig(0, false);
            return;

        }

        try {


            // transferring values from config to settings-class

            hyPlus.hySettings.HYPLUS_GENERAL_TOGGLE = hyPlus.getConfig().get("HYPLUS_GENERAL_TOGGLE").getAsBoolean();
            hyPlus.hySettings.HYPLUS_GENERAL_LOOP_TOGGLE = hyPlus.getConfig().get("HYPLUS_GENERAL_LOOP_TOGGLE").getAsBoolean();


            if (hyPlus.getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString().equals("")) {

                hyPlus.hySettings.HYPLUS_GENERAL_APIKEY = null;

            } else if (!hyPlus.getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString().equals(hyPlus.hySettings.HYPLUS_GENERAL_APIKEY)) {

                hyPlus.hySettings.HYPLUS_GENERAL_APIKEY = hyPlus.getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString();
                hyPlus.startAPI();

            }

            // External API
            hyPlus.hySettings.HYPLUS_API_TOGGLE = hyPlus.getConfig().get("HYPLUS_API_TOGGLE").getAsBoolean();


            // About
            hyPlus.hySettings.HYPLUS_ABOUT_UPDATE = true;


            // AutoFriend module
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_TOGGLE = hyPlus.getConfig().get("HYPLUS_AUTOFRIEND_TOGGLE").getAsBoolean();


            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_NON = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_NON");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_VIP = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_VIP");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_VIPP = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_VIPP");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_MVP = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_MVP");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_MVPP = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_MVPP");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_MVPPP = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_MVPPP");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_YT = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_YT");
            hyPlus.hyFriend.HYPLUS_AUTOFRIEND_AA_STAFF = HyUtilities.validateCheckbox(hyPlus, "HYPLUS_AUTOFRIEND_AA_STAFF");

            // Duels
            hyPlus.hySettings.HYPLUS_DUELS_ANTIBLUR = hyPlus.getConfig().get("HYPLUS_DUELS_ANTIBLUR").getAsBoolean();

            // Quickplay
            hyPlus.hySettings.HYPLUS_QUICKPLAY_TOGGLE = hyPlus.getConfig().get("HYPLUS_QUICKPLAY_TOGGLE").getAsBoolean();
            hyPlus.hySettings.HYPLUS_QUICKPLAY_GUIKEY = hyPlus.getConfig().get("HYPLUS_QUICKPLAY_GUIKEY").getAsInt();

            // BetterMSG
            hyPlus.hyBetterMsg.HYPLUS_BETTERMSG_TOGGLE = hyPlus.getConfig().get("HYPLUS_BETTERMSG_TOGGLE").getAsBoolean();
            hyPlus.hyBetterMsg.HYPLUS_BETTERMSG_STYLE = HyBetterMsgType.getByName(hyPlus.getConfig().get("HYPLUS_BETTERMSG_STYLE").getAsString());

            // GameDetector
            hyPlus.hyDiscordPresence.HYPLUS_DP_TOGGLE =    hyPlus.getConfig().get("HYPLUS_DP_TOGGLE").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_DELAY =     hyPlus.getConfig().get("HYPLUS_DP_DELAY").getAsInt();
            hyPlus.hyDiscordPresence.HYPLUS_DP_GAME =      hyPlus.getConfig().get("HYPLUS_DP_GAME").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_MODE =      hyPlus.getConfig().get("HYPLUS_DP_MODE").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_LOBBY =     hyPlus.getConfig().get("HYPLUS_DP_LOBBY").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_MAP =       hyPlus.getConfig().get("HYPLUS_DP_MAP").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_TIME =      hyPlus.getConfig().get("HYPLUS_DP_TIME").getAsBoolean();
            hyPlus.hyDiscordPresence.HYPLUS_DP_SPECIFIC =  hyPlus.getConfig().get("HYPLUS_DP_SPECIFIC").getAsBoolean();

            // LocationDetection
            hyPlus.hyLocationDetector.HYPLUS_LD_TOGGLE =   hyPlus.getConfig().get("HYPLUS_LD_TOGGLE").getAsBoolean();
            hyPlus.hyLocationDetector.HYPLUS_LD_API =      hyPlus.getConfig().get("HYPLUS_LD_API").getAsBoolean();

            // PartyDetector
            hyPlus.hySettings.HYPLUS_HPD_TOGGLE = hyPlus.getConfig().get("HYPLUS_HPD_TOGGLE").getAsBoolean();

            // ChallengeTracker
            hyPlus.hyQuestTracker.HYPLUS_CTR_TOGGLE = hyPlus.getConfig().get("HYPLUS_CTR_TOGGLE").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_DAILY = hyPlus.getConfig().get("HYPLUS_CTR_DAILY").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_WEEKLY = hyPlus.getConfig().get("HYPLUS_CTR_WEEKLY").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_COMPLETED = hyPlus.getConfig().get("HYPLUS_CTR_COMPLETED").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_SORTORDER = hyPlus.getConfig().get("HYPLUS_CTR_SORTORDER").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_DP_DAILY = hyPlus.getConfig().get("HYPLUS_CTR_DP_DAILY").getAsBoolean();
            hyPlus.hyQuestTracker.HYPLUS_CTR_DP_WEEKLY = hyPlus.getConfig().get("HYPLUS_CTR_DP_WEEKLY").getAsBoolean();

            // PlayerTagChanger
            hyPlus.hyPlayerTagExchanger.HYPLUS_PTC_TOGGLE = hyPlus.getConfig().get("HYPLUS_PTC_TOGGLE").getAsBoolean();
            hyPlus.hyPlayerTagExchanger.HYPLUS_PTC_CHANGER = hyPlus.getConfig().get("HYPLUS_PTC_CHANGER").getAsBoolean();
            hyPlus.hyPlayerTagExchanger.HYPLUS_PTC_INTERVAL = hyPlus.getConfig().get("HYPLUS_PTC_INTERVAL").getAsInt();


        } catch (Exception e) {

            System.out.println("Can't load one of the fields, retrying. If this keeps happening restart your client.");
            loadConfig(retry + 1, false);
            return;

        }


        System.out.println("Config loaded.");

        //this.onDisable();
        //this.onEnable();

        //"f0678253-89db-45bb-9730-b5eb952481ea"


    }

    public void checkConfig(boolean reset, String property, Object defaultValue) {

        //System.out.println("Checking for '" + property + "'...");

        if (hyPlus.getConfig().has(property)) {

            //TBCLogger.log(TBCLoggingType.INFORMATION, config.name, "Loaded '" + property + "'");
            if (reset) {

                hyPlus.getConfig().remove(property);
                checkConfig(true, property, defaultValue);
                return;

            }

            System.out.println("Loaded '" + property + "'.");

        } else {

            if (reset) {

                System.out.println("Property '" + property + "' were put to default.");

            }
            else {

                System.out.println("Property '" + property + "' not yet in config, creating!");

            }


            if (defaultValue instanceof String) {
                hyPlus.getConfig().addProperty(property, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                hyPlus.getConfig().addProperty(property, (int) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                hyPlus.getConfig().addProperty(property, (boolean) defaultValue);
            } else if (defaultValue instanceof Character) {
                hyPlus.getConfig().addProperty(property, (char) defaultValue);
            } else {
                System.out.println("uhm this is not indented nor wanted. fix asap? " + property + " " + defaultValue);
            }
            updateCheck = true;

        }

    }

    public void changeConfigValue(String property, Object value) {

        if (hyPlus.getConfig().has(property)) {

            hyPlus.getConfig().remove(property);

        }


        if (value instanceof String) {
            hyPlus.getConfig().addProperty(property, (String) value);
        } else if (value instanceof Integer) {
            hyPlus.getConfig().addProperty(property, (int) value);
        } else if (value instanceof Boolean) {
            hyPlus.getConfig().addProperty(property, (boolean) value);
        } else if (value instanceof Character) {
            hyPlus.getConfig().addProperty(property, (char) value);
        } else {
            System.out.println("uhm this is not indented neither wanted. fix asap? (changer)" + property + " " + value);
        }


    }

}
