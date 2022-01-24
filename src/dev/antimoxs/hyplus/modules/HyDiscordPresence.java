package dev.antimoxs.hyplus.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.response.StatusResponse;
import dev.antimoxs.hypixelapi.util.hypixelFetcher;
import dev.antimoxs.hyplus.HyModule;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.modulesOLD.status.Hychievement;
import dev.antimoxs.hyplus.objects.*;
import dev.antimoxs.utilities.time.wait;
import net.labymod.api.events.MessageReceiveEvent;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HyDiscordPresence implements IHyPlusModule, IHyPlusEvent {

    private final ControlElement.IconData icon_update = new ControlElement.IconData(Material.BOOK_AND_QUILL);

    public int delay = 0;
    //String lastgame = "";
    long starttime = 0L;

    // Presence settings

    public HySetting HYPLUS_DP_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_TOGGLE", "Discord Presence", "Toggle the Hypixel Discord Presence", true, true, Material.PAPER);
    public HySetting HYPLUS_DP_DELAY = new HySetting(HySettingType.INT, "HYPLUS_DP_DELAY", "Update delay", "Delays the Discord presence update for x seconds.", 0, 0, Material.REDSTONE_WIRE);
    public HySetting HYPLUS_DP_GAME = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_GAME", "Show Game", "Toggle the display of your game. (If off, it also deactivates the mode.)", true, true, Material.REDSTONE);
    public HySetting HYPLUS_DP_MODE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_MODE", "Show mode", "Toggle the display of your game-mode.", true, true, Material.LEATHER_BOOTS);
    public HySetting HYPLUS_DP_LOBBY = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_LOBBY", "Show Lobby number §l*", "Toggle the display of the lobbynumber when you are in a lobby. (Only works when using ingame detection.)", true, true, Material.WEB);
    public HySetting HYPLUS_DP_MAP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_MAP", "Show Map", "Toggle the display of the current map.", true, true, Material.MAP);
    public HySetting HYPLUS_DP_TIME = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_TIME", "Show Time", "Toggle the display of the elapsed play time.", true, true, Material.WATCH);
    public HySetting HYPLUS_DP_SPECIFIC = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_SPECIFIC", "Show specific information", "Toggle the display of special information such as round stats or player statistics.", false, false, Material.NETHER_STAR);
    public HySetting HYPLUS_DP_STATE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_STATE", "Show playing state", "Toggle the display of the playing state", false, true, Material.SIGN);



    public boolean presenceCheck() {

        if (!HyPlus.getInstance().hypixel.checkOnServer()) {

            HyPlus.getInstance().discordApp.shutdown();
            if (LabyMod.getMainConfig().getSettings().discordRichPresence) {

                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }

        }

        if (!LabyMod.getMainConfig().getSettings().discordRichPresence) {

            HyPlus.getInstance().discordApp.shutdown();
            LabyMod.getInstance().getDiscordApp().shutdown();
            return false;

        }

        if (HYPLUS_DP_TOGGLE.getValueBoolean()) {

            LabyMod.getInstance().getDiscordApp().shutdown();
            if (!HyPlus.getInstance().discordApp.init()) {

                System.out.println("Can't start HyPlus DiscordRPC?");
                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }
            return true;

        }
        else {

            HyPlus.getInstance().discordApp.shutdown();
            LabyMod.getInstance().getDiscordApp().initialize();
            return false;

        }

    }

    /**
     * Update the server location displayed on discord
     * @param locationIn {@link HyServerLocation}
     */
    public void updatePresenceNEW(HyServerLocation locationIn) {

        // let's make sure the presence is enabled and running
        if (!presenceCheck()) return;

        // waiting set time from user
        if (HYPLUS_DP_DELAY.getValueInt() != 0) {

            HyPlus.getInstance().displayIgMessage(getModuleName(), "Delaying update...");
            wait.sc((long)HYPLUS_DP_DELAY.getValueInt());

        }
        HyPlus.getInstance().displayIgMessage(getModuleName(), "Updating Discord ServerStatus....");





    }
    public void updatePresence(HyServerLocation locationIn) {


        if (!presenceCheck()) return;

        String updateDelay = HYPLUS_DP_DELAY.getDefaultInt() == 0 ? "" : "[in " + HYPLUS_DP_DELAY + "s]";
        HyPlus.getInstance().displayIgMessage(getModuleName(), "Updating ServerStatus... " + updateDelay);
        //hyPlus.displayIgMessage(getModuleName(), location.getJson());
        wait.sc((long)HYPLUS_DP_DELAY.getValueInt());

        HyServerLocation location = new HyServerLocation();
        location.server = locationIn.server;
        location.gametype = locationIn.gametype;
        location.lobbyname = locationIn.lobbyname;
        location.mode = locationIn.mode;
        location.map = locationIn.map;
        location.rawloc = locationIn.rawloc;
        location.online = locationIn.online;

        boolean start = true;

        // Check if online
        if (!location.online) {
            System.out.println("the fuck? - seems like we're not on hypixel");
            return;
        }

        // Fetch type and mode
        if (location.server.equals("limbo")) {

            location.gametype = "Limbo";
            location.mode = "Idling";

        }
        else {


            if (HYPLUS_DP_GAME.getValueBoolean()) {

                location.gametype = hypixelFetcher.fetchGame(location.gametype);

                if (HYPLUS_DP_MODE.getValueBoolean()) {

                    location.mode = hypixelFetcher.fetchMode(location.mode);

                    if (location.gametype.equalsIgnoreCase("housing") && location.map.equalsIgnoreCase("base")) {

                        location.mode = "Visiting a world";

                    }

                    if (location.mode.equalsIgnoreCase("LOBBY")) {

                        int index = location.lobbyname.toLowerCase().indexOf("lobby");

                        if (location.lobbyname.length() >= index+5 && HYPLUS_DP_LOBBY.getValueBoolean()) {

                            String lobbyN = location.lobbyname.substring(index + 5);
                            location.mode = location.mode + " " + lobbyN;

                        }

                    }

                }
                else {

                    location.mode = "HyPlus by Antimoxs";

                }

            }
            else {

                location.gametype = "Playing on Hypixel";
                location.mode = "HyPlus by Antimoxs";

            }

        }

        // fetch map
        if (HYPLUS_DP_MAP.getValueBoolean() && location.map != null) {

            String mn = location.map.toLowerCase();

            if (mn.equals("the pit")) {

                location.map = "Classic";

            } else if (mn.startsWith("the pit ")) {

                location.map = location.map.replaceFirst("The Pit ", "");

            } else if (
                            mn.equals("undefined") ||
                            mn.equals("?") ||
                            mn.equalsIgnoreCase(location.mode) ||
                            mn.equals("base")
            ) {

                location.map = "Hypixel";

            }

        }
        else {

            location.map = "Hypixel";

        }

        // Update GameType
        if (HyPlus.getInstance().discordApp.getRichPresence().updateType(location.gametype)) {

            start = true;
            starttime = System.currentTimeMillis();

        }

        // Update game specific information -> disabled
        if (HYPLUS_DP_SPECIFIC.getValueBoolean() && false) {

            if (location.gametype.equalsIgnoreCase("murdermystery")) {

                ScorePlayerTeam team = Minecraft.getMinecraft().theWorld.getScoreboard().getTeam("team_8");
                String timeString = team.getColorPrefix() + team.getColorSuffix();
                timeString = HyUtilities.matchOutColorCode(timeString);
                String[] cast = timeString.split(":");
                starttime = System.currentTimeMillis() + (Long.parseLong(cast[1])*60) + (Long.parseLong(cast[2]));


            }

        }

        if (HYPLUS_DP_STATE.getValueBoolean()) {

            if (location.mode.equalsIgnoreCase("lobby")) {

                HyPlus.getInstance().discordApp.getRichPresence().updateState("Lobby");

            }
            else if (HyPlus.getInstance().hyPartyManager.getParty().getPGames()) {

                HyPlus.getInstance().discordApp.getRichPresence().updateState("Private");

            }
            else {

                HyPlus.getInstance().discordApp.getRichPresence().updateState("Playing");

            }
            System.out.println("State is not null");


        }
        else {

            System.out.println("State is null");
            HyPlus.getInstance().discordApp.getRichPresence().updateState(null);

        }

        String imageKey = location.gametype.equals("Playing on Hypixel") ? "hypixel" : location.gametype;

        // Send other updaters
        HyPlus.getInstance().discordApp.getRichPresence().updateMode(location.mode);
        HyPlus.getInstance().discordApp.getRichPresence().updateMap(location.map);
        if (HYPLUS_DP_TIME.getValueBoolean()) {
            HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(start, starttime);
        }
        else {
            HyPlus.getInstance().discordApp.getRichPresence().removeTimestamp();
        }
        HyPlus.getInstance().discordApp.getRichPresence().updateImageL(imageKey.toLowerCase(), "Playing '" + location.gametype + ": " + location.mode + "' on " + location.map);
        HyPlus.getInstance().discordApp.getRichPresence().updateImageS("hyplus", "LabyMod with HyPlus! v." + HyPlus.getInstance().getVersion());

        HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();

    }



    // -------------------------------------------------------------
    // Module methods

    @Override
    public String getModuleName() {
        return "DiscordPresence";
    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        // Main toggle for the Discord presence
        BooleanElement dp = new BooleanElement(HYPLUS_DP_TOGGLE.getDisplayName(), HYPLUS_DP_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_DP_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            this.checkConfig(false);

            presenceCheck();

        }, HYPLUS_DP_TOGGLE.getValueBoolean());
        dp.setDescriptionText(HYPLUS_DP_TOGGLE.getDescription());

        // Update button to resend the status
        ButtonElement dp_update = new ButtonElement(

                "Resend status", icon_update, (buttonElement) -> {

            updatePresence(HyPlus.getInstance().hyLocationDetector.getCurrentLocation());

            }, "Resend", "Resend the Discord status.", Color.ORANGE

        );

        HeaderElement dp_info = new HeaderElement("The status is automatically updated upon joining.");

        // Sub-Settings

        // Delay in s between location update and Discord update // to prevent sniping if u want lol
        NumberElement dp_delay = new NumberElement(HYPLUS_DP_DELAY.getDisplayName(), HYPLUS_DP_DELAY.getIcon() , HYPLUS_DP_DELAY.getValueInt());
        dp_delay.setMinValue(0);
        dp_delay.setMaxValue(120);
        dp_delay.addCallback(accepted -> {
            HYPLUS_DP_DELAY.changeConfigValue(HyPlus.getInstance(), accepted);
            delay = 0;
        });
        dp_delay.setDescriptionText(HYPLUS_DP_DELAY.getDescription());

        // Location information
        BooleanElement dp_game = new BooleanElement(HYPLUS_DP_GAME.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_GAME.getIcon(), HYPLUS_DP_GAME.getConfigName(), HYPLUS_DP_GAME.getDefaultBoolean());
        dp_game.setDescriptionText(HYPLUS_DP_GAME.getDescription());

        BooleanElement dp_mode = new BooleanElement(HYPLUS_DP_MODE.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_MODE.getIcon(), HYPLUS_DP_MODE.getConfigName(), HYPLUS_DP_MODE.getDefaultBoolean());
        dp_mode.setDescriptionText(HYPLUS_DP_MODE.getDescription());

        BooleanElement dp_lobbyN = new BooleanElement(HYPLUS_DP_LOBBY.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_LOBBY.getIcon(), HYPLUS_DP_LOBBY.getConfigName(), HYPLUS_DP_LOBBY.getDefaultBoolean());
        dp_lobbyN.setDescriptionText(HYPLUS_DP_LOBBY.getDescription());

        BooleanElement dp_map = new BooleanElement(HYPLUS_DP_MAP.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_MAP.getIcon(), HYPLUS_DP_MAP.getConfigName(), HYPLUS_DP_MAP.getDefaultBoolean());
        dp_map.setDescriptionText(HYPLUS_DP_MAP.getDescription());

        BooleanElement dp_time = new BooleanElement(HYPLUS_DP_TIME.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_TIME.getIcon(), HYPLUS_DP_TIME.getConfigName(), HYPLUS_DP_TIME.getDefaultBoolean());
        dp_time.setDescriptionText(HYPLUS_DP_TIME.getDescription());

        BooleanElement dp_state = new BooleanElement(HYPLUS_DP_STATE.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_STATE.getIcon(), HYPLUS_DP_STATE.getConfigName(), HYPLUS_DP_STATE.getDefaultBoolean());
        dp_state.setDescriptionText(HYPLUS_DP_STATE.getDescription());

        BooleanElement dp_spfc = new BooleanElement(HYPLUS_DP_SPECIFIC.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_SPECIFIC.getIcon(), HYPLUS_DP_SPECIFIC.getConfigName(), HYPLUS_DP_SPECIFIC.getDefaultBoolean());
        dp_spfc.setDescriptionText(HYPLUS_DP_SPECIFIC.getDescription());
        dp_spfc.setBlocked(true);

        HeaderElement dp_lobbyNNote = new HeaderElement("§l* Only in lobbies and over §lingame§r detection.");

        AdvancedElement adv_all = new AdvancedElement(HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getDisplayName(), HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getConfigName(), HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getIcon());
        adv_all.setDescriptionText(HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getDescription());
        adv_all.setSettingEnabled(true);

        Settings party_sub = new Settings();
        party_sub.addAll(HyPlus.getInstance().hyPartyManager.getSubSettings());

        adv_all.setSubSettings(party_sub);



        Settings discord_sub = new Settings();
        discord_sub.add(dp_update);
        discord_sub.add(dp_info);
        discord_sub.add(dp_delay);
        discord_sub.add(dp_game);
        discord_sub.add(dp_mode);
        discord_sub.add(dp_lobbyN);
        discord_sub.add(dp_map);
        discord_sub.add(dp_time);
        discord_sub.add(dp_state);
        discord_sub.add(dp_spfc);
        discord_sub.add(dp_lobbyNNote);
        discord_sub.add(adv_all);
        dp.setSubSettings(discord_sub);

        moduleSettings.add(dp);

        return moduleSettings;

    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_DELAY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_GAME);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_MODE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_LOBBY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_MAP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_TIME);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_SPECIFIC);

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        Thread t = new Thread(() -> {

            updatePresence(location);

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    @Override
    public void onHypixelQuit() {

        updatePresence(new HyServerLocation());

    }
}
