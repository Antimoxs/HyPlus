package dev.antimoxs.hyplus.modules.quests;

import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.DrawUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class HyTrackboxBox extends GuiButton {

    // TODO: convert to non mc class (net.minecraft.*)
    public ControlElement.IconData bg = new ControlElement.IconData("textures/hyplus/challenge_background.png");
    public ControlElement.IconData linebg = new ControlElement.IconData("textures/hyplus/challenge_linebg.png");
    public ControlElement.IconData line = new ControlElement.IconData("textures/hyplus/challenge_line.png");
    /** Button width in pixels */
    public int width;
    /** Button height in pixels */
    public int height;
    /** The x position of this control. */
    public int xPosition;
    /** The y position of this control. */
    public int yPosition;
    /** The string displayed on this control. */
    public String displayString;
    public int id;
    /** True if this control is enabled, false to disable. */
    public boolean enabled;
    /** Hides the button completely if false. */
    public boolean visible;
    protected boolean hovered;
    public int packedFGColour; //FML


    public HyTrackboxBox(int buttonId, int x, int y, int widthIn, String buttonText)
    {
        super(buttonId, x, y, widthIn, 18, buttonText);
        this.enabled = true;
        this.visible = true;
        this.id = buttonId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = widthIn;
        this.height = 18;
        this.displayString = buttonText;



    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver)
    {
        int i = 0;

        if (!this.enabled)
        {
            i = 0;
        }
        else if (mouseOver)
        {
            i = this.height*2;
        }

        return i;
    }

    /**
     * Draws this button to the screen.
     */
    public void drawButton(Minecraft mc, int mouseX, int mouseY, int percent)
    {
        if (this.visible)
        {

            /*
            System.out.println("W: " + this.width);
            System.out.println("P: " + percent);
            System.out.println("%: " + (int)(((float)this.width/100)*percent));
            */


            FontRenderer fontrenderer = mc.fontRendererObj;
            mc.getTextureManager().bindTexture(buttonTextures);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);


            //draw.drawRectangle(this.xPosition, this.yPosition, this.xPosition + width, this.yPosition+height, 7039851);

            DrawUtils draw = LabyMod.getInstance().getDrawUtils();
            draw.bindTexture(this.bg.getTextureIcon());
            draw.drawTexture(this.xPosition, this.yPosition, 0, 0, width, height,width, height);

            DrawUtils draw2 = LabyMod.getInstance().getDrawUtils();
            draw2.bindTexture(this.linebg.getTextureIcon());
            draw2.drawTexture(this.xPosition, this.yPosition, this.width, 3, this.width, 3);

            int widthLine = (int)(((float)this.width/100)*percent);
            DrawUtils draw3 = LabyMod.getInstance().getDrawUtils();
            draw3.bindTexture(this.line.getTextureIcon());
            draw3.drawTexture(this.xPosition, this.yPosition, widthLine, 3, widthLine, 3);

            //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width / 2, this.height);
            //this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, this.width / 2, i, this.width / 2, this.height);
            //this.mouseDragged(mc, mouseX, mouseY);
            int j = 14737632;

            if (packedFGColour != 0)
            {
                j = packedFGColour;
            }
            else
            if (!this.enabled)
            {
                j = 10526880;
            }
            else if (this.hovered)
            {
                j = 16777120;
            }

            //this.drawString(fontrenderer, widthLine + "", this.xPosition + 100, this.yPosition + 10, j);
            this.drawString(fontrenderer, this.displayString, this.xPosition + 5, this.yPosition + height/3, j);
        }
    }

    /**
     * Fired when the mouse button is dragged. Equivalent of MouseListener.mouseDragged(MouseEvent e).
     */
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int mouseX, int mouseY)
    {
    }

    /**
     * Returns true if the mouse has been pressed on this control. Equivalent of MouseListener.mousePressed(MouseEvent
     * e).
     */
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
    }

    /**
     * Whether the mouse cursor is currently over the button.
     */
    public boolean isMouseOver()
    {
        return this.hovered;
    }

    public void drawButtonForegroundLayer(int mouseX, int mouseY)
    {
    }

    public void playPressSound(SoundHandler soundHandlerIn)
    {
        soundHandlerIn.playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
    }

    public int getButtonWidth()
    {
        return this.width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

}
