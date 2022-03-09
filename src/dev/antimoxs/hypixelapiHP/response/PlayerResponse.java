package dev.antimoxs.hypixelapiHP.response;

import dev.antimoxs.hypixelapiHP.objects.player.Player;

public class PlayerResponse extends BaseResponse {

    private dev.antimoxs.hypixelapiHP.objects.player.Player player = null;

    public dev.antimoxs.hypixelapiHP.objects.player.Player getPlayer() {

        return player == null ? new Player() : player;

    }

}
