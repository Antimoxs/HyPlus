package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hypixelapiHP.util.kvp;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.objects.AdvancedElement;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HyAbout implements IHyPlusModule {

    private ArrayList<kvp> changes = new ArrayList<kvp>();

    public HyAbout(kvp... update) {

        this.changes.addAll(Arrays.asList(update));

    }


    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> settings = new ArrayList<>();

        SettingsElement header = new HeaderElement("General Information:");
        SettingsElement creator = new ControlElement( "HyPlus by Antimoxs", new ControlElement.IconData(Material.PAPER));
        SettingsElement version = new ControlElement( "Version: " + HyPlus.getInstance().getVersion(), new ControlElement.IconData(Material.BEACON));
        SettingsElement lastUpdated = new ControlElement( "Last updated: " + HyPlus.getInstance().getLastUpdated(), new ControlElement.IconData(Material.WATCH));

        Settings subSettings = new Settings();
        subSettings.add(header);
        subSettings.add(creator);
        subSettings.add(version);
        subSettings.add(lastUpdated);

        AdvancedElement about = new AdvancedElement("About Hyplus", "HYPLUS_ABOUT", new ControlElement.IconData(Material.BOOK_AND_QUILL));
        about.setSettingEnabled(true);

        about.setSubSettings(subSettings);
        settings.add(about);

        return settings;

    }


    @Override
    public String getModuleName() {

        return "HyAbout";

    }

    @Override
    public void checkConfig(boolean reset) {

    }

    @Override
    public boolean showInSettings() {
        return true;
    }
}
