package dev.antimoxs.hyplus.modules;

import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySetting;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyTablist implements IHyPlusModule, IHyPlusEvent {

    /**
     * Updates the tablist banner to the HyPlus tablist.
     *
     */

    public static final HySetting<Boolean> HYPLUS_TL_DISPLAY = new HySetting<>("HYPLUS_TL_DISPLAY", "Toggle Hypixel Logo", "Toggle whether the Hypixel logo should be displayed above the tablist.", true, Material.FEATHER);

    private static final String BANNER_LINK_1 = "https://i.imgur.com/nZttT7W.png";
    private static final String BANNER_LINK_2 = "https://i.imgur.com/1lydzgc.png";

    private static final String BANNER_LINK_3 = "https://i.imgur.com/Zjdiglg.png"; // Hypixel logo only :)

    public void sendTabList() {

        if (!HYPLUS_TL_DISPLAY.getValue()) return;

        // DEBUG System.out.println("[HyTablist] Sending HyPlus tab-banner.");
        // https://i.imgur.com/VyPn1Sm.png
        JsonObject object2 = new JsonObject();
        object2.addProperty("url", BANNER_LINK_3);
        LabyMod.getInstance().getEventManager().callServerMessage("server_banner", object2);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        ArrayList<SettingsElement> settings = new ArrayList<>();

        BooleanElement tablist = new BooleanElement(HYPLUS_TL_DISPLAY.getDisplayName(), HyPlus.getInstance(), HYPLUS_TL_DISPLAY.getIcon(), HYPLUS_TL_DISPLAY.getConfigName(), HYPLUS_TL_DISPLAY.getDefault());
        tablist.setDescriptionText(HYPLUS_TL_DISPLAY.getDescription());

        settings.add(tablist);

        return settings;

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        if (!HYPLUS_TL_DISPLAY.getValue()) return;
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
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_TL_DISPLAY);
    }
}
