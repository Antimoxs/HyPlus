package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modulesOLD.quickplay.HyQuickPlayMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HyListenerKeyInput {

    private final HyPlus hyPlus;

    public HyListenerKeyInput(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        try {

            if (Keyboard.isKeyDown(hyPlus.hySettings.HYPLUS_QUICKPLAY_GUIKEY)) {

                // TODO: cast into event system
                System.out.println("key");
                Minecraft.getMinecraft().displayGuiScreen(new HyQuickPlayMenu());


            } else {


            }

        }
        catch (IndexOutOfBoundsException ignored) {


        }

    }

}
