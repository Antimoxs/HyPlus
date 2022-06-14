package dev.antimoxs.hyplus.modules.friends;

import dev.antimoxs.hypixelapiHP.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapiHP.objects.friends.FriendList;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.api.friends.HyFriendRequest;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.gui.elements.CheckBox;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyFriend implements IHyPlusModule, IHyPlusEvent {

    public static final HySetting<Boolean> HYPLUS_AUTOFRIEND_TOGGLE = new HySetting<>("HYPLUS_AUTOFRIEND", "Autofriend", "Auto-accept friend invites.", true, Material.SKULL);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_NON = new HySetting<>("HYPLUS_AUTOFRIEND_AA_NON", "Nons", "Accept invites from no-ranks.", true, Material.LEATHER_BOOTS);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_VIP = new HySetting<>("HYPLUS_AUTOFRIEND_AA_VIP", "VIPs", "Accept invites from VIPs.", true, Material.CHAINMAIL_BOOTS);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_VIPP = new HySetting<>("HYPLUS_AUTOFRIEND_AA_VIPP", "VIP+s", "Accept invites from VIP+s.", true, Material.GOLD_BOOTS);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_MVP = new HySetting<>("HYPLUS_AUTOFRIEND_AA_MVP", "MVPs", "Accept invites from MVPs.", true, Material.IRON_BOOTS);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_MVPP = new HySetting<>("HYPLUS_AUTOFRIEND_AA_MVPP", "MVP+s", "Accept invites from MVP+s.", true, Material.DIAMOND_BOOTS);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_MVPPP = new HySetting<>("HYPLUS_AUTOFRIEND_AA_MVPPP", "MVP++s", "Accept invites from MVP++s.", true, Material.NETHER_STAR);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_YT = new HySetting<>("HYPLUS_AUTOFRIEND_AA_YT", "YTs", "Accept invites from YTs.", true, Material.PAINTING);
    private static final HySetting<Boolean> HYPLUS_AUTOFRIEND_AA_STAFF = new HySetting<>("HYPLUS_AUTOFRIEND_AA_STAFF", "Staff", "Accept invites from Staff.", true, Material.COMMAND);

    private FriendList fl;

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_AUTOFRIEND_TOGGLE.getDisplayName(), HYPLUS_AUTOFRIEND_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_AUTOFRIEND_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_AUTOFRIEND_TOGGLE.getValue());
        toggle.setDescriptionText(HYPLUS_AUTOFRIEND_TOGGLE.getDescription());


        ColorPickerCheckBoxBulkElement autoAccept = new ColorPickerCheckBoxBulkElement("AutoAccept");

        CheckBox aa_non = new CheckBox("Nons", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_NON), () -> CheckBox.EnumCheckBoxValue.ENABLED, 0 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_non.setHasDefault( false );
        aa_non.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_NON.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_non.setDescription(HYPLUS_AUTOFRIEND_AA_NON.getDescription());

        CheckBox aa_vip = new CheckBox("VIPs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_VIP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 1 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vip.setHasDefault( false );
        aa_vip.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_VIP.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_vip.setDescription(HYPLUS_AUTOFRIEND_AA_VIP.getDescription());

        CheckBox aa_vipp = new CheckBox("VIP+s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_VIPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 2 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vipp.setHasDefault( false );
        aa_vipp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_VIPP.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_vipp.setDescription(HYPLUS_AUTOFRIEND_AA_VIPP.getDescription());

        CheckBox aa_mvp = new CheckBox("MVPs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 3 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvp.setHasDefault( false );
        aa_mvp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVP.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_mvp.setDescription(HYPLUS_AUTOFRIEND_AA_MVP.getDescription());

        CheckBox aa_mvpp = new CheckBox("MVP+s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 4 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvpp.setHasDefault( false );
        aa_mvpp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVPP.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_mvpp.setDescription(HYPLUS_AUTOFRIEND_AA_MVPP.getDescription());

        CheckBox aa_mvppp = new CheckBox("MVP++s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVPPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 5 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvppp.setHasDefault( false );
        aa_mvppp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVPPP.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_mvppp.setDescription(HYPLUS_AUTOFRIEND_AA_MVPPP.getDescription());

        CheckBox aa_yt = new CheckBox("YTs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_YT), () -> CheckBox.EnumCheckBoxValue.ENABLED, 6 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_yt.setHasDefault( false );
        aa_yt.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_YT.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_yt.setDescription(HYPLUS_AUTOFRIEND_AA_YT.getDescription());

        CheckBox aa_staff = new CheckBox("Staff", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_STAFF), () -> CheckBox.EnumCheckBoxValue.ENABLED, 7 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_staff.setHasDefault( false );
        aa_staff.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_STAFF.changeConfigValue(HyPlus.getInstance(), HyUtilities.validateCheckbox(accepted, 1));
            checkConfig(false);

        });
        aa_staff.setDescription(HYPLUS_AUTOFRIEND_AA_STAFF.getDescription());

        autoAccept.addCheckbox(aa_non);
        autoAccept.addCheckbox(aa_vip);
        autoAccept.addCheckbox(aa_vipp);
        autoAccept.addCheckbox(aa_mvp);
        autoAccept.addCheckbox(aa_mvpp);
        autoAccept.addCheckbox(aa_mvppp);
        autoAccept.addCheckbox(aa_yt);
        autoAccept.addCheckbox(aa_staff);

        autoAccept.setDisplayName("AutoAccept from:");

        Settings s = new Settings();
        s.add(new HeaderElement("Auto accept request from:"));
        s.add(autoAccept);

        toggle.setSubSettings(s);

        moduleSettings.add(toggle);
        //moduleSettings.add(autoAccept);

        return moduleSettings;

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public String getModuleName() {
        return "AutoFriend";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_NON);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_VIP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_VIPP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVPP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVPPP);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_YT);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_STAFF);

    }

    @Override
    public boolean onFriendRequest(HyFriendRequest request) {

        HyPlus.getInstance().displayIgMessage("HyFriend", "FA from: " + request.toString());
        // remove before release (?)

        String name = request.getName();
        String rank = request.getRank();

        if (!(rank.startsWith("[") && rank.endsWith("]"))) {

            if (HYPLUS_AUTOFRIEND_AA_NON.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }

        }
        else {

            switch (rank.toUpperCase()) {

                case "[VIP]": if (HYPLUS_AUTOFRIEND_AA_VIP.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[VIP+]": if (HYPLUS_AUTOFRIEND_AA_VIPP.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[MVP]": if (HYPLUS_AUTOFRIEND_AA_MVP.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[MVP+]": if (HYPLUS_AUTOFRIEND_AA_MVPP.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[MVP++]": if (HYPLUS_AUTOFRIEND_AA_MVPPP.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[YOUTUBE]":
                case "[PIG+++]":
                    if (HYPLUS_AUTOFRIEND_AA_YT.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                case "[HELPER]":
                case "[GM]":
                case "[MODERATOR]":
                case "[OWNER]":
                case "[ADMIN]":
                    if (HYPLUS_AUTOFRIEND_AA_STAFF.getValue()) { HyPlus.getInstance().sendMessageIngameChat("/friend accept " + name); return true; }
                default:
                    return false;

            }

        }

        return false;


    }

    public void reloadFL() {

        if (HyPlus.getInstance().hypixelApi == null) {

            HyPlus.getInstance().displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
            return;

        }

        try {
            fl = HyPlus.getInstance().hypixelApi.createFriendsRequest(String.valueOf(LabyMod.getInstance().getPlayerUUID())).getAsFriendlist();
            HyPlus.getInstance().displayIgMessage("HyFriend", "Loaded FriendList.");
        } catch (ApiRequestException e) {
            HyPlus.getInstance().displayIgMessage("HyFriend", "Failed to load FriendList.");
            fl = new FriendList();
        }



    }

    public FriendList getFriendList() { return this.fl; }

}
