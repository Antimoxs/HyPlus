package dev.antimoxs.hyplus.objects;

import com.google.gson.annotations.Expose;

import java.util.Objects;

public class HyServerLocation {

    // location fields
    @Expose public String server = "";
    @Expose public String gametype = "";
    @Expose public String lobbyname = "";   // only in lobbies
    @Expose public String mode = "LOBBY";   // only in games
    @Expose public String map = "";         // only in games

    // extra fields
    public String rawloc = "MAIN";          // copied value from gametype since gametype will be name-fetched
    public String rawmod = "LOBBY";          // copied value from mode since mode will be name-fetched
    public boolean online = true;           // only changed when online is false in api response


    public boolean isLimbo() { return server.equalsIgnoreCase("limbo"); }
    public boolean isLobby() { return !Objects.equals(lobbyname, ""); }
    public String getLobbyNumber() {

        if (!isLobby()) return "";
        int index = lobbyname.toLowerCase().indexOf("lobby");
        if (lobbyname.length() >= index+5) {

            return lobbyname.substring(index + 5);


        }
        return "";

    }


    public String getJson() {

        return toString();

    }

    @Override
    public String toString() {

        return "{\"server\"=\"" +
                server + "\",\"gametype\"=\"" +
                gametype + "\",\"lobbyname\"=\"" +
                lobbyname + "\",\"mode\"=\"" +
                mode + "\",\"map\"=\"" +
                map + "\",\"rawloc\"=\"" +
                rawloc + "\",\"online\"=\"" +
                online + "\"}";

    }
}
