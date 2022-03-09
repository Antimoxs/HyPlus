package dev.antimoxs.hypixelapiHP.objects.player.statGames;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class PSGDuels {

    /*

    Rename all stats not starting with their mode to the mode.

     */

    // General
    public String duels_recently_played = "No data for 'duels_recently_played'";
    public String show_lb_option = "No data for 'show_lb_option";
    public String rematch_option_1 = "No data for 'rematch_option_1";
    public String selected_2 = "No data for 'selected_2'";
    public String selected_1 = "No data for 'selected_1'";
    public String selected_2_new = "No data for 'selected_2_new'";
    public String selected_1_new = "No data for 'selected_1_new'";
    public String chat_enabled = "No data for 'chat_enabled'";
    public String kit_menu_option = "No data for 'kit_menu_option'";
    public String[] maps_won_on = new String[0];
    public String[] packages = new String[0];
    public int pingPreference = -1;
    public String active_weaponpacks = "No data for 'active_weaponpacks";
    public String duels_recently_played2 = "No data for 'duels_recently_played2'";
    public int leaderboardPage_win_streak = -1;
    public String shop_sort = "No data for 'shop_sort'";
    public String active_auras = "No data for 'active_auras'";
    public String active_projectile_trail = "No data for 'active_auras'";
    public String active_goal = "No data for 'active_goal'";
    public String active_cosmetictitle = "No data for 'active_cosmetictitle'";
    public String active_emblem = "No data for 'active_emblem'";
    public String progress_mode = "No data for 'progress_mode'";

    @SerializedName("duels_chests")
    public int lootchests = -1;
    @SerializedName("Duels_openedChests")
    public int lootchests_opened = -1;
    @SerializedName("Duels_openedCommons")
    public int lootchests_opened_commons= -1;
    @SerializedName("Duels_openedRares")
    public int lootchests_opened_rare = -1;
    @SerializedName("Duels_openedEpics")
    public int lootchests_opened_epic = -1;
    @SerializedName("Duels_openedLegendaries")
    public int lootchests_opened_legendary = -1;
    @SerializedName("duels_chest_history")
    public String[] lootchests_history = new String[0];


    // Overall
    @SerializedName("melee_swings")
    public int general_melee_swings = -1;
    @SerializedName("melee_hits")
    public int general_melee_hits = -1;
    @SerializedName("rounds_played")
    public int general_rounds_played = -1;
    @SerializedName("wins")
    public int general_wins = -1;
    @SerializedName("kills")
    public int general_kills = -1;
    @SerializedName("health_regenerated")
    public int general_health_regenerated = -1;
    @SerializedName("damage_dealt")
    public int general_damage_dealt = -1;
    @SerializedName("heal_pots_used")
    public int general_heal_pots_used = -1;
    @SerializedName("bow_shots")
    public int general_bow_shots = -1;
    @SerializedName("bow_hits")
    public int general_bow_hits = -1;
    @SerializedName("games_played_duels")
    public int general_games_played = -1;
    @SerializedName("losses")
    public int general_losses = -1;
    @SerializedName("deaths")
    public int general_deaths = -1;
    @SerializedName("coins")
    public int general_coins = -1;
    @SerializedName("blocks_placed")
    public int general_blocks_placed = -1;
    @SerializedName("golden_apples_eaten")
    public int general_golden_apples_eaten = -1;
    @SerializedName("current_winstreak")
    public int general_current_winstreak = -1;
    @SerializedName("best_overall_winstreak")
    public int general_best_overall_winstreak = -1;

    public int all_modes_rookie_title_prestige = -1;
    public int all_modes_iron_title_prestige = -1;
    public int all_modes_gold_title_prestige = -1;
    public int all_modes_diamond_title_prestige = -1;
    public int all_modes_master_title_prestige = -1;
    public int all_modes_legend_title_prestige = -1;
    public int all_modes_grandmaster_title_prestige = -1;
    public int all_modes_godlike_title_prestige = -1;


    // UHC
    @SerializedName("layout_uhc_duel_layout")
    public HashMap<Integer, String> uhc_duel_layout = new HashMap<>();

    @SerializedName("best_uhc_winstreak")
    public int uhc_winstreak_best = -1;
    @SerializedName("current_uhc_winstreak")
    public int uhc_winstreak_current = -1;

    public int uhc_rookie_title_prestige = -1;
    public int uhc_iron_title_prestige = -1;
    public int uhc_gold_title_prestige = -1;
    public int uhc_diamond_title_prestige = -1;
    public int uhc_master_title_prestige = -1;
    public int uhc_legend_title_prestige = -1;
    public int uhc_grandmaster_title_prestige = -1;
    public int uhc_godlike_title_prestige = -1;

    @SerializedName("current_winstreak_mode_uhc_duel")
    public int uhc_duel_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_uhc_duel")
    public int uhc_duel_winstreak_best = -1;
    @SerializedName("duels_winstreak_best_uhc_duel")
    public int uhc_duel_winstreak_daily = -1;
    public int uhc_duel_health_regenerated = -1;
    public int uhc_duel_rounds_played = -1;
    public int uhc_duel_damage_dealt = -1;
    public int uhc_duel_bow_shots = -1;
    public int uhc_duel_melee_swings = -1;
    public int uhn_duel_melee_hits = -1;
    public int uhc_duel_bow_hits = -1;
    public int uhc_duel_kills = -1;
    public int uhc_duel_wins = -1;
    public int uhc_duel_losses = -1;
    public int uhc_duel_deaths = -1;
    public int uhc_duel_golden_apples_eaten = -1;
    public int uhc_duel_blocks_placed = -1;

    @SerializedName("current_winstreak_mode_uhc_doubles")
    public int uhc_doubles_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_uhc_doubles")
    public int uhc_doubles_winstreak_best = -1;
    @SerializedName("duels_winstreak_best_uhc_doubles")
    public int uhc_doubles_winstreak_daily = -1;
    public int uhc_doubles_bow_shots = -1;
    public int uhc_doubles_rounds_played = -1;
    public int uhc_doubles_melee_swings = -1;
    public int uhc_doubles_health_regenerated = -1;
    public int uhc_doubles_melee_hits = -1;
    public int uhc_doubles_damage_dealt = -1;
    public int uhc_doubles_bow_hits = -1;
    public int uhc_doubles_deaths = -1;
    public int uhc_doubles_losses = -1;
    public int uhc_doubles_golden_apples_eaten = -1;
    public int uhc_doubles_wins = -1;
    public int uhc_doubles_blocks_placed = -1;
    public int uhc_doubles_kills = -1;
    public boolean uhc_doubles = false;

    @SerializedName("current_winstreak_mode_uhc_meetup")
    public int uhc_meetup_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_uhc_meetup")
    public int uhc_meetup_winstreak_best = -1;
    @SerializedName("duels_winstreak_best_uhc_meetup")
    public int uhc_meezup_winstreak_daily = -1;
    public int uhc_meetup_blocks_placed = -1;
    public int uhc_meetup_bow_hits = -1;
    public int uhc_meetup_bow_shots = -1;
    public int uhc_meetup_damage_dealt = -1;
    public int uhc_meetup_health_regenerated = -1;
    public int uhc_meetup_melee_hits = -1;
    public int uhc_meetup_melee_swings = -1;
    public int uhc_meetup_rounds_played = -1;

    @SerializedName("current_winstreak_mode_uhc_four")
    public int uhc_four_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_uhc_four")
    public int uhc_four_winstreak_best = -1;
    @SerializedName("duels_winstreak_best_uhc_four")
    public int uhc_four_winstreak_daily = -1;
    public int uhc_four_bow_shots = -1;
    public int uhc_four_damage_dealt = -1;
    public int uhc_four_golden_apple_eaten = -1;
    public int uhc_four_health_regenerated = -1;
    public int uhc_four_kills = -1;
    public int uhc_four_melee_hits = -1;
    public int uhc_four_melee_swings = -1;
    public int uhc_four_rounds_played = -1;
    public int uhc_four_wins = -1;
    public int uhc_four_blocks_placed = -1;
    public int uhc_four_bow_hits = -1;
    public int uhc_four_deaths = -1;
    public int uhc_four_losses = -1;

    // Combo
    @SerializedName("duels_winstreak_best_combo_duel")
    public int combo_duel_winstreak_best = -1;
    @SerializedName("duels_winstreak_combo_duel")
    public int combo_duel_winstreak_current = -1;
    public int combo_rookie_title_prestige = -1;
    public int combo_duel_golden_apples_eaten = -1;
    public int combo_duel_health_regenerated = -1;
    public int combo_duel_melee_hits = -1;
    public int combo_duel_melee_swings = -1;
    public int combo_duel_rounds_played = -1;

    // Potion
    public int potion_duel_rounds_played = -1;
    public int potion_duel_melee_hits = -1;
    public int potion_duel_health_regenerated = -1;
    public int potion_duel_heal_pots_used = -1;
    public int potion_duel_kills = -1;
    public int potion_duel_wins = -1;
    public int potion_duel_melee_swings = -1;
    public int potion_duels_damage_dealt = -1;
    public int potion_duel_deaths = -1;
    public int potion_duel_losses = -1;

    // OP
    public int current_op_winstreak = -1;
    public int op_duel_deaths = -1;
    public int op_duel_damage_dealt = -1;
    public int op_duel_melee_swings = -1;
    public int op_duel_melee_hits = -1;
    public int op_duel_health_regenerated = -1;
    public int op_duel_rounds_played = -1;
    public int op_duel_losses = -1;
    public int op_rookie_title_prestige = -1;
    public int best_winstreak_mode_op_duel = -1;
    public int best_op_winstreak = -1;
    public int current_winstreak_mode_op_duel = -1;
    public int op_duel_kills = -1;
    public int op_duel_wins = -1;
    public int op_doubles_damage_dealt = -1;
    public int op_doubles_health_regenerated = -1;
    public int op_doubles_melee_hits = -1;
    public int op_doubles_melee_swings = -1;
    public int op_doubles_rounds_played = -1;
    public int current_winstreak_mode_op_duels = -1;
    public int op_doubles_deaths = -1;
    public int op_doubles_losses = -1;
    public int best_winstreak_mode_op_doubles = -1;
    public int op_doubles_wins= -1;
    public int duels_winstreak_best_op_doubles = -1;
    public int op_doubles_kills = -1;

    // NoDebuff
    public int current_no_debuff_winstreak = -1;
    public int best_no_debuff_winstreak = -1;
    public int no_debuff_rookie_title_prestige = -1;

    // SkyWars
    public String sw_duels_kit_new = "No data for 'sw_duels_kit_new'";
    public int sw_duel_rounds_played = -1;
    public int sw_duel_damage_dealt = -1;
    public int sw_duel_health_regenerated = -1;
    public int sw_duel_melee_hits = -1;
    public int sw_duel_melee_swings = -1;
    public int sw_duel_bow_hits = -1;
    public int sw_duel_bow_shots = -1;
    public int sw_doubles_melee_hits = -1;
    public int sw_doubles_melee_swings = -1;
    public int sw_doubles_rounds_played = -1;
    public int sw_doubles_health_regenerated = -1;
    public int sw_doubles_damage_dealt = -1;
    public int best_skywars_winstreak = -1;
    public int current_skywars_winstreak = -1;
    public int sw_duel_kit_wins = -1;
    public int sw_duel_paladin_kit_wins = -1;
    public int paladin_kit_wins = -1;
    public int sw_duel_kills = -1;
    public int sw_duel_wins = -1;
    public int duels_winstreak_best_sw_duel = -1;
    public int sw_duel_pyro_kit_wins = -1;
    public int pyro_kit_wins = -1;
    public int sw_duel_blacksmith_kit_wins = -1;
    public int blacksmith_kit_wins = -1;
    public int sw_tournament_melee_hits = -1;
    public int sw_tournament_damage_dealt = -1;
    public int sw_tournament_losses = -1;
    public int sw_tournament_kills = -1;
    public int sw_tournament_melee_swings = -1;
    public int sw_tournament_health_regenerated = -1;
    public int sw_tournament_rounds_played = -1;
    public int sw_tournament_deaths = -1;
    public int skywars_rookie_title_prestige = -1;
    public String sw_duels_kit_new2 = "No data for 'sw_duels_kit_new2'";
    public int sw_duel_blocks_placed = -1;
    public int sw_doubles_deaths = -1;
    public int sw_doubles_losses = -1;
    public int sw_doubles_blocks_placed = -1;
    public String sw_duels_kit_new3 = "No data for 'sw_duels_kit_new3'";
    public int sw_doubles_bow_hits = -1;
    public int sw_doubles_bow_shots = -1;
    public int current_winstreak_mode_sw_doubles = -1;
    public int best_winstreak_mode_sw_doubles = -1;
    public int pyromancer_kit_wins = -1;
    public int sw_doubles_kills = -1;
    public int sw_doubles_kit_wins = -1;
    public int sw_doubles_pyromancer_kit_wins = -1;
    public int sw_doubles_wins = -1;
    public int duels_winstreak_best_sw_duels = -1;
    public int champion_kit_wins = -1;
    public int sw_doubles_champion_kit_wins = -1;
    public int scout_kit_wins = -1;
    public int sw_doubles_scout_kit_wins = -1;
    public int magician_kit_wins = -1;
    public int sw_doubles_magician_kit_wins = -1;
    public int armorer_kit_wins = -1;
    public int sw_doubles_armorer_kit_wins = -1;
    public int bowman_kit_wins = -1;
    public int sw_doubles_bowman_kit_wins = -1;
    public int athlete_kit_wins = -1;
    public int sw_doubles_athlete_kit_wins = -1;
    public int sw_doubles_blacksmith_kit_wins = -1;
    public int hound_kit_wins = -1;
    public int sw_doubles_hound_kit_wins = -1;

    // Blitz
    public String blitz_duels_kit = "No data for 'blitz_duels_kit'";
    public int blitz_duel_melee_hits = -1;
    public int blitz_duel_melee_swings = -1;
    public int blitz_duel_rounds_played = -1;
    public int blitz_duel_health_regenerated = -1;
    public int blitz_duel_damage_dealt = -1;
    public int best_blitz_winstreak = -1;
    public int current_blitz_winstreak = -1;
    public int duels_winstreak_best_blitz_duel = -1;
    public int blitz_duel_kit_wins = -1;
    public int golem_kit_wins = -1;
    public int kit_wins = -1;
    public int blitz_duel_wins = -1;
    public int blitz_duel_kills = -1;
    public int blitz_duel_golem_kit_wins = -1;
    public int blitz_duel_jockey_kit_wins = -1;
    public int jockey_kit_wins = -1;
    public int blitz_rookie_title_prestige = -1;
    public int blitz_duel_blocks_placed = -1;
    public int current_winstreak_mode_blitz_duel = -1;
    public int blitz_duel_deaths = -1;
    public int blitz_duel_losses = -1;
    public int blitz_duel_bow_hits = -1;
    public int blitz_duel_bow_shots = -1;
    public int best_winstreak_mode_blitz_duel = -1;
    public int blitz_duel_troll_kit_wins = -1;
    public int troll_kit_wins = -1;

    // Sumo
    public int sumo_duel_melee_swings = -1;
    public int sumo_duel_rounds_played = -1;
    public int sumo_duel_melee_hits = -1;
    public int current_sumo_winstreak = -1;
    public int best_sumo_winstreak = -1;
    public int duels_winstreak_best_sumo_duel = -1;
    public int sumo_duel_kills = -1;
    public int sumo_duel_wins = -1;
    public int sumo_duel_losses = -1;
    public int sumo_duel_deaths = -1;
    public int sumo_rookie_title_prestige = -1;
    public int best_winstreak_mode_sumo_duel = -1;
    public int current_winstreak_mode_sumo_duel = -1;

    // BowSpleef
    public int bowspleef_duel_rounds_played = -1;
    public int best_winstreak_mode_bowspleef_duel = -1;
    public int current_winstreak_mode_bowspleef_duel = -1;
    public int bowspleef_duel_bow_shots = -1;
    public int bowspleef_duel_wins = -1;

    // MegaWalls
    public String mw_duels_class = "No data for 'mw_duels_class'";
    public int mw_duel_bow_shots = -1;
    public int mw_duel_melee_hits = -1;
    public int mw_duel_rounds_played = -1;
    public int mw_duel_damage_dealt = -1;
    public int mw_duel_melee_swings = -1;
    public int mw_duel_health_regenerated = -1;
    public int mw_duel_bow_hits = -1;
    public int mega_walls_rookie_title_prestige = -1;
    public int mw_doubles_blocks_placed = -1;
    public int mw_doubles_damage_dealt = -1;
    public int mw_doubles_health_regenerated = -1;
    public int mw_doubles_melee_hits = -1;
    public int mw_doubles_melee_swings = -1;
    public int mw_doubles_rounds_plaxed = -1;
    public int mw_doubles_bow_hits = -1;
    public int mw_doubles_bow_shots = -1;
    @SerializedName("current_winstreak_mode_mw_doubles")
    public int current_winstreak_mode_mw_doubles = -1;
    public int best_mega_walls_winstreak = -1;
    public int current_mega_walls_winstreak = -1;
    public int best_winstreak_mode_mw_doubles = -1;
    public int mw_doubles_kills = -1;
    public int mw_doubles_kit_wins = -1;
    public int mw_doubles_skeleton_kit_wins = -1;
    public int mw_doubles_wins = -1;
    public int skeleton_kit_wins = -1;

    // ?
    public int enderman_a_meters_travelled_standard = -1;
    public int enderman_a_meters_travelled = -1;
    public int meters_travelled_standard = -1;
    public int enderman_meters_travelled = -1;
    public int meters_travelled = -1;
    public int enderman_meters_travelled_standart = -1;

    // Bow
    public int bow_rookie_title_prestige = -1;
    public int bow_duel_bow_hits = -1;
    public int bow_duel_bow_shots = -1;
    public int bow_duel_health_regenerated = -1;
    public int bow_duel_damage_dealt = -1;
    public int bow_duel_rounds_played = -1;

    // Classic
    public int classic_rookie_title_prestige = -1;
    public int classic_duel_damage_dealt = -1;
    public int classic_duel_health_regenerated = -1;
    public int classic_duel_melee_hits = -1;
    public int classic_duel_melee_swings = -1;
    public int classic_duel_rounds_played = -1;
    public int classic_duel_bow_shots = -1;
    public int classic_duel_bow_hits = -1;

    // Tournament
    public int tournament_rookie_title_prestige = -1;

    // TNT
    public int tnt_games_rookie_title_prestige = -1;
    public int best_tnt_games_winstreak = -1;
    public int current_tnt_games_winstreak = -1;

    // Bridge
    @SerializedName("goals")
    public int bridge_goals = -1;
    public int bridge_kills = -1;
    public int bridge_deaths = -1;
    @SerializedName("bridgeMapWins")
    public String[] bridge_mapWins = new String[0];
    @SerializedName("layout_bridge_duel_layout")
    public HashMap<Integer, String> bridge_duel_layout = new HashMap<>();
    @SerializedName("current_bridge_winstreak")
    public int bridge_winstreak = -1;
    @SerializedName("best_bridge_winstreak")
    public int bridge_winstreak_best = -1;

    public int bridge_rookie_title_prestige = -1;
    public int bridge_iron_title_prestige = -1;
    public int bridge_gold_title_prestige = -1;
    public int bridge_diamond_title_prestige = -1;
    public int bridge_master_title_prestige = -1;
    public int bridge_legend_title_prestige = -1;
    public int bridge_grandmaster_title_prestige = -1;
    public int bridge_godlike_title_prestige = -1;

    @SerializedName("best_winstreak_mode_bridge_duel")
    public int bridge_duel_winstreak_best = -1;
    @SerializedName("current_winstreak_mode_bridge_duel")
    public int bridge_duel_winstreak_current = -1;
    public int bridge_duel_bridge_deaths = -1;
    public int bridge_duel_bridge_kills = -1;
    public int bridge_duel_goals = -1;
    public int bridge_duel_wins = -1;
    public int bridge_duel_health_regenerated = -1;
    public int bridge_duel_damage_dealt = -1;
    public int bridge_duel_melee_hits = -1;
    public int bridge_duel_blocks_placed = -1;
    public int bridge_duel_melee_swings = -1;
    public int bridge_duel_rounds_played = -1;
    public int bridge_duel_bow_hits = -1;
    public int bridge_duel_bow_shots = -1;

    @SerializedName("current_winstreak_mode_bridge_doubles")
    public int bridge_doubles_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_bridge_doubles")
    public int bridge_doubles_winstreak_best = -1;
    @SerializedName("duels_winstreak_best_bridge_doubles")
    public int bridge_doubles_winstreak_daily = -1;
    public int bridge_doubles_bridge_deaths = -1;
    public int bridge_doubles_bridge_kills = -1;
    public int bridge_doubles_rounds_played = -1;
    public int bridge_doubles_kills = -1;
    public int bridge_doubles_wins = -1;
    public int bridge_doubles_deaths = -1;
    public int bridge_doubles_damage_dealt = -1;
    public int bridge_doubles_health_regenerated = -1;
    public int bridge_doubles_blocks_placed = -1;
    public int bridge_doubles_bow_shots = -1;
    public int bridge_doubles_melee_swings = -1;
    public int bridge_doubles_bow_hits = -1;
    public int bridge_doubles_melee_hits = -1;
    public int bridge_doubles_goals = -1;
    public int bridge_doubles_losses = -1;

    @SerializedName("best_winstreak_mode_bridge_four")
    public int bridge_four_winstreak_best = -1;
    @SerializedName("current_winstreak_mode_bridge_four")
    public int bridge_four_winstreak_current = -1;
    public int bridge_four_damage_dealt = -1;
    public int bridge_four_blocks_placed = -1;
    public int bridge_four_melee_swings = -1;
    public int bridge_four_health_regenerated = -1;
    public int bridge_four_melee_hits = -1;
    public int bridge_four_bridge_deaths = -1;
    public int bridge_four_bridge_kills = -1;
    public int bridge_four_goals = -1;
    public int bridge_four_wins = -1;
    public int bridge_four_deaths = -1;
    public int bridge_four_rounds_played = -1;
    public int bridge_four_kills = -1;
    public int bridge_four_losses = -1;

    public int bridge_2v2v2v2_blocks_placed = -1;
    public int bridge_2v2v2v2_health_regenerated = -1;
    public int bridge_2v2v2v2_rounds_played = -1;

    @SerializedName("current_winstreak_mode_bridge_3v3v3v3")
    public int bridge_3v3v3v3_winstreak_current = -1;
    @SerializedName("best_winstreak_mode_bridge_3v3v3v3")
    public int bridge_3v3v3v3_winstreak_best = -1;
    public int bridge_3v3v3v3_blocks_placed = -1;
    public int bridge_3v3v3v3_bow_hits = -1;
    public int bridge_3v3v3v3_bow_shots = -1;
    public int bridge_3v3v3v3_bridge_deaths = -1;
    public int bridge_3v3v3v3_bridge_kills = -1;
    public int bridge_3v3v3v3_damage_dealt = -1;
    public int bridge_3v3v3v3_health_regenerated = -1;
    public int bridge_3v3v3v3_melee_hits = -1;
    public int bridge_3v3v3v3_melee_swings = -1;
    public int bridge_3v3v3v3_deaths = -1;
    public int bridge_3v3v3v3_losses = -1;
    public int bridge_3v3v3v3_rounds_played = -1;
    public int bridge_3v3v3v3_wins = -1;
    public int bridge_3v3v3v3_kills = -1;
    public int bridge_3v3v3v3_goals = -1;
















}
