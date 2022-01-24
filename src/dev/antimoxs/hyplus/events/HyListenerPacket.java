package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.utils.Consumer;
import net.minecraft.network.play.server.S01PacketJoinGame;

public class HyListenerPacket implements Consumer<Object> {

    @Override
    public void accept(Object o) {

        // Check if we are enabled.
        if (!HyPlus.getInstance().hyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return;

        if (HyPlus.getInstance().hypixel.checkOnServer()) {

            if (o instanceof S01PacketJoinGame) {

                HyPlus.getInstance().hyEventManager.callPacketJoinGame((S01PacketJoinGame) o);

            }

        }

    }

}
