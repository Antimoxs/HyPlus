package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.modules.HyGeneral;
import dev.antimoxs.hyplus.modules.HyModuleManager;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsgType;
import dev.antimoxs.hyplus.objects.HySetting;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;

public class HyConfigManager {

    private boolean updateCheck = false;

    public void loadConfig(int retry, boolean reset) {

        if (retry >= 10) {

            HyGeneral.HYPLUS_GENERAL_TOGGLE.changeConfigValue(HyPlus.getInstance(), false);
            HyPlus.getInstance().hyGeneral.checkConfig(false);
            System.out.println("--- Unable to properly read HyPlus config file. ---");
            return;

        }

        try {

            for (IHyPlusModule module : HyPlus.getInstance().hyModuleManager.getModules()) {

                module.checkConfig(reset);

            }

            if (updateCheck) {

                updateCheck = false;
                HyPlus.debugLog("Some properties updated, reloading!");
                loadConfig(0, false);
                return;

            }

        } catch (Exception e) {

            System.out.println("Can't load one of the fields, retrying. If this keeps happening restart your client.");
            loadConfig(retry + 1, false);
            return;

        }

        HyPlus.getInstance().startAPI();

        System.out.println("HyPlus Config loaded.");
        HyPlus.getInstance().saveConfig();

    }

    public void checkConfig(boolean reset, HySetting setting) {

        String property = setting.getConfigName();

        HyPlus.debugLog("Checking for '" + property + "'...");

        if (HyPlus.getInstance().getConfig().has(property)) {

            if (reset) {

                HyPlus.getInstance().getConfig().remove(property);
                checkConfig(true, setting);
                return;

            }

            switch (setting.getType()) {

                case STRING: setting.changeValue(HyPlus.getInstance().getConfig().get(property).getAsString()); break;
                case INT: setting.changeValue(HyPlus.getInstance().getConfig().get(property).getAsInt()); break;
                case BOOLEAN: setting.changeValue(HyPlus.getInstance().getConfig().get(property).getAsBoolean()); break;
                case CHAR: setting.changeValue(HyPlus.getInstance().getConfig().get(property).getAsCharacter()); break;
                default: System.out.println("uhm this is not indented nor wanted. fix asap? " + property); return;

            }

            HyPlus.debugLog("Loaded '" + property + "'.");

        } else {

            if (reset) {

                HyPlus.debugLog("Property '" + property + "' were put to default.");

            }
            else {

                HyPlus.debugLog("Property '" + property + "' not yet in config, creating!");

            }

            switch (setting.getType()) {

                case STRING: HyPlus.getInstance().getConfig().addProperty(property, setting.getDefaultString()); break;
                case INT: HyPlus.getInstance().getConfig().addProperty(property, setting.getDefaultInt()); break;
                case BOOLEAN: HyPlus.getInstance().getConfig().addProperty(property, setting.getDefaultBoolean()); break;
                case CHAR: HyPlus.getInstance().getConfig().addProperty(property, setting.getDefaultChar()); break;
                default: System.out.println("uhm this is not indented nor wanted. (create) fix asap? " + property); return;

            }

            updateCheck = true;

        }
        HyPlus.getInstance().saveConfig();

    }

    public void changeConfigValue(String property, Object value) {

        if (HyPlus.getInstance().getConfig().has(property)) {

            HyPlus.getInstance().getConfig().remove(property);

        }

        if (value instanceof String) {
            HyPlus.getInstance().getConfig().addProperty(property, (String) value);
        } else if (value instanceof Integer) {
            HyPlus.getInstance().getConfig().addProperty(property, (int) value);
        } else if (value instanceof Boolean) {
            HyPlus.getInstance().getConfig().addProperty(property, (boolean) value);
        } else if (value instanceof Character) {
            HyPlus.getInstance().getConfig().addProperty(property, (char) value);
        } else {
            System.out.println("uhm this is not indented neither wanted. fix asap? (changer)" + property + " " + value);
        }

    }

}
