package dev.antimoxs.hyplus.modules.partyManager;

import com.google.gson.Gson;
import dev.antimoxs.hypixelapi.objects.games.Game;
import dev.antimoxs.hyplus.objects.HySimplePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class HyParty {

    private boolean exists = false;
    private boolean isPublic = false;
    private boolean allInvite = false;
    private boolean pGames = false;
    private boolean muted = false;
    private int count = 0;
    private int cap = -1;

    private HySimplePlayer partyLeader = new HySimplePlayer();
    private HashMap<String, HySimplePlayer> partyMods = new HashMap<>();
    private HashMap<String, HySimplePlayer> partyMembers = new HashMap<>();

    public void setExists(boolean exists) { this.exists = exists; }
    public void setPublic(boolean isPublic) { this.isPublic = isPublic; }
    public void setAllInvite(boolean allInvite) { this.allInvite = allInvite; }
    public void setPGames(boolean pGames) { this.pGames = pGames; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public void setCount(int count) { this.count = count; }
    public void setCap(int cap) { this.cap = cap; }

    public void addPlayer(HySimplePlayer player) {

        partyMembers.put(player.getName(), player);

    }
    public void addMod(HySimplePlayer player) {

        partyMods.put(player.getName(), player);

    }
    public void removePlayer(String name) {

        this.partyMembers.remove(name);

    }
    public void removeMod(String name) {

        this.partyMods.remove(name);

    }
    public void setPartyLeader(HySimplePlayer player) {

        this.partyLeader = player;

    }
    public void clearMembers() {

        if (!this.partyMembers.isEmpty()) this.partyMembers.clear();

    }
    public void clearMods() {

        if (!this.partyMods.isEmpty()) this.partyMods.clear();

    }

    public void reset() {

        this.exists = false;
        this.isPublic = false;
        this.allInvite = false;
        this.pGames = false;
        this.count = 0;
        this.cap = -1;
        this.partyLeader = new HySimplePlayer();
        this.clearMembers();
        this.clearMods();

    }

    public boolean doesExist() { return this.exists; }
    public boolean isPublic() { return this.isPublic; }
    public boolean getAllInvite() { return this.allInvite; }
    public boolean getPGames() { return this.pGames; }
    public boolean getMuted() { return this.muted; }
    public int getCount() { return this.count; }
    public int getCap() { return this.cap; }
    public HySimplePlayer getPartyLeader() { return this.partyLeader; }
    public HashMap<String, HySimplePlayer> getPartyMembers() { return this.partyMembers; }
    public HashMap<String, HySimplePlayer> getPartyMods() { return this.partyMods; }
    public ArrayList<HySimplePlayer> getAllMembers() {

        ArrayList<HySimplePlayer> players = new ArrayList<>();

        players.addAll(this.partyMembers.values());
        players.addAll(this.partyMods.values());

        return players;

    }

    public String getJson() {

        return new Gson().toJson(this);

    }

    public boolean isInParty(String name) {

        for (HySimplePlayer player : this.getAllMembers()) {

            if (player.getPlayerBlank().equals(name)) return true;

        }
        return false;

    }

    public int getMax() {

        return isPublic() ? getCap() : getCount();

    }

}
