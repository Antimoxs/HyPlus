package dev.antimoxs.hyplus.modulesOLD.status;

import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.main.LabyMod;

import java.util.UUID;

public class HyPresence {

    private dev.antimoxs.hyplus.HyPlus HyPlus;

    public HyPresence(dev.antimoxs.hyplus.HyPlus HyPlus) {

        this.HyPlus = HyPlus;

    }



    public void updatePrecense(HyPlus HyPlus, String text) {


        HyPlus.getApi().getLabyModChatClient().updatePlayingOnServerState("pooop");
        HyPlus.getApi().getLabyModChatClient().updateAlertDisplayType();
        System.out.println("poop");


    }

    public void updateGuiStatus() {

        //HyPlus.discordApp.guiStatusUpdate();

    }

    // custom presence
    private void updateGameInfoCustom(boolean hasGame, long startTime, long endTime, String gamemode, String details) {

        //HyPlus.discordApp.updateGame(hasGame, startTime, endTime, gamemode);
        //HyPlus.discordApp.updateDetails(hasGame, details);


    }

    public void updateGameInfo(boolean hasGame, String gamemode, String details, long startTime, long endTime ) {

        if (HyPlus.hyDiscordPresence.HYPLUS_DP_TOGGLE) {

            updateGameInfoCustom(hasGame, startTime, endTime, gamemode, details);
            return;

        }

        // Create game json object
        JsonObject obj = new JsonObject();
        obj.addProperty( "hasGame", hasGame );

        if ( hasGame ) {
            obj.addProperty( "game_mode", gamemode );
            obj.addProperty( "game_startTime", startTime ); // Set to 0 for countdown
            obj.addProperty( "game_endTime", endTime ); // // Set to 0 for timer
        }

        sendToSelf(obj);

    }

    public void updatePartyInfo(boolean hasParty, UUID partyLeaderUUID, int partySize, int maxPartyMembers ) {
        String domain = "mc.hypixel.net";

        // Only for discord rpc users (Just set an flag to true when a LabyMod user joins the network) [optional]

        // Create party json object
        JsonObject obj = new JsonObject();
        obj.addProperty( "hasParty", hasParty );

        if ( hasParty ) {
            obj.addProperty( "partyId", partyLeaderUUID.toString() + ":" + domain );
            obj.addProperty( "party_size", partySize );
            obj.addProperty( "party_max", maxPartyMembers );
        }


        sendToSelf(obj);
    }

    public void sendToSelf(JsonObject obj) {


        if (HyPlus.hyDiscordPresence.HYPLUS_DP_TOGGLE) {

            System.out.println("RPC - Custom");
            HyPlus.discordApp.onServerMessage("discord_rpc", obj);

        }
        else {

            System.out.println("RPC - Laby");
            LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", obj);

        }

    }

}
