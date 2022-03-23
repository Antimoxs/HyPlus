package dev.antimoxs.hyplus.modules.headStats;

import dev.antimoxs.hypixelapiHP.requests.MojangRequest;
import dev.antimoxs.hypixelapiHP.util.BedWarsCalculator;
import dev.antimoxs.hypixelapiHP.util.LevelCalculator;
import dev.antimoxs.hypixelapiHP.util.kvp;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.events.PlayerSpawnEvent;
import dev.antimoxs.hyplus.internal.HyPlayerStorage;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import dev.antimoxs.hyplus.api.player.HySimplePlayer;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyHeadStats implements IHyPlusModule, IHyPlusEvent {

    public static final HySetting HYPLUS_HS_GENERAL = new HySetting(HySettingType.BOOLEAN, "HP_HS_GENERAL", "Head-stats", "Display floating head stats", true, true, Material.BOOKSHELF);
    public static final HySetting HYPLUS_HS_HPXL_LEVEL = new HySetting(HySettingType.BOOLEAN, "HP_HS_HPXL_LEVEL", "Hypixel Level", "Display level", true, true, Material.PAPER);
    public static final HySetting HYPLUS_HS_BW_STARS = new HySetting(HySettingType.BOOLEAN, "HP_HS_BW_STARS", "BedWars stars", "Display BedWarsStars", true, true, Material.BED);

    @Override
    public String getModuleName() {
        return "HeadStats [BETA]";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_HS_GENERAL);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_HS_HPXL_LEVEL);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_HS_BW_STARS);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_HS_GENERAL.getDisplayName(), HYPLUS_HS_GENERAL.getIcon(), (booleanElement) -> {

            HYPLUS_HS_GENERAL.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            HyPlus.getInstance().hyPlayerTagExchanger.clearSubtitles();

        }, HYPLUS_HS_GENERAL.getValueBoolean());
        toggle.setDescriptionText(HYPLUS_HS_GENERAL.getDescription());

        Settings subs = new Settings();
        subs.addAll(getSubSettings());

        toggle.setSubSettings(subs);

        moduleSettings.add(toggle);

        return moduleSettings;

    }

    public ArrayList<SettingsElement> getSubSettings() {

        ArrayList<SettingsElement> subSettings = new ArrayList<>();

        HeaderElement betaNotice = new HeaderElement("This feature is currently in beta.");
        HeaderElement betaNotice2 = new HeaderElement("When changing settings, rejoin to fully apply them!");
        BooleanElement level = new BooleanElement(HYPLUS_HS_HPXL_LEVEL.getDisplayName(), HyPlus.getInstance(), HYPLUS_HS_HPXL_LEVEL.getIcon(), HYPLUS_HS_HPXL_LEVEL.getConfigName(), HYPLUS_HS_HPXL_LEVEL.getDefaultBoolean());
        level.setDescriptionText(HYPLUS_HS_HPXL_LEVEL.getDescription());
        BooleanElement bw_stars = new BooleanElement(HYPLUS_HS_BW_STARS.getDisplayName(), HyPlus.getInstance(), HYPLUS_HS_BW_STARS.getIcon(), HYPLUS_HS_BW_STARS.getConfigName(), HYPLUS_HS_BW_STARS.getDefaultBoolean());
        level.setDescriptionText(HYPLUS_HS_BW_STARS.getDescription());

        subSettings.add(betaNotice);
        subSettings.add(betaNotice2);
        subSettings.add(level);
        subSettings.add(bw_stars);

        return subSettings;

    }

    @Override
    public void onPlayerSpawn(PlayerSpawnEvent event) {

        if (HYPLUS_HS_GENERAL.getValueBoolean()) {
            try {
                if (!HyPlayerStorage.playerInStorage(event.playerUUID)) {

                    HySimplePlayer player = new HySimplePlayer();
                    player.setName(MojangRequest.getName(event.playerUUID.toString()));
                    HyPlayerStorage.requestPlayer(event.playerUUID);
                    HyPlayerStorage.addPlayer(event.playerUUID, player);

                }

                if (HYPLUS_HS_HPXL_LEVEL.getValueBoolean()) {

                    int nexp = HyPlayerStorage.getPlayerObject(event.playerUUID).networkExp;
                    String level = "Level " + ((int) (LevelCalculator.getExactLevel(nexp) * 10)) / 10;
                    HyPlus.getInstance().hyPlayerTagExchanger.getTagForPlayer(event.playerUUID).setValue(new kvp(level, 1));

                } else {

                    HyPlus.getInstance().hyPlayerTagExchanger.getTagForPlayer(event.playerUUID).removeValueById(1);

                }
                if (HYPLUS_HS_BW_STARS.getValueBoolean()) {

                    int xp = HyPlayerStorage.getPlayerObject(event.playerUUID).stats.Bedwars.exp;
                    if (xp != 0) {
                        int stars = BedWarsCalculator.getLevelForExp(xp);
                        String level = "BedWars " + stars;
                        HyPlus.getInstance().hyPlayerTagExchanger.getTagForPlayer(event.playerUUID).setValue(new kvp(level, 2));
                    }

                } else {

                    HyPlus.getInstance().hyPlayerTagExchanger.getTagForPlayer(event.playerUUID).removeValueById(2);

                }

            } catch (Exception e) {

                //HyPlus.getInstance().displayIgMessage(getModuleName(), "Failed to load headstats.");

            }
        }


    }

}
