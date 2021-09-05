package dev.antimoxs.hypixelapi.util;

public class hypixelFetcher {

    // Status fetches

    public static String fetchGame(String gameType) {

        switch (gameType) {

            case "MAIN_LOBBY":
            case "MAIN": return "Main";
            case "QUAKECRAFT": return "Quake";
            case "WALLS": return "Walls";
            case "PAINTBALL": return "Paintball";
            case "SURVIVAL_GAMES": return "Blitz";
            case "TNTGAMES": return "TNTGames";
            case "VAMPIREZ": return "VampireZ";
            case "WALLS3": return "MegaWalls";
            case "ARCADE": return "Arcade";
            case "ARENA": return "ArenaBrawl";
            case "UHC": return "UHC";
            case "COPS_AND_CRIMS":
            case "MCGO": return "CopsAndCrims";
            case "WARLORDS":
            case "BATTLEGROUND": return "Warlords";
            case "SUPER_SMASH": return "SmashHeroes";
            case "GINGERBREAD": return "TKR";
            case "HOUSING": return "Housing";
            case "SKYWARS": return "SkyWars";
            case "CRAZY_WALLS":
            case "TRUE_COMBAT": return "CrazyWalls";
            case "SPEED_UHC": return "SpeedUHC";
            case "SKYCLASH": return "SkyClash";
            case "LEGACY": return "Classic";
            case "PROTOTYPE": return "Prototype";
            case "BEDWARS": return "BedWars";
            case "MURDER_MYSTERY": return "MurderMystery";
            case "BUILD_BATTLE": return "BuildBattle";
            case "DUELS": return "Duels";
            case "SKYBLOCK": return "SkyBlock";
            case "PIT": return "ThePit";
            case "ATLAS": return "Atlas";
            case "REPLAY": return "Replay";
            case "TOURNAMENT": return "Tournament";

            default: return gameType;

        }

    }
    public static String fetchAll(String gameType, String mode, String map) {

        switch (gameType) {

            case "MAIN": { return "Main " + fetchMode(mode); }
            case "QUAKECRAFT": { return "Quake"; }
            case "WALLS": { return "Walls"; }
            case "PAINTBALL": { return "Paintball"; }
            case "SURVIVAL_GAMES": { return "Blitz"; }
            case "TNTGAMES": { return "TNTGames"; }
            case "VAMPIREZ": { return "VampireZ"; }
            case "WALLS3": { return "MegaWalls"; }
            case "ARCADE": { return "Arcade " + fetchArcade(mode); }
            case "ARENA": { return "ArenaBrawl"; }
            case "UHC": { return "UHC"; }
            case "MCGO": { return "CopsAndCrims"; }
            case "BATTLEGROUND": { return "Warlords"; }
            case "SUPER_SMASH": { return "Smash Heroes"; }
            case "GINGERBREAD": { return "TKR"; }
            case "HOUSING": { return "Housing"; }
            case "SKYWARS": { return "SkyWars"; }
            case "TRUE_COMBAT": { return "CrazyWalls"; }
            case "SPEED_UHC": { return "Speed UHC"; }
            case "SKYCLASH": { return "SkyClash"; }
            case "LEGACY": { return "Classic"; }
            case "PROTOTYPE": { return "Prototype"; }
            case "BEDWARS": { return "BedWars"; }
            case "MURDER_MYSTERY": { return "MurderMystery"; }
            case "BUILD_BATTLE": { return "BuildBattle"; }
            case "DUELS": { return "Duels " + fetchDuels(mode) + map; }
            case "SKYBLOCK": { return "SkyBlock " + fetchSkyBlock(mode); }
            case "PIT": { return "The Pit"; }
            case "REPLAY": { return "Replay"; }

            default: return gameType;

        }

    }

    public static String fetchMode(String mode) {

        switch (mode.toUpperCase()) {

            // Generic
            case "LOBBY": return "Lobby";
            case "BASE": return "";
            case "UNDEFINED": return "";

            case "SOLO":
            case "SOLO_NORMAL": return "Solo";

            case "TEAMS":
            case "TEAMS_NORMAL": return "Teams";

            case "NORMAL": return "Normal";
            case "STANDART": return "Standart";

            // Arcade
            case "HOLE_IN_THE_WALL": return "HoleInTheWall";
            case "SOCCER": return "Football";
            case "ONEINTHEQUIVER": return "Bounty Hunters";
            case "DRAW_THEIR_THING": return "Pixel Painters";
            case "PVP_CTW": return "Capture The Wool";
            case "DRAGONWARS2": return "Dragon Wars";
            case "ENDER": return "Ender Spleef";
            case "STARWARS": return "Galaxy Wars";
            case "THROW_OUT": return "Throw Out";
            case "DEFENDER": return "Creeper Attack";
            case "FARM_HUNT": return "Farm Hunt";
            case "PARTY": return "Party Games";
            case "ZOMBIES_DEAD_END": return "Zombies";
            case "ZOMBIES_BAD_BLOOD": return "Zombies";
            case "ZOMBIES_ALIEN_ARCADIUM": return "Zombies";
            case "HIDE_AND_SEEK_PARTY_POOPER": return "HideAndSeek-PartyPooper";
            case "HIDE_AND_SEEK_PROP_HUNT": return "HideAndSeek-PropHunt";
            case "SIMON_SAYS": return "Hypixel Says";
            case "MINI_WALLS": return "Mini Walls";
            case "DAYONE": return "Blocking Dead";

            case "EASTER_SIMULATOR": return "EasterSimulator";

            // SkyBlock
            case "HUB": return "Hub";
            case "DYNAMIC": return "Private Island";
            case "MINING_1": return "GoldMine";
            case "MINING_2": return "Deep Caverns";
            case "MINING_3": return "DwarfenMine";
            case "FORAGING_1": return "The Park";
            case "COMBAT_1": return "Spider's Den";
            case "COMBAT_2": return "Blazing Fortress";
            case "COMBAT_3": return "The End";
            case "FARMING_1": return "The Barn";
            case "FARMING_2": return "Mushroom Island";
            case "DUNGEON_HUB": return "Dungeon Hub";
            case "DUNGEON": return "Dungeon";

            // Towerwars
            case "TOWERWARS_SOLO": return "TowerWars Solo";
            case "TOWERWARS_TEAMS_OF_TWO": return "TowerWars Teams";

            // TNT Games
            case "TNTRUN": return "TNT-Run";
            case "PVPRUN": return "PVP-Run";
            case "BOWSPLEEF": return "BowSpleef";
            case "TNTTAG": return "TNT-Tag";
            case "CAPTURE": return "Wizards";

            // BuildBattle
            case "SOLO_PRO": return "ProMode";
            case "GUESS_THE_BUILD": return "GuessTheBuild";

            // SkyWars
            case "SOLO_INSANE": return "Insane Solo";
            case "TEAMS_INSANE": return "Insane Teams";
            case "MEGA_NORMAL": return "Mega";
            case "RANKED_NORMAL": return "Ranked";
            case "SOLO_INSANE_SLIME": return "Slime Solo";
            case "TEAMS_INSANE_SLIME": return "Slime Teams";
            case "SOLO_INSANE_LUCKY": return "LuckyBlocks Solo";
            case "TEAMS_INSANE_LUCKY": return "LuckyBlocks Teams";
            case "SOLO_INSANE_TNT_MADNESS": return "TNTMadness Solo";
            case "TEAMS_INSANE_TNT_MADNESS": return "TNTMadness Teams";
            case "SOLO_INSANE_RUSH": return "Rush Solo";
            case "TEAMS_INSANE_RUSH": return "Rush Teams";

            // BedWars
            case "EIGHT_ONE": return "Solo";
            case "EIGHT_TWO": return "Doubles";
            case "FOUR_THREE": return "3v3v3v3";
            case "FOUR_FOUR": return "4v4v4v4";
            case "TWO_FOUR": return "4v4";
            case "EIGHT_TWO_LUCKY": return "LuckyBlocks Doubles";
            case "FOUR_FOUR_LUCKY": return "LuckyBlocks 4v4v4v4";
            case "EIGHT_TWO_RUSH": return "Rush Doubles";
            case "FOUR_FOUR_RUSH": return "Rush 4v4v4v4";
            case "EIGHT_TWO_ARMED": return "Armed Doubles";
            case "FOUR_FOUR_ARMED": return "Armed 4v4v4v4";
            case "EIGHT_TWO_ULTIMATE": return "Ultimate Doubles";
            case "FOUR_FOUR_ULTIMATE": return "Ultimate 4v4v4v4";
            case "EIGHT_TWO_VOIDLESS": return "Voidless Doubles";
            case "FOUR_FOUR_VOIDLESS": return "Voidless 4v4v4v4";
            case "CASTLE": return "Castle";
            case "PRACTICE": return "Practice";

            // Duels
            case "BOW_DUEL": return "Bow 1v1";
            case "CLASSIC_DUEL": return "Classic 1v1";
            case "OP_DUEL": return "OP 1v1";
            case "UHC_DUEL": return "UHC 1v1";
            case "POTION_DUEL": return "NoDebuff 1v1";
            case "MW_DUEL": return "MegaWalls 1v1";
            case "BLITZ_DUEL": return "Blitz 1v1";
            case "SW_DUEL": return "SkyWars 1v1";
            case "COMBO_DUEL": return "Combo 1v1";
            case "BOWSPLEEF_DUEL": return "BowSpleef 1v1";
            case "DSUMO_DUEL": return "Sumo 1v1";
            case "BRIDGE_DUEL": return "Bridge 1v1";
            case "UHC_MEETUP": return "UHC Deathmatch";
            case "UHC_DOUBLES": return "UHC 2v2";
            case "UHC_TEAMS": return "UHC 4v4";
            case "SW_DOUBLES": return "SkyWars 2v2";
            case "MW_DOUBLES": return "MegaWalls 2v2";
            case "OP_DOUBLES": return "OP 2v2";
            case "BRIDGE_DOUBLES": return "Bridge 2v2";
            case "BRIDGE_FOUR": return "Bridge 4v4";
            case "BRIDGE_2V2V2V2": return "Bridge 2v2v2v2";
            case "BRIDGE_3V3V3V3": return "Bridge 3v3v3v3";

            // Classic
            case "1V1": return "1v1";
            case "2V2": return "2v2";
            case "4V4": return "4v4";

            // MCGO (CopsAndCrims)
            case "NORMAL_PARTY": return "Party Normal";
            case "DEATHMATCH_PARTY": return "Party Deathmatch";
            case "DEATHMATCH": return "Deathmatch";

            // MegaWalls
            case "FACE_OFF": return "FaceOff";
            case "GVG": return "GvG";
            case "CTF_MINI": return "CaptureTheFlag";
            case "DOMINATION": return "Domination";
            case "TEAM_DEATHMATCH": return "Deathmatch";

            // MurderMystery
            case "MURDER_CLASSIC": return "Classic";
            case "MURDER_DOUBLE_UP": return "Double Up";
            case "MURDER_ASSASSINS": return "Assassins";
            case "MURDER_INFECTION": return "Infection";

            // The Pit
            case "PIT": return "";

            default: return mode;

        }

    }

    private static String fetchSkyBlock(String mode) {

        switch (mode) {

            case "HUB": return "Hub";
            case "DYNAMIC": return "Private Island";
            case "MINING_1": return "GoldMine";
            case "MINING_2": return "Deep Caverns";
            case "MINING_3": return "DwarfenMine";
            case "FORAGING_1": return "The Park";
            case "COMBAT_1": return "Spider's Den";
            case "COMBAT_2": return "Blazing Fortress";
            case "COMBAT_3": return "The End";
            case "FARMING_1": return "The Barn";
            case "FARMING_2": return "Mushroom Island";
            case "DUNGEON_HUB": return "Dungeon Hub";
            case "DUNGEON": return "Dungeon";
            default: return mode;

        }

    }
    private static String fetchDuels(String mode) {

        switch (mode) {

            case "BOW_DUEL": return "Bow 1v1";
            case "CLASSIC_DUEL": return "Classic 1v1";
            case "OP_DUEL": return "OP 1v1";
            case "UHC_DUEL": return "UHC 1v1";
            case "POTION_DUEL": return "NoDebuff 1v1";
            case "MW_DUEL": return "MegaWalls 1v1";
            case "BLITZ_DUEL": return "Blitz 1v1";
            case "SW_DUEL": return "SkyWars 1v1";
            case "COMBO_DUEL": return "Combo 1v1";
            case "BOWSPLEEF_DUEL": return "BowSpleef 1v1";
            case "DSUMO_DUEL": return "Sumo 1v1";
            case "BRIDGE_DUEL": return "Bridge 1v1";
            case "UHC_MEETUP": return "UHC Deathmatch";
            case "UHC_DOUBLES": return "UHC 2v2";
            case "UHC_TEAMS": return "UHC 4v4";
            case "SW_DOUBLES": return "SkyWars 2v2";
            case "MW_DOUBLES": return "MegaWalls 2v2";
            case "OP_DOUBLES": return "OP 2v2";
            case "BRIDGE_DOUBLES": return "Bridge 2v2";
            case "BRIDGE_FOUR": return "Bridge 4v4";
            case "BRIDGE_2V2V2V2": return "Bridge 2v2v2v2";
            case "BRIDGE_3V3V3V3": return "Bridge 3v3v3v3";
            default: return mode;

        }

    }
    private static String fetchArcade(String mode) {

        switch (mode) {

            case "HOLE_IN_THE_WALL": return "HoleInTheWall";
            case "SOCCER": return "Soccer";
            case "ONEINTHEQUIVER": return "BountyHunters";
            case "DRAW_THEIR_THING": return "PixelPainters";
            case "PVP_CTW": return "CaptureTheWool";
            case "DRAGONWARS2": return "DragonWars";
            case "ENDER": return "EnderSpleef";
            case "STARWARS": return "GalaxyWars";
            case "THROW_OUT": return "ThrowOut";
            case "DEFENDER": return "CreeperAttack";
            case "FARM_HUNT": return "FarmHunt";
            case "PARTY": return "PartyGames";
            case "ZOMBIES_DEAD_END": return "Zombies";
            case "ZOMBIES_BAD_BLOOD": return "Zombies";
            case "ZOMBIES_ALIEN_ARCADIUM": return "Zombies";
            case "HIDE_AND_SEEK_PARTY_POOPER": return "HideAndSeek-PartyPooper";
            case "HIDE_AND_SEEK_PROP_HUNT": return "HideAndSeek-PropHunt";
            case "SIMON_SAYS": return "HypixelSays";
            case "MINI_WALLS": return "MiniWalls";
            case "DAYONE": return "TheBlockingDead";
            default: return mode;

        }

    }

    // challenge fetches

    public static kvp fetchhObjective(String key, String add) {

        switch ((add + key).toLowerCase()) {

            case "quake_daily_play": return new kvp("Play 3 games", 3);
            case "quake_daily_kill": return new kvp("Kill 50 players", 50);
            case "quake_daily_win": return new kvp("Win a game", 1);
            case "quake_weekly_play": return new kvp("Play 20 games", 20);
            case "quake_weekly_streak": return new kvp("Have a killstreak of 10", 20);

            case "walls_daily_play": return new kvp("Play a game", 1);
            case "walls_daily_kill": return new kvp("Kill 5 players", 5);
            case "walls_daily_win": return new kvp("Win a game", 1);
            case "walls_weekly_play": return new kvp("Play 7 games", 7);
            case "walls_weekly_kills": return new kvp("Kill 25 player", 25);

            case "win": return new kvp("Win a game", 1);
            case "kill": return new kvp("Kill 100 player", 100);
            case "weeklykill": return new kvp("Kill 750 players", 750);
            case "weeklyplay": return new kvp("Play 30 games", 30);

            case "blitz_games_played": return new kvp("Play a game", 1);
            case "winblitz": return new kvp("Win a game", 1);
            case "lootchestblitz": return new kvp("Loot 25 Chests", 25);
            case "killblitz10": return new kvp("Kill 5 players", 5);
            case "weeklyblitz_games_played": return new kvp("Play 15 games", 15);
            case "weeklywinblitz": return new kvp("Win 5 games", 5);
            case "weeklykillblitz10": return new kvp("Kill 30 players", 30);
            case "weeklylootchestblitz": return new kvp("Loot 100 chests", 100);
            case "weeklydealtdamageblitz": return new kvp("Deal 250 damage", 250);

            case "tnt_daily_win": return new kvp("Win a game", 1);
            case "tnt_weekly_play": return new kvp("Play 20 games", 20);
            case "tnt_tntrun_daily": return new kvp("Walk over 500 blocks", 500);
            case "tnt_tntrun_weekly": return new kvp("Walk over 2000 blocks", 2000);
            case "tnt_pvprun_daily": return new kvp("Kill 3 players", 3);
            case "tnt_pvprun_weekly": return new kvp("Kill 25 players", 25);
            case "tnt_bowspleef_daily": return new kvp("Survive 40 players", 40);
            case "tnt_bowspleef_weekly": return new kvp("Survive 200 players", 200);
            case "tnt_tnttag_daily": return new kvp("Survive 7 rounds", 7);
            case "tnt_tnttag_weekly": return new kvp("Survive 50 rounds", 50);
            case "tnt_wizards_daily": return new kvp("", 4);
            case "": return new kvp("", 4);
            default: return new kvp("", 0);

        }

    }

}
