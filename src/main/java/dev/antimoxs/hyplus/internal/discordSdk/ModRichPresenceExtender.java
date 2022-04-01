package dev.antimoxs.hyplus.internal.discordSdk;

import com.jagrosh.discordipc.entities.RichPresence;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.HyDiscordPresence;
import dev.antimoxs.hyplus.modules.party.HyPartyManager;
import dev.antimoxs.hyplus.objects.HyGameStatus;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class ModRichPresenceExtender {

    private boolean updateRequired = true;


    /**
     *
     * LabyMod - HyPlus
     * MurderMystery: Playing
     * DoubleUp (3 of 3)
     * 04:02 left
     * <p>
     * LabyMod - HyPlus
     * gameType: gameState
     * gameMode party
     * time
     *
     */

    private String largeImage = "hypixel"; // default hypixel logo
    private String smallImage = "hyplus"; // hyplus logo (default); does not change?
    private String largeImageText = "Playing on Hypixel";
    private String smallImageText = "LabyMod with HyPlus!";

    private String server = "unknown"; // server (ex. m77B)
    private String gameType = "Hypixel"; // The name of the current game type
    private String gameMap = null; // Current map; null when not specified
    private String gameMode = "Lobby"; // Mode of the game (ex. Doubles)
    private HyGameStatus.State playState = HyGameStatus.State.UNDEFINED; // Current state, aka Lobby, Playing or Private




    private boolean party = false;
    private int partyMax = 0;
    private int partySize = 0;
    private String partyID = "";

    private boolean timestamp = true;
    private long timeStart = 0;
    private long timeEnd = 0;

    private boolean joinSecret = false;
    private String joinSecretS = "";

    private final DiscordManager discord;

    public ModRichPresenceExtender(DiscordManager discord) {

        this.discord = discord;

    }

    // Update the current party
    public void updateParty(boolean party, int partyMax, int partySize, String partyID) {

        if (this.partyMax != partyMax) {
            this.partyMax = partyMax;
            updateRequired = true;
        }
        if (this.partySize != partySize) {
            this.partySize = partySize;
            updateRequired = true;
        }
        if (!Objects.equals(this.partyID, partyID)) {
            this.partyID = partyID;
            updateRequired = true;
        }
        if (this.party != party) {
            this.party = party;
            updateRequired = true;
        }



    }

    // Update the current timestamps
    public void updateTimestamps(boolean start, long timestamp) {

        HyPlus.debugLog("[HP-RPC] updated times: " + timestamp);
        if (start) {

            this.timeEnd = 0;
            this.timeStart = timestamp;

        }
        else {

            this.timeEnd = timestamp;
            this.timeStart = 0;

        }
        this.timestamp = true;
        this.updateRequired = true;

    }

    public void removeTimestamp() {

        this.timestamp = false;
        this.updateRequired = true;

    }

    // Update ths current game information
    public void updateServer(String server) {

        HyPlus.debugLog("[HP-RPC] updated s  : " + server);
        if (!Objects.equals(this.server, server)) {

            this.server = server;
            this.updateRequired = true;

        }

    }
    public void updateMode(String mode) {

        HyPlus.debugLog("[HP-RPC] updated mode: " + mode);
        if (!Objects.equals(this.gameMode, mode)) {

            this.gameMode = mode;
            this.updateRequired = true;

        }

    }
    public void updateMap(String map) {

        HyPlus.debugLog("[HP-RPC] updated map: " + map);
        if (!Objects.equals(this.gameMap, map)) {

            this.gameMap = map;
            this.updateRequired = true;

        }

    }
    public boolean updateType(String type) {

        HyPlus.debugLog("[HP-RPC] updated type: " + type);
        if (!Objects.equals(this.gameType, type)) {

            this.gameType = type;
            this.updateRequired = true;

        }

        return this.updateRequired;

    }
    public boolean updateState(HyGameStatus.State state) {

        HyPlus.debugLog("[HP-RPC] updated state: " + state.name);
        if (this.playState != state) {

            this.playState = state;
            this.updateRequired = true;
            return true;

        }
        return false;

    }

    // Update image data
    public void updateImageL(String icon, String text) {

        updateImageIconL(icon);
        updateImageTextL(text);

    }
    public void updateImageS(String icon, String text) {

        updateImageIconS(icon);
        updateImageTextS(text);

    }
    public void updateImageIconL(String image) {

        if (!this.largeImage.equals(image)) {

            this.largeImage = image;
            this.updateRequired = true;

        }

    }
    public void updateImageIconS(String image) {

        if (!this.smallImage.equals(image)) {

            this.smallImage = image;
            this.updateRequired = true;

        }

    }
    public void updateImageTextL(String text) {

        if (!this.largeImageText.equals(text)) {
            this.largeImageText = text;
            this.updateRequired = true;
        }

    }
    public void updateImageTextS(String text) {

        if (!this.smallImageText.equals(text)) {
            this.smallImageText = text;
            this.updateRequired = true;
        }

    }

    // Update join secret
    public void updateJoinSecret(boolean joinSecret, String joinSecretS) {

        if (joinSecret) {
            this.joinSecretS = joinSecretS;
        }
        else {
            this.joinSecretS = null;
        }
        this.joinSecret = joinSecret;
        this.updateRequired = true;

    }

    public RichPresence build() {


        // settings:
        boolean sgame = HyDiscordPresence.HYPLUS_DP_GAME.getValueBoolean();
        boolean smode = HyDiscordPresence.HYPLUS_DP_MODE.getValueBoolean();
        boolean stime = HyDiscordPresence.HYPLUS_DP_TIME.getValueBoolean();
        boolean sstate = HyDiscordPresence.HYPLUS_DP_STATE.getValueBoolean();
        boolean sparty = HyPartyManager.HYPLUS_PM_SHOW.getValueBoolean();

        RichPresence.Builder builder = new RichPresence.Builder();

        // Discord Presence Images
        builder.setLargeImage(sgame ? this.largeImage : "hypixel", sgame ? this.largeImageText : "Playing on Hypixel.");
        builder.setSmallImage(this.smallImage, this.smallImageText);

        // Game Information
        builder.setDetails(sgame ? this.gameType + (sstate ? (this.playState == HyGameStatus.State.UNDEFINED ? "" : ": " + this.playState.name) : "") : "Playing on Hypixel.");
        builder.setState(smode && sgame ? (this.gameType.equals(this.gameMode) ? null : this.gameMode) : "HyPlus by Antimoxs.");

        // Party Indicators
        if (sparty) {
            builder.setParty(this.partyID, this.partySize, party ? this.partyMax : 0);
        }

        // Timestamps

        if (this.timestamp && stime) {

            if (this.timeEnd != 0) {

                builder.setEndTimestamp(this.timeEnd);
                builder.setStartTimestamp(null);

            }
            else if (this.timeStart != 0) {

                builder.setStartTimestamp(this.timeStart);
                builder.setEndTimestamp(null);

            }


        }

        // Join secret
        if (joinSecret && sparty) {

            builder.setJoinSecret(joinSecretS);
            //builder.setMatchSecret(joinSecretS);

        }


        return builder.build();


        /*
        DiscordRichPresence drp = new DiscordRichPresence();

        // Discord Presence Images
        drp.largeImageText = sgame ? this.largeImageText : "Playing on Hypixel.";
        drp.smallImageText = this.smallImageText;
        drp.largeImageKey = sgame ? this.largeImage : "hypixel";
        drp.smallImageKey = this.smallImage;

        // Game Information
        drp.details = sgame ? this.gameType + (sstate ? (this.playState == HyGameStatus.State.UNDEFINED ? "" : ": " + this.playState.name) : "") : "Playing on Hypixel.";
        drp.state = smode && sgame ? (this.gameType.equals(this.gameMode) ? null : this.gameMode) : "HyPlus by Antimoxs.";

        // Party Indicators
        if (sparty) {
            drp.partyId = this.partyID;
            drp.partyMax = party ? this.partyMax : 0;
            drp.partySize = this.partySize;
        }
        else {

            drp.partyId = null;
            drp.partyMax = 0;
            drp.partySize = 0;

        }

        // Timestamps

        if (this.timestamp && stime) {

            drp.endTimestamp = this.timeEnd;
            drp.startTimestamp = this.timeStart;

        }

        // Join secret
        drp.joinSecret = joinSecret && sparty ? joinSecretS : null;

        drp.instance = 1;
        return drp;

         */

        //return activity;

    }

    private static OffsetDateTime fromMillis(long epochMillis) {
        return OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
    }


    public void updateRichPresence() {

        if (updateRequired) {

            HyPlus.getInstance().discordManager.updateActivity(this.build());
            this.updateRequired = false;
        }

    }

    public void forceUpdate() {
        this.updateRequired = true;
        updateRichPresence();
    }


}
