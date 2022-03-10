package dev.antimoxs.hyplus.objects;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.objects.player.Player;
import dev.antimoxs.hypixelapiHP.requests.MojangRequest;
import dev.antimoxs.hypixelapiHP.response.PlayerResponse;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;

public class HySimplePlayer {

    private String name = "#UndefinedPlayer#";
    private String rank = "#rank#";
    private String plusColor = "&c";
    private boolean goldenName = true;

    private Player playerObject = null;

    public HySimplePlayer() {}

    public HySimplePlayer(String name, String rank, String plusColor, boolean goldenName) {

        this.name = name;
        if (rank != null) {
            this.rank = rank;
        }
        else {
            this.rank = "";
        }
        this.plusColor = plusColor;
        this.goldenName = goldenName;

    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public String getRankFormatted() {

        switch (rank) {

            case "": return "&7";
            case "VIP": return "&a[VIP]";
            case "VIP+": return "&a[VIP&6+&a]";
            case "MVP": return "&b[MVP]";
            case "MVP+": return "&b[MVP" + plusColor + "+&b]";
            case "MVP++": return goldenName ? "&6[MVP" + plusColor + "++&6]" : "&b[MVP" + plusColor + "++&b]";

        }

        return "";

    }

    public Player getPlayerObject() {

        if (this.playerObject == null) {

            requestPlayerObject();

        }

        return playerObject;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public void setPlayerObject(Player player) { this.playerObject = player; }

    public String getPlayer() {

        return rank + " " + name;

    }

    public String getPlayerBlank() {

        String name = HyUtilities.matchOutColorCode(this.name);
        return name.split(" ")[name.startsWith("[") ? 1 : 0];

    }

    public void requestPlayerObject() {

        requestPlayerObject(MojangRequest.getUUID(name));

    }
    public void requestPlayerObject(String uuid) {

        try {

            HyPlus.debugLog("Requesting Player: " + uuid);
            PlayerResponse p = HyPlus.getInstance().hypixelApi.createPlayerRequestUUID(uuid);

            if (p.success) {

                this.playerObject = p.getPlayer();

            }
            else {

                HyPlus.debugLog("Failed player OBJ lookup: " + p.cause);

            }

        } catch (ApiRequestException e) {
            e.printStackTrace();
        }

    }

    public boolean hasPlayerObject() { return this.playerObject == null; }

}
