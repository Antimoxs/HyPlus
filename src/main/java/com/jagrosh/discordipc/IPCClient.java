//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jagrosh.discordipc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.entities.Packet.OpCode;
import com.jagrosh.discordipc.entities.pipe.Pipe;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.io.Closeable;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.HashMap;

import dev.antimoxs.hyplus.HyPlus;

public final class IPCClient implements Closeable {
    private final long clientId;
    private final HashMap<String, Callback> callbacks = new HashMap();
    private volatile Pipe pipe;
    private IPCListener listener = null;
    private Thread readThread = null;

    public IPCClient(long clientId) {
        this.clientId = clientId;
    }

    public void setListener(IPCListener listener) {
        this.listener = listener;
        if (this.pipe != null) {
            this.pipe.setListener(listener);
        }

    }

    public void connect(DiscordBuild... preferredOrder) throws NoDiscordClientException {
        this.checkConnected(false);
        this.callbacks.clear();
        this.pipe = null;
        this.pipe = Pipe.openPipe(this, this.clientId, this.callbacks, preferredOrder);
        HyPlus.debugLog("Client is now connected and ready!");
        if (this.listener != null) {
            this.listener.onReady(this);
        }

        this.startReading();
    }

    public void sendRichPresence(RichPresence presence) {
        this.sendRichPresence(presence, (Callback)null);
    }

    public void sendRichPresence(RichPresence presence, Callback callback) {
        this.checkConnected(true);
        HyPlus.debugLog("Sending RichPresence to discord: " + (presence == null ? null : presence.toJson().toString()));

        JsonObject args = new JsonObject();
        args.addProperty("pid", getPID());
        args.add("activity", presence == null ? null : presence.toJson());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cmd", "SET_ACTIVITY");
        jsonObject.add("args", args);

        this.pipe.send(OpCode.FRAME, jsonObject, callback);
    }

    public void reply(long userId, boolean accept) {
        this.reply(userId, accept, null);
    }

    public void reply(long userId, boolean accept, Callback callback) {
        this.checkConnected(true);
        HyPlus.debugLog("Sending Reply to discord: " + accept + " for " + userId);

        JsonObject reply = new JsonObject();
        reply.addProperty("user_id", userId);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("cmd", accept ? "SEND_ACTIVITY_JOIN_INVITE" : "CLOSE_ACTIVITY_REQUEST");
        jsonObject.add("args", reply);

        this.pipe.send(OpCode.FRAME, jsonObject, callback);
    }

    public void subscribe(Event sub) {
        this.subscribe(sub, (Callback)null);
    }

    public void subscribe(Event sub, Callback callback) {
        this.checkConnected(true);
        if (!sub.isSubscribable()) {
            throw new IllegalStateException("Cannot subscribe to " + sub + " event!");
        } else {
            HyPlus.debugLog(String.format("Subscribing to Event: %s", sub.name()));

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("cmd", "SUBSCRIBE");
            jsonObject.addProperty("evt", sub.name());

            this.pipe.send(OpCode.FRAME, jsonObject, callback);
        }
    }

    public PipeStatus getStatus() {
        return this.pipe == null ? PipeStatus.UNINITIALIZED : this.pipe.getStatus();
    }

    public void close() {
        this.checkConnected(true);

        try {
            this.pipe.close();
        } catch (IOException var2) {
            HyPlus.debugLog("Failed to close pipe " + var2);
        }

    }

    public DiscordBuild getDiscordBuild() {
        return this.pipe == null ? null : this.pipe.getDiscordBuild();
    }

    private void checkConnected(boolean connected) {
        if (connected && this.getStatus() != PipeStatus.CONNECTED) {
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is not connected!", this.clientId));
        } else if (!connected && this.getStatus() == PipeStatus.CONNECTED) {
            throw new IllegalStateException(String.format("IPCClient (ID: %d) is already connected!", this.clientId));
        }
    }

    private void startReading() {
        this.readThread = new Thread(() -> {
            while(true) {
                try {
                    Packet p;
                    if ((p = this.pipe.read()).getOp() != OpCode.CLOSE) {
                        JsonObject json = p.getJson();
                        JsonElement evt = json.get("evt");

                        Event event = IPCClient.Event.of(evt.isJsonNull() ? null : evt.getAsString());
                        String nonce = json.get("nonce").getAsString();
                        switch (event) {
                            case NULL:
                                if (nonce != null && this.callbacks.containsKey(nonce)) {
                                    ((Callback)this.callbacks.remove(nonce)).succeed(p);
                                }
                                break;
                            case ERROR:
                                if (nonce != null && this.callbacks.containsKey(nonce)) {
                                    ((Callback)this.callbacks.remove(nonce)).fail(json.get("data").getAsJsonObject().get("message").getAsString());
                                }
                                break;
                            case ACTIVITY_JOIN:
                                HyPlus.debugLog("Reading thread received a 'join' event.");
                                break;
                            case ACTIVITY_SPECTATE:
                                HyPlus.debugLog("Reading thread received a 'spectate' event.");
                                break;
                            case ACTIVITY_JOIN_REQUEST:
                                HyPlus.debugLog("Reading thread received a 'join request' event.");
                                break;
                            case UNKNOWN:
                                HyPlus.debugLog("Reading thread encountered an event with an unknown type: " + json.get("evt").getAsString());
                        }

                        if (this.listener == null || !json.has("cmd") || !json.get("cmd").getAsString().equals("DISPATCH")) {
                            continue;
                        }

                        try {
                            JsonObject data = json.get("data").getAsJsonObject();
                            switch (IPCClient.Event.of(json.get("evt").getAsString())) {
                                case ACTIVITY_JOIN:
                                    this.listener.onActivityJoin(this, data.get("secret").getAsString());
                                    continue;
                                case ACTIVITY_SPECTATE:
                                    this.listener.onActivitySpectate(this, data.get("secret").getAsString());
                                    continue;
                                case ACTIVITY_JOIN_REQUEST:
                                    JsonObject u = data.get("user").getAsJsonObject();
                                    User user = new User(u.get("username").getAsString(), u.get("discriminator").getAsString(), Long.parseLong(u.get("id").getAsString()), u.get("avatar").getAsString());
                                    this.listener.onActivityJoinRequest(this, data.get("secret").getAsString(), user);
                            }
                        } catch (Exception var8) {
                            HyPlus.debugLog("Exception when handling event: " + var8);
                        }
                        continue;
                    }

                    this.pipe.setStatus(PipeStatus.DISCONNECTED);
                    if (this.listener != null) {
                        this.listener.onClose(this, p.getJson());
                    }
                } catch (IOException var9) {
                    HyPlus.debugLog("Reading thread encountered an IOException " + var9);

                    this.pipe.setStatus(PipeStatus.DISCONNECTED);
                    if (this.listener != null) {
                        this.listener.onDisconnect(this, var9);
                    }
                }

                return;
            }
        });
        HyPlus.debugLog("Starting IPCClient reading thread!");
        this.readThread.start();
    }

    private static int getPID() {
        String pr = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(pr.substring(0, pr.indexOf(64)));
    }

    public static enum Event {
        NULL(false),
        READY(false),
        ERROR(false),
        ACTIVITY_JOIN(true),
        ACTIVITY_SPECTATE(true),
        ACTIVITY_JOIN_REQUEST(true),
        UNKNOWN(false);

        private final boolean subscribable;

        private Event(boolean subscribable) {
            this.subscribable = subscribable;
        }

        public boolean isSubscribable() {
            return this.subscribable;
        }

        static Event of(String str) {
            if (str == null) {
                return NULL;
            } else {
                Event[] var1 = values();
                int var2 = var1.length;

                for(int var3 = 0; var3 < var2; ++var3) {
                    Event s = var1[var3];
                    if (s != UNKNOWN && s.name().equalsIgnoreCase(str)) {
                        return s;
                    }
                }

                return UNKNOWN;
            }
        }
    }
}
