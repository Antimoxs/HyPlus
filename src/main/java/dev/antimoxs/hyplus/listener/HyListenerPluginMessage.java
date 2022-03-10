package dev.antimoxs.hyplus.listener;

import net.labymod.main.listeners.PluginMessageListener;
import net.labymod.utils.Consumer;
import net.minecraft.network.PacketBuffer;

public class HyListenerPluginMessage extends PluginMessageListener {

    @Override
    public void receiveMessage(String channelName, PacketBuffer packetBuffer) {

        if (channelName.equals("badlion:timers")) {

            // in progress

        }

    }
}
