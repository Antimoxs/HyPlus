package dev.antimoxs.hyplus.modules.quickplay;

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

import javax.naming.ldap.Control;

public class HyQuickPlayButton extends GuiButton {

        public ControlElement.IconData buttonTextures;
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

        public HyQuickPlayButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, ControlElement.IconData buttonTextures)
        {
            super(buttonId, x, y, widthIn, heightIn, buttonText);
            this.enabled = true;
            this.visible = true;
            this.id = buttonId;
            this.xPosition = x;
            this.yPosition = y;
            this.width = widthIn;
            this.height = heightIn;
            this.displayString = buttonText;
            this.buttonTextures = buttonTextures;
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
        public void drawButton(Minecraft mc, int mouseX, int mouseY)
        {
            if (this.visible)
            {

                DrawUtils draw = LabyMod.getInstance().getDrawUtils();
                FontRenderer fontrenderer = mc.fontRendererObj;
                mc.getTextureManager().bindTexture(buttonTextures.getTextureIcon());
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = this.getHoverState(this.hovered);
                //GlStateManager.enableBlend();
                //GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                //GlStateManager.blendFunc(770, 771);
                //System.out.println(this.xPosition + ", " + this.yPosition + ", " + i + ", " + this.width + ", " + this.height);
                //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width, this.height);

                draw.drawTexture(this.xPosition, this.yPosition, 0, i, 256, 128,64, 64);

                //this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, i, this.width / 2, this.height);
                //this.drawTexturedModalRect(this.xPosition + this.width / 2, this.yPosition, this.width / 2, i, this.width / 2, this.height);
                this.mouseDragged(mc, mouseX, mouseY);
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

                this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
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
