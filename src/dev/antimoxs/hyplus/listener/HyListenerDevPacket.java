package dev.antimoxs.hyplus.listener;

import com.google.gson.Gson;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.party.HyParty;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import net.labymod.utils.Consumer;

import java.nio.charset.StandardCharsets;

public class HyListenerDevPacket implements Consumer<PacketAddonDevelopment> {

    @Override
    public void accept(PacketAddonDevelopment packetAddonDevelopment) {

        String key = packetAddonDevelopment.getKey();

        if (!key.startsWith("hyplus")) return;

        String json = new String(packetAddonDevelopment.getData(), StandardCharsets.UTF_8);

        Thread t = new Thread(() -> {


            switch (key) {

                case "hyplus:partydata": HyPlus.getInstance().hyEventManager.callPartyDataPacket(new Gson().fromJson(json, HyParty.class)); break;

            }

        });
        Runtime.getRuntime().addShutdownHook(t);
        t.start();


    }

}
