package dev.antimoxs.hyplus.internal.discordapp;

import com.google.gson.JsonElement;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.discordapp.DiscordApp;
import net.labymod.discordapp.api.DiscordEventHandlers;
import net.labymod.discordapp.api.DiscordLibraryProvider;
import net.labymod.discordapp.api.DiscordRPCLibrary;
import net.labymod.discordapp.listeners.*;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;

import java.io.File;
import java.util.UUID;


public class DiscordAppExtender extends DiscordApp {


    private static final String APPLICATION_ID = "824252829404233729"; // HyPlus Application ID
    public static File libraryFile = null;
    private DiscordEventHandlers handlers = new DiscordEventHandlers();
    private ModRichPresenceExtender richPresence;
    private boolean initialized = false;
    private boolean connected = false;
    private UUID queuedJoinKey = null;

    public DiscordAppExtender() {
        handlers.ready = new ReadyListener(this);
        handlers.disconnected = new DisconnectListener(this);
        handlers.errored = new ErroredListener(this);
        handlers.joinGame = new DiscordJoinListener(this); // custom
        handlers.joinRequest = new DiscordRequestListener(this); // custom
        handlers.spectateGame = new SpectateGameListener(this);
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                DiscordAppExtender.this.shutdown();
            }
        }));
        this.richPresence = new ModRichPresenceExtender(this);
    }

    // Use this method to check and enable if not
    public boolean init() {

        initialize();
        return initialized;

    }

    @Override
    public void initialize() {
        if (!this.initialized) {
            if (LabyMod.getSettings().discordRichPresence) {
                Debug.log(Debug.EnumDebugMode.DISCORD, "Initialize discord rpc..");
                DiscordLibraryProvider discordLibraryProvider = new DiscordLibraryProvider();
                if (discordLibraryProvider.isValidOS()) {
                    discordLibraryProvider.execute(new Consumer<File>() {
                        public void accept(File libraryFile) {
                            if (libraryFile != null) {
                                DiscordApp.libraryFile = libraryFile;

                                try {
                                    // DEBUG System.out.println("INIT DC HANDLERS");
                                    DiscordRPCLibrary.initialize(APPLICATION_ID, handlers, 2, (String)null);
                                    initialized = true;
                                    onLibraryLoaded();
                                } catch (Throwable var3) {
                                    initialized = false;
                                    var3.printStackTrace();
                                }
                            } else {
                                Debug.log(Debug.EnumDebugMode.DISCORD, "No Discord library found!");
                            }

                        }
                    });
                }

            }
        }
    }

    @Override
    public void shutdown() {
        if (this.initialized) {
            Debug.log(Debug.EnumDebugMode.DISCORD, "Shutdown discord rpc.. [HYPIXEL]");
            DiscordRPCLibrary.shutdown();
            this.initialized = false;
            this.connected = false;
        }

    }

    private void onLibraryLoaded() {
        Debug.log(Debug.EnumDebugMode.DISCORD, "Discord library's successfully loaded!");
        // Event for server rpc :: LabyMod.getInstance().getEventManager().register((PluginMessageEvent) this);
        this.richPresence.forceUpdate();
    }

    // ------------------------------------------------------------

    // Overriding LabyCode



    @Override
    public void receiveMessage(String channelName, PacketBuffer packetBuffer) {
        if (channelName.equals("MC|Brand")) {
            ServerData serverData = Minecraft.getMinecraft().getCurrentServerData();
            boolean singlePlayer = Minecraft.getMinecraft().isSingleplayer();
            if (singlePlayer) {

                // DEBUG System.out.println("Why are we in singleplayer? We should be on Hypixel!");
                this.shutdown();

            }
        }

    }

    // Excluding server rpc - there is non from Hypixel - they dont care about laby :(
    @Override
    public void onServerMessage(String messageKey, JsonElement serverMessage) {
        System.out.println("RPC FROM SERVER...?");

    }

    // Whatever this does but we need it... I guess
    @Override
    public void respond(String userId, int reply) {
        DiscordRPCLibrary.respond(userId, reply);
    }

    // Overriding so we don't run any code
    @Override
    public void redeemSpectateKey(UUID spectateKey, String serverDomain) {
        System.out.println("We do not support spectating on Hypixel.");
    }

    // We're overriding LabyMod's method for joining to adapt it to the party system on hypixel.
    @Override
    public void redeemJoinKey(UUID joinKey, String serverDomain) {

        HyPlus.getInstance().log("Joining Hypixel via Discord key...");
        Debug.log(Debug.EnumDebugMode.DISCORD, "Redeem join secret key: " + joinKey.toString() + " on " + serverDomain);
        this.queuedJoinKey = joinKey;

        // switching server to hypixel - using default hypixel proxy
        if (!LabyMod.getInstance().switchServer("mc.hypixel.net", true)) {

            //JsonObject obj = new JsonObject();
            //obj.addProperty("joinSecret", this.queuedJoinKey.toString());
            String name = HyPlus.getInstance().hyPartyManager.rematchInvite(this.queuedJoinKey);
            HyPlus.getInstance().sendMessageIngameChat("/party join " + name);
            HyPlus.getInstance().hyPartyManager.getParty().setPublic(true);
            HyPlus.getInstance().sendMessageIngameChat("/pl"); // <- update the party :)
            // Hypixel does not use it anyways so lets save data LabyMod.getInstance().getLabyModAPI().sendJsonMessageToServer("discord_rpc", obj);

        }

    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    // ------------------------------------------------------------

    public ModRichPresenceExtender getRichPresence() {

        return this.richPresence;

    }

}
