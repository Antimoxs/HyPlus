package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyGeneral implements IHyPlusModule {

    private final HyPlus hyPlus;

    public final HySetting HYPLUS_GENERAL_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_GENERAL_TOGGLE", "HyPlus enabled", "Toggle all features of the HyPlus addon.", true, true, Material.REDSTONE);
    public final HySetting HYPLUS_GENERAL_LOOP_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_GENERAL_LOOP_TOGGLE", "HyLoop", "Toggle the HyLoop.", true, true, Material.FISHING_ROD);
    public final HySetting HYPLUS_GENERAL_APIKEY = new HySetting(HySettingType.STRING, "HYPLUS_GENERAL_APIKEY", "Hypixel API-Key", "Set the HypixelAPI key. (Needed for a range of features.)", "", "", Material.TRIPWIRE_HOOK);


    public HyGeneral(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public String getModuleName() {
        return "General";
    }

    @Override
    public void checkConfig(boolean reset) {

        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_TOGGLE);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_LOOP_TOGGLE);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_GENERAL_APIKEY);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> list = new ArrayList<>();

        BooleanElement gen_all = new BooleanElement(HYPLUS_GENERAL_TOGGLE.getDisplayName(), HYPLUS_GENERAL_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_GENERAL_TOGGLE.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);

        }, HYPLUS_GENERAL_TOGGLE.getValueBoolean());
        gen_all.setDescriptionText(HYPLUS_GENERAL_TOGGLE.getDescription());
        BooleanElement gen_loop = new BooleanElement(HYPLUS_GENERAL_LOOP_TOGGLE.getDisplayName(), HYPLUS_GENERAL_LOOP_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_GENERAL_LOOP_TOGGLE.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);

        }, HYPLUS_GENERAL_LOOP_TOGGLE.getValueBoolean());
        gen_loop.setDescriptionText(HYPLUS_GENERAL_LOOP_TOGGLE.getDescription());
        StringElement gen_apikey = new StringElement(HYPLUS_GENERAL_APIKEY.getDisplayName(), HYPLUS_GENERAL_APIKEY.getIcon(), HYPLUS_GENERAL_APIKEY.getValueString(), (accepted) -> {

           HYPLUS_GENERAL_APIKEY.changeConfigValue(hyPlus, accepted);
           checkConfig(false);
           hyPlus.startAPI();

        });
        gen_apikey.setDescriptionText(HYPLUS_GENERAL_APIKEY.getDescription());

        list.add(gen_all);
        list.add(gen_loop);
        list.add(gen_apikey);

        return list;

    }

    public String getApiKey() {

        return this.HYPLUS_GENERAL_APIKEY.getValueString();

    }

}
