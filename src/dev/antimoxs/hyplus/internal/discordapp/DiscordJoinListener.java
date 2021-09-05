package dev.antimoxs.hyplus.internal.discordapp;

import net.labymod.discordapp.api.DiscordEventHandlers;

import java.util.UUID;

public class DiscordJoinListener implements DiscordEventHandlers.joinGame_callback {
    private DiscordAppExtender discordApp;

    public DiscordJoinListener(DiscordAppExtender discordApp) {
        this.discordApp = discordApp;
    }

    public void apply(String secret) {

        System.out.println("[HyDISCORD] TRYING TO JOIN: " + secret);
        discordApp.redeemJoinKey(UUID.fromString(secret), "mc.hypixel.net");

    }
}
