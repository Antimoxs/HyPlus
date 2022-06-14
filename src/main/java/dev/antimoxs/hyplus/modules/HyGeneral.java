package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyGeneral implements IHyPlusModule {

    public static final HySetting<Boolean> HYPLUS_GENERAL_TOGGLE = new HySetting<>("HYPLUS_GENERAL_TOGGLE", "HyPlus enabled", "Toggle all features of the HyPlus addon.", true, Material.REDSTONE);
    public static final HySetting<Boolean> HYPLUS_GENERAL_LOOP_TOGGLE = new HySetting<>("HYPLUS_GENERAL_LOOP_TOGGLE", "ModuleLoop (-performance)", "Toggle the HyLoop, used for module updates, ex. Discord callbacks (Low performance impact)", true, Material.FISHING_ROD);
    public static final HySetting<String> HYPLUS_GENERAL_APIKEY = new HySetting<>("HYPLUS_GENERAL_APIKEY", "Hypixel API-Key", "Set the HypixelAPI key. (Needed for a range of features.) Get your key with '/api' ingame.", "", Material.TRIPWIRE_HOOK);

    @Override
    public String getModuleName() {
        return "General";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_LOOP_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_APIKEY);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> list = new ArrayList<>();

        BooleanElement gen_all = new BooleanElement(HYPLUS_GENERAL_TOGGLE.getDisplayName(), HYPLUS_GENERAL_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_GENERAL_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

            HyPlus.getInstance().hyDiscordPresence.presenceCheck(); // check for presence


        }, HYPLUS_GENERAL_TOGGLE.getValue());
        gen_all.setDescriptionText(HYPLUS_GENERAL_TOGGLE.getDescription());
        BooleanElement gen_loop = new BooleanElement(HYPLUS_GENERAL_LOOP_TOGGLE.getDisplayName(), HYPLUS_GENERAL_LOOP_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_GENERAL_LOOP_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_GENERAL_LOOP_TOGGLE.getValue());
        gen_loop.setDescriptionText(HYPLUS_GENERAL_LOOP_TOGGLE.getDescription());
        StringElement gen_apikey = new StringElement(HYPLUS_GENERAL_APIKEY.getDisplayName(), HYPLUS_GENERAL_APIKEY.getIcon(), HYPLUS_GENERAL_APIKEY.getValue(), (accepted) -> {

           HYPLUS_GENERAL_APIKEY.changeConfigValue(HyPlus.getInstance(), accepted);
           checkConfig(false);
           HyPlus.getInstance().startAPI();

        });
        gen_apikey.setDescriptionText(HYPLUS_GENERAL_APIKEY.getDescription());

        list.add(gen_all);
        list.add(gen_loop);
        list.add(gen_apikey);

        return list;

    }

    public String getApiKey() {

        return HYPLUS_GENERAL_APIKEY.getValue();

    }

}
