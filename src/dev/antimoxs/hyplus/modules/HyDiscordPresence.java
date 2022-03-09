package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.response.GamelistResponse;
import dev.antimoxs.hypixelapiHP.util.hypixelFetcher;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.Hypixel;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.objects.*;
import dev.antimoxs.utilities.time.wait;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.scoreboard.ScorePlayerTeam;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HyDiscordPresence implements IHyPlusModule, IHyPlusEvent {

    private final ControlElement.IconData icon_update = new ControlElement.IconData(Material.BOOK_AND_QUILL);
    private HyGameStatus currentStatus = new HyGameStatus();
    private GamelistResponse indexedGames = null;

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
    public HySetting HYPLUS_DP_SPECIFIC = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_SPECIFIC", "Show specific information", "Toggle the display of special information such round.", false, false, Material.NETHER_STAR);
    public HySetting HYPLUS_DP_STATE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_DP_STATE", "Show playing state", "Toggle the display of the playing state", true, true, Material.SIGN);


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

                HyPlus.debugLog("Can't start HyPlus DiscordRPC?");
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
     * Update the server location displayed on discord !!! should run async
     * @param locationIn {@link HyServerLocation}
     */
    public void updatePresence(HyServerLocation locationIn) {

        // let's make sure the presence is enabled and running
        if (!presenceCheck()) return;
        // waiting set time from user212
        if (HYPLUS_DP_DELAY.getValueInt() != 0) {

            HyPlus.getInstance().displayIgMessage(getModuleName(), "Delaying update...");
            wait.sc((long)HYPLUS_DP_DELAY.getValueInt());

        }


        HyPlus.getInstance().displayIgMessage(getModuleName(), "Updating Discord ServerStatus....");

        // Update Server and hyplus icon
        HyPlus.getInstance().discordApp.getRichPresence().updateServer(locationIn.server);
        HyPlus.getInstance().discordApp.getRichPresence().updateImageS("hyplus", "LabyMod with HyPlus! v" + HyPlus.getInstance().getVersion());

        // Check if game is not toggled on
        if (!HYPLUS_DP_GAME.getValueBoolean()) {

            HyPlus.getInstance().discordApp.getRichPresence().updateState(HyGameStatus.State.UNDEFINED);
            HyPlus.getInstance().discordApp.getRichPresence().updateType("Playing on Hypixel");
            HyPlus.getInstance().discordApp.getRichPresence().updateMode("with HyPlus by Antimoxs.");
            HyPlus.getInstance().discordApp.getRichPresence().updateImageL("hypixel", "Playing on Hypixel.net with HyPlus.");
            HyPlus.getInstance().discordApp.getRichPresence().removeTimestamp();
            HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();
            return;

        }

        HyPlus.debugLog("[HYDP] STATE IS: " + HYPLUS_DP_STATE.getValueBoolean());

        // Check if Limbo
        if (locationIn.isLimbo()) {

            HyPlus.getInstance().discordApp.getRichPresence().updateState(HYPLUS_DP_STATE.getValueBoolean() ? HyGameStatus.State.IDLING : HyGameStatus.State.UNDEFINED);
            if (HyPlus.getInstance().discordApp.getRichPresence().updateType("Limbo")) {

                updateTimeStamps(false, currentStatus, locationIn);
                //HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

            }
            HyPlus.getInstance().discordApp.getRichPresence().updateImageL("limbo", "Currently Idling in Limbo");
            HyPlus.getInstance().discordApp.getRichPresence().updateMode("Currently Afk");
            HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();
            return;

        }

        String game = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype) : this.indexedGames.getGame(locationIn.gametype).name;
        String gameImage = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype) : this.indexedGames.getGame(locationIn.gametype).databaseName.toLowerCase();
        // check if lobby
        if (locationIn.isLobby()) {

            HyPlus.getInstance().discordApp.getRichPresence().updateState(HYPLUS_DP_STATE.getValueBoolean() ? HyGameStatus.State.LOBBY : HyGameStatus.State.UNDEFINED);
            if (HyPlus.getInstance().discordApp.getRichPresence().updateType(game)) {

                updateTimeStamps(false, currentStatus, locationIn);
                //HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

            }
            String s1 = this.HYPLUS_DP_LOBBY.getValueBoolean() ? " " + locationIn.getLobbyNumber() : "";
            HyPlus.getInstance().discordApp.getRichPresence().updateImageL(gameImage, "Online on " + (this.HYPLUS_DP_MODE.getValueBoolean() ? game + " lobby" + s1 : "Hypixel"));
            HyPlus.getInstance().discordApp.getRichPresence().updateMode("Lobby" + s1);
            HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();
            return;

        }
        // ingame location update

        String mode = this.indexedGames == null ? hypixelFetcher.fetchMode(locationIn.mode) : this.indexedGames.getGame(locationIn.gametype).fetchMode(locationIn.mode);
        String map = updateMap("Hypixel", mode);

        // set custom message when on housing
        if (locationIn.gametype.toLowerCase().equals("housing")) mode = "Visiting a world.";

        // update gamestate
        updateGameState(Hypixel.getGameStatus(locationIn), game, mode, map, locationIn);

        // update gameType
        if (HyPlus.getInstance().discordApp.getRichPresence().updateType(game)) {

            HyPlus.getInstance().discordApp.getRichPresence().updateImageIconL(gameImage);

        }

        // update mode and map
        HyPlus.getInstance().discordApp.getRichPresence().updateMode(HYPLUS_DP_MODE.getValueBoolean() ? mode : "on Hypixel.");
        HyPlus.getInstance().discordApp.getRichPresence().updateMap(map);

        // send rpc update
        HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();

    }

    private String updateMap(String map, String mode) {

        // adjust map for 'the pit'
        if (HYPLUS_DP_MAP.getValueBoolean() && map != null) {

            String mn = map.toLowerCase();
            if (mn.equals("the pit")) {
                map = "Classic";
            }
            else if (mn.startsWith("the pit ")) {
                map = map.replaceFirst("The Pit ", "");
            }
            else if (mn.equals("undefined") || mn.equals("?") || mn.equalsIgnoreCase(mode) || mn.equals("base")) {
                map = "Hypixel";
            }

        }
        return map;

    }

    /**
     * Update the current game data displayed on discord !!! should run async
     * @return
     */
    public void updateGameState(HyGameStatus status) {

        HyServerLocation locationIn = HyPlus.getInstance().hyLocationDetector.getCurrentLocation();
        String game = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype) : this.indexedGames.getGame(locationIn.gametype).name;
        String mode = this.indexedGames == null ? hypixelFetcher.fetchMode(locationIn.mode) : this.indexedGames.getGame(locationIn.gametype).fetchMode(locationIn.mode);

        updateGameState(status, game, mode, updateMap(locationIn.map, mode), locationIn);

    }
    public void updateGameState(HyGameStatus status, String game, String mode, String map, HyServerLocation locationIn) {

        // check if its a new state
        if (this.currentStatus.equals(status)) return;
        HyGameStatus.State lastState = this.currentStatus.state;
        this.currentStatus = status;

        // check for game start
        if (lastState == HyGameStatus.State.PREGAME && status.state == HyGameStatus.State.INGAME) {

            HyPlus.getInstance().hyLocationDetector.getLocationServer();
            wait.sc(2L);
            if (!HyPlus.getInstance().hyLocationDetector.getCurrentLocation().isLobby() || !HyPlus.getInstance().hyLocationDetector.getCurrentLocation().isLimbo()) {

                // DEBUG HyPlus.getInstance().displayIgMessage("GameDetection", "GAMESTART DETECTED!");
                HyPlus.getInstance().hyEventManager.callGameStart(locationIn);

            }
            if (HYPLUS_DP_STATE.getValueBoolean() && status.state == HyGameStatus.State.INGAME) {

                HyPlus.getInstance().discordApp.getRichPresence().updateState(this.HYPLUS_DP_STATE.getValueBoolean() ? status.state : HyGameStatus.State.UNDEFINED);
                if (game.equals("limbo") || locationIn.isLimbo()) return;
                String s1 = HYPLUS_DP_MODE.getValueBoolean() ? " " + mode : "";
                String s2 = HYPLUS_DP_MAP.getValueBoolean() ? " on " + map : "";
                HyPlus.getInstance().discordApp.getRichPresence().updateImageTextL("Playing " + game + s1 + s2 + ".");

            }

        }
        else {

            // update state
            HyGameStatus.State currStatus = HYPLUS_DP_STATE.getValueBoolean() ? status.state : HyGameStatus.State.UNDEFINED;
            if (HyPlus.getInstance().discordApp.getRichPresence().updateState(this.HYPLUS_DP_STATE.getValueBoolean() ? (game.equals("limbo") || locationIn.isLimbo() ? HyGameStatus.State.IDLING : currStatus) : HyGameStatus.State.UNDEFINED)) {

                if (HYPLUS_DP_STATE.getValueBoolean() && status.state == HyGameStatus.State.PREGAME) {

                    HyPlus.getInstance().discordApp.getRichPresence().updateImageTextL("In a " + game + " Pre-Game Lobby.");

                } else if (HYPLUS_DP_STATE.getValueBoolean() && status.state == HyGameStatus.State.INGAME) {

                    if (game.equals("limbo") || locationIn.isLimbo()) return;
                    String s1 = HYPLUS_DP_MODE.getValueBoolean() ? " " + mode : "";
                    String s2 = HYPLUS_DP_MAP.getValueBoolean() ? " on " + map : "";
                    HyPlus.getInstance().discordApp.getRichPresence().updateImageTextL("Playing " + game + s1 + s2 + ".");

                } else {

                    if (game.equals("limbo") || locationIn.isLimbo()) return;

                    HyPlus.getInstance().discordApp.getRichPresence().updateImageTextL("Playing " + game + " on Hypixel.");

                }

            }

            // update timers
            updateTimeStamps(false, status, locationIn);

        }

    }

    public void updateTimeStamps(boolean gameStart, HyGameStatus status, HyServerLocation locationIn) {

        // update timestamp
        if (HYPLUS_DP_TIME.getValueBoolean()) {

            // if called by onGameStart(), it directly adjusts timer to avoid any problems by async location updates
            if (gameStart) {

                // check for games that only have one timer and can be synced
                if (status.endingTimestamp != 0L) {

                    if (locationIn.gametype.equalsIgnoreCase("MURDER_MYSTERY")) {

                        HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(false, status.endingTimestamp); // display murder game countdown
                        return;

                    }

                }

                HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());
                return;

            }
            // otherwise we just run normal update
            else {
                // check state
                switch (status.state) {

                    case UNDEFINED:
                    case LOBBY:
                    case IDLING: {

                        HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());
                        return;

                    }
                    case PREGAME: {

                        // do we have a countdown yet?
                        HyPlus.getInstance().displayIgMessage(getModuleName(), status.toString());
                        if (status.endingTimestamp != 0L) {

                            HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(false, status.endingTimestamp);

                        } else if (status.startingTimestamp != 0L) {

                            HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(false, status.startingTimestamp); // start countdown

                        } else {

                            HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, status.startingTimestamp);

                        }
                        return;

                    }
                    case INGAME: {

                        // check for games that only have one timer and can be synced
                        if (status.endingTimestamp != 0L) {

                            if (locationIn.gametype.equalsIgnoreCase("MURDER_MYSTERY")) {

                                HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(false, status.endingTimestamp); // display murder game countdown
                                return;

                            }

                        }

                        HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());
                        return;

                    }

                }
            }

        } else {

            HyPlus.getInstance().discordApp.getRichPresence().removeTimestamp();

        }

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
            HyPlus.getInstance().discordApp.getRichPresence().forceUpdate();

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
        //discord_sub.add(dp_spfc); <- will be available in a future update
        discord_sub.add(dp_lobbyNNote);
        discord_sub.add(adv_all);
        dp.setSubSettings(discord_sub);

        moduleSettings.add(dp);

        return moduleSettings;

    }

    @Override
    public void checkConfig(boolean reset) {

        // load config values
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_DELAY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_GAME);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_MODE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_LOBBY);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_MAP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_TIME);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_STATE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_DP_SPECIFIC);

    }

    // HyPlus event listeners

    @Override
    public void onLocationChange(HyServerLocation location) {

        Thread t = new Thread(() -> {

            updatePresence(location);

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    // just used here to not spam the update :)
    private boolean changeQueued = false;
    @Override
    public void onGameStatusChange() {

        if (changeQueued) return;
        changeQueued = true;

        Thread t = new Thread(() -> {

            wait.ms(1600L); // wait 2000ms to make sure the scoreboard has updated.
            this.updateGameState(Hypixel.getGameStatus(HyPlus.getInstance().hyLocationDetector.getCurrentLocation()));
            HyPlus.getInstance().discordApp.getRichPresence().updateRichPresence();
            changeQueued = false;

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    @Override
    public void onHypixelQuit() {

        updatePresence(new HyServerLocation());

    }

    @Override
    public void onHypixelJoin() {

        Thread t = new Thread(() -> {

            if (HyPlus.getInstance().hypixelApi == null) {

                HyPlus.getInstance().displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
                return;

            }

            try {
                HyPlus.getInstance().displayIgMessage(getModuleName(), "Loading Gamelist from Hypixel.");
                this.indexedGames = HyPlus.getInstance().hypixelApi.createGamelistRequest();
            } catch (ApiRequestException e) {
                HyPlus.getInstance().displayIgMessage(null, "§6§l[HyPlus]§4§l [ERROR]: §7Failed to load Gamelist from Hypixel.§r");
                throw new RuntimeException(e);
            }

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    @Override
    public void onGameStart(HyServerLocation location) {

        // update timers as ex.
        this.updateTimeStamps(true, Hypixel.getGameStatus(location), location);

    }
}
