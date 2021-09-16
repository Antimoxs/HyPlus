package dev.antimoxs.hyplus.objects;

import dev.antimoxs.hyplus.HyUtilities;

public class HySimplePlayer {

    private String name = "#UndefinedPlayer#";
    private String rank = "#rank#";

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

        String name = HyUtilities.matchOutColorCode(this.name);
        return name.split(" ")[name.startsWith("[") ? 1 : 0];

    }

}
