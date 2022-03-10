package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.HyAdvanced;
import dev.antimoxs.hyplus.modules.friends.HyFriendRequest;
import dev.antimoxs.hyplus.modules.party.HyParty;
import dev.antimoxs.hyplus.modules.party.HyPartyMessageType;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import net.labymod.main.LabyMod;
import net.minecraft.network.play.server.S01PacketJoinGame;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HyEventManager {

    private final ArrayList<IHyPlusEvent> events = new ArrayList<>();

    public void register(IHyPlusEvent event) {

        events.add(event);

    }

    public void callLocationChange(HyServerLocation location) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onLocationChange(location);

        }

        // calling externals
        sendAddonPacketSelfAPI("location", location.getJson());

        HyPlus.debugLog("[HyEvent] location-change");

    }
    public void callGameStatusChange(String s) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onGameStatusChange();

        }

        // calling externals
        //sendAddonPacketSelfAPI("location", location.getJson());

        HyPlus.debugLog("[HyEvent] status-change: " + s);

    }
    public void callLocationResponse(String json) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onInternalLocationResponse(json);

        }

    }

    public void callPrivateMessage(boolean incoming, String message, String content) {

        // calling internals
        for (IHyPlusEvent event : events) {

            if (incoming) {
                event.onPrivateMessageReceived(message, content);
            } else {
                event.onPrivateMessageSent(message, content);
            }

        }

        // calling externals
        sendAddonPacketSelfAPI(incoming ? "msg-received" : "msg-sent", message);

    }

    public boolean callFriendRequest(HyFriendRequest request) {

        boolean accepted = false;

        // calling internals
        for (IHyPlusEvent event : events) {

            accepted = accepted || event.onFriendRequest(request);
            request.setAccepted(accepted);

        }

        // calling externals
        sendAddonPacketSelfAPI("friendrequest", request.getJson());

        HyPlus.debugLog("FRIEND REQUEST AUTO ACCEPT: " + accepted);

        return accepted;

    }

    public void callPacketJoinGame(S01PacketJoinGame packetJoinGame) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onPacketJoinGame(packetJoinGame);

        }

        // calling externals
        sendAddonPacketSelfAPI("packet-join", "{}");

        HyPlus.debugLog("[HyEvent] packet-join");

    }

    public void callHypixelJoin() {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onHypixelJoin();

        }

        // calling externals
        sendAddonPacketSelfAPI("hypixel-join", "{}");

    }
    public void callHypixelQuit() {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onHypixelQuit();

        }

        // calling externals
        sendAddonPacketSelfAPI("hypixel-quit", "{}");

    }

    public void callPartyMessage(String s, HyPartyMessageType type) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onInternalPartyMessage(s, type);

        }

    }

    public void callPartyMessageAsync(String s, HyPartyMessageType type) {

        // calling internals
        Thread t = new Thread(() -> {

            callPartyMessage(s, type);

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();

    }

    public void callPartyDataPacket(HyParty party) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onPartyDataPacket(party);

        }

    }

    private void sendAddonPacketSelfAPI(String key, String json) {

        String jsonBytes = HyAdvanced.HYPLUS_ADVANCED_API.getValueBoolean() ? json : "{}";
        PacketAddonDevelopment pad = new PacketAddonDevelopment(
                LabyMod.getInstance().getPlayerUUID(),
                HyAdvanced.HYPLUS_ADVANCED_API.getValueBoolean() ? "hyplus:" + key : "hyplus:disabled",
                jsonBytes.getBytes(StandardCharsets.UTF_8)
        );
        LabyMod.getInstance().getLabyModAPI().sendAddonDevelopmentPacket(pad);

    }

    public void callGameStart(HyServerLocation location) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onGameStart(location);

        }

        // calling externals
        sendAddonPacketSelfAPI("game-start", "{}");

        HyPlus.debugLog("[HyEvent] game-start");

    }

    public void callPlayerSpawn(PlayerSpawnEvent playerSpawnEvent) {


        // calling internals
        for (IHyPlusEvent event : events) {

            // calling internals
            Thread t = new Thread(() -> {

                event.onPlayerSpawn(playerSpawnEvent);

            });
            Runtime.getRuntime().addShutdownHook(t);
            t.start();


        }

        // calling externals !!! no externals on packets :)
        //sendAddonPacketSelfAPI("player-spawn", "{}");

        HyPlus.debugLog("[HyEvent] player-spawn");

    }

}
