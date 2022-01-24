package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class HyListenerQuit implements Consumer<ServerData> {


    @Override
    public void accept(ServerData serverData) {

        HyPlus.getInstance().hyEventManager.callHypixelQuit();
        //hyPlus.hyGameDetector.presenceCheck(false);
        //HyPlus.hyGameDetector.sendTabList();

    }

}
