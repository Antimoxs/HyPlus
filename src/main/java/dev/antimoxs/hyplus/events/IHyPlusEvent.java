package dev.antimoxs.hyplus.events;


import dev.antimoxs.hyplus.api.friends.HyFriendRequest;
import dev.antimoxs.hyplus.modules.party.HyParty;
import dev.antimoxs.hyplus.modules.party.HyPartyMessageType;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import net.minecraft.network.play.server.S01PacketJoinGame;

public interface IHyPlusEvent {

    default void onLocationChange(HyServerLocation location) {}
    default void onGameStatusChange() {}
    default void onInternalLocationResponse(String json) {}

    default void onPrivateMessageSent(String message, String content) {}
    default void onPrivateMessageReceived(String message, String content) {}

    default boolean onFriendRequest(HyFriendRequest request) {
        return false;
    }

    default void onPacketJoinGame(S01PacketJoinGame packet) {}

    default void onHypixelJoin() {}
    default void onHypixelQuit() {}

    default void onPartyDataPacket(HyParty party) {}

    default void onInternalPartyMessage(String message, HyPartyMessageType type) {}

    default void onGameStart(HyServerLocation location) {}

    default void onPlayerSpawn(PlayerSpawnEvent event) {}

}
