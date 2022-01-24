package dev.antimoxs.hyplus.internal;

import dev.antimoxs.hypixelapi.objects.player.Player;
import dev.antimoxs.hyplus.objects.HySimplePlayer;

import java.util.HashMap;
import java.util.UUID;

public class HyPlayerStorage {

    private HashMap<UUID, HySimplePlayer> players = new HashMap<>();


    public HySimplePlayer getPlayerByUUID(UUID uuid) {

        if (players.containsKey(uuid)) { return players.get(uuid); }
        return null;

    }

    public boolean playerInStorage(UUID uuid) {

        return players.containsKey(uuid);

    }

    public HySimplePlayer addPlayer(UUID uuid, HySimplePlayer player) {

        players.put(uuid, player);
        return player;

    }

}
