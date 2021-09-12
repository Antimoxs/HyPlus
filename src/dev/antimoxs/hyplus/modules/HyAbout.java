package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.HyModule;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.AdvancedElement;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HyAbout implements IHyPlusModule {

    private dev.antimoxs.hyplus.HyPlus HyPlus;
    private ArrayList<kvp> changes = new ArrayList<kvp>();

    public HyAbout(HyPlus HyPlus, kvp... update) {

        this.HyPlus = HyPlus;
        this.changes.addAll(Arrays.asList(update));

    }


    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> settings = new ArrayList<>();

        SettingsElement header = new HeaderElement("General Information:");
        SettingsElement creator = new ControlElement( "HyPlus by Antimoxs", new ControlElement.IconData(Material.PAPER));
        SettingsElement version = new ControlElement( "Version: " + HyPlus.getVersion(), new ControlElement.IconData(Material.BEACON));
        SettingsElement lastUpdated = new ControlElement( "Last updated: " + HyPlus.getLastUpdated(), new ControlElement.IconData(Material.WATCH));

        SettingsElement updatelogHeader = new HeaderElement("Changelog:");

        Settings subSettings = new Settings();
        subSettings.add(header);
        subSettings.add(creator);
        subSettings.add(version);
        subSettings.add(lastUpdated);

        subSettings.add(updatelogHeader);

        for (kvp kvp : changes) {

            SettingsElement element;

            // 0 = not specified (notice)
            // 1 = added
            // 2 = removed
            // 3 = updated
            // else = invalid

            switch (kvp.i) {

                case 0: element = new ControlElement(kvp.string, new ControlElement.IconData(Material.AIR)); break;
                case 1: element = new ControlElement(kvp.string, new ControlElement.IconData(Material.EMERALD_BLOCK)); break;
                case 2: element = new ControlElement(kvp.string, new ControlElement.IconData(Material.REDSTONE_BLOCK)); break;
                case 3: element = new ControlElement(kvp.string, new ControlElement.IconData(Material.GOLD_BLOCK)); break;
                default: element = new ControlElement("Unknown change", new ControlElement.IconData(Material.BARRIER)); break;

            }

            subSettings.add(element);



        }

        //ul1.setSubSettings(subSettings);


        //settings.add(ul1);

        //BooleanElement updateLog = new BooleanElement("About HyPlus", HyPlus, new ControlElement.IconData(Material.BOOK_AND_QUILL), "HYPLUS_ABOUT_UPDATE", true);
        AdvancedElement about = new AdvancedElement("About Hyplus", "HYPLUS_ABOUT", new ControlElement.IconData(Material.BOOK_AND_QUILL));
        about.setSettingEnabled(true);

        /*ButtonElement updateLog = new ButtonElement("About HyPlus",
                new ControlElement.IconData(Material.BOOK_AND_QUILL),
                new Consumer<ButtonElement>() {
                    @Override
                    public void accept(ButtonElement buttonElement) {



                    }
                },
                "View",
                "See all information about the HyPlus Addon.",
                Color.GRAY
        );*/

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
