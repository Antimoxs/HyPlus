package dev.antimoxs.hyplus.modules.serverConnector;

import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerLoginClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class GuiServerConnecting extends GuiConnecting {

    public ControlElement.IconData bg = new ControlElement.IconData("textures/hyplus/hypixelbg.jpg");

    private final GuiConnecting guiConnecting;

    public GuiServerConnecting(GuiConnecting guiConnecting) {
        super(guiConnecting.mc.currentScreen, guiConnecting.mc, guiConnecting.mc.getCurrentServerData());
        this.mc = guiConnecting.mc;
        this.guiConnecting = guiConnecting;
        drawScreen(this.width, this.height, 1.0f);

        //this.connect(serveraddress.getIP(), serveraddress.getPort());
    }


    protected void keyTyped(char p_keyTyped_1_, int p_keyTyped_2_) throws IOException {
    }

    @Override
    public void initGui() {
        System.out.println("INIT");
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
    }



    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        //this.drawDefaultBackground();
        DrawUtils draw = LabyMod.getInstance().getDrawUtils();
        draw.bindTexture(bg.getTextureIcon());
        System.out.println("DRAWING!");
        draw.drawTexture(0.0, 0.0, 0.0, 0.0, this.width, this.height, this.width, this.height, 1.0f);
        /*
        if (mc.getNetHandler() == null) {
            drawCenteredString(
                    fontRendererObj,
                    I18n.format("connect.connecting",
                            new Object[0]) + " to Hypixel",
                    this.width / 2,
                    this.height / 2 - 50,
                    16777215);
        } else {
            drawCenteredString(
                    fontRendererObj,
                    I18n.format("connect.authorizing",
                            new Object[0]) + " to Hypixel",
                    this.width / 2,
                    this.height / 2 - 50,
                    16777215);
        }*/

        drawScreenSuper(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
    }

    public void drawScreenSuper(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_) {
        System.out.println("DARWING SUPER?");
        int j;
        for(j = 0; j < super.buttonList.size(); ++j) {
            ((GuiButton)super.buttonList.get(j)).drawButton(super.mc, p_drawScreen_1_, p_drawScreen_2_);
        }

        for(j = 0; j < super.labelList.size(); ++j) {
            ((GuiLabel)super.labelList.get(j)).drawLabel(super.mc, p_drawScreen_1_, p_drawScreen_2_);
        }

    }

}

