package dev.antimoxs.hyplus.modules.headStats;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.hypixelapiHP.util.kvp;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyPlusConfig;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.api.events.ServerMessageEvent;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class HyPlayerTagExchanger implements IHyPlusModule, IHyPlusEvent, ServerMessageEvent {

    private int delay = 3;
    private boolean active = true;
    private HashMap<UUID, HyPlayerTag> playerTags = new HashMap<>();
    private JsonArray nextTags = new JsonArray();

    // PlayerTagChanger

    public final HySetting HYPLUS_PTC_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PTC_TOGGLE", "Player subtitles / HeadStats", "Toggle player tags", true, true, Material.NAME_TAG);
    public final HySetting HYPLUS_PTC_CHANGER = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PTC_CHANGER", "Change tags", "Iterate through tags if there are multiple ones.", true, true, Material.REDSTONE);
    public final HySetting HYPLUS_PTC_INTERVAL = new HySetting(HySettingType.INT, "HYPLUS_PTC_INTERVAL", "Tag changing interval", "Set the interval in which the tags are iterated.", 3, 3, Material.SIGN);



    public void setSubtitle(HyPlayerTag tag) { setSubtitle(tag.getUUID(), tag.getNextValue(), tag.getSize()); }
    public void setSubtitle(UUID subtitlePlayer, String value) { setSubtitle(subtitlePlayer, value, 1.0d); }
    public void setSubtitle(UUID subtitlePlayer, String value, double size) {


        //System.out.println("SAVING TAG: " + subtitlePlayer);
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

        //System.out.println("Adding TAG: " + tag.getUUID());
        if (playerTags.containsKey(tag.getUUID())) {

            //System.out.println("Already in list.");
            return tag;

        }
        else {


            playerTags.put(tag.getUUID(), tag);
            //System.out.println("Added to tags, " + playerTags.size());

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

            //System.out.println("CLEARED TAGS");

            playerTags.clear();
            addSubtitle(HyPlusConfig.antimoxs());
            //System.out.println("TAGS: " + HyPlusConfig.antimoxs().getValues().size());
            //System.out.println("RECEIVED TAGS: " + getTagForPlayer(HyPlusConfig.antimoxs().getUUID()).getValues().size());
            update(false);

        }

    }

    public HyPlayerTag getTagForPlayer(UUID uuid) {

        if (playerTags.containsKey(uuid)) {

            //System.out.println("RETURNING TAG");
            return playerTags.get(uuid);

        }
        //System.out.println("RETURNING NEW TAG");
        return addSubtitle(new HyPlayerTag(uuid, 1.6d));

    }

    @Override
    public String getModuleName() {
        return "HyPlayerSubtitles";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PTC_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PTC_CHANGER);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PTC_INTERVAL);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_PTC_TOGGLE.getDisplayName(), HYPLUS_PTC_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_PTC_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            if (!booleanElement) emptySubtitles();
            update(!booleanElement);


        }, HYPLUS_PTC_TOGGLE.getValueBoolean());
        toggle.setDescriptionText(HYPLUS_PTC_TOGGLE.getDescription());
        BooleanElement changer_toggle = new BooleanElement(HYPLUS_PTC_CHANGER.getDisplayName(), HYPLUS_PTC_CHANGER.getIcon(), (booleanElement) -> {

            HYPLUS_PTC_CHANGER.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_PTC_CHANGER.getValueBoolean());
        changer_toggle.setDescriptionText(HYPLUS_PTC_CHANGER.getDescription());

        NumberElement changer_interval = new NumberElement(HYPLUS_PTC_INTERVAL.getDisplayName(), HYPLUS_PTC_INTERVAL.getIcon(), HYPLUS_PTC_INTERVAL.getValueInt());
        changer_interval.setMinValue(1); // 1s
        changer_interval.setMaxValue(600); // 10min
        changer_interval.addCallback((accepted) -> {

            HYPLUS_PTC_INTERVAL.changeConfigValue(HyPlus.getInstance(), accepted);
            checkConfig(false);
            delay = 0;

        });
        changer_interval.setDescriptionText(HYPLUS_PTC_INTERVAL.getDescription());

        ButtonElement resetCache = new ButtonElement("Reset Cache",
                new ControlElement.IconData(Material.BARRIER),
                new Consumer<ButtonElement>() {
                    @Override
                    public void accept(ButtonElement buttonElement) {

                        checkConfig(false);
                        emptySubtitles();
                        update(true);
                        active = HYPLUS_PTC_TOGGLE.getValueBoolean();

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
        toggle.setSettingEnabled(true);

        moduleSettings.add(toggle);

        return moduleSettings;

    }

    private void update(boolean clearAfter) {

        Thread updater = new Thread(() -> {

            nextTags = new JsonArray();

            //System.out.println("UPDATING TAGS: " + playerTags.size());
            for (UUID uuid : playerTags.keySet()) {

                setSubtitle(uuid, HYPLUS_PTC_CHANGER.getValueBoolean() ? playerTags.get(uuid).getNextValue() : playerTags.get(uuid).getStaticValue(), playerTags.get(uuid).getSize());

            }

            // Send to LabyMod using the API
            HyPlus.getInstance().getApi().getUserManager().onServerMessage("account_subtitle", nextTags);

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

            emptySubtitles();
            update(true);

        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();
    }

    @Override
    public boolean loop() {

        if (active && HYPLUS_PTC_TOGGLE.getValueBoolean()) {

            if (delay <= 0) {

                update(false);
                delay = HYPLUS_PTC_INTERVAL.getValueInt();

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
