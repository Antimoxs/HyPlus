package dev.antimoxs.hyplus.modulesOLD.discord;

import com.google.gson.JsonObject;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.main.LabyMod;

import java.util.UUID;

public class HyPlay {

    HyPlus hyPlus;

    public HyPlay(HyPlus HyPlus) {

        this.hyPlus = HyPlus;

    }

    public void generateInvite(String name) {

        String uuidJoin = MojangRequest.getUUID(name);
        System.out.println(uuidJoin);
        uuidJoin = uuidJoin.substring(0,8)
                + "-"
                + uuidJoin.substring(8,12)
                + "-"
                + uuidJoin.substring(12,16)
                + "-"
                + uuidJoin.substring(16,20)
                + "-"
                + uuidJoin.substring(20,32);
        hyPlus.discordApp.getRichPresence().updateJoinSecret(true, uuidJoin);

    }


    public void sendSecrets(String matchSecret, String joinSecret, String domain) {

        // The LabyMod client sends a "INFO" message in the "LMC" channel on join
        JsonObject obj = new JsonObject();

        String uuidMatch = MojangRequest.getUUID(matchSecret);
        System.out.println(uuidMatch);
        uuidMatch = uuidMatch.substring(0,8)
                + "-"
                + uuidMatch.substring(8,12)
                + "-"
                + uuidMatch.substring(12,16)
                + "-"
                + uuidMatch.substring(16,20)
                + "-"
                + uuidMatch.substring(20,32);
        System.out.println(uuidMatch);
        String uuidJoin = MojangRequest.getUUID(joinSecret);
        System.out.println(uuidJoin);
        uuidJoin = uuidJoin.substring(0,8)
                + "-"
                + uuidJoin.substring(8,12)
                + "-"
                + uuidJoin.substring(12,16)
                + "-"
                + uuidJoin.substring(16,20)
                + "-"
                + uuidJoin.substring(20,32);

        System.out.println(uuidJoin);

        // Add all secrets
        addSecret( obj, "hasMatchSecret", "matchSecret", UUID.randomUUID(), domain );
        //addSecret( obj, "hasSpectateSecret", "spectateSecret", user.getSpectateSecret(), domain );
        addSecret( obj, "hasJoinSecret", "joinSecret", UUID.randomUUID(), domain );

        LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", obj);

    }

    public JsonObject addSecret(JsonObject jsonObject, String hasKey, String key, UUID secret, String domain) {
        jsonObject.addProperty( hasKey, true );
        jsonObject.addProperty( key, secret.toString() + ":" + domain );
        return jsonObject;
    }


    public String rematchInvite(UUID invite) {

        String r = MojangRequest.getName(invite.toString());
        System.out.println("JOINING TO: " + r);
        return r;

    }

}
