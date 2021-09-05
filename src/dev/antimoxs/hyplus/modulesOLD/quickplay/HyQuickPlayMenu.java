package dev.antimoxs.hyplus.modulesOLD.quickplay;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class HyQuickPlayMenu extends GuiScreen {

    private GuiButton mButtonClose;
    private GuiButton qpBW;
    private GuiButton qpSW;
    private GuiButton qpTNT;
    private GuiButton qpSG;
    private GuiButton qpDUELS;
    private GuiButton qpSUHC;

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(mButtonClose = new GuiButton(0, this.width / 2 - 100, this.height - (this.height / 4) + 10, "Close"));

        ResourceLocation bw = new ResourceLocation("assets/minecraft/hyplus/quickplay/bw.png");
        ResourceLocation sw = new ResourceLocation("assets/minecraft/hyplus/quickplay/sw.png");
        ResourceLocation tnt = new ResourceLocation("assets/minecraft/hyplus/quickplay/tnt.png");
        ResourceLocation sg = new ResourceLocation("assets/minecraft/hyplus/quickplay/sg.png");
        ResourceLocation duels = new ResourceLocation("assets/minecraft/hyplus/quickplay/duels.png");
        ResourceLocation suhc = new ResourceLocation("assets/minecraft/hyplus/quickplay/suhc.png");



        qpBW = new HyQuickPlayButton(1, 30, 30, 64, 64, "bw", bw);
        qpSW = new HyQuickPlayButton(1, 124, 30, 64, 64, "sw", sw);
        qpTNT = new HyQuickPlayButton(1, 218, 30, 64, 64, "tnt", tnt);
        qpSG = new HyQuickPlayButton(1, 312, 30, 64, 64, "sg", sg);
        qpDUELS = new HyQuickPlayButton(1, 406, 30, 64, 64, "duels", duels);
        qpSUHC = new HyQuickPlayButton(1, 500, 30, 64, 64, "suhc", suhc);

        this.buttonList.add(qpBW);
        this.buttonList.add(qpSW);
        this.buttonList.add(qpTNT);
        this.buttonList.add(qpSG);
        this.buttonList.add(qpDUELS);
        this.buttonList.add(qpSUHC);


    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == mButtonClose) {
            mc.thePlayer.closeScreen();
        }
        else if (button == qpBW) {



        }
        else if (button == qpSW) {



        }

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }
}