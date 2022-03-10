package dev.antimoxs.hyplus.internal.discordapp;

import net.labymod.api.events.MessageSendEvent;
import net.labymod.core.LabyModCore;
import net.labymod.discordapp.api.DiscordEventHandlers;
import net.labymod.discordapp.api.DiscordJoinRequest;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.support.util.Debug;
import net.labymod.utils.ModColor;

public class DiscordRequestListener implements DiscordEventHandlers.joinRequest_callback, MessageSendEvent {
    private DiscordAppExtender discordApp;

    public DiscordRequestListener(DiscordAppExtender discordApp) {
        this.discordApp = discordApp;
        LabyMod.getInstance().getEventManager().register(this);
    }

    public boolean onSend(String message) {
        if (message.toLowerCase().startsWith("/hydiscordrpc ")) {
            String[] args = message.split(" ");
            if (args.length >= 3) {
                boolean accepted = args[1].equalsIgnoreCase("accept");
                this.discordApp.respond(args[2], accepted ? 1 : 0);
                LabyMod.getInstance().displayMessageInChat(LanguageManager.translate("discordrpc_join_request_accepted"));
            }

            return true;
        } else {
            return false;
        }
    }

    public void apply(DiscordJoinRequest request) {
        Debug.log(Debug.EnumDebugMode.DISCORD, "Join request: " + request.username + "#" + request.userId);
        LabyModCore.getMinecraft().displayMessageInChatCustomAction(LanguageManager.translate("discordrpc_join_request_chat", request.username), 2, "/hydiscordrpc accept " + request.userId);
        String imageURL = String.format("https://cdn.discordapp.com/avatars/%s/%s.png", request.userId, request.avatar);
        String username = ModColor.cl('3') + request.username + ModColor.cl('8') + "#" + ModColor.cl('7') + request.discriminator;
        String message = ModColor.cl('a') + LanguageManager.translate("discordrpc_join_request_achievement");
        if (LabyMod.getSettings().discordShowAchievements) {
            LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(imageURL, username, message);
        }

    }
}
