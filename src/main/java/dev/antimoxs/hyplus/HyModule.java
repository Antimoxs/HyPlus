package dev.antimoxs.hyplus;

import net.labymod.settings.elements.SettingsElement;

import java.util.List;

public interface HyModule {

    List<SettingsElement> getModuleSettings();
    boolean loop();
    HyModule getModule();
    void activate();
    void deactivate();
    String getModuleName();

}
