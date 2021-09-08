package dev.antimoxs.hyplus.modules.playerTagCycle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.HyPlayerTag;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyPlusConfig;
import net.labymod.api.events.ServerMessageEvent;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.NumberElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HyPlayerTagExchanger implements IHyPlusModule, IHyPlusEvent, ServerMessageEvent {

    private HyPlus hyPlus;
    private int delay = 3;
    private boolean active = true;
    private HashMap<UUID, HyPlayerTag> playerTags = new HashMap<>();
    private JsonArray nextTags = new JsonArray();

    // PlayerTagChanger
    public boolean HYPLUS_PTC_TOGGLE = true;
    public boolean HYPLUS_PTC_CHANGER = true;
    public int HYPLUS_PTC_INTERVAL = 3;

    public HyPlayerTagExchanger(HyPlus HyPlus) {

        this.hyPlus = HyPlus;

    }


    public void setSubtitle(HyPlayerTag tag) { setSubtitle(tag.getUUID(), tag.getNextValue(), tag.getSize()); }
    public void setSubtitle(UUID subtitlePlayer, String value) { setSubtitle(subtitlePlayer, value, 1.0d); }
    public void setSubtitle(UUID subtitlePlayer, String value, double size) {


        System.out.println("SAVING TAG: " + subtitlePlayer);
        // List of all subtitles

        // Add subtitle
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty("uuid", subtitlePlayer.toString());

        // Optional: Size of the subtitle
        subtitle.addProperty("size", size); // Range is 0.8 - 1.6 (1.6 is Minecraft default)

        // no value = remove the subtitle
        if(value != null) subtitle.addProperty("value", value);

        // You can set multiple subtitles in one packet
        nextTags.add(subtitle);


    }

    public HyPlayerTag addSubtitle(HyPlayerTag tag) {

        System.out.println("Adding TAG: " + tag.getUUID());
        if (playerTags.containsKey(tag.getUUID())) {

            System.out.println("Already in list.");
            return tag;

        }
        else {


            playerTags.put(tag.getUUID(), tag);
            System.out.println("Added to tags, " + playerTags.size());

        }
        return tag;

    }

    public void removeSubtitle(HyPlayerTag tag) {

        removeSubtitle(tag.getUUID());

    }
    public void removeSubtitle(UUID uuid) {

        if (playerTags.containsKey(uuid)) {

            playerTags.remove(uuid);

        }

    }

    public void emptySubtitles() {

        for (HyPlayerTag tag : playerTags.values()) {

            tag.clearValues();

        }

    }
    public void clearSubtitles() {

        if (!playerTags.isEmpty()) {

            System.out.println("CLEARED TAGS");
            playerTags.clear();
            addSubtitle(HyPlusConfig.antimoxs());
            System.out.println("TAGS: " + HyPlusConfig.antimoxs().getValues().size());
            System.out.println("RECEIVED TAGS: " + getTagForPlayer(HyPlusConfig.antimoxs().getUUID()).getValues().size());
            update(false);

        }

    }

    public HyPlayerTag getTagForPlayer(UUID uuid) {

        if (playerTags.containsKey(uuid)) {

            System.out.println("RETURNING TAG");
            return playerTags.get(uuid);

        }
        System.out.println("RETURNING NEW TAG");
        return addSubtitle(new HyPlayerTag(uuid, 1.6d));

    }

    @Override
    public String getModuleName() {
        return "HyPlayerTagChanger";
    }

    @Override
    public void checkConfig(boolean reset) {

        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_PTC_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_PTC_CHANGER", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_PTC_INTERVAL", 3);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("HyPlus Player Subtitles", hyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_PTC_TOGGLE", true);
        SettingsElement changer_toggle = new BooleanElement("Player Subtitle changer", hyPlus, new ControlElement.IconData(Material.PAPER), "HYPLUS_PTC_CHANGER", true);

        NumberElement changer_interval = new NumberElement( "Update interval", new ControlElement.IconData( Material.TRIPWIRE_HOOK) , HYPLUS_PTC_INTERVAL);
        changer_interval.setMinValue(1); // 1s
        changer_interval.setMaxValue(600); // 10min
        changer_interval.addCallback( new Consumer<Integer>() {
            @Override
            public void accept( Integer accepted ) {
                hyPlus.hyConfigManager.changeConfigValue("HYPLUS_PTC_INTERVAL", accepted);
                HYPLUS_PTC_INTERVAL = accepted;
                hyPlus.hyPlayerTagExchanger.delay = 0;
            }
        });

        ButtonElement resetCache = new ButtonElement("Reset Cache",
                new ControlElement.IconData(Material.BARRIER),
                new Consumer<ButtonElement>() {
                    @Override
                    public void accept(ButtonElement buttonElement) {

                        emptySubtitles();
                        update(true);

                    }
                },
                "Clear",
                "Clear current subtitle cache.",
                Color.ORANGE
        );

        Settings changer_sub = new Settings();
        changer_sub.add(changer_toggle);
        changer_sub.add(changer_interval);
        changer_sub.add(resetCache);
        toggle.setSubSettings(changer_sub);

        moduleSettings.add(toggle);

        return moduleSettings;

    }

    private void update(boolean clearAfter) {

        Thread updater = new Thread(() -> {

            nextTags = new JsonArray();

            System.out.println("UPDATING TAGS: " + playerTags.size());
            for (UUID uuid : playerTags.keySet()) {

                setSubtitle(uuid, playerTags.get(uuid).getNextValue(), playerTags.get(uuid).getSize());

            }

            // Send to LabyMod using the API
            hyPlus.getApi().getUserManager().onServerMessage("account_subtitle", nextTags);

            if (clearAfter) clearSubtitles();

        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();

    }

    @Override
    public void onHypixelJoin() {
        active = true;
    }

    @Override
    public void onHypixelQuit() {
        active = false;
        Thread updater = new Thread(() -> {

            for (UUID uuid : playerTags.keySet()) {

                setSubtitle(uuid, null, playerTags.get(uuid).getSize());

            }

        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();
    }

    @Override
    public boolean loop() {

        active = HYPLUS_PTC_TOGGLE;

        if (active) {

            if (delay <= 0) {

                update(false);
                delay = HYPLUS_PTC_INTERVAL;

            }
            else {

                delay--;

            }

        }

        return true;

    }


    // could maybe be removed
    @Override
    public void onServerMessage(String s, JsonElement jsonElement) {

        if (!active) return;
        if (s.equals("account_subtitle")) {

            try {
                JsonArray jsonArray = jsonElement.getAsJsonArray();

                for(int i = 0; i < jsonArray.size(); ++i) {
                    JsonObject accountEntry = jsonArray.get(i).getAsJsonObject();
                    if (accountEntry.has("uuid")) {
                        UUID uuid = UUID.fromString(accountEntry.get("uuid").getAsString());
                        String subTitle = accountEntry.has("value") ? accountEntry.get("value").getAsString() : null;
                        if (subTitle != null) {
                            subTitle = ModColor.createColors(subTitle);
                        }

                        double subTitleSize = accountEntry.has("size") ? accountEntry.get("size").getAsDouble() : 0.5D;
                        if (subTitleSize < 0.8D) {
                            subTitleSize = 0.8D;
                        }

                        if (subTitleSize > 1.6D) {
                            subTitleSize = 1.6D;
                        }


                        getTagForPlayer(uuid).removeValueById(0);

                        if (subTitle != null) {
                            kvp kvp = new kvp(subTitle, 0);
                            getTagForPlayer(uuid).addValue(kvp);
                        }

                    }
                }
            } catch (Exception var12) {
                var12.printStackTrace();
            }
        }

    }

}
