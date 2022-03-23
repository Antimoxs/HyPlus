package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.objects.HySetting;
import net.labymod.gui.elements.CheckBox;

public class HyUtilities {

    public static String matchOutColorCode(String s) {


        s = s.replaceAll("§0", "");
        s = s.replaceAll("§1", "");
        s = s.replaceAll("§2", "");
        s = s.replaceAll("§3", "");
        s = s.replaceAll("§4", "");
        s = s.replaceAll("§5", "");
        s = s.replaceAll("§6", "");
        s = s.replaceAll("§7", "");
        s = s.replaceAll("§8", "");
        s = s.replaceAll("§9", "");
        s = s.replaceAll("§a", "");
        s = s.replaceAll("§b", "");
        s = s.replaceAll("§c", "");
        s = s.replaceAll("§d", "");
        s = s.replaceAll("§e", "");
        s = s.replaceAll("§f", "");
        s = s.replaceAll("§g", "");

        s = s.replaceAll("§k", "");
        s = s.replaceAll("§l", "");
        s = s.replaceAll("§m", "");
        s = s.replaceAll("§n", "");
        s = s.replaceAll("§o", "");
        s = s.replaceAll("§r", "");

        return s;

    }

    public static CheckBox.EnumCheckBoxValue validateCheckbox(HyPlus HyPlus, String configName) {

        if (HyPlus.getConfig().get(configName).getAsInt() == 1) {

            return CheckBox.EnumCheckBoxValue.ENABLED;

        }
        else if (HyPlus.getConfig().get(configName).getAsInt() == 2) {

            return CheckBox.EnumCheckBoxValue.DISABLED;

        }
        else if (HyPlus.getConfig().get(configName).getAsInt() == 0) {

            return CheckBox.EnumCheckBoxValue.DEFAULT;

        }
        else {

            return CheckBox.EnumCheckBoxValue.INDETERMINATE;

        }

    }
    public static int validateCheckbox(CheckBox.EnumCheckBoxValue value) {

        switch (value) {

            case ENABLED: { return 1; }
            case DISABLED: { return 2; }
            case DEFAULT: { return 0; }
            case INDETERMINATE: { return 3; }
            default: return -1;

        }

    }
    public static boolean validateCheckbox(CheckBox.EnumCheckBoxValue value, int... matchesOne) {

        for (int i : matchesOne) {

            if (validateCheckbox(value) == i) { return true; }

        }
        return false;

    }
    public static CheckBox.EnumCheckBoxValue vCheckbox(HySetting setting) {

        if (setting.getValueBoolean()) return CheckBox.EnumCheckBoxValue.ENABLED;
        return CheckBox.EnumCheckBoxValue.DISABLED;

    }

    public static String dashUUID(String uuidJoin) {

        uuidJoin = uuidJoin.substring(0,8)
                + "-"
                + uuidJoin.substring(8,12)
                + "-"
                + uuidJoin.substring(12,16)
                + "-"
                + uuidJoin.substring(16,20)
                + "-"
                + uuidJoin.substring(20,32);

        return uuidJoin;

    }

}
