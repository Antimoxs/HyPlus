package dev.antimoxs.hyplus.modules.serverConnector;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;

import java.lang.reflect.Method;

public class HyGuiConnecting extends GuiScreen {

    private final GuiScreen parent;
    private final String info;
    public final ControlElement.IconData bg = new ControlElement.IconData("textures/hyplus/hypixelbg.jpg");

    public HyGuiConnecting(GuiScreen lastScreen, String info) {

        this.parent = lastScreen;
        this.info = info;

    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float pTicks) {

        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.bindTexture(bg.getTextureIcon());
        int Wwidth = Minecraft.getMinecraft().displayWidth;
        int Wheight = Minecraft.getMinecraft().displayHeight;
        HyPlus.debugLog("DRAWING! W: " + Wwidth + " | H: " + Wheight + " | 8192x4602");
        GlStateManager.pushMatrix();
        float x = (float)width/(512*2);  //512 -> my texture size
        float y = (float)height/(384*2); //384 -> my texture size
        GlStateManager.scale(x, y, 1.0);
        draw.bindTexture(bg.getTextureIcon());
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 512*2, 384*2, 512*2, 384*2);
        GlStateManager.popMatrix();
        draw.drawCenteredString(info, width/2, height/2);
        drawCenteredString(LabyModCore.getMinecraft().getFontRenderer(), "HYPIXEL", 20, 500, 70);

        super.drawScreen(mouseX, mouseY, pTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {

        try {

            if (parent instanceof GuiConnecting) {

                Class<?> clazz = ((GuiConnecting) parent).getClass();
                Method m = clazz.getDeclaredMethod("a", GuiButton.class);
                m.setAccessible(true);
                m.invoke(parent,new GuiButton(0, 0, 0, ""));

            }

            /*
            if (Minecraft.getMinecraft().getNetHandler() != null & Minecraft.getMinecraft().getNetHandler().getNetworkManager() != null) {
                //LabyModCore.getMinecraft().getConnection().getNetworkManager().closeChannel(new ChatComponentText("Aborted"));
                Minecraft.getMinecraft().getNetHandler().getNetworkManager().closeChannel(new ChatComponentText("Aborted"));
                //LabyMod.getInstance().connectToServer();
            }*/

        }
        catch (Exception e) {

            LogManager.getLogger().error("[HYPLUS] Failed to close channel LMAO");
            e.printStackTrace();

        }

        Minecraft.getMinecraft().displayGuiScreen(new GuiMultiplayer(null));

    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel")));
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
}
