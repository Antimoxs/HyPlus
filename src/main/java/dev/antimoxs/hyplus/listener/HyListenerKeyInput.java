package dev.antimoxs.hyplus.listener;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.HyGeneral;
import dev.antimoxs.hyplus.modules.quickplay.HyQuickPlayMenu;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class HyListenerKeyInput {


    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {

        // Check if we are enabled.
        if (!HyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return;

        //System.out.println("KEY: " + Keyboard.getEventKey());

        // Quickplay not yet finished :(
        /*
        try {

            if (Keyboard.isKeyDown(hyPlus.hyQuickPlay.HYPLUS_QUICKPLAY_KEY.getValueInt())) {

                // TODO: cast into event system
                //System.out.println("key");
                Minecraft.getMinecraft().displayGuiScreen(new HyQuickPlayMenu());


            } else {


            }

        }
        catch (IndexOutOfBoundsException ignored) {


        }

         */

    }

}
