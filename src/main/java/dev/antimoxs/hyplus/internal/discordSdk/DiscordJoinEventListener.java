package dev.antimoxs.hyplus.internal.discordSdk;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.User;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.main.lang.LanguageManager;
import net.labymod.support.util.Debug;
import net.labymod.utils.ModColor;

import java.util.UUID;

public class DiscordJoinEventListener implements MessageSendEvent, IPCListener {

    private final DiscordManager discordManager;

    public DiscordJoinEventListener(DiscordManager discordManager) {

        this.discordManager = discordManager;

    }

    @Override
    public void onActivityJoin(IPCClient client, String secret) {

        discordManager.redeemJoinKey(UUID.fromString(secret), "mc.hypixel.net");

    }

    @Override
    public void onActivityJoinRequest(IPCClient client, String secret, User user) {

        Debug.log(Debug.EnumDebugMode.DISCORD, "Join request: " + user.getName() + "#" + user.getDiscriminator());
        LabyModCore.getMinecraft().displayMessageInChatCustomAction(LanguageManager.translate("discordrpc_join_request_chat", user.getName()), 2, "/hydiscordrpc accept " + user.getId());
        String imageURL = user.getAvatarUrl();
        String username = ModColor.cl('3') + user.getName() + ModColor.cl('8') + "#" + ModColor.cl('7') + user.getDiscriminator();
        String message = ModColor.cl('a') + LanguageManager.translate("discordrpc_join_request_achievement");
        if (LabyMod.getSettings().discordShowAchievements) {
            LabyMod.getInstance().getGuiCustomAchievement().displayAchievement(imageURL, username, message);
        };

    }

    @Override
    public boolean onSend(String message) {
        if (message.toLowerCase().startsWith("/hydiscordrpc ")) {
            String[] args = message.split(" ");
            if (args.length >= 3) {
                boolean accepted = args[1].equalsIgnoreCase("accept");
                this.discordManager.respond(Long.parseLong(args[2]), accepted);

                LabyMod.getInstance().displayMessageInChat(LanguageManager.translate("discordrpc_join_request_accepted"));
            }

            return true;
        } else {
            return false;
        }
    }


}
