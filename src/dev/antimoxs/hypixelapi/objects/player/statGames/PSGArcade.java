package dev.antimoxs.hypixelapi.objects.player.statGames;

import com.google.gson.annotations.SerializedName;

public class PSGArcade {

    @SerializedName("weekly_coins_a")
    private int weeklyCoinsA = -1;
    @SerializedName("monthly_coins_a")
    private int monthlyCoinsA = -1;
    @SerializedName("monthly_coins_b")
    private int monthlyCoinsB = -1;
    @SerializedName("weekly_coins_b")
    private int weeklyCoinsB = -1;
    @SerializedName("coins")
    private int coins = -1;

    @SerializedName("dec2016_achievements")
    private boolean dec2016_achievements = false;
    @SerializedName("dec2016_achievements2")
    private boolean dec2016_achievements2 = false;

    // Mini walls
    @SerializedName("miniwalls_activeKit")
    private String miniwalls_activeKit = "";
    @SerializedName("kills_mini_walls")
    private int mw_kills = -1;
    @SerializedName("deaths_mini_walls")
    private int mw_deaths = -1;
    @SerializedName("arrows_shot_mini_walls")
    private int mw_bow_shots = -1;
    @SerializedName("wither_damage_mini_walls")
    private int mw_wither_damage = -1;
    @SerializedName("final_kills_mini_walls")
    private int mw_finals = -1;
    @SerializedName("wins_mini_walls")
    private int mw_wins = -1;
    @SerializedName("wither_kills_mini_walls")
    private int mw_wither_kills = -1;
    @SerializedName("arrows_hit_mini_walls")
    private int mw_arrow_hits = -1;


    // DAYONE
    @SerializedName("headshots_dayone")
    private int dayone_headshots = -1;
    @SerializedName("kills_dayone")
    private int dayone_kills = -1;

    // Simon says
    @SerializedName("rounds_simon_says")
    private int ss_rounds = -1;

    // Hole in the wall
    @SerializedName("hitw_record_q")
    private int hitw_qualifications_record = -1;
    @SerializedName("hitw_record_f")
    private int hitw_finals_record = -1;
    @SerializedName("rounds_hole_in_the_wall")
    private int hitw_rounds = -1;
    @SerializedName("wins_hole_in_the_wall")
    private int hitw_wins = -1;

    // STAMP ????
    @SerializedName("stamp_level")
    private int stamp_level = -1;
    @SerializedName("time_stamp")
    private long stamp_time = 0L;

    // ZOMBIES
    @SerializedName("fastest_time_10_zombies")
    private  int zb_10_time = -1;
    @SerializedName("fastest_time_10_zombies_deadend_normal")
    private int zb_10_time_normal_deadend = -1;
    @SerializedName("fastest_time_20_zombies")
    private int zb_20_time = -1;
    @SerializedName("fastest_time_20_zombies_deadend_normal")
    private int zb_20_time_normal_deadend = -1;
    @SerializedName("best_round_zombies")
    private int zb_best_round = -1;

    @SerializedName("headshots_zombies")
    private int zb_headshots = -1;
    @SerializedName("deaths_zombies")
    private int zb_deaths = -1;

}
