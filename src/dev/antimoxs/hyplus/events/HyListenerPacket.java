package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.utils.Consumer;
import net.minecraft.network.play.server.S01PacketJoinGame;

public class HyListenerPacket implements Consumer<Object> {

    private final HyPlus hyPlus;

    public HyListenerPacket(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public void accept(Object o) {

        // Check if we are enabled.
        if (!hyPlus.hyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return;

        if (hyPlus.hypixel.checkOnServer()) {

            if (o instanceof S01PacketJoinGame) {

                hyPlus.hyEventManager.callPacketJoinGame((S01PacketJoinGame) o);

            }

        }

    }

}
