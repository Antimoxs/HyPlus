package dev.antimoxs.hyplus.internal;

import dev.antimoxs.hypixelapi.objects.player.Player;

import java.util.HashMap;
import java.util.UUID;

public class HyPlayerStorage {

    private HashMap<UUID, Player> players = new HashMap<>();


    public Player getPlayerByUUID(UUID uuid) {

        if (players.containsKey(uuid)) { return players.get(uuid); }
        return null;

    }

    public boolean playerInStorage(UUID uuid) {

        return players.containsKey(uuid);

    }

    public Player addPlayer(UUID uuid, Player player) {

        players.put(uuid, player);
        return player;

    }

}
