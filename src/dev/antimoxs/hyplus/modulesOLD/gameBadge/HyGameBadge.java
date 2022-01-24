package dev.antimoxs.hyplus.modulesOLD.gameBadge;

import dev.antimoxs.hypixelapi.objects.player.statGames.PSGDuels;
import dev.antimoxs.hyplus.HyModule;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HyGameBadge implements HyModule {


    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> settings = new ArrayList<>();

        //BooleanElement badge_toggle = new BooleanElement("Toggle Badge", HyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_GBADGE_TOGGLE", HyPlus.hySettings.HYPLUS_GBADGE_TOGGLE);
        //BooleanElement bedwars_toggle = new BooleanElement("Show in BedWars", HyPlus, new ControlElement.IconData(Material.BED), "HYPLUS_GBADGE_BEDWARS_TOGGLE", HyPlus.hySettings.HYPLUS_GBADGE_BEDWARS_TOGGLE);
        //BooleanElement duels_toggle = new BooleanElement("Show in Duels", HyPlus, new ControlElement.IconData(Material.BED), "HYPLUS_GBADGE_DUELS_TOGGLE", HyPlus.hySettings.HYPLUS_GBADGE_DUELS_TOGGLE);

        Settings duels_sub = new Settings();

        for (Field f : PSGDuels.class.getDeclaredFields()) {

            BooleanElement elem = new BooleanElement(f.getName(), HyPlus.getInstance(), new ControlElement.IconData(Material.BED), "HYPLUS_GBADGE_BEDWARS_STATS_" + f.getName().toUpperCase(), false);
            duels_sub.add(elem);

        }

        //duels_toggle.setSubSettings(duels_sub);

        Settings badge_sub = new Settings();
        //badge_sub.add(bedwars_toggle);
        //badge_sub.add(duels_toggle);

        //badge_toggle.setSubSettings(badge_sub);

        //settings.add(badge_toggle);


        return settings;

    }

    @Override
    public boolean loop() {
        return true;
    }

    @Override
    public HyModule getModule() {
        return this;
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public String getModuleName() {
        return "HyGameBadge";
    }

}
