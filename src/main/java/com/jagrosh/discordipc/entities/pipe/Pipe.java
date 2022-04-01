//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.jagrosh.discordipc.entities.pipe;

import com.google.gson.JsonObject;
import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.Callback;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.Packet.OpCode;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import dev.antimoxs.hyplus.HyPlus;

public abstract class Pipe {

    private static final int VERSION = 1;
    PipeStatus status;
    IPCListener listener;
    private DiscordBuild build;
    final IPCClient ipcClient;
    private final HashMap<String, Callback> callbacks;
    private static final String[] unixPaths = new String[]{"XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP"};

    Pipe(IPCClient ipcClient, HashMap<String, Callback> callbacks) {
        this.status = PipeStatus.CONNECTING;
        this.ipcClient = ipcClient;
        this.callbacks = callbacks;
    }

    public static Pipe openPipe(IPCClient ipcClient, long clientId, HashMap<String, Callback> callbacks, DiscordBuild... preferredOrder) throws NoDiscordClientException {
        if (preferredOrder == null || preferredOrder.length == 0) {
            preferredOrder = new DiscordBuild[]{DiscordBuild.ANY};
        }

        Pipe pipe = null;
        Pipe[] open = new Pipe[DiscordBuild.values().length];

        int i;
        for(i = 0; i < 10; ++i) {
            try {
                String location = getPipeLocation(i);
                HyPlus.debugLog(String.format("Searching for IPC: " + location));
                pipe = createPipe(ipcClient, callbacks, location);

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("v", 1);
                jsonObject.addProperty("client_id", Long.toString(clientId));

                pipe.send(OpCode.HANDSHAKE, jsonObject, (Callback)null);
                Packet p = pipe.read();
                pipe.build = DiscordBuild.from(p.getJson().get("data").getAsJsonObject().get("config").getAsJsonObject().get("api_endpoint").getAsString());
                HyPlus.debugLog(String.format("Found a valid client (%s) with packet: %s", pipe.build.name(), p.toString()));
                if (pipe.build == preferredOrder[0] || DiscordBuild.ANY == preferredOrder[0]) {
                    HyPlus.debugLog(String.format("Found preferred client: %s", pipe.build.name()));
                    break;
                }

                open[pipe.build.ordinal()] = pipe;
                open[DiscordBuild.ANY.ordinal()] = pipe;
                pipe.build = null;
                pipe = null;
            } catch (IOException var11) {
                pipe = null;
            }
        }

        if (pipe == null) {
            for(i = 1; i < preferredOrder.length; ++i) {
                DiscordBuild cb = preferredOrder[i];
                HyPlus.debugLog(String.format("Looking for client build: %s", cb.name()));
                if (open[cb.ordinal()] != null) {
                    pipe = open[cb.ordinal()];
                    open[cb.ordinal()] = null;
                    if (cb == DiscordBuild.ANY) {
                        for(int k = 0; k < open.length; ++k) {
                            if (open[k] == pipe) {
                                pipe.build = DiscordBuild.values()[k];
                                open[k] = null;
                            }
                        }
                    } else {
                        pipe.build = cb;
                    }

                    HyPlus.debugLog(String.format("Found preferred client: %s", pipe.build.name()));
                    break;
                }
            }

            if (pipe == null) {
                throw new NoDiscordClientException();
            }
        }

        for(i = 0; i < open.length; ++i) {
            if (i != DiscordBuild.ANY.ordinal() && open[i] != null) {
                try {
                    open[i].close();
                } catch (IOException var10) {
                    HyPlus.debugLog("Failed to close an open IPC pipe! " + var10);
                }
            }
        }

        pipe.status = PipeStatus.CONNECTED;
        return pipe;
    }

    private static Pipe createPipe(IPCClient ipcClient, HashMap<String, Callback> callbacks, String location) {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return new WindowsPipe(ipcClient, callbacks, location);
        } else if (!osName.contains("linux") && !osName.contains("mac")) {
            throw new RuntimeException("Unsupported OS: " + osName);
        } else {
            try {
                return new UnixPipe(ipcClient, callbacks, location);
            } catch (IOException var5) {
                throw new RuntimeException(var5);
            }
        }
    }

    public void send(OpCode op, JsonObject data, Callback callback) {
        try {
            String nonce = generateNonce();

            data.addProperty("nonce", nonce);

            Packet p = new Packet(op, data);
            if (callback != null && !callback.isEmpty()) {
                this.callbacks.put(nonce, callback);
            }

            this.write(p.toBytes());
            HyPlus.debugLog(String.format("Sent packet: %s", p.toString()));
            if (this.listener != null) {
                this.listener.onPacketSent(this.ipcClient, p);
            }
        } catch (IOException var6) {
            HyPlus.debugLog("Encountered an IOException while sending a packet and disconnected!");
            this.status = PipeStatus.DISCONNECTED;
        }

    }

    public abstract Packet read() throws IOException;

    public abstract void write(byte[] var1) throws IOException;

    private static String generateNonce() {
        return UUID.randomUUID().toString();
    }

    public PipeStatus getStatus() {
        return this.status;
    }

    public void setStatus(PipeStatus status) {
        this.status = status;
    }

    public void setListener(IPCListener listener) {
        this.listener = listener;
    }

    public abstract void close() throws IOException;

    public DiscordBuild getDiscordBuild() {
        return this.build;
    }

    private static String getPipeLocation(int i) {
        if (System.getProperty("os.name").contains("Win")) {
            return "\\\\?\\pipe\\discord-ipc-" + i;
        } else {
            String tmppath = null;
            String[] var2 = unixPaths;
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                String str = var2[var4];
                tmppath = System.getenv(str);
                if (tmppath != null) {
                    break;
                }
            }

            if (tmppath == null) {
                tmppath = "/tmp";
            }

            return tmppath + "/discord-ipc-" + i;
        }
    }
}
