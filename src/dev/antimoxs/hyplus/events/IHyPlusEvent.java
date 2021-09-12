package dev.antimoxs.hyplus.events;


import dev.antimoxs.hyplus.modules.friends.HyFriendRequest;
import dev.antimoxs.hyplus.modules.partyManager.HyPartyMessageType;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import net.minecraft.network.play.server.S01PacketJoinGame;

public interface IHyPlusEvent {

    default void onLocationChange(HyServerLocation location) {}
    default void onInternalLocationResponse(String json) {}

    default void onPrivateMessageSent(String message, String content) {}
    default void onPrivateMessageReceived(String message, String content) {}

    default boolean onFriendRequest(HyFriendRequest request) {
        return false;
    }

    default void onPacketJoinGame(S01PacketJoinGame packet) {}

    default void onHypixelJoin() {}
    default void onHypixelQuit() {}

    default void onInternalPartyMessage(String message, HyPartyMessageType type) {}

}
