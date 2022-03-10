package dev.antimoxs.hyplus.api.friends;

public class HyFriendRequest {

    private String name;
    private String rank;
    private long time;
    private boolean accepted = false;

    public HyFriendRequest(String name, String rank) {

        this.name = name;
        this.rank = rank;
        this.time = System.currentTimeMillis();

    }

    public void setAccepted(boolean accepted) { this.accepted = accepted; }

    public String getName() { return this.name; }
    public String getRank() { return this.rank; }
    public long getTimestamp() { return this.time; }
    public boolean getAccepted() { return this.accepted; }

    public String getJson() {

        return "{\"name\"=\"" + name + "\",\"rank\"=\"" + rank + "\",\"time\"=\"" + time + "\",\"accepted\"=\"" + accepted + "\"}";

    }

    @Override
    public String toString() {

        return rank + " " + name;

    }
}
