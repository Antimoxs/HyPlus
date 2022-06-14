package dev.antimoxs.hyplus.modules.quickplay;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.KeyElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyQuickPlay implements IHyPlusModule {

    public static final HySetting<Boolean> HYPLUS_QUICKPLAY_TOGGLE = new HySetting("HYPLUS_QUICKPLAY_TOGGLE", "Quickplay", "Toggles if the quickplay hotkey is enabled.", true, Material.POTION);
    public static final HySetting<Integer> HYPLUS_QUICKPLAY_KEY = new HySetting("HYPLUS_QUICKPLAY_KEY", "Quickplay menu key", "Set the activation key for the quickplay menu", 89, Material.ACACIA_STAIRS);

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement qp_toggle = new BooleanElement(HYPLUS_QUICKPLAY_TOGGLE.getDisplayName(), HYPLUS_QUICKPLAY_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_QUICKPLAY_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_QUICKPLAY_TOGGLE.getValue());
        qp_toggle.setDescriptionText(HYPLUS_QUICKPLAY_TOGGLE.getDescription());
        KeyElement qp_key = new KeyElement(HYPLUS_QUICKPLAY_KEY.getDisplayName(), HYPLUS_QUICKPLAY_KEY.getIcon(), HYPLUS_QUICKPLAY_KEY.getValue(), (accepted) -> {

            HYPLUS_QUICKPLAY_KEY.changeConfigValue(HyPlus.getInstance(), accepted);
            checkConfig(false);

        });
        qp_key.setDescriptionText(HYPLUS_QUICKPLAY_KEY.getDescription());

        Settings quickplay_sub = new Settings();
        quickplay_sub.add(qp_key);

        qp_toggle.setSubSettings(quickplay_sub);
        moduleSettings.add(qp_toggle);

        return moduleSettings;

    }

    @Override
    public String getModuleName() {
        return "Quickplay";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_QUICKPLAY_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_QUICKPLAY_KEY);

    }

    @Override
    public boolean showInSettings() {

        return true;

    }


}
