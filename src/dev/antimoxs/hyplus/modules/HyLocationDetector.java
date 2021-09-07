package dev.antimoxs.hyplus.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.response.StatusResponse;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import dev.antimoxs.utilities.time.wait;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class HyLocationDetector implements IHyPlusModule, IHyPlusEvent {

    private final HyPlus hyPlus;
    private int responseWaiter = 1;
    private boolean lastLocrawForced = false;
    private HyServerLocation currentLocation = new HyServerLocation();

    // LocationDetector settings
    public boolean HYPLUS_LD_TOGGLE = true;
    public boolean HYPLUS_LD_API = true;

    public HyLocationDetector(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }
    public HyServerLocation getCurrentLocation() { return this.currentLocation; }

    /**
     * Update the current location based on the settings.
     */
    public void getLocationAsync(boolean forceUpdate) {

        Thread t = new Thread(() -> {

            System.out.println("[LocationDetection] Updating location... (Api: " + HYPLUS_LD_API + ")");
            if (HYPLUS_LD_API) {

                getLocationAPI(forceUpdate);

            }
            else {

                wait.ms(500L);
                lastLocrawForced = forceUpdate;
                getLocationServer();
                wait.ms(3000L);
                hyPlus.displayIgMessage(null, "[LocationDetection] Server: " + currentLocation.server);
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
        hyPlus.sendMessageIngameChat("/locraw");

        System.out.println("WAITING FOR RESPONSE");
        while (this.responseWaiter > 0) {
            System.out.println(this.responseWaiter);
        } // wait for hypixel's response
        System.out.println("RESPONSE FROM HYPIXEL! CONTINUE...");

        return;

    }

    /**
     * Update the current location via the HypixelAPI.
     * An indexed APIKey is needed to perform this, or
     * it will return without updating the data. This
     * function isn't recommended and {@link HyLocationDetector#getLocationServer()}
     * should be used instead.
     */
    public void getLocationAPI(boolean forceUpdate) {

        if (hyPlus.tbcHypixelApi == null) {

            return;

        }

        try {


            // Get the data from the API
            StatusResponse r = hyPlus.tbcHypixelApi.createStatusRequest(LabyMod.getInstance().getPlayerUUID().toString());


            // transfer data into new location object
            HyServerLocation location = new HyServerLocation();

            location.online = r.getSession().online;
            location.gametype = r.getSession().gameType;
            location.rawloc = r.getSession().gameType;
            location.mode = r.getSession().mode;
            location.map = r.getSession().map;

            // set current location

            if (!currentLocation.getJson().equals(location.getJson()) || forceUpdate) {

                hyPlus.hyEventManager.callLocationChange(location);

            }

            currentLocation = location;



            return;


        }
        catch (ApiRequestException e) {

            System.out.println("An API error occured: " + e.getReason());

        }

        return;

    }

    @Override
    public void onInternalLocationResponse(String json) {

        // parse response json from hypixel
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        HyServerLocation serverLoccation = gson.fromJson(json, HyServerLocation.class);


        Scoreboard sb = Minecraft.getMinecraft().theWorld.getScoreboard();
        if (sb != null && !sb.getScoreObjectives().isEmpty()) {

            for (ScoreObjective ob : sb.getScoreObjectives()) {

                String title = HyUtilities.matchOutColorCode(ob.getDisplayName());
                if (title.toLowerCase().contains("atlas")) {

                    serverLoccation.gametype = "ATLAS";
                    serverLoccation.mode = "Catching cheaters";
                    break;

                }

            }

        }


        System.out.println("Current location: " + serverLoccation.server + ", " + serverLoccation.gametype + " - " + serverLoccation.mode);

        // copy the gametype to rawloc (gametype will be fetched)
        serverLoccation.rawloc = serverLoccation.gametype;


        if (!currentLocation.getJson().equals(serverLoccation.getJson()) || lastLocrawForced) {

            hyPlus.hyEventManager.callLocationChange(serverLoccation);
            lastLocrawForced = false;

        }

        currentLocation = serverLoccation;



        this.responseWaiter = 0;

    }

    @Override
    public void onHypixelJoin() {

        // Auto-Update on join?
        if (HYPLUS_LD_TOGGLE) {

            getLocationAsync(false);

        }

    }

    @Override
    public void onPacketJoinGame(S01PacketJoinGame packet) {

        if (hyPlus.hypixel.checkOnServer()) {

            getLocationAsync(false);

        }

    }

    @Override
    public String getModuleName() {
        return "LocationDetector";
    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement locationSettings = new BooleanElement("Location detection", hyPlus, new ControlElement.IconData(Material.COMPASS), "HYPLUS_LD_TOGGLE", true);

        HeaderElement locSettings_info = new HeaderElement("§lThe Location detection is needed for a");
        HeaderElement locSettings_info2 = new HeaderElement("§lrange of features and should not be deactivated.");
        ButtonElement locSettings_refresh = new ButtonElement("Update Status",
                new ControlElement.IconData(Material.BOOK_AND_QUILL),
                new Consumer<ButtonElement>() {
                    @Override
                    public void accept(ButtonElement buttonElement) {

                        getLocationAsync(true);

                    }
                },
                "Refresh",
                "Update the current status.",
                Color.ORANGE
        );

        BooleanElement locSettings_useApi = new BooleanElement("Use API instead of ingame detection", hyPlus, new ControlElement.IconData(Material.COMMAND), "HYPLUS_LD_API", false);





        Settings location_sub = new Settings();
        location_sub.add(locSettings_info);
        location_sub.add(locSettings_info2);
        location_sub.add(locSettings_refresh);
        location_sub.add(locSettings_useApi);

        locationSettings.setSubSettings(location_sub);
        moduleSettings.add(locationSettings);

        return moduleSettings;

    }

}