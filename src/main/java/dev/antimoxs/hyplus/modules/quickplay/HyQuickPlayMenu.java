package dev.antimoxs.hyplus.modules.quickplay;

import net.labymod.settings.elements.ControlElement;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class HyQuickPlayMenu extends GuiScreen {

    // TODO: convert to non mc class (net.minecraft.*)

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

        ControlElement.IconData bw = new ControlElement.IconData("textures/hyplus/quickplay/bw.png");
        ControlElement.IconData sw = new ControlElement.IconData("textures/hyplus/quickplay/sw.png");
        ControlElement.IconData tnt = new ControlElement.IconData("textures/hyplus/quickplay/tnt.png");
        ControlElement.IconData sg = new ControlElement.IconData("textures/hyplus/quickplay/sg.png");
        ControlElement.IconData duels = new ControlElement.IconData("textures/hyplus/quickplay/duels.png");
        ControlElement.IconData suhc = new ControlElement.IconData("textures/hyplus/quickplay/suhc.png");




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