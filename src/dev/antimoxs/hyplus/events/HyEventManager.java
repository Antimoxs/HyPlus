package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.friends.HyFriendRequest;
import dev.antimoxs.hyplus.modules.partyDetector.HyPartyMessageType;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import net.labymod.main.LabyMod;
import net.minecraft.network.play.server.S01PacketJoinGame;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HyEventManager {

    HyPlus hyPlus;

    public HyEventManager(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    private ArrayList<IHyPlusEvent> events = new ArrayList<>();

    public void register(IHyPlusEvent event) {

        events.add(event);

    }

    public void callLocationChange(HyServerLocation location) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onLocationChange(location);

        }

        // calling externals
        sendAddonPacket("location", location.getJson());

        System.out.println("[HyEvent] location-change");

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
        sendAddonPacket(incoming ? "msg-received" : "msg-sent", message);

    }

    public boolean callFriendRequest(HyFriendRequest request) {

        boolean accepted = false;

        // calling internals
        for (IHyPlusEvent event : events) {

            accepted = accepted || event.onFriendRequest(request);
            request.setAccepted(accepted);

        }

        // calling externals
        sendAddonPacket("friendrequest", request.getJson());

        return accepted;

    }

    public void callPacketJoinGame(S01PacketJoinGame packetJoinGame) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onPacketJoinGame(packetJoinGame);

        }

        // calling externals
        sendAddonPacket("packet-join", "{}");

        System.out.println("[HyEvent] packet-join");

    }

    public void callHypixelJoin() {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onHypixelJoin();

        }

        // calling externals
        sendAddonPacket("hypixel-join", "{}");

    }
    public void callHypixelQuit() {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onHypixelQuit();

        }

        // calling externals
        sendAddonPacket("hypixel-quit", "{}");

    }

    public void callPartyMessage(String s, HyPartyMessageType type) {

        // calling internals
        for (IHyPlusEvent event : events) {

            event.onInternalPartyMessage(s, type);

        }

    }

    private void sendAddonPacket(String key, String json) {

        String jsonBytes = hyPlus.hySettings.HYPLUS_API_TOGGLE ? json : "{}";
        PacketAddonDevelopment pad = new PacketAddonDevelopment(
                LabyMod.getInstance().getPlayerUUID(),
                hyPlus.hySettings.HYPLUS_API_TOGGLE ? "hyplus:" + key : "hyplus:disabled",
                jsonBytes.getBytes(StandardCharsets.UTF_8)
        );
        LabyMod.getInstance().getLabyModAPI().sendAddonDevelopmentPacket(pad);

    }

}