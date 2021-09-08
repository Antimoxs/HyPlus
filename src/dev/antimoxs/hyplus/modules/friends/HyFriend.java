package dev.antimoxs.hyplus.modules.friends;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.friends.FriendList;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
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
    public boolean HYPLUS_AUTOFRIEND_TOGGLE = true;

    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_NON = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_VIP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_VIPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_MVPPP = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_YT = CheckBox.EnumCheckBoxValue.ENABLED;
    public CheckBox.EnumCheckBoxValue HYPLUS_AUTOFRIEND_AA_STAFF = CheckBox.EnumCheckBoxValue.ENABLED;

    public HyFriend(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("Autofriend", hyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_AUTOFRIEND_TOGGLE", true);


        ColorPickerCheckBoxBulkElement autoAccept = new ColorPickerCheckBoxBulkElement("AutoAccept");

        CheckBox aa_non = new CheckBox("Nons", HYPLUS_AUTOFRIEND_AA_NON, () -> CheckBox.EnumCheckBoxValue.ENABLED, 0 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_non.setHasDefault( false );
        aa_non.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_NON = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_NON");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_NON", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_vip = new CheckBox("VIPs", HYPLUS_AUTOFRIEND_AA_VIP, () -> CheckBox.EnumCheckBoxValue.ENABLED, 1 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vip.setHasDefault( false );
        aa_vip.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_VIP = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_VIP");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_VIP", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_vipp = new CheckBox("VIP+s", HYPLUS_AUTOFRIEND_AA_VIPP, () -> CheckBox.EnumCheckBoxValue.ENABLED, 2 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_vipp.setHasDefault( false );
        aa_vipp.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_VIPP = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_VIPP");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_VIPP", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_mvp = new CheckBox("MVPs", HYPLUS_AUTOFRIEND_AA_MVP, () -> CheckBox.EnumCheckBoxValue.ENABLED, 3 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvp.setHasDefault( false );
        aa_mvp.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_MVP = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_MVP");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_MVP", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_mvpp = new CheckBox("MVP+s", HYPLUS_AUTOFRIEND_AA_MVPP, () -> CheckBox.EnumCheckBoxValue.ENABLED, 4 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvpp.setHasDefault( false );
        aa_mvpp.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_MVPP = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_MVPP");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_MVPP", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_mvppp = new CheckBox("MVP++s", HYPLUS_AUTOFRIEND_AA_MVPPP, () -> CheckBox.EnumCheckBoxValue.ENABLED, 5 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_mvppp.setHasDefault( false );
        aa_mvppp.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_MVPPP = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_MVPPP");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_MVPPP", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_yt = new CheckBox("YTs", HYPLUS_AUTOFRIEND_AA_YT, () -> CheckBox.EnumCheckBoxValue.ENABLED, 6 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_yt.setHasDefault( false );
        aa_yt.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_YT = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_YT");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_YT", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

        CheckBox aa_staff = new CheckBox("Staff", HYPLUS_AUTOFRIEND_AA_STAFF, () -> CheckBox.EnumCheckBoxValue.ENABLED, 7 /* x */, 0 /* y */, 0  /* width */, 0 /* height */ );
        aa_staff.setHasDefault( false );
        aa_staff.setUpdateListener( new Consumer<CheckBox.EnumCheckBoxValue>() {
            @Override
            public void accept( CheckBox.EnumCheckBoxValue accepted ) {
                HYPLUS_AUTOFRIEND_AA_STAFF = accepted;
                hyPlus.getConfig().remove("HYPLUS_AUTOFRIEND_AA_STAFF");
                hyPlus.getConfig().addProperty("HYPLUS_AUTOFRIEND_AA_STAFF", HyUtilities.validateCheckbox(accepted));
                hyPlus.loadConfig();
            }
        });

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

        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_TOGGLE", true);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_NON", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_VIP", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_VIPP", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVP", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVPP", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVPPP", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_YT", 0);
        hyPlus.hyConfigManager.checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_STAFF", 0);

    }

    @Override
    public boolean onFriendRequest(HyFriendRequest request) {

        hyPlus.displayIgMessage("HyFriend", "FA from: " + request.toString());
        // remove before release (?)

        String name = request.getName();
        String rank = request.getRank();

        if (!(rank.startsWith("[") && rank.endsWith("]"))) {

            if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_NON, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;

        }
        else {

            switch (rank.toUpperCase()) {

                case "[VIP]": if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_VIP, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[VIP+]": if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_VIPP, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP]": if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_MVP, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP+]": if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_MVPP, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[MVP++]": if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_MVPPP, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[YOUTUBE]":
                case "[PIG+++]":
                    if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_YT, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;
                case "[HELPER]":
                case "[MODERATOR]":
                case "[OWNER]":
                case "[ADMIN]":
                    if (HyUtilities.validateCheckbox(HYPLUS_AUTOFRIEND_AA_STAFF, 0, 1)) hyPlus.sendMessageIngameChat("/friend accept " + name); return true;

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
