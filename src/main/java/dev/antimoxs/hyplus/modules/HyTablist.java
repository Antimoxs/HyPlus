package dev.antimoxs.hyplus.modules;

import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import net.labymod.main.LabyMod;

public class HyTablist implements IHyPlusModule, IHyPlusEvent {

    /**
     * Updates the tablist banner to the HyPlus tablist.
     *
     */

    private static final String BANNER_LINK_1 = "https://i.imgur.com/nZttT7W.png";
    private static final String BANNER_LINK_2 = "https://i.imgur.com/1lydzgc.png";

    private static final String BANNER_LINK_3 = "https://i.imgur.com/Zjdiglg.png"; // Hypixel logo only :)

    public void sendTabList() {

        // DEBUG System.out.println("[HyTablist] Sending HyPlus tab-banner.");
        // https://i.imgur.com/VyPn1Sm.png
        JsonObject object2 = new JsonObject();
        object2.addProperty("url", BANNER_LINK_3);
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

    @Override
    public void checkConfig(boolean reset) {

    }
}
