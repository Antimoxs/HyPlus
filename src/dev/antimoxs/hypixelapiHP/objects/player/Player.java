package dev.antimoxs.hypixelapiHP.objects.player;



import com.google.gson.annotations.SerializedName;
import dev.antimoxs.hypixelapiHP.objects.player.quests.PlayerQuest;

import java.util.HashMap;

public class Player {

    @SerializedName("_id")
    public String id = "";
    public String uuid = "";
    public long firstLogin = 0L;
    public String playername = "";
    public long lastLogin = 0L;
    public String displayname = playername;
    public String[] knowAliases = new String[0];
    public String[] knowAliasesLower = new String[0];
    public String[] achievementsOneTime = new String[0];
    public PlayerCompassStat compassStats = new PlayerCompassStat();
    public PlayerStats stats = new PlayerStats();
    public int karma = 0;
    public int networkExp = 0;
    public HashMap<String, Integer> achievements = new HashMap<>();
    public String userLanguage = "";
    public dev.antimoxs.hypixelapiHP.objects.player.PlayerHousingMeta housingMeta = new PlayerHousingMeta();
    public String[] friendRequestsUuid = new String[0];
    public HashMap<PlayerPetConsumables, Integer> petConsumables = new HashMap<>();
    public dev.antimoxs.hypixelapiHP.objects.player.PlayerVanityPackage vanityMeta = new PlayerVanityPackage();
    public PlayerVoting voting = new PlayerVoting();
    public PlayerEugene eugene = new PlayerEugene();
    public long lastAdsenseGenerateTime = 0L;
    public long lastClaimedReward = 0L;
    public int totalRewards = 0;
    public int totalDailyRewards = 0;
    public int rewardStreak = 0;
    public int rewardScore = 0;
    public int rewardHighScore = 0;
    public int adsense_tokens = 0;
    public long lastLogout = 0L;
    public PlayerParkourCompletions parkourCompletions = new PlayerParkourCompletions();
    //private PlayerQuests quests = new PlayerQuests(); // Add quests
    public HashMap<String, PlayerQuest> quests = new HashMap<>();
    private dev.antimoxs.hypixelapiHP.objects.player.PlayerPetStats petStats = new PlayerPetStats(); // Add pet stats
    public String mcVersionRp = "";
    public String network_update_book = "";
    public HashMap<String, Long> achievementRewardsNew = new HashMap<>();
    public String[] achievementTracking = new String[0];
    @SerializedName("channel")
    public String chat_channel = "";
    public String vanityFavorites = "";
    public PlayerGiftingMeta giftingMeta = new PlayerGiftingMeta();
    private PlayerChallenges challenges = new PlayerChallenges(); // Add challenges
    public boolean spec_first_person = false;
    public HashMap<String, Integer> achievementSync = new HashMap<>();
    public String newPackageRank = "";
    public String rank = "";
    public long levelUp_VIP = 0L;
    public long levelUp_VIP_PLUS = 0L;
    public long levelUp_MVP = 0L;
    public long levelUp_MVP_PLUS = 0L;
    public HashMap<String, Boolean> questSettings = new HashMap<>();
    public long petJourneyTimestamp = 0L;
    public String rankPlusColor = "";
    public boolean guildNotifications = false;
    private dev.antimoxs.hypixelapiHP.objects.player.PlayerParkourCheckpoints parkourCheckpointBests = new PlayerParkourCheckpoints(); // Add checkpoints
    public PlayerSocialMedia socialMedia = new PlayerSocialMedia();
    public int achievementPoints = 0;
    public int fortuneBuff = 0;
    private PlayerTourney tourney = new PlayerTourney(); // Add tourney
    public PlayerAchievementTotem achievementTotem = new PlayerAchievementTotem();
    public String currentClickEffect = "";
    public int gifts_grinch = 0;
    public String currentGadget = "";
    public String monthlyPackageRank = "";
    public String mostRecentMonthlyPackageRank = "";
    public String collectibles_menu_sort = "";
    public String monthlyRankColor = "";
    public boolean main2017Tutorial = false;
    public String mostRecentGameType = "";

    // seasonal stuff

    // leveling rewards?



}
