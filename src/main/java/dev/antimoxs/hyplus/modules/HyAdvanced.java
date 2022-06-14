package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.objects.AdvancedElement;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyAdvanced implements IHyPlusModule{

    public static final HySetting<Boolean> HYPLUS_ADVANCED_TOGGLE = new HySetting<>("HYPLUS_ADVANCED_TOGGLE", "Advanced settings", "View advanced settings", true, Material.COMMAND);
    public static final HySetting<Boolean> HYPLUS_ADVANCED_API = new HySetting<>("HYPLUS_ADVANCED_API", "External HyPlus API", "Toggle the HyPlus API for other addons.", true, Material.COMMAND);

    public static final HySetting<Boolean> HYPLUS_ADVANCED_DEBUGLOG = new HySetting<>("HYPLUS_ADVANCED_DEBUGLOG", "Enable DebugLog", "Toggle the HyPlus Debug Log", false, Material.ANVIL);


    @Override
    public String getModuleName() {
        return "Advanced";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_ADVANCED_API);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_ADVANCED_DEBUGLOG);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> list = new ArrayList<>();

        AdvancedElement adv_all = new AdvancedElement(HYPLUS_ADVANCED_TOGGLE.getDisplayName(), HYPLUS_ADVANCED_TOGGLE.getConfigName(), HYPLUS_ADVANCED_TOGGLE.getIcon());
        adv_all.setDescriptionText(HYPLUS_ADVANCED_TOGGLE.getDescription());
        adv_all.setSettingEnabled(true);

        BooleanElement adv_api = new BooleanElement(HYPLUS_ADVANCED_API.getDisplayName(), HYPLUS_ADVANCED_API.getIcon(), (booleanElement) -> {

            HYPLUS_ADVANCED_API.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_ADVANCED_API.getValue());
        adv_api.setDescriptionText(HYPLUS_ADVANCED_API.getDescription());

        BooleanElement adv_dl = new BooleanElement(HYPLUS_ADVANCED_DEBUGLOG.getDisplayName(), HYPLUS_ADVANCED_DEBUGLOG.getIcon(), (booleanElement) -> {

            HYPLUS_ADVANCED_DEBUGLOG.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_ADVANCED_DEBUGLOG.getValue());
        adv_api.setDescriptionText(HYPLUS_ADVANCED_DEBUGLOG.getDescription());

        Settings adv_sub = new Settings();
        adv_sub.add(adv_api);
        adv_sub.add(adv_dl);

        adv_all.setSubSettings(adv_sub);

        list.add(adv_all);


        return list;

    }


}
