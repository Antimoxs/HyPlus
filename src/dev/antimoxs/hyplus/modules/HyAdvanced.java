package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.objects.AdvancedElement;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.StringElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyAdvanced implements IHyPlusModule{

    private final HyPlus hyPlus;

    public final HySetting HYPLUS_ADVANCED_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_ADVANCED_TOGGLE", "Advanced settings", "View advanced settings", true, true, Material.COMMAND);
    public final HySetting HYPLUS_ADVANCED_API = new HySetting(HySettingType.BOOLEAN, "HYPLUS_ADVANCED_API", "External HyPlus API", "Toggle the HyPlus API for other addons.", true, true, Material.COMMAND);


    public HyAdvanced(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public String getModuleName() {
        return "Advanced";
    }

    @Override
    public void checkConfig(boolean reset) {

        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_ADVANCED_API);

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

            HYPLUS_ADVANCED_API.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);

        }, HYPLUS_ADVANCED_API.getValueBoolean());
        adv_api.setDescriptionText(HYPLUS_ADVANCED_API.getDescription());

        Settings adv_sub = new Settings();
        adv_sub.add(adv_api);

        adv_all.setSubSettings(adv_sub);

        list.add(adv_all);


        return list;

    }


}
