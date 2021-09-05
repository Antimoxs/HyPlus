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
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hyplus.objects.HyServerLocation;
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

    private final HyPlus hyPlus;

    public int delay = 0;
    //String lastgame = "";
    long starttime = 0L;

    // Presence settings
    public int HYPLUS_DP_DELAY = 0;
    public boolean HYPLUS_DP_TOGGLE = true;
    public boolean HYPLUS_DP_GAME = true;
    public boolean HYPLUS_DP_MODE = true;
    public boolean HYPLUS_DP_LOBBY = true;
    public boolean HYPLUS_DP_MAP = true;
    public boolean HYPLUS_DP_TIME = true;
    public boolean HYPLUS_DP_SPECIFIC = false;

    // init
    public HyDiscordPresence(HyPlus HyPlus) {

        this.hyPlus = HyPlus;

    }



    public boolean presenceCheck() {

        if (!hyPlus.hypixel.checkOnServer()) {

            hyPlus.discordApp.shutdown();
            if (LabyMod.getMainConfig().getSettings().discordRichPresence) {

                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }

        }

        if (!LabyMod.getMainConfig().getSettings().discordRichPresence) {

            hyPlus.discordApp.shutdown();
            LabyMod.getInstance().getDiscordApp().shutdown();
            return false;

        }

        if (HYPLUS_DP_TOGGLE) {

            LabyMod.getInstance().getDiscordApp().shutdown();
            if (!hyPlus.discordApp.init()) {

                System.out.println("Can't start HyPlus DiscordRPC?");
                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }
            return true;

        }
        else {

            hyPlus.discordApp.shutdown();
            LabyMod.getInstance().getDiscordApp().initialize();
            return false;

        }

    }

    public void updatePresence(HyServerLocation locationIn) {

        if (!presenceCheck()) return;

        String updateDelay = HYPLUS_DP_DELAY == 0 ? "" : "[in " + HYPLUS_DP_DELAY + "s]";
        hyPlus.displayIgMessage(getModuleName(), "Updating ServerStatus... " + updateDelay);
        //hyPlus.displayIgMessage(getModuleName(), location.getJson());
        wait.sc((long)HYPLUS_DP_DELAY);

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


            if (HYPLUS_DP_GAME) {

                location.gametype = hypixelFetcher.fetchGame(location.gametype);

                if (HYPLUS_DP_MODE) {

                    location.mode = hypixelFetcher.fetchMode(location.mode);

                    if (location.gametype.equalsIgnoreCase("housing") && location.map.equalsIgnoreCase("base")) {

                        location.mode = "Visiting a world";

                    }

                    if (location.mode.equalsIgnoreCase("LOBBY")) {

                        int index = location.lobbyname.toLowerCase().indexOf("lobby");

                        if (location.lobbyname.length() >= index+5 && HYPLUS_DP_LOBBY) {

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
        if (HYPLUS_DP_MAP && location.map != null) {

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

                location.map = null;

            }

        }
        else {

            location.map = null;

        }

        // Update GameType
        if (hyPlus.discordApp.getRichPresence().updateType(location.gametype)) {

            start = true;
            starttime = System.currentTimeMillis();

        }

        // Update game specific information -> disabled
        if (HYPLUS_DP_SPECIFIC && false) {

            if (location.gametype.equalsIgnoreCase("murdermystery")) {

                ScorePlayerTeam team = Minecraft.getMinecraft().theWorld.getScoreboard().getTeam("team_8");
                String timeString = team.getColorPrefix() + team.getColorSuffix();
                timeString = HyUtilities.matchOutColorCode(timeString);
                String[] cast = timeString.split(":");
                starttime = System.currentTimeMillis() + (Long.parseLong(cast[1])*60) + (Long.parseLong(cast[2]));


            }

        }

        String imageKey = location.gametype.equals("Playing on Hypixel") ? "hypixel" : location.gametype;

        // Send other updaters
        hyPlus.discordApp.getRichPresence().updateMode(location.mode);
        hyPlus.discordApp.getRichPresence().updateMap(location.map);
        if (HYPLUS_DP_TIME) {
            hyPlus.discordApp.getRichPresence().updateTimestamps(start, starttime);
        }
        else {
            hyPlus.discordApp.getRichPresence().removeTimestamp();
        }
        hyPlus.discordApp.getRichPresence().updateImageL(imageKey.toLowerCase(), "Playing '" + location.gametype + ": " + location.mode + "' on Hypixel.");
        hyPlus.discordApp.getRichPresence().updateImageS("hyplus", "LabyMod with HyPlus! v." + hyPlus.getVersion());

        hyPlus.discordApp.getRichPresence().updateRichPresence();

    }



    // -------------------------------------------------------------
    // Module methods

    @Override
    public String getModuleName() {
        return "GameDetector";
    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement dp = new BooleanElement("Discord Presence", hyPlus, new ControlElement.IconData(Material.PAPER), "HYPLUS_DP_TOGGLE", true);

        ButtonElement dp_update = new ButtonElement("Resend status",
                new ControlElement.IconData(Material.BOOK_AND_QUILL),
                buttonElement -> updatePresence(hyPlus.hyLocationDetector.getCurrentLocation()),
                "Resend",
                "Resend the Discord status.",
                Color.ORANGE
        );

        HeaderElement dp_info = new HeaderElement("The status is automatically updated upon joining.");


        NumberElement dp_delay = new NumberElement( "Update delay", new ControlElement.IconData( Material.REDSTONE_WIRE) , HYPLUS_DP_DELAY);
        dp_delay.setMinValue(0);
        dp_delay.setMaxValue(120);
        dp_delay.addCallback(accepted -> {
            hyPlus.changeConfigValue("HYPLUS_DP_DELAY", accepted);
            delay = 0;
        });

        BooleanElement dp_game = new BooleanElement("Show Game", hyPlus, new ControlElement.IconData(Material.REDSTONE), "HYPLUS_DP_GAME", true);
        BooleanElement dp_mode = new BooleanElement("Show Mode", hyPlus, new ControlElement.IconData(Material.IRON_BOOTS), "HYPLUS_DP_MODE", true);
        BooleanElement dp_lobbyN = new BooleanElement("Show Lobby number §l*", hyPlus, new ControlElement.IconData(Material.WEB), "HYPLUS_DP_LOBBY", true);

        BooleanElement dp_map = new BooleanElement("Show Map", hyPlus, new ControlElement.IconData(Material.MAP), "HYPLUS_DP_MAP", true);
        BooleanElement dp_time = new BooleanElement("Show Time", hyPlus, new ControlElement.IconData(Material.WATCH), "HYPLUS_DP_TIME", true);
        BooleanElement dp_spfc = new BooleanElement("Show specific information", hyPlus, new ControlElement.IconData(Material.NETHER_STAR), "HYPLUS_DP_SPECIFIC", true);

        HeaderElement dp_lobbyNNote = new HeaderElement("§l* Only in lobbies and over §lingame§r detection.");


        Settings discord_sub = new Settings();
        discord_sub.add(dp_update);
        discord_sub.add(dp_info);
        discord_sub.add(dp_delay);
        discord_sub.add(dp_game);
        discord_sub.add(dp_mode);
        discord_sub.add(dp_lobbyN);
        discord_sub.add(dp_map);
        discord_sub.add(dp_time);
        discord_sub.add(dp_spfc);
        discord_sub.add(dp_lobbyNNote);
        dp.setSubSettings(discord_sub);

        moduleSettings.add(dp);

        return moduleSettings;

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
