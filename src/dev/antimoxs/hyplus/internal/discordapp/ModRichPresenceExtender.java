package dev.antimoxs.hyplus.internal.discordapp;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.discordapp.api.DiscordRPCLibrary;
import net.labymod.discordapp.api.DiscordRichPresence;

public class ModRichPresenceExtender {

    private DiscordRichPresence drp = new DiscordRichPresence();

    private boolean updateRequired = true;

    private String largeImage = "hypixel"; // default hypixel logo
    private String smallImage = "hyplus"; // hyplus logo (default); does not change?
    private String largeImageText = "Playing on Hypixel";
    private String smallImageText = "LabyMod with HyPlus!";

    private String gameType = "Hypixel"; // The name of the current game type
    private String gameMap = null; // Current map; null when not specified
    private String gameMode = "Lobby"; // Mode of the game (ex. Doubles)
    private String playState = "Idle"; // Current state, aka Lobby, Playing or Private

    private boolean party = false;
    private int partyMax = 0;
    private int partySize = 0;
    private String partyID = "";

    private boolean timestamp = true;
    private long timeStart = 0;
    private long timeEnd = 0;

    private boolean joinSecret = false;
    private String joinSecretS = "";

    private DiscordAppExtender discord;

    public ModRichPresenceExtender(DiscordAppExtender discord) {

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
        if (this.partyID != partyID) {
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
    public void updateMode(String mode) {

        if (this.gameMode != mode) {

            this.gameMode = mode;
            this.updateRequired = true;

        }

    }
    public void updateMap(String map) {

        if (this.gameMap != map) {

            this.gameMap = map;
            this.updateRequired = true;

        }

    }
    public boolean updateType(String type) {

        if (this.gameType != type) {

            this.gameType = type;
            this.updateRequired = true;

        }

        return this.updateRequired;

    }
    public void updateState(String state) {

        if (this.playState != state) {

            this.playState = state;
            this.updateRequired = true;

        }

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

    public DiscordRichPresence build() {

        drp = new DiscordRichPresence();

        // Discord Presence Images
        this.drp.largeImageText = this.largeImageText;
        this.drp.smallImageText = this.smallImageText;
        this.drp.largeImageKey = this.largeImage;
        this.drp.smallImageKey = this.smallImage;

        // Game Information
        this.drp.details = this.gameType + (this.playState == null ? "" : ": " + this.playState);
        this.drp.state = this.gameMode;

        // Party Indicators
        this.drp.partyId = this.partyID;
        this.drp.partyMax = party ? this.partyMax : 0;
        this.drp.partySize = this.partySize;

        // Timestamps

        if (this.timestamp) {

            this.drp.endTimestamp = this.timeEnd;
            this.drp.startTimestamp = this.timeStart;

        }

        // Join secret
        this.drp.joinSecret = joinSecret ? joinSecretS : null;

        this.drp.instance = 1;
        return this.drp;
    }


    public void updateRichPresence() {

        if (updateRequired) {
            DiscordRPCLibrary.updatePresence(this.build());
            this.updateRequired = false;
        }

    }

    public void forceUpdate() {
        this.updateRequired = true;
        discord.setConnected(true);
        updateRichPresence();
    }

    public void runCallbacks() {

        DiscordRPCLibrary.runCallbacks();

    }

}
