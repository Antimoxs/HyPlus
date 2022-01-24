package dev.antimoxs.hyplus.objects;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.player.Player;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hypixelapi.response.PlayerResponse;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;

public class HySimplePlayer {

    private String name = "#UndefinedPlayer#";
    private String rank = "#rank#";

    private Player playerObject = null;

    public HySimplePlayer() {}

    public HySimplePlayer(String name, String rank, String plusColor) {

        this.name = name;
        if (rank != null) {
            this.rank = rank;
        }
        else {
            this.rank = "";
        }

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
            case "MVP": return "&a[VIP]";
            case "MVP+": return "&a[VIP]";
            case "MVP++": return "&a[VIP]";

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

        try {

            PlayerResponse p = HyPlus.getInstance().hypixelApi.createPlayerRequestUUID(MojangRequest.getUUID(name));

            if (p.success) {

                this.playerObject = p.getPlayer();

            }
            else {

                System.out.println("Failed player OBJ lookup: " + p.cause);

            }

        } catch (ApiRequestException e) {
            e.printStackTrace();
        }

    }

    public boolean hasPlayerObject() { return this.playerObject == null; }

}
