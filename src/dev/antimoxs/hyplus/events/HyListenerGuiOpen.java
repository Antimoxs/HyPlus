package dev.antimoxs.hyplus.events;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HyListenerGuiOpen {

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {

        // Custom connection
        if (event.gui instanceof GuiConnecting) {

            if (Minecraft.getMinecraft().getCurrentServerData().serverName.endsWith("hypixel.net")) {

                System.out.println("\n\n\n\n\n\n HYPIXEL HERE WE COME\n\n\n\n\n\n");

            }

        }

    }

}
