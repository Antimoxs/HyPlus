package dev.antimoxs.hyplus.listener;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.serverConnector.HyGuiConnecting;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HyListenerGuiOpen {

    // testing class
    public ControlElement.IconData bg = new ControlElement.IconData("textures/hyplus/hypixelbg.jpg");

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {

        // Custom connection
        if (event.gui instanceof GuiConnecting) {

            GuiScreen guiScreen = LabyModCore.getForge().getGuiOpenEventGui(event);
            if (Minecraft.getMinecraft().getCurrentServerData().serverName.endsWith("hypixel.net")) {

                // Let's make sure it's not our gui :)
                if (event.gui instanceof HyGuiConnecting) return;

                // yes it's useless just testing tho
                HyPlus.debugLog("\n\n\n\n\n\n HYPIXEL HERE WE COME\n\n\n\n\n\n");
                //Minecraft.getMinecraft().displayGuiScreen(new GuiServerConnecting((HyGuiConnecting) event.gui));

                try {
                    HyPlus.debugLog("Replacing GUI");
                    guiScreen = new HyGuiConnecting(guiScreen, event.gui instanceof GuiDownloadTerrain ? "Joining..." : "Connecting to Hypixel...");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

                LabyModCore.getForge().setGuiOpenEventGui(event, guiScreen);

            }

        }
        else if (event.gui instanceof GuiDownloadTerrain) {

            GuiScreen guiScreen = LabyModCore.getForge().getGuiOpenEventGui(event);
            if (Minecraft.getMinecraft().getCurrentServerData().serverName.endsWith("hypixel.net")) {

                // Let's make sure it's not our gui :)
                if (event.gui instanceof HyGuiConnecting) return;

                // yes it's useless just testing tho
                HyPlus.debugLog("\n\n\n\n\n\n HYPIXEL HERE WE COME\n\n\n\n\n\n");
                //Minecraft.getMinecraft().displayGuiScreen(new GuiServerConnecting((HyGuiConnecting) event.gui));

                try {
                    // DEBUG System.out.println("Replacing GUI");
                    guiScreen = new HyGuiConnecting(guiScreen, event.gui instanceof GuiDownloadTerrain ? "Joining..." : "Connecting to Hypixel...");
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

                LabyModCore.getForge().setGuiOpenEventGui(event, guiScreen);

            }

        }

    }

}
