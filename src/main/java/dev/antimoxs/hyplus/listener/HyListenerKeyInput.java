package dev.antimoxs.hyplus.listener;

import dev.antimoxs.hyplus.modules.HyGeneral;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class HyListenerKeyInput {


    // TODO: convert to non mc class (SubscribeEvent,InputEvent)
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
