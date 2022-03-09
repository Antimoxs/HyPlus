package dev.antimoxs.hyplus.internal;

import dev.antimoxs.hyplus.objects.HySimplePlayer;

import java.util.HashMap;
import java.util.UUID;

public class HyPlayerStorage {

    private static HashMap<UUID, HySimplePlayer> players = new HashMap<>();


    public static HySimplePlayer getPlayerByUUID(UUID uuid) {

        if (players.containsKey(uuid)) { return players.get(uuid); }
        return null;

    }

    public static boolean playerInStorage(UUID uuid) {

        return players.containsKey(uuid);

    }

    public static HySimplePlayer addPlayer(UUID uuid, HySimplePlayer player) {

        players.put(uuid, player);
        return player;

    }

}
