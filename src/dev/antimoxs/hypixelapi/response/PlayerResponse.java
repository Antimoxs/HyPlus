package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.player.Player;

public class PlayerResponse {

    public boolean success = false;
    private Player player = null;
    public String cause = "";

    public Player getPlayer() {

        return player == null ? new Player() : player;

    }

}
