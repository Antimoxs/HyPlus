package dev.antimoxs.hyplus;

import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.modules.playerTagCycle.HyPlayerTag;

import java.util.UUID;

public class HyPlusConfig {

    public static String name = "HyPlus";
    public static String version = "v0.1.0";

    public static HyPlayerTag antimoxs() {

        return new HyPlayerTag(UUID.fromString("df66f7ab-d5eb-4d25-8d95-0d65bec4caee"), 1.6d,
                new kvp("§6§lHyPlus Creator", -1),
                new kvp("§ddiscord.gg/ATdbUS4",-2));

    }

}
