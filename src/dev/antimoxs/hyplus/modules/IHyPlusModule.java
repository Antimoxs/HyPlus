package dev.antimoxs.hyplus.modules;

import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;

import java.util.ArrayList;
import java.util.List;

public interface IHyPlusModule {

    String getModuleName();
    void checkConfig(boolean reset);

    default boolean showInSettings() { return false; }
    default boolean loop() { return true; };
    default List<SettingsElement> getModuleSettings() {

        ArrayList<SettingsElement> defaultSettings = new ArrayList<>();
        defaultSettings.add(new HeaderElement("No settings for this module found."));
        return defaultSettings;

    };


}
