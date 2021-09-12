package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.modules.HyModuleManager;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsgType;
import dev.antimoxs.hyplus.objects.HySetting;

public class HyConfigManager {

    private final HyPlus hyPlus;
    private boolean updateCheck = false;

    public HyConfigManager(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    public void loadConfig(int retry, boolean reset) {

        if (retry >= 10) {

            hyPlus.onDisable();
            hyPlus.hyGeneral.HYPLUS_GENERAL_TOGGLE.changeConfigValue(hyPlus, false);
            hyPlus.hyGeneral.checkConfig(false);
            System.out.println("--- Unable to properly read HyPlus config file. ---");
            return;

        }

        // Check if all needed config entries are set to prevent errors

        //hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_API_TOGGLE", false);
        //hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_ABOUT_UPDATE", true);
        //hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_ABOUT", 0);
        //hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_VERSION", "-");

        //hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_DUELS_ANTIBLUR", 0);


        for (IHyPlusModule module : hyPlus.hyModuleManager.getModules()) {

            module.checkConfig(reset);

        }

        if (updateCheck) {

            updateCheck = false;
            System.out.println("Some properties updated, reloading!");
            loadConfig(0, false);
            return;

        }

        try {


            // transferring values from config to settings-class

            // External API
            //hyPlus.hySettings.HYPLUS_API_TOGGLE = hyPlus.getConfig().get("HYPLUS_API_TOGGLE").getAsBoolean();


            // About
            //hyPlus.hySettings.HYPLUS_ABOUT_UPDATE = true;




            // Duels
            //hyPlus.hySettings.HYPLUS_DUELS_ANTIBLUR = hyPlus.getConfig().get("HYPLUS_DUELS_ANTIBLUR").getAsBoolean();



        } catch (Exception e) {

            System.out.println("Can't load one of the fields, retrying. If this keeps happening restart your client.");
            loadConfig(retry + 1, false);
            return;

        }

        hyPlus.startAPI();

        System.out.println("Config loaded.");
        hyPlus.saveConfig();

        //this.onDisable();
        //this.onEnable();

        //"f0678253-89db-45bb-9730-b5eb952481ea"


    }

    public void checkConfig(boolean reset, HySetting setting) {

        String property = setting.getConfigName();


        //System.out.println("Checking for '" + property + "'...");

        if (hyPlus.getConfig().has(property)) {

            //TBCLogger.log(TBCLoggingType.INFORMATION, config.name, "Loaded '" + property + "'");
            if (reset) {

                hyPlus.getConfig().remove(property);
                checkConfig(true, setting);
                return;

            }

            switch (setting.getType()) {

                case STRING: setting.changeValue(hyPlus.getConfig().get(property).getAsString()); break;
                case INT: setting.changeValue(hyPlus.getConfig().get(property).getAsInt()); break;
                case BOOLEAN: setting.changeValue(hyPlus.getConfig().get(property).getAsBoolean()); break;
                case CHAR: setting.changeValue(hyPlus.getConfig().get(property).getAsCharacter()); break;
                default: System.out.println("uhm this is not indented nor wanted. fix asap? " + property); return;

            }

            System.out.println("Loaded '" + property + "'.");

        } else {

            if (reset) {

                System.out.println("Property '" + property + "' were put to default.");

            }
            else {

                System.out.println("Property '" + property + "' not yet in config, creating!");

            }

            switch (setting.getType()) {

                case STRING: hyPlus.getConfig().addProperty(property, setting.getDefaultString()); break;
                case INT: hyPlus.getConfig().addProperty(property, setting.getDefaultInt()); break;
                case BOOLEAN: hyPlus.getConfig().addProperty(property, setting.getDefaultBoolean()); break;
                case CHAR: hyPlus.getConfig().addProperty(property, setting.getDefaultChar()); break;
                default: System.out.println("uhm this is not indented nor wanted. (create) fix asap? " + property); return;

            }



            updateCheck = true;

        }
        hyPlus.saveConfig();

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
