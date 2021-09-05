package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsgType;
import net.labymod.gui.elements.CheckBox;

public class HySettings {

    private HyPlus HyPlus;

    public HySettings(HyPlus HyPlus) {

        this.HyPlus = HyPlus;

    }

    // General
    public boolean HYPLUS_GENERAL_TOGGLE = true;
    public boolean HYPLUS_GENERAL_LOOP_TOGGLE = true;
    public String HYPLUS_GENERAL_APIKEY = "";

    // External API
    public boolean HYPLUS_API_TOGGLE = false;

    // About
    public boolean HYPLUS_ABOUT_UPDATE = true;

    // Quickplay
    public boolean HYPLUS_QUICKPLAY_TOGGLE = true;
    public int HYPLUS_QUICKPLAY_GUIKEY = 89;






    // AutoGG
    public boolean HYPLUS_AUTOGG_TOGGLE = true;

    // AntiBlur
    public boolean HYPLUS_DUELS_ANTIBLUR = true;





    // PartyDetector
    public boolean HYPLUS_HPD_TOGGLE = true;
    public int HYPLUS_HPD_INTERVAL = 5;



    // GameBadge -----------------------------------------------------
    public boolean HYPLUS_GBADGE_TOGGLE = true;
    public boolean HYPLUS_GBADGE_TYPE = true;
    public boolean HYPLUS_GBADGE_MODE = true;
    public boolean HYPLUS_GBADGE_MAP = true;

    // BedWars
    public boolean HYPLUS_GBADGE_BEDWARS_TOGGLE = true;

    public boolean HYPLUS_GBADGE_BEDWARS_STATS_KILLS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_DEATHS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_KD = true;

    public boolean HYPLUS_GBADGE_BEDWARS_STATS_FINALS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_FINALDEATHS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_FKD = true;

    public boolean HYPLUS_GBADGE_BEDWARS_STATS_BEDBREAKS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_BEDLOSES = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_BEDRATE = true;

    public boolean HYPLUS_GBADGE_BEDWARS_STATS_WINS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_LOSS = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_WLR = true;

    public boolean HYPLUS_GBADGE_BEDWARS_STATS_STREAK = true;
    public boolean HYPLUS_GBADGE_BEDWARS_STATS_STREAKBEST = true;

    // SkyWars
    public boolean HYPLUS_GBADGE_SKYWARS_TOGGLE = true;

    public boolean HYPLUS_GBADGE_SKYWARS_STATS_KILLS = true;
    public boolean HYPLUS_GBADGE_SKYWARS_STATS_DEATHS = true;
    public boolean HYPLUS_GBADGE_SKYWARS_STATS_KD = true;

    public boolean HYPLUS_GBADGE_SKYWARS_STATS_WINS = true;
    public boolean HYPLUS_GBADGE_SKYWARS_STATS_LOSS = true;
    public boolean HYPLUS_GBADGE_SKYWARS_STATS_WL = true;

    public boolean HYPLUS_GBADGE_SKYWARS_STATS_STREAK = true;
    public boolean HYPLUS_GBADGE_SKYWARS_STATS_STREAKBEST = true;

    // Murder
    public boolean HYPLUS_GBADGE_MURDER_TOGGLE = true;

    public boolean HYPLUS_GBADGE_MURDER_STATS_HERO = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_MURDERKILLS = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_MURDERKNIVES = true;

    public boolean HYPLUS_GBADGE_MURDER_STATS_WINS = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_LOSS = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_WL = true;

    public boolean HYPLUS_GBADGE_MURDER_STATS_MWINS = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_MLOSS = true;
    public boolean HYPLUS_GBADGE_MURDER_STATS_MWL = true;

    // Duels
    public boolean HYPLUS_GBADGE_DUELS_TOGGLE = true;

    public boolean HYPLUS_GBADGE_DUELS_STATS_KILLS = true;
    public boolean HYPLUS_GBADGE_DUELS_STATS_DEATHS = true;
    public boolean HYPLUS_GBADGE_DUELS_STATS_KD = true;

    public boolean HYPLUS_GBADGE_DUELS_STATS_WINS = true;
    public boolean HYPLUS_GBADGE_DUELS_STATS_LOSS = true;
    public boolean HYPLUS_GBADGE_DUELS_STATS_WL = true;

    public boolean HYPLUS_GBADGE_DUELS_STATS_STREAK = true;
    public boolean HYPLUS_GBADGE_DUELS_STATS_STREAKBEST = true;

    // To be continued for other modes...

}
