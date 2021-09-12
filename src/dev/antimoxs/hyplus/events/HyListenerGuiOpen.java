package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.modules.serverConnector.GuiServerConnecting;
import net.labymod.api.LabyModAPI;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
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

            if (Minecraft.getMinecraft().getCurrentServerData().serverName.endsWith("hypixel.net")) {

                // Let's make sure it's not our gui :)
                if (event.gui instanceof GuiServerConnecting) return;

                System.out.println("\n\n\n\n\n\n HYPIXEL HERE WE COME\n\n\n\n\n\n");
                //Minecraft.getMinecraft().displayGuiScreen(new GuiServerConnecting((GuiConnecting) event.gui));

                Minecraft mc = Minecraft.getMinecraft();

                DrawUtils draw = LabyMod.getInstance().getDrawUtils();
                draw.bindTexture(bg.getTextureIcon());
                System.out.println("DRAWING!");
                draw.drawTexture(0.0, 0.0, 0.0, 0.0, mc.displayWidth, mc.displayHeight, mc.displayWidth, mc.displayHeight);

            }

        }

    }

}
