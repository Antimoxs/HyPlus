package dev.antimoxs.hyplus.api.player;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.player.Player;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hypixelapi.response.PlayerResponse;


public class HySimplePlayer {

    private String name = "#UndefinedPlayer#";
    private String rank = "#rank#";
    private String plusColor = "&c";
    private boolean goldenName = true;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlayer() {

        return rank + " " + name;

    }

    public String getPlayerBlank() {

        String name = matchOutColorCode(this.name);
        return name.split(" ")[name.startsWith("[") ? 1 : 0];

    }

    public static String matchOutColorCode(String s) {


        s = s.replaceAll("§0", "");
        s = s.replaceAll("§1", "");
        s = s.replaceAll("§2", "");
        s = s.replaceAll("§3", "");
        s = s.replaceAll("§4", "");
        s = s.replaceAll("§5", "");
        s = s.replaceAll("§6", "");
        s = s.replaceAll("§7", "");
        s = s.replaceAll("§8", "");
        s = s.replaceAll("§9", "");
        s = s.replaceAll("§a", "");
        s = s.replaceAll("§b", "");
        s = s.replaceAll("§c", "");
        s = s.replaceAll("§d", "");
        s = s.replaceAll("§e", "");
        s = s.replaceAll("§f", "");
        s = s.replaceAll("§g", "");

        s = s.replaceAll("§k", "");
        s = s.replaceAll("§l", "");
        s = s.replaceAll("§m", "");
        s = s.replaceAll("§n", "");
        s = s.replaceAll("§o", "");
        s = s.replaceAll("§r", "");

        return s;

    }

}
