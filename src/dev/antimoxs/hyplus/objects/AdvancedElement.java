package dev.antimoxs.hyplus.objects;

import net.labymod.api.permissions.Permissions;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.Module;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.LabyModModuleEditorGui;
import net.labymod.settings.elements.ControlElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.DrawUtils;
import net.labymod.utils.ModColor;
import net.labymod.utils.manager.TooltipHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

import java.util.Iterator;
import java.util.List;

public class AdvancedElement extends ControlElement {

    protected ControlElement.IconData iconData;
    private GuiButton buttonAdvanced;


    public AdvancedElement(String displayName, String configEntryName, IconData iconData) {
        super(displayName, configEntryName, iconData);
        this.iconData = iconData;
        createButton();
    }

    private void createButton() {
        this.buttonAdvanced = new GuiButton(-2, 80, 0, 23, 20, "");
    }


    @Override
    public GuiButton getButtonAdvanced() {
        return buttonAdvanced;
    }


    private void renderAdvancedButton(int x, int y, int maxX, int maxY, boolean mouseOver, int mouseX, int mouseY) {
        if (this.hasSubList()) {
            if (buttonAdvanced != null) {
                boolean enabled;
                if (this.isModule()) {
                    enabled = false;
                } else {
                    enabled = true;
                }

                LabyModCore.getMinecraft().setButtonXPosition(this.buttonAdvanced, maxX - this.getSubListButtonWidth() - 2);
                LabyModCore.getMinecraft().setButtonYPosition(this.buttonAdvanced, y + 1);
                this.buttonAdvanced.enabled = enabled;
                LabyModCore.getMinecraft().drawButton(this.buttonAdvanced, mouseX, mouseY);
                this.mc.getTextureManager().bindTexture(ModTextures.BUTTON_ADVANCED);
                GlStateManager.enableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, enabled ? 1.0F : 0.2F);
                LabyMod.getInstance().getDrawUtils().drawTexture((double)(LabyModCore.getMinecraft().getXPosition(this.buttonAdvanced) + 4), (double)(LabyModCore.getMinecraft().getYPosition(this.buttonAdvanced) + 3), 0.0D, 0.0D, 256.0D, 256.0D, 14.0D, 14.0D, 2.0F);

            }
        }
    }

    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        //super.draw(x, y, maxX, maxY, mouseX, mouseY);
        this.mouseOver = mouseX > x && mouseX < maxX && mouseY > y && mouseY < maxY;
        if (this.displayName != null) {
            LabyMod.getInstance().getDrawUtils().drawRectangle(x, y, maxX, maxY, ModColor.toRGB(80, 80, 80, 60));
            int iconWidth = this.iconData != null ? 25 : 2;
            if (this.iconData != null) {
                if (this.iconData.hasTextureIcon()) {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(this.iconData.getTextureIcon());
                    LabyMod.getInstance().getDrawUtils().drawTexture((double)(x + 3), (double)(y + 3), 256.0D, 256.0D, 16.0D, 16.0D);
                } else if (this.iconData.hasMaterialIcon()) {
                    LabyMod.getInstance().getDrawUtils().drawItem(this.iconData.getMaterialIcon().createItemStack(), (double)(x + 3), (double)(y + 2), (String)null);
                }
            }

            List<String> list = LabyMod.getInstance().getDrawUtils().listFormattedStringToWidth(this.getDisplayName().isEmpty() ? ModColor.cl("4") + "Unknown" : this.getDisplayName(), maxX - (x + iconWidth) - this.getObjectWidth() - 5 - (this.hasSubList() ? iconWidth : 0));
            int listY = y + 7 - ((list.size() > 2 ? 2 : list.size()) - 1) * 5;
            int i = 0;
            Iterator var11 = list.iterator();

            while(var11.hasNext()) {
                String line = (String)var11.next();
                LabyMod.getInstance().getDrawUtils().drawString(line, (double)(x + iconWidth), (double)listY);
                listY += 10;
                ++i;
                if (i > 1) {
                    break;
                }
            }

            this.renderAdvancedButton(x, y, maxX, maxY, this.mouseOver, mouseX, mouseY);


        }

    }

}
