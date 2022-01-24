package dev.antimoxs.hypixelapi.response;

import dev.antimoxs.hypixelapi.objects.player.Player;

public class PlayerResponse extends BaseResponse {

    private Player player = null;

    public Player getPlayer() {

        return player == null ? new Player() : player;

    }

}
