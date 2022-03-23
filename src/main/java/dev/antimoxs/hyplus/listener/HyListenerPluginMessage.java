package dev.antimoxs.hyplus.listener;

import net.labymod.main.listeners.PluginMessageListener;
import net.minecraft.network.PacketBuffer;

public class HyListenerPluginMessage extends PluginMessageListener {

    // TODO: convert to non mc class (PacketBuffer)

    @Override
    public void receiveMessage(String channelName, PacketBuffer packetBuffer) {

        if (channelName.equals("badlion:timers")) {

            // in progress

        }

    }
}
