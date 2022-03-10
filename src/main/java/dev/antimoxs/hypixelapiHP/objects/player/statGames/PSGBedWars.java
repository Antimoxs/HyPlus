package dev.antimoxs.hypixelapiHP.objects.player.statGames;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class PSGBedWars {

    @SerializedName("Experience")
    public int exp = 0;

    public Practise practise = new Practise();
    public PrivateGames privategames = new PrivateGames();

    static class Practise {

        public String selected = "";
        public Records records = new Records();
        public Bridging bridging = new Bridging();
        public Mlg mlg = new Mlg();
        public FireballJumping fireball_jumping = new FireballJumping();

        static class Records {

            @SerializedName("bridging_distance_30:elevation_NONE:angle_STRAIGHT:")
            public int bridging_distance_30_elevation_NONE_angle_STRAIGHT = 0;
            @SerializedName("bridging_distance_30:elevation_NONE:angle_DIAGONAL:")
            public int bridging_distance_30_elevation_NONE_angle_DIAGONAL = 0;


        }
        static class Bridging {

            public int blocks_placed = 0;
            public int successful_attempts = 0;
            public int failed_attempts = 0;

        }
        static class Mlg {

            public int blocks_placed = 0;
            public int successful_attempts = 0;
            public int failed_attempts = 0;

        }
        static class FireballJumping {

            public int blocks_placed = 0;
            public int successful_attempts = 0;
            public int failed_attempts = 0;

        }

    }
    static class PrivateGames {

        public String respawn_time = "5 Seconds";
        public boolean one_hit_one_kill = false;
        public String health_buff = "Normal Health";
        public boolean bed_instabreak = false;
        public boolean max_team_upgrades = false;
        public String speed = "No Speed";
        public boolean low_gravity = false;
        public String event_time = "1x - Normal";
        public boolean no_emeralds = false;
        public boolean disable_block_protection = false;
        public boolean no_diamonds = false;

    }

}
