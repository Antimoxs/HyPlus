package dev.antimoxs.hypixelapiHP.util;

public class BedWarsCalculator {

    static int EASY_LEVELS = 4;
    static int EASY_LEVELS_XP = 7000;
    static int XP_PER_PRESTIGE = 96 * 5000 + EASY_LEVELS_XP;

    static int LEVELS_PER_PRESTIGE = 100;

    public static String getPrestigeForExp(int exp) {
        return getPrestigeForLevel(getLevelForExp(exp));
    }

    /**
     * @param level
     * @return BedWarsPrestige|null
     */
    public static String getPrestigeForLevel(int level) {
        int prestige = (int)Math.floor(level / LEVELS_PER_PRESTIGE);

        switch (prestige) {
            case 0:
                return "NONE";
            case 1:
                return "IRON";
            case 2:
                return "GOLD";
            case 3:
                return "DIAMOND";
            case 4:
                return "EMERALD";
            case 5:
                return "SAPPHIRE";
            case 6:
                return "RUBY";
            case 7:
                return "CRYSTAL";
            case 8:
                return "OPAL";
            case 9:
                return "AMETHYST";
            case 10:
            default:
                return "RAINBOW";
        }
    }

    /**
     * Calculate level for given bedwars experience
     *
     * @param exp
     * @return float
     */
    public static int getLevelForExp(int exp) {
        int prestiges = (int) Math.floor(exp / XP_PER_PRESTIGE);

        int level = prestiges * LEVELS_PER_PRESTIGE;

        int expWithoutPrestiges = exp - (prestiges * XP_PER_PRESTIGE);
        for (int i = 1; i <= EASY_LEVELS; ++i) {
            int expForEasyLevel = getExpForLevel(i);
            if (expWithoutPrestiges < expForEasyLevel) {
                break;
            }
            level++;
            expWithoutPrestiges -= expForEasyLevel;
        }
        level += Math.floor(expWithoutPrestiges / 5000);

        return level;
    }

    public static int getExpForLevel(int level) {
        if (level == 0) return 0;

        int respectedLevel = getLevelRespectingPrestige(level);
        if (respectedLevel > EASY_LEVELS) {
            return 5000;
        }

        switch (respectedLevel) {
            case 1:
                return 500;
            case 2:
                return 1000;
            case 3:
                return 2000;
            case 4:
                return 3500;
        }
        return 5000;
    }

    /**
     * Returns "2" instead of "102" if prestiges happen every 100 levels e.g.
     * @param level
     * @return float|int
     */
    public static int getLevelRespectingPrestige(int level) {
        if (level > 10 * LEVELS_PER_PRESTIGE) {
            return level - 10 * LEVELS_PER_PRESTIGE;
        } else {
            return level % LEVELS_PER_PRESTIGE;
        }
    }

}
