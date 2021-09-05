package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class HyListenerQuit implements Consumer<ServerData> {

    private final HyPlus hyPlus;

    public HyListenerQuit(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public void accept(ServerData serverData) {

        hyPlus.hyEventManager.callHypixelQuit();
        //hyPlus.hyGameDetector.presenceCheck(false);
        //HyPlus.hyGameDetector.sendTabList();

    }

}
