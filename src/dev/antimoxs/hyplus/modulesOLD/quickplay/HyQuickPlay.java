package dev.antimoxs.hyplus.modulesOLD.quickplay;

import dev.antimoxs.hyplus.HyModule;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.KeyElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class HyQuickPlay implements HyModule {

    HyPlus HyPlus;
    boolean active = true;

    public HyQuickPlay(HyPlus HyPlus) {

        this.HyPlus = HyPlus;

    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("Quickplay", HyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_QUICKPLAY_TOGGLE", true);
        SettingsElement key = new KeyElement("Quickplay menu key", HyPlus, new ControlElement.IconData(Material.ACACIA_STAIRS), "HYPLUS_QUICKPLAY_GUIKEY", HyPlus.hySettings.HYPLUS_QUICKPLAY_GUIKEY);

        Settings quickplay_sub = new Settings();
        quickplay_sub.add(key);

        toggle.setSubSettings(quickplay_sub);
        moduleSettings.add(toggle);

        return moduleSettings;

    }

    @Override
    public boolean loop() {

        if (HyPlus.hySettings.HYPLUS_QUICKPLAY_TOGGLE) {

            if (!active) activate();
            //System.out.println("HYQUICKPLAY LOOP");

        }
        else {

            if (active) deactivate();

        }

        return true;
    }

    @Override
    public HyModule getModule() {
        return this;
    }

    @Override
    public void activate() {
        this.active = true;
    }

    @Override
    public void deactivate() {
        this.active = false;
    }

    @Override
    public String getModuleName() {
        return "Quickplay";
    }



}
