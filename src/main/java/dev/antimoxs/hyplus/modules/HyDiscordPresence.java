package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.response.GamelistResponse;
import dev.antimoxs.hypixelapiHP.util.hypixelFetcher;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.Hypixel;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.party.HyPartyManager;
import dev.antimoxs.hyplus.objects.*;
import dev.antimoxs.utilities.time.wait;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HyDiscordPresence implements IHyPlusModule, IHyPlusEvent {

    // Presence settings

    public static final HySetting<Boolean> HYPLUS_DP_TOGGLE = new HySetting<>("HYPLUS_DP_TOGGLE", "Discord Presence", "Toggle the Hypixel Discord Presence", true, Material.PAPER);
    public static final HySetting<Integer> HYPLUS_DP_DELAY = new HySetting<>("HYPLUS_DP_DELAY", "Update delay", "Delays the Discord presence update for x seconds.", 0, Material.REDSTONE_WIRE);
    public static final HySetting<Boolean> HYPLUS_DP_GAME = new HySetting<>("HYPLUS_DP_GAME", "Show Game", "Toggle the display of your game. (If off, it also deactivates the mode.)", true, Material.REDSTONE);
    public static final HySetting<Boolean> HYPLUS_DP_MODE = new HySetting<>("HYPLUS_DP_MODE", "Show mode", "Toggle the display of your game-mode.",true, Material.LEATHER_BOOTS);
    public static final HySetting<Boolean> HYPLUS_DP_LOBBY = new HySetting<>("HYPLUS_DP_LOBBY", "Show Lobby number *", "Toggle the display of the lobbynumber when you are in a lobby. (Only works when using ingame detection.)", true, Material.WEB);
    public static final HySetting<Boolean> HYPLUS_DP_MAP = new HySetting<>("HYPLUS_DP_MAP", "Show Map", "Toggle the display of the current map.", true, Material.MAP);
    public static final HySetting<Boolean> HYPLUS_DP_TIME = new HySetting<>("HYPLUS_DP_TIME", "Show Time", "Toggle the display of the elapsed play time.", true, Material.WATCH);
    public static final HySetting<Boolean> HYPLUS_DP_SPECIFIC = new HySetting<>("HYPLUS_DP_SPECIFIC", "Show specific information", "Toggle the display of special information such round.",  false, Material.NETHER_STAR);
    public static final HySetting<Boolean> HYPLUS_DP_STATE = new HySetting<>("HYPLUS_DP_STATE", "Show playing state", "Toggle the display of the playing state", true, Material.SIGN);

    public static final HySetting<Boolean> HYPLUS_DP_MSG = new HySetting<>("HYPLUS_DP_MSG", "Display update message", "Toggle the display of the update message", true, Material.FEATHER);

    private final ControlElement.IconData icon_update = new ControlElement.IconData(Material.BOOK_AND_QUILL);
    private HyGameStatus currentStatus = new HyGameStatus();
    private String lastGameAndMode = "";
    private GamelistResponse indexedGames = null;

    public int delay = 0;

    public boolean presenceCheck() {

        if (!HyPlus.getInstance().hypixel.checkOnServer()) {

            HyPlus.getInstance().discordManager.shutdown();
            if (LabyMod.getMainConfig().getSettings().discordRichPresence) {

                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }

        }

        if (!LabyMod.getMainConfig().getSettings().discordRichPresence) {

            HyPlus.getInstance().discordManager.shutdown();
            LabyMod.getInstance().getDiscordApp().shutdown();
            return false;

        }

        return true;

    }

    public boolean enabledCheck() {

        if (HYPLUS_DP_TOGGLE.getValue()) {

            LabyMod.getInstance().getDiscordApp().shutdown();
            if (!HyPlus.getInstance().discordManager.startInstance()) {

                HyPlus.debugLog("Can't start HyPlus DiscordRPC?");
                LabyMod.getInstance().getDiscordApp().initialize();
                return false;

            }
            return true;

        }
        else {

            HyPlus.getInstance().discordManager.shutdown();
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
        if (!enabledCheck()) {

            updatePresenceQuiet(locationIn);
            return;

        }
        // waiting set time from user212
        if (HYPLUS_DP_DELAY.getValue() != 0) {

            if (HYPLUS_DP_MSG.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), "Delaying update...");
            wait.sc((long)HYPLUS_DP_DELAY.getValue());

        }

        if (HYPLUS_DP_MSG.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), "Updating Discord ServerStatus....");

        // update presence
        updatePresenceQuiet(locationIn);

        // send rpc update
        HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();

    }

    private void updatePresenceQuiet(HyServerLocation locationIn) {

        // Update Server and hyplus icon
        HyPlus.getInstance().discordManager.getRichPresence().updateServer(locationIn.server);
        HyPlus.getInstance().discordManager.getRichPresence().updateImageS("hyplus", "LabyMod with HyPlus! v" + HyPlus.getInstance().getVersion());

        // Check if game is not toggled on
        if (!HYPLUS_DP_GAME.getValue()) {

            HyPlus.getInstance().discordManager.getRichPresence().updateState(HyGameStatus.State.UNDEFINED);
            HyPlus.getInstance().discordManager.getRichPresence().updateType("Playing on Hypixel");
            HyPlus.getInstance().discordManager.getRichPresence().updateMode("with HyPlus by Antimoxs.");
            HyPlus.getInstance().discordManager.getRichPresence().updateImageL("hypixel", "Playing on Hypixel.net with HyPlus.");
            HyPlus.getInstance().discordManager.getRichPresence().removeTimestamp();
            HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();
            return;

        }

        HyPlus.debugLog("[HYDP] STATE IS: " + HYPLUS_DP_STATE.getValue());

        // Check if Limbo
        if (locationIn.isLimbo()) {

            HyPlus.getInstance().discordManager.getRichPresence().updateState(HYPLUS_DP_STATE.getValue() ? HyGameStatus.State.IDLING : HyGameStatus.State.UNDEFINED);
            if (HyPlus.getInstance().discordManager.getRichPresence().updateType("Limbo")) {

                updateTimeStamps(false, currentStatus, locationIn);
                //HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

            }
            HyPlus.getInstance().discordManager.getRichPresence().updateImageL("limbo", "Currently Idling in Limbo");
            HyPlus.getInstance().discordManager.getRichPresence().updateMode("Currently Afk");
            HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();
            return;

        }

        String game = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype) : this.indexedGames.getGame(locationIn.gametype).name;
        String gameImage = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype).toLowerCase() : this.indexedGames.getGame(locationIn.gametype).databaseName.toLowerCase();
        // check if lobby
        if (locationIn.isLobby()) {

            HyPlus.getInstance().discordManager.getRichPresence().updateState(HYPLUS_DP_STATE.getValue() ? HyGameStatus.State.LOBBY : HyGameStatus.State.UNDEFINED);
            if (HyPlus.getInstance().discordManager.getRichPresence().updateType(game)) {

                updateTimeStamps(false, currentStatus, locationIn);
                //HyPlus.getInstance().discordApp.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

            }
            String s1 = HYPLUS_DP_LOBBY.getValue() ? " " + locationIn.getLobbyNumber() : "";
            HyPlus.getInstance().discordManager.getRichPresence().updateImageL(gameImage, "Online on " + (HYPLUS_DP_MODE.getValue() ? game + " lobby" + s1 : "Hypixel"));
            HyPlus.getInstance().discordManager.getRichPresence().updateMode("Lobby" + s1);
            HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();
            return;

        }
        // ingame location update

        String mode = this.indexedGames == null ? hypixelFetcher.fetchMode(locationIn.mode) : this.indexedGames.getGame(locationIn.gametype).fetchMode(locationIn.mode);
        String map = updateMap("Hypixel", mode);

        // set custom message when on housing
        if (locationIn.gametype.equalsIgnoreCase("housing")) mode = "Visiting a world.";

        // update gamestate
        updateGameState(Hypixel.getGameStatus(locationIn), game, mode, map, locationIn);

        // update gameType
        if (HyPlus.getInstance().discordManager.getRichPresence().updateType(game)) {

            HyPlus.getInstance().discordManager.getRichPresence().updateImageIconL(gameImage);

        }

        // update mode and map
        HyPlus.getInstance().discordManager.getRichPresence().updateMode(HYPLUS_DP_MODE.getValue() ? mode : "on Hypixel.");
        HyPlus.getInstance().discordManager.getRichPresence().updateMap(map);


    }

    private String updateMap(String map, String mode) {

        // adjust map for 'the pit'
        if (HYPLUS_DP_MAP.getValue() && map != null) {

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
     */
    public void updateGameState(HyGameStatus status) {

        HyServerLocation locationIn = HyPlus.getInstance().hyLocationDetector.getCurrentLocation();
        String game = this.indexedGames == null ? hypixelFetcher.fetchGame(locationIn.gametype) : this.indexedGames.getGame(locationIn.gametype).name;
        String mode = this.indexedGames == null ? hypixelFetcher.fetchMode(locationIn.mode) : this.indexedGames.getGame(locationIn.gametype).fetchMode(locationIn.mode);

        updateGameState(status, game, mode, updateMap(locationIn.map, mode), locationIn);

    }
    public void updateGameState(HyGameStatus status, String game, String mode, String map, HyServerLocation locationIn) {

        HyPlus.debugLog("[HYDP] Updating GameState");

        // check if its a new state
        if (this.currentStatus.equals(status) && lastGameAndMode.equals(game+mode+map)) {

            // skyblock mode check

            if (game.equalsIgnoreCase("skyblock")) {

                String s1 = HYPLUS_DP_MODE.getValue() ? " " + mode : "";
                String s2 = HYPLUS_DP_MAP.getValue() ? " on " + map : "";
                HyPlus.getInstance().discordManager.getRichPresence().updateImageTextL("Playing " + game + s1 + s2 + ".");

            }

            return;

        }
        lastGameAndMode = game+mode+map;
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
            if (HYPLUS_DP_STATE.getValue() && status.state == HyGameStatus.State.INGAME) {

                HyPlus.getInstance().discordManager.getRichPresence().updateState(HYPLUS_DP_STATE.getValue() ? status.state : HyGameStatus.State.UNDEFINED);
                if (game.equals("limbo") || locationIn.isLimbo()) return;
                String s1 = HYPLUS_DP_MODE.getValue() ? " " + mode : "";
                String s2 = HYPLUS_DP_MAP.getValue() ? " on " + map : "";
                HyPlus.getInstance().discordManager.getRichPresence().updateImageTextL("Playing " + game + s1 + s2 + ".");


            }

        }



        else {

            // update state
            HyGameStatus.State currStatus = HYPLUS_DP_STATE.getValue() ? status.state : HyGameStatus.State.UNDEFINED;
            if (HyPlus.getInstance().discordManager.getRichPresence().updateState(HYPLUS_DP_STATE.getValue() ? (game.equals("limbo") || locationIn.isLimbo() ? HyGameStatus.State.IDLING : currStatus) : HyGameStatus.State.UNDEFINED)) {

                if (HYPLUS_DP_STATE.getValue() && status.state == HyGameStatus.State.PREGAME) {

                    HyPlus.getInstance().discordManager.getRichPresence().updateImageTextL("In a " + game + " Pre-Game Lobby.");

                } else if (HYPLUS_DP_STATE.getValue() && status.state == HyGameStatus.State.INGAME) {

                    if (game.equals("limbo") || locationIn.isLimbo()) return;
                    String s1 = HYPLUS_DP_MODE.getValue() ? " " + mode : "";
                    String s2 = HYPLUS_DP_MAP.getValue() ? " on " + map : "";
                    HyPlus.getInstance().discordManager.getRichPresence().updateImageTextL("Playing " + game + s1 + s2 + ".");

                } else {

                    if (game.equals("limbo") || locationIn.isLimbo()) return;

                    HyPlus.getInstance().discordManager.getRichPresence().updateImageTextL("Playing " + game + " on Hypixel.");

                }

            }

            // update timers
            updateTimeStamps(false, status, locationIn);

        }

    }

    public void updateTimeStamps(boolean gameStart, HyGameStatus status, HyServerLocation locationIn) {

        // update timestamp
        if (HYPLUS_DP_TIME.getValue()) {

            // if called by onGameStart(), it directly adjusts timer to avoid any problems by async location updates
            if (gameStart) {

                // check for games that only have one timer and can be synced
                if (status.endingTimestamp != 0L) {

                    if (locationIn.gametype.equalsIgnoreCase("MURDER_MYSTERY")) {

                        HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(false, status.endingTimestamp); // display murder game countdown
                        return;

                    }

                }

                HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

            }
            // otherwise we just run normal update
            else {
                // check state
                switch (status.state) {

                    case UNDEFINED:
                    case LOBBY:
                    case IDLING: {

                        HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(true, System.currentTimeMillis());
                        return;

                    }
                    case PREGAME: {

                        // do we have a countdown yet?
                        if (HyAdvanced.HYPLUS_ADVANCED_DEBUGLOG.getValue()) HyPlus.getInstance().displayIgMessage(getModuleName(), status.toString());
                        if (status.endingTimestamp != 0L) {

                            HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(false, status.endingTimestamp);

                        }
                        else {

                            // start countdown ?
                            HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(status.startingTimestamp == 0L, status.startingTimestamp);

                        }
                        return;

                    }
                    case INGAME: {

                        // check for games that only have one timer and can be synced
                        if (status.endingTimestamp != 0L) {

                            if (locationIn.gametype.equalsIgnoreCase("MURDER_MYSTERY")) {

                                HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(false, status.endingTimestamp); // display murder game countdown
                                return;

                            }

                        }

                        HyPlus.getInstance().discordManager.getRichPresence().updateTimestamps(true, System.currentTimeMillis());

                    }

                }
            }

        } else {

            HyPlus.getInstance().discordManager.getRichPresence().removeTimestamp();

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

        }, HYPLUS_DP_TOGGLE.getValue());
        dp.setDescriptionText(HYPLUS_DP_TOGGLE.getDescription());

        // Update button to resend the status
        ButtonElement dp_update = new ButtonElement(

                "Resend status", icon_update, (buttonElement) -> {

            updatePresence(HyPlus.getInstance().hyLocationDetector.getCurrentLocation());
            HyPlus.getInstance().discordManager.getRichPresence().forceUpdate();

            }, "Resend", "Resend the Discord status.", Color.ORANGE

        );

        HeaderElement dp_info = new HeaderElement("The status is automatically updated upon joining.");

        // Sub-Settings

        // Delay in s between location update and Discord update // to prevent sniping if u want lol
        NumberElement dp_delay = new NumberElement(HYPLUS_DP_DELAY.getDisplayName(), HYPLUS_DP_DELAY.getIcon() , HYPLUS_DP_DELAY.getValue());
        dp_delay.setMinValue(0);
        dp_delay.setMaxValue(120);
        dp_delay.addCallback(accepted -> {
            HYPLUS_DP_DELAY.changeConfigValue(HyPlus.getInstance(), accepted);
            delay = 0;
        });
        dp_delay.setDescriptionText(HYPLUS_DP_DELAY.getDescription());

        // Location information
        BooleanElement dp_game = new BooleanElement(HYPLUS_DP_GAME.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_GAME.getIcon(), HYPLUS_DP_GAME.getConfigName(), HYPLUS_DP_GAME.getDefault());
        dp_game.setDescriptionText(HYPLUS_DP_GAME.getDescription());

        BooleanElement dp_mode = new BooleanElement(HYPLUS_DP_MODE.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_MODE.getIcon(), HYPLUS_DP_MODE.getConfigName(), HYPLUS_DP_MODE.getDefault());
        dp_mode.setDescriptionText(HYPLUS_DP_MODE.getDescription());

        BooleanElement dp_lobbyN = new BooleanElement(HYPLUS_DP_LOBBY.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_LOBBY.getIcon(), HYPLUS_DP_LOBBY.getConfigName(), HYPLUS_DP_LOBBY.getDefault());
        dp_lobbyN.setDescriptionText(HYPLUS_DP_LOBBY.getDescription());

        BooleanElement dp_map = new BooleanElement(HYPLUS_DP_MAP.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_MAP.getIcon(), HYPLUS_DP_MAP.getConfigName(), HYPLUS_DP_MAP.getDefault());
        dp_map.setDescriptionText(HYPLUS_DP_MAP.getDescription());

        BooleanElement dp_time = new BooleanElement(HYPLUS_DP_TIME.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_TIME.getIcon(), HYPLUS_DP_TIME.getConfigName(), HYPLUS_DP_TIME.getDefault());
        dp_time.setDescriptionText(HYPLUS_DP_TIME.getDescription());

        BooleanElement dp_state = new BooleanElement(HYPLUS_DP_STATE.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_STATE.getIcon(), HYPLUS_DP_STATE.getConfigName(), HYPLUS_DP_STATE.getDefault());
        dp_state.setDescriptionText(HYPLUS_DP_STATE.getDescription());

        BooleanElement dp_spfc = new BooleanElement(HYPLUS_DP_SPECIFIC.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_SPECIFIC.getIcon(), HYPLUS_DP_SPECIFIC.getConfigName(), HYPLUS_DP_SPECIFIC.getDefault());
        dp_spfc.setDescriptionText(HYPLUS_DP_SPECIFIC.getDescription());
        dp_spfc.setBlocked(true);

        HeaderElement dp_lobbyNNote = new HeaderElement("* Only with §lingame§r detection.");

        BooleanElement dp_msg = new BooleanElement(HYPLUS_DP_MSG.getDisplayName(), HyPlus.getInstance(), HYPLUS_DP_MSG.getIcon(), HYPLUS_DP_MSG.getConfigName(), HYPLUS_DP_MSG.getDefault());
        dp_msg.setDescriptionText(HYPLUS_DP_MSG.getDescription());

        AdvancedElement adv_all = new AdvancedElement(HyPartyManager.HYPLUS_PM_TOGGLE.getDisplayName(), HyPartyManager.HYPLUS_PM_TOGGLE.getConfigName(), HyPartyManager.HYPLUS_PM_TOGGLE.getIcon());
        adv_all.setDescriptionText(HyPartyManager.HYPLUS_PM_TOGGLE.getDescription());
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
        discord_sub.add(dp_msg);
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

        Thread t = new Thread(() -> updatePresence(location));
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
            HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();
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
