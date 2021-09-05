package dev.antimoxs.hypixelapi.objects.player;

import java.util.HashMap;

public class PlayerAchievementTotem {

    public boolean canCustomize = false;
    public int allowed_max_height = 0;
    public String[] unlockedParts = new String[0];
    public HashMap<String, String> selectedParts = new HashMap<>();
    public String[] unlockedColors = new String[0];
    public HashMap<String, String> selectedColors = new HashMap<>();

}
