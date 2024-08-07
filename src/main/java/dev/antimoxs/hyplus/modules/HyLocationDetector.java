package dev.antimoxs.hyplus.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.response.StatusResponse;
import dev.antimoxs.hypixelapiHP.util.hypixelFetcher;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import dev.antimoxs.utilities.time.wait;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HyLocationDetector implements IHyPlusModule, IHyPlusEvent {

    // LocationDetector settings
    public static final HySetting<Boolean> HYPLUS_LD_TOGGLE = new HySetting<>("HYPLUS_LD_TOGGLE", "Toggle location detection", "The location detection is important for a range of features and should not be turned off.", true, Material.COMPASS);
    public static final HySetting<Boolean> HYPLUS_LD_LABYCHAT = new HySetting<>("HYPLUS_LD_LABYCHAT", "Show location in the labychat", "Toggle whether your location is displayed in ur labychat status.", true, Material.PAPER);
    public static final HySetting<Boolean> HYPLUS_LD_API = new HySetting<>("HYPLUS_LD_API", "Use API instead of ingame detection.", "You can use the HypixelAPI for location detection but it's not recommended.", false, Material.COMMAND);


    private int responseWaiter = 1;
    private boolean lastLocrawForced = false;
    private HyServerLocation currentLocation = new HyServerLocation();

    public HyServerLocation getCurrentLocation() { return this.currentLocation; }

    /**
     * Update the current location based on the settings.
     */
    public void getLocationAsync(boolean forceUpdate) {

        Thread t = new Thread(() -> {

            HyPlus.debugLog("[LocationDetection] Updating location... (Api: " + HYPLUS_LD_API + ")");
            if (HYPLUS_LD_API.getValue()) {

                getLocationAPI(forceUpdate);

            }
            else {

                wait.ms(1000L); // Delay to make sure hypixel switched account state to correct server
                lastLocrawForced = forceUpdate;
                getLocationServer();
                wait.ms(3000L);
                // DEBUG HyPlus.getInstance().displayIgMessage(null, "[LocationDetection] Server: " + currentLocation.server);
                if (currentLocation.server.equals("limbo")) getLocationServer(); // check twice in case rawloc returned fake-limbo

            }

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    /**
     * Update the current location via the <code>locraw</code> ingame command.
     * This is only available when ingame, otherwise you should use
     * {@link HyLocationDetector#getLocationAPI(boolean)} to update the data via HypixelAPI.
     * The detection and updating requires access to the chat response.
     */
    public void getLocationServer() {

        this.responseWaiter++;
        HyPlus.getInstance().sendMessageIngameChat("/locraw");

        HyPlus.debugLog("WAITING FOR RESPONSE");
        while (this.responseWaiter > 0) {
            System.out.println(this.responseWaiter);
        } // wait for hypixel's response
        HyPlus.debugLog("RESPONSE FROM HYPIXEL! CONTINUE...");

    }

    /**
     * Update the current location via the HypixelAPI.
     * An indexed APIKey is needed to perform this, or
     * it will return without updating the data. This
     * function isn't recommended and {@link HyLocationDetector#getLocationServer()}
     * should be used instead.
     */
    public void getLocationAPI(boolean forceUpdate) {

        if (HyPlus.getInstance().hypixelApi == null) {

            return;

        }
        try {

            // Get the data from the API
            StatusResponse r = HyPlus.getInstance().hypixelApi.createStatusRequest(LabyMod.getInstance().getPlayerUUID().toString());

            // transfer data into new location object
            HyServerLocation location = new HyServerLocation();

            location.online = r.getSession().online;
            location.gametype = r.getSession().gameType;
            location.rawloc = r.getSession().gameType;
            location.mode = r.getSession().mode;
            location.rawmod = r.getSession().mode;
            location.map = r.getSession().map;

            // Check for additional information
            additionalScoreboardCheck(location);

            // set current location

            if (!currentLocation.getJson().equals(location.getJson()) || forceUpdate) {

                HyPlus.getInstance().hyEventManager.callLocationChange(location);

            }

            if (!Objects.equals(currentLocation.gametype, location.gametype)) refreshLabyChatStatus(location.rawloc);

            currentLocation = location;

        }
        catch (ApiRequestException e) {

            HyPlus.debugLog("An API error occurred: " + e.getReason());

        }

    }

    @Override
    public void onInternalLocationResponse(String json) {

        // parse response json from hypixel
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        HyServerLocation serverLoccation = gson.fromJson(json, HyServerLocation.class);

        // Check for additional information
        additionalScoreboardCheck(serverLoccation);

        System.out.println("Current location: " + serverLoccation.server + ", " + serverLoccation.gametype + " - " + serverLoccation.mode);

        // copy the gametype to rawloc (gametype will be fetched)
        serverLoccation.rawloc = serverLoccation.gametype;
        serverLoccation.rawmod = serverLoccation.mode;

        if (!Objects.equals(currentLocation.gametype, serverLoccation.gametype)) refreshLabyChatStatus(serverLoccation.rawloc);

        if (!currentLocation.getJson().equals(serverLoccation.getJson()) || lastLocrawForced) {

            currentLocation = serverLoccation;
            HyPlus.getInstance().hyEventManager.callLocationChange(serverLoccation);
            lastLocrawForced = false;

        }
        else {

            currentLocation = serverLoccation;

        }

        this.responseWaiter = 0;

    }

    @Override
    public void onHypixelJoin() {

        // Auto-Update on join?
        if (HYPLUS_LD_TOGGLE.getValue()) {

            getLocationAsync(false);

        }

    }

    @Override
    public void onPacketJoinGame(S01PacketJoinGame packet) {

        if (HyPlus.getInstance().hypixel.checkOnServer()) {

            getLocationAsync(false);

        }

    }

    @Override
    public String getModuleName() {
        return "LocationDetector";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_LD_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_LD_LABYCHAT);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_LD_API);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement locationSettings = new BooleanElement(
                HYPLUS_LD_TOGGLE.getDisplayName(), HYPLUS_LD_TOGGLE.getIcon(), (bool) -> HYPLUS_LD_TOGGLE.changeConfigValue(HyPlus.getInstance(), bool), HYPLUS_LD_TOGGLE.getValue()
        );
        locationSettings.setDescriptionText(HYPLUS_LD_TOGGLE.getDescription());

        HeaderElement locSettings_info = new HeaderElement("§lThe Location detection is needed for a");
        HeaderElement locSettings_info2 = new HeaderElement("§lrange of features and should not be deactivated.");
        ButtonElement locSettings_refresh = new ButtonElement(
                "Update Status", new ControlElement.IconData(Material.BOOK_AND_QUILL), buttonElement -> getLocationAsync(true), "Refresh","Update the current status.", Color.ORANGE
        );

        BooleanElement locSettings_labychat = new BooleanElement(HYPLUS_LD_LABYCHAT.getDisplayName(), HYPLUS_LD_LABYCHAT.getIcon(), (bool) -> {

            HYPLUS_LD_LABYCHAT.changeConfigValue(HyPlus.getInstance(), bool);
            LabyMod.getInstance().getLabyConnect().updatePlayingOnServerState(null);

        }, HYPLUS_LD_LABYCHAT.getValue());
        locationSettings.setDescriptionText(HYPLUS_LD_LABYCHAT.getDescription());

        BooleanElement locSettings_useApi = new BooleanElement(
                HYPLUS_LD_API.getDisplayName(), HYPLUS_LD_API.getIcon(), (bool) -> HYPLUS_LD_API.changeConfigValue(HyPlus.getInstance(), bool), HYPLUS_LD_API.getValue()
        );
        locationSettings.setDescriptionText(HYPLUS_LD_API.getDescription());

        Settings location_sub = new Settings();
        location_sub.add(locSettings_info);
        location_sub.add(locSettings_info2);
        location_sub.add(locSettings_refresh);
        location_sub.add(locSettings_labychat);
        location_sub.add(locSettings_useApi);

        locationSettings.setSubSettings(location_sub);
        moduleSettings.add(locationSettings);

        return moduleSettings;

    }

    // basically check for atlas

    // TODO: convert to non mc class (Scoreboard)
    private void additionalScoreboardCheck(HyServerLocation location) {

        Scoreboard sb = LabyModCore.getMinecraft().getWorld().getScoreboard();
        if (sb != null && !sb.getScoreObjectives().isEmpty()) {

            for (ScoreObjective ob : sb.getScoreObjectives()) {

                String title = HyUtilities.matchOutColorCode(ob.getDisplayName());
                if (title.toLowerCase().contains("atlas")) {

                    location.gametype = "ATLAS";
                    location.mode = "Catching cheaters";
                    break;

                }

            }

        }

    }

    public void refreshLabyChatStatus(String gametypeIn) {

        if (HYPLUS_LD_LABYCHAT.getValue()) {

            String gametype = hypixelFetcher.fetchGame(gametypeIn);
            //String gamemode = hypixelFetcher.fetchModeStatus(location.rawmod);

            LabyMod.getInstance().getLabyConnect().updatePlayingOnServerState(gametype);

        }

    }

}
