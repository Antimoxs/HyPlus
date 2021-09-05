package dev.antimoxs.hypixelapi.objects.player.statGames;

import com.google.gson.annotations.SerializedName;

public class PSGMurderMystery {

    @SerializedName("detective_chance")
    private int chance_detective = -1;
    @SerializedName("murderer_chance")
    private int chance_murder = -1;
    @SerializedName("games_MURDER_DOUBLE_UP")
    private int murder_DU;


    public int getChanceMurder() { return chance_murder; }
    public int getChanceDetective() { return chance_detective; }
    public int getMurderCount_DoubleUp() { return murder_DU; }

}
