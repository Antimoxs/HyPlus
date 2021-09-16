package dev.antimoxs.hyplus.modules.friends;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.friends.FriendList;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.gui.elements.CheckBox;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyFriend implements IHyPlusModule, IHyPlusEvent {

    private HyPlus hyPlus;


    private FriendList fl;

    // AutoFriendModule
    //public boolean HYPLUS_AUTOFRIEND_TOGGLE = true;
/*
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_NON = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_VIP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_VIPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVPPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_YT = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_STAFF = CheckBox.EnumCheckBoxValue.ENABLED;


 */
    public final HySetting HYPLUS_AUTOFRIEND_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND", "Autofriend", "Auto-accept friend invites.", true, true, Material.SKULL);
    private final HySetting HYPLUS_AUTOFRIEND_AA_NON = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_NON", "Nons", "Accept invites from no-ranks.", true, true, Material.LEATHER_BOOTS);
    private final HySetting HYPLUS_AUTOFRIEND_AA_VIP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_VIP", "VIPs", "Accept invites from VIPs.", true, true, Material.CHAINMAIL_BOOTS);
    private final HySetting HYPLUS_AUTOFRIEND_AA_VIPP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_VIPP", "VIP+s", "Accept invites from VIP+s.", true, true, Material.GOLD_BOOTS);
    private final HySetting HYPLUS_AUTOFRIEND_AA_MVP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_MVP", "MVPs", "Accept invites from MVPs.", true, true, Material.IRON_BOOTS);
    private final HySetting HYPLUS_AUTOFRIEND_AA_MVPP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_MVPP", "MVP+s", "Accept invites from MVP+s.", true, true, Material.DIAMOND_BOOTS);
    private final HySetting HYPLUS_AUTOFRIEND_AA_MVPPP = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_MVPPP", "MVP++s", "Accept invites from MVP++s.", true, true, Material.NETHER_STAR);
    private final HySetting HYPLUS_AUTOFRIEND_AA_YT = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_YT", "YTs", "Accept invites from YTs.", true, true, Material.PAINTING);
    private final HySetting HYPLUS_AUTOFRIEND_AA_STAFF = new HySetting(HySettingType.BOOLEAN, "HYPLUS_AUTOFRIEND_AA_STAFF", "Staff", "Accept invites from Staff.", true, true, Material.COMMAND);

    public HyFriend(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_AUTOFRIEND_TOGGLE.getDisplayName(), HYPLUS_AUTOFRIEND_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_AUTOFRIEND_TOGGLE.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);

        }, HYPLUS_AUTOFRIEND_TOGGLE.getValueBoolean());
        toggle.setDescriptionText(HYPLUS_AUTOFRIEND_TOGGLE.getDescription());


        ColorPickerCheckBoxBulkElement autoAccept = new ColorPickerCheckBoxBulkElement("AutoAccept");

        CheckBox aa_non = new CheckBox("Nons", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_NON), () -> CheckBox.EnumCheckBoxValue.ENABLED, 0 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_non.setHasDefault( false );
        aa_non.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_NON.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_non.setDescription(HYPLUS_AUTOFRIEND_AA_NON.getDescription());

        CheckBox aa_vip = new CheckBox("VIPs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_VIP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 1 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vip.setHasDefault( false );
        aa_vip.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_VIP.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_vip.setDescription(HYPLUS_AUTOFRIEND_AA_VIP.getDescription());

        CheckBox aa_vipp = new CheckBox("VIP+s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_VIPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 2 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vipp.setHasDefault( false );
        aa_vipp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_VIPP.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_vipp.setDescription(HYPLUS_AUTOFRIEND_AA_VIPP.getDescription());

        CheckBox aa_mvp = new CheckBox("MVPs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 3 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvp.setHasDefault( false );
        aa_mvp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVP.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_mvp.setDescription(HYPLUS_AUTOFRIEND_AA_MVP.getDescription());

        CheckBox aa_mvpp = new CheckBox("MVP+s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 4 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvpp.setHasDefault( false );
        aa_mvpp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVPP.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_mvpp.setDescription(HYPLUS_AUTOFRIEND_AA_MVPP.getDescription());

        CheckBox aa_mvppp = new CheckBox("MVP++s", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_MVPPP), () -> CheckBox.EnumCheckBoxValue.ENABLED, 5 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvppp.setHasDefault( false );
        aa_mvppp.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_MVPPP.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_mvppp.setDescription(HYPLUS_AUTOFRIEND_AA_MVPPP.getDescription());

        CheckBox aa_yt = new CheckBox("YTs", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_YT), () -> CheckBox.EnumCheckBoxValue.ENABLED, 6 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_yt.setHasDefault( false );
        aa_yt.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_YT.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
            checkConfig(false);

        });
        aa_yt.setDescription(HYPLUS_AUTOFRIEND_AA_YT.getDescription());

        CheckBox aa_staff = new CheckBox("Staff", HyUtilities.vCheckbox(HYPLUS_AUTOFRIEND_AA_STAFF), () -> CheckBox.EnumCheckBoxValue.ENABLED, 7 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_staff.setHasDefault( false );
        aa_staff.setUpdateListener((accepted) -> {

            HYPLUS_AUTOFRIEND_AA_STAFF.changeConfigValue(hyPlus, HyUtilities.validateCheckbox(accepted));
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

        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_TOGGLE);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_NON);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_VIP);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_VIPP);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVP);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVPP);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_MVPPP);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_YT);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_AUTOFRIEND_AA_STAFF);

    }

    @Override
    public boolean onFriendRequest(HyFriendRequest request) {

        hyPlus.displayIgMessage("HyFriend", "FA from: " + request.toString());
        // remove before release (?)

        String name = request.getName();
        String rank = request.getRank();

        if (!(rank.startsWith("[") && rank.endsWith("]"))) {

            if (HYPLUS_AUTOFRIEND_AA_NON.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;

        }
        else {

            switch (rank.toUpperCase()) {

                case "[VIP]": if (HYPLUS_AUTOFRIEND_AA_VIP.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[VIP+]": if (HYPLUS_AUTOFRIEND_AA_VIPP.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP]": if (HYPLUS_AUTOFRIEND_AA_MVP.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP+]": if (HYPLUS_AUTOFRIEND_AA_MVPP.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP++]": if (HYPLUS_AUTOFRIEND_AA_MVPPP.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[YOUTUBE]":
                case "[PIG+++]":
                    if (HYPLUS_AUTOFRIEND_AA_YT.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[HELPER]":
                case "[MODERATOR]":
                case "[OWNER]":
                case "[ADMIN]":
                    if (HYPLUS_AUTOFRIEND_AA_STAFF.getValueBoolean()) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;

            }

        }

        return false;

    }

    public void reloadFL() {

        if (hyPlus.tbcHypixelApi == null) {

            hyPlus.displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
            return;

        }

        try {
            fl = hyPlus.tbcHypixelApi.createFriendsRequest(String.valueOf(LabyMod.getInstance().getPlayerUUID())).getAsFriendlist();
            hyPlus.displayIgMessage("HyFriend", "Loaded FriendList.");
        } catch (ApiRequestException e) {
            hyPlus.displayIgMessage("HyFriend", "Failed to load FriendList.");
            fl = new FriendList();
        }



    }

}
