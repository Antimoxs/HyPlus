package dev.antimoxs.hyplus.objects;

import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;

public class ButtonElement extends ControlElement {

    // TODO: convert to non mc class (GuiButton)
    private final Consumer<ButtonElement> buttonListener;
    private final Color color;
    private final GuiButton button;

    public ButtonElement(String displayName, ControlElement.IconData iconData, Consumer<ButtonElement> buttonListener, String buttonText, String description, Color color) {
        super(displayName, iconData);
        this.buttonListener = buttonListener;
        this.setDescriptionText(description);
        this.button = new GuiButton(-10, 0, 0, 0, 20, buttonText);
        this.color = color;
    }

    @Override
    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        if (this.displayName != null) {
            LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, this.color.getRGB());
        }

        int buttonWidth = this.displayName == null ? maxX - x : this.mc.fontRendererObj.getStringWidth(this.button.displayString) + 20;
        this.button.setWidth(buttonWidth);
        LabyModCore.getMinecraft().setButtonXPosition(this.button, maxX - buttonWidth - 2);
        LabyModCore.getMinecraft().setButtonYPosition(this.button, y + 1);
        LabyModCore.getMinecraft().drawButton(this.button, mouseX, mouseY);
    }

    public void setEnabled(boolean enabled) {

        this.button.enabled = enabled;

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.buttonListener != null && this.button.mousePressed(this.mc, mouseX, mouseY)) {
            this.button.playPressSound(this.mc.getSoundHandler());
            this.buttonListener.accept(this);
        }

    }

    public GuiButton getButton() {

        return this.button;

    }



}
