package dev.antimoxs.hyplus.internal;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.response.PlayerResponse;
import dev.antimoxs.hypixelapiHP.objects.player.Player;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.api.player.HySimplePlayer;

import java.util.HashMap;
import java.util.UUID;

public class HyPlayerStorage {

    private static final HashMap<UUID, HySimplePlayer> players = new HashMap<>();
    private static final HashMap<UUID, Player> playerObjects = new HashMap<>();


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

    public static Player getPlayerObject(UUID uuid) {

        if (playerObjects.containsKey(uuid)) return playerObjects.get(uuid);
        return requestPlayer(uuid);

    }

    public static Player requestPlayer(UUID uuid) {

        try {

            HyPlus.debugLog("Requesting Player: " + uuid);
            PlayerResponse p = HyPlus.getInstance().hypixelApi.createPlayerRequestUUID(uuid.toString());

            if (p.success) {

                playerObjects.remove(uuid);
                playerObjects.put(uuid, p.getPlayer());
                return p.getPlayer();

            }
            else {

                HyPlus.debugLog("Failed player OBJ lookup: " + p.cause);
                return null;

            }

        } catch (ApiRequestException e) {
            e.printStackTrace();
        }
        return null;

    }

}
