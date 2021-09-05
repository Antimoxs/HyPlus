package dev.antimoxs.hyplus.objects;

public class HySimplePlayer {

    private String name = "#UndefinedPlayer#";
    private String rank = "#rank#";

    public HySimplePlayer() {}

    public HySimplePlayer(String name, String rank) {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPlayer() {

        return rank + " " + name;

    }

}
