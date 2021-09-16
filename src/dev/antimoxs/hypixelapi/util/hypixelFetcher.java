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
            case "BEDWARS_EIGHT_ONE": return "Solo";
            case "BEDWARS_EIGHT_TWO": return "Doubles";
            case "BEDWARS_FOUR_THREE": return "3v3v3v3";
            case "BEDWARS_FOUR_FOUR": return "4v4v4v4";
            case "BEDWARS_TWO_FOUR": return "4v4";
            case "BEDWARS_EIGHT_TWO_LUCKY": return "LuckyBlocks Doubles";
            case "BEDWARS_FOUR_FOUR_LUCKY": return "LuckyBlocks 4v4v4v4";
            case "BEDWARS_EIGHT_TWO_RUSH": return "Rush Doubles";
            case "BEDWARS_FOUR_FOUR_RUSH": return "Rush 4v4v4v4";
            case "BEDWARS_EIGHT_TWO_ARMED": return "Armed Doubles";
            case "BEDWARS_FOUR_FOUR_ARMED": return "Armed 4v4v4v4";
            case "BEDWARS_EIGHT_TWO_ULTIMATE": return "Ultimate Doubles";
            case "BEDWARS_FOUR_FOUR_ULTIMATE": return "Ultimate 4v4v4v4";
            case "BEDWARS_EIGHT_TWO_VOIDLESS": return "Voidless Doubles";
            case "BEDWARS_FOUR_FOUR_VOIDLESS": return "Voidless 4v4v4v4";
            case "BEDWARS_CASTLE": return "Castle";
            case "BEDWARS_PRACTICE": return "Practice";

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

    public static String fetchGamemodeStatus(String gameType) {

        switch (gameType) {

            case "MAIN_LOBBY":
            case "MAIN": return "§f§lMain";
            case "QUAKECRAFT": return "§9§lQuake";
            case "WALLS": return "§3§lWalls";
            case "PAINTBALL": return "§a§lP§e§la§6§li§c§ln§d§lt§9§lb§3§la§7§ll§f§ll";
            case "SURVIVAL_GAMES": return "§e§lBlitz";
            case "TNTGAMES": return "§c§lT§f§lN§c§lT§f§lGames";
            case "VAMPIREZ": return "§8§lVampire§c§lZ";
            case "WALLS3": return "§2§lMega§d§lWalls";
            case "ARCADE": return "§2§lArcade";
            case "ARENA": return "§6§lArenaBrawl";
            case "UHC": return "§6§lUHC";
            case "COPS_AND_CRIMS":
            case "MCGO": return "§9§lCops§f§lAnd§8§lCrims";
            case "WARLORDS":
            case "BATTLEGROUND": return "§d§lWarlords";
            case "SUPER_SMASH": return "§c§lSmash§e§lHeroes";
            case "GINGERBREAD": return "§6§lTKR";
            case "HOUSING": return "§b§lHousing";
            case "SKYWARS": return "§b§lSky§c§lWars";
            case "CRAZY_WALLS":
            case "TRUE_COMBAT": return "§e§lCr§c§laz§e§lyW§c§lal§e§lls";
            case "SPEED_UHC": return "§9§lSpeed§6§lUHC";
            case "SKYCLASH": return "§a§lSky§c§lClash";
            case "LEGACY": return "§9§lClassic";
            case "PROTOTYPE": return "§6§lPrototype";
            case "BEDWARS": return "§c§lBed§f§lWars";
            case "MURDER_MYSTERY": return "§5§lMurderMystery";
            case "BUILD_BATTLE": return "§9§lBuildBattle";
            case "DUELS": return "§d§lDuels";
            case "SKYBLOCK": return "§a§lSky§b§lBlock";
            case "PIT": return "§7§lThe§4§lPit";
            case "ATLAS": return "§6§lAtlas";
            case "REPLAY": return "§1§lReplay";
            case "TOURNAMENT": return "§c§lTournament";

            default: return gameType;

        }

    }
    public static String fetchModeStatus(String mode) {

        switch (mode.toUpperCase()) {

            // Generic
            case "LOBBY": return "§fLobby";
            case "BASE": return "";
            case "UNDEFINED": return "";

            case "SOLO":
            case "SOLO_NORMAL": return "§fSolo";

            case "TEAMS":
            case "TEAMS_NORMAL": return "§7Teams";

            case "NORMAL": return "§aNormal";
            case "STANDART": return "§aStandart";

            // Arcade
            case "HOLE_IN_THE_WALL": return "§9HoleInTheWall";
            case "SOCCER": return "§fFo§8ot§fba§8ll";
            case "ONEINTHEQUIVER": return "§bBounty Hunters";
            case "DRAW_THEIR_THING": return "§ePixel Painters";
            case "PVP_CTW": return "§6Capture The Wool";
            case "DRAGONWARS2": return "§8Dragon Wars";
            case "ENDER": return "§2Ender Spleef";
            case "STARWARS": return "§dGalaxy Wars";
            case "THROW_OUT": return "§9Throw Out";
            case "DEFENDER": return "§2Creeper Attack";
            case "FARM_HUNT": return "§eFarm Hunt";
            case "PARTY": return "§dParty Games";
            case "ZOMBIES_DEAD_END": return "§2Zombies";
            case "ZOMBIES_BAD_BLOOD": return "§2Zombies";
            case "ZOMBIES_ALIEN_ARCADIUM": return "§2Zombies";
            case "HIDE_AND_SEEK_PARTY_POOPER": return "§7HideAndSeek-PartyPooper";
            case "HIDE_AND_SEEK_PROP_HUNT": return "§7HideAndSeek-PropHunt";
            case "SIMON_SAYS": return "§aHypixel Says";
            case "MINI_WALLS": return "§cMini Walls";
            case "DAYONE": return "§2Blocking §cDead";

            case "EASTER_SIMULATOR": return "§bEaster§aSimulator";

            // SkyBlock
            case "HUB": return "§3Hub";
            case "DYNAMIC": return "§fPrivate Island";
            case "MINING_1": return "§6GoldMine";
            case "MINING_2": return "§7Deep Caverns";
            case "MINING_3": return "§8DwarfenMine";
            case "FORAGING_1": return "§aThe Park";
            case "COMBAT_1": return "§fSpider's §8Den";
            case "COMBAT_2": return "§6Blazing §8Fortress";
            case "COMBAT_3": return "§eThe End";
            case "FARMING_1": return "§9The Barn";
            case "FARMING_2": return "§dMushroom Island";
            case "DUNGEON_HUB": return "§cDungeon §fHub";
            case "DUNGEON": return "§cDungeon";

            // Towerwars
            case "TOWERWARS_SOLO": return "§9TowerWars §fSolo";
            case "TOWERWARS_TEAMS_OF_TWO": return "§9TowerWars §7Teams";

            // TNT Games
            case "TNTRUN": return "§cT§fN§cT§7-Run";
            case "PVPRUN": return "§cPVP§7-Run";
            case "BOWSPLEEF": return "§dBow§fSpleef";
            case "TNTTAG": return "§cT§fN§cT§7-Tag";
            case "CAPTURE": return "§dWizards";

            // BuildBattle
            case "SOLO_PRO": return "§dPro§fMode";
            case "GUESS_THE_BUILD": return "§3GuessTheBuild";

            // SkyWars
            case "SOLO_INSANE": return "§cInsane §fSolo";
            case "TEAMS_INSANE": return "§cInsane §7Teams";
            case "MEGA_NORMAL": return "§bMega";
            case "RANKED_NORMAL": return "§dRanked";
            case "SOLO_INSANE_SLIME": return "§aSlime §fSolo";
            case "TEAMS_INSANE_SLIME": return "§aSlime §7Teams";
            case "SOLO_INSANE_LUCKY": return "§eLuckyBlocks §fSolo";
            case "TEAMS_INSANE_LUCKY": return "§eLuckyBlocks §7Teams";
            case "SOLO_INSANE_TNT_MADNESS": return "§cT§fN§cT§8Madness §fSolo";
            case "TEAMS_INSANE_TNT_MADNESS": return "§cT§fN§cT§8Madness §7Teams";
            case "SOLO_INSANE_RUSH": return "§9Rush §fSolo";
            case "TEAMS_INSANE_RUSH": return "§9Rush §7Teams";

            // BedWars
            case "BEDWARS_EIGHT_ONE": return "§fSolo";
            case "BEDWARS_EIGHT_TWO": return "§7Doubles";
            case "BEDWARS_FOUR_THREE": return "§a3v3v3v3";
            case "BEDWARS_FOUR_FOUR": return "§b4v4v4v4";
            case "BEDWARS_TWO_FOUR": return "§c4v4";
            case "BEDWARS_EIGHT_TWO_LUCKY": return "§eLuckyBlocks §7Doubles";
            case "BEDWARS_FOUR_FOUR_LUCKY": return "§eLuckyBlocks §b4v4v4v4";
            case "BEDWARS_EIGHT_TWO_RUSH": return "§9Rush §7Doubles";
            case "BEDWARS_FOUR_FOUR_RUSH": return "§9Rush §b4v4v4v4";
            case "BEDWARS_EIGHT_TWO_ARMED": return "§8Armed §7Doubles";
            case "BEDWARS_FOUR_FOUR_ARMED": return "§8Armed §b4v4v4v4";
            case "BEDWARS_EIGHT_TWO_ULTIMATE": return "§6Ultimate §7Doubles";
            case "BEDWARS_FOUR_FOUR_ULTIMATE": return "§6Ultimate §b4v4v4v4";
            case "BEDWARS_EIGHT_TWO_VOIDLESS": return "§fVoidless §7Doubles";
            case "BEDWARS_FOUR_FOUR_VOIDLESS": return "§fVoidless §b4v4v4v4";
            case "BEDWARS_CASTLE": return "§8Castle";
            case "BEDWARS_PRACTICE": return "§5Practice";

            // Duels
            case "BOW_DUEL": return "§fBow 1v1";
            case "CLASSIC_DUEL": return "§fClassic 1v1";
            case "OP_DUEL": return "§fOP 1v1";
            case "UHC_DUEL": return "§fUHC 1v1";
            case "POTION_DUEL": return "§fNoDebuff 1v1";
            case "MW_DUEL": return "§fMegaWalls 1v1";
            case "BLITZ_DUEL": return "§fBlitz 1v1";
            case "SW_DUEL": return "§fSkyWars 1v1";
            case "COMBO_DUEL": return "§fCombo 1v1";
            case "BOWSPLEEF_DUEL": return "§fBowSpleef 1v1";
            case "DSUMO_DUEL": return "§fSumo 1v1";
            case "BRIDGE_DUEL": return "§fBridge 1v1";
            case "UHC_MEETUP": return "§fUHC Deathmatch";
            case "UHC_DOUBLES": return "§fUHC 2v2";
            case "UHC_TEAMS": return "§fUHC 4v4";
            case "SW_DOUBLES": return "§fSkyWars 2v2";
            case "MW_DOUBLES": return "§fMegaWalls 2v2";
            case "OP_DOUBLES": return "§fOP 2v2";
            case "BRIDGE_DOUBLES": return "§fBridge 2v2";
            case "BRIDGE_FOUR": return "§fBridge 4v4";
            case "BRIDGE_2V2V2V2": return "§fBridge 2v2v2v2";
            case "BRIDGE_3V3V3V3": return "§fBridge 3v3v3v3";

            // Classic
            case "1V1": return "§f1v1";
            case "2V2": return "§72v2";
            case "4V4": return "§b4v4";

            // MCGO (CopsAndCrims)
            case "NORMAL_PARTY": return "§dParty §aNormal";
            case "DEATHMATCH_PARTY": return "§dParty §cDeathmatch";
            case "DEATHMATCH": return "§cDeathmatch";

            // MegaWalls
            case "FACE_OFF": return "§cFace§fOff";
            case "GVG": return "§2GvG";
            case "CTF_MINI": return "§6CaptureTheFlag";
            case "DOMINATION": return "§9Domination";
            case "TEAM_DEATHMATCH": return "§cDeathmatch";

            // MurderMystery
            case "MURDER_CLASSIC": return "§9Classic";
            case "MURDER_DOUBLE_UP": return "§dDouble Up";
            case "MURDER_ASSASSINS": return "§cAssassins";
            case "MURDER_INFECTION": return "§2Infection";

            // The Pit
            case "PIT": return "";

            default: return mode;

        }

    }

}
