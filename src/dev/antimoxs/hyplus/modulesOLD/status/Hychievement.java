package dev.antimoxs.hyplus.modulesOLD.status;

import com.google.gson.JsonObject;
import net.labymod.main.LabyMod;

public class Hychievement {

    public static void sendCurrentPlayingGamemode(boolean visible, String gamemodeName) {

        JsonObject object = new JsonObject();
        object.addProperty( "show_gamemode", visible ); // Gamemode visible for everyone
        object.addProperty( "gamemode_name", gamemodeName ); // Name of the current playing gamemode

        // Send to LabyMod using the API
        LabyMod.getInstance().getLabyConnect().onServerMessage("server_gamemode", object);



    }

}
