//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jagrosh.discordipc.entities;

import java.time.OffsetDateTime;
import java.util.TimerTask;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

public class RichPresence {
    private final String state;
    private final String details;
    private final Timestamps timestamps;
    private final Assets assets;
    private final Party party;
    private final Secrets secrets;
    private final boolean instance;

    public RichPresence(String state, String details, Timestamps timestamps, Assets assets, Party party, Secrets secrets, boolean instance) {
        this.state = state;
        this.details = details;
        this.timestamps = timestamps;
        this.assets = assets;

        if (party.id == null || party.id.equals("")) {

            this.party = null;

        }
        else {

            this.party = party;

        }
        this.secrets = secrets;
        this.instance = instance;
    }

    public JsonElement toJson() {

        Gson gson = new Gson();
        return gson.toJsonTree(this);
        /*

        return (new JSONObject()).put("state", this.state).put("details", this.details).put("timestamps", (new JSONObject()).put("start", this.startTimestamp == null ? null : this.startTimestamp.toEpochSecond()).put("end", this.endTimestamp == null ? null : this.endTimestamp.toEpochSecond())).put("assets", (new JSONObject()).put("large_image", this.largeImageKey).put("large_text", this.largeImageText).put("small_image", this.smallImageKey).put("small_text", this.smallImageText)).put("party", this.partyId == null ? null : (new JSONObject()).put("id", this.partyId).put("size", (new JSONArray()).put(this.partySize).put(this.partyMax))).put("secrets", (new JSONObject()).put("join", this.joinSecret).put("spectate", this.spectateSecret).put("match", this.matchSecret)).put("instance", this.instance);

         */
    }

    public static class Builder {
        private String state;
        private String details;
        private Long startTimestamp;
        private Long endTimestamp;
        private String largeImageKey;
        private String largeImageText;
        private String smallImageKey;
        private String smallImageText;
        private String partyId;
        private int partySize;
        private int partyMax;
        private String matchSecret;
        private String joinSecret;
        private String spectateSecret;
        private boolean instance;

        public Builder() {
        }

        public RichPresence build() {

            return new RichPresence(
                    this.state,
                    this.details,
                    new Timestamps(this.startTimestamp, this.endTimestamp),
                    new Assets(this.largeImageKey, this.largeImageText, this.smallImageKey, this.smallImageText),
                    new Party(this.partyId, this.partySize, this.partyMax),
                    new Secrets(this.joinSecret, this.spectateSecret, this.matchSecret),
                    this.instance
            );

        }

        public Builder setState(String state) {
            this.state = state;
            return this;
        }

        public Builder setDetails(String details) {
            this.details = details;
            return this;
        }

        public Builder setStartTimestamp(Long startTimestamp) {
            this.startTimestamp = startTimestamp;
            return this;
        }

        public Builder setEndTimestamp(Long endTimestamp) {
            this.endTimestamp = endTimestamp;
            return this;
        }

        public Builder setLargeImage(String largeImageKey, String largeImageText) {
            this.largeImageKey = largeImageKey;
            this.largeImageText = largeImageText;
            return this;
        }

        public Builder setLargeImage(String largeImageKey) {
            return this.setLargeImage(largeImageKey, (String)null);
        }

        public Builder setSmallImage(String smallImageKey, String smallImageText) {
            this.smallImageKey = smallImageKey;
            this.smallImageText = smallImageText;
            return this;
        }

        public Builder setSmallImage(String smallImageKey) {
            return this.setSmallImage(smallImageKey, (String)null);
        }

        public Builder setParty(String partyId, int partySize, int partyMax) {
            this.partyId = partyId;
            this.partySize = partySize;
            this.partyMax = partyMax;
            return this;
        }

        public Builder setMatchSecret(String matchSecret) {
            this.matchSecret = matchSecret;
            return this;
        }

        public Builder setJoinSecret(String joinSecret) {
            this.joinSecret = joinSecret;
            return this;
        }

        public Builder setSpectateSecret(String spectateSecret) {
            this.spectateSecret = spectateSecret;
            return this;
        }

        public Builder setInstance(boolean instance) {
            this.instance = instance;
            return this;
        }
    }

    public static class Timestamps {

        private final Long start;
        private final Long end;

        public Timestamps(Long start, Long end) {

            this.start = start;
            this.end = end;

        }

    }

    public static class Assets {

        private final String large_image;
        private final String large_text;
        private final String small_image;
        private final String small_text;

        public Assets(String large_image, String large_text, String small_image, String small_text) {

            this.large_image = large_image;
            this.large_text = large_text;
            this.small_image = small_image;
            this.small_text = small_text;

        }

    }

    public static class Party {

        private final String id;
        private final Integer[] size;

        public Party(String id, int current_size, int max_size) {

            this.id = id;
            this.size = new Integer[2];
            size[0] = current_size;
            size[1] = max_size;

        }

    }

    public static class Secrets {

        private final String join;
        private final String spectate;
        private final String match;

        public Secrets(String join, String spectate, String match) {

            this.join = join;
            this.spectate = spectate;
            this.match = match;

        }

    }

}
