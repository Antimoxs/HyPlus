package dev.antimoxs.hyplus.modules;

import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import dev.antimoxs.utilities.time.wait;
import net.labymod.main.LabyMod;
import net.minecraft.network.play.server.S01PacketJoinGame;

public class HyTablist implements IHyPlusModule, IHyPlusEvent {

    private final HyPlus hyPlus;

    public HyTablist(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }


    /**
     * Updates the tablist banner to the HyPlus tablist.
     *
     */
    public void sendTabList() {

        System.out.println("[HyTablist] Sending HyPlus tab-banner.");
        // https://i.imgur.com/VyPn1Sm.png
        JsonObject object2 = new JsonObject();
        object2.addProperty("url", "https://i.imgur.com/nZttT7W.png");
        LabyMod.getInstance().getEventManager().callServerMessage("server_banner", object2);

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        Thread tablistIMG = new Thread(this::sendTabList);
        Runtime.getRuntime().addShutdownHook(tablistIMG);
        tablistIMG.start();

    }

    @Override
    public String getModuleName() {

        return "HyTablist";

    }

}
