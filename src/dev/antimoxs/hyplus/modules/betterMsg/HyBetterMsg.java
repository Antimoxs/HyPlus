package dev.antimoxs.hyplus.modules.betterMsg;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import net.labymod.gui.elements.DropDownMenu;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.DropDownElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyBetterMsg implements IHyPlusModule, IHyPlusEvent {


    // BetterMsg

    public static final HySetting HYPLUS_BETTERMSG_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_BETTERMSG_TOGGLE", "Better Msg", "Toggle prettier private messages.", true, true, Material.PAINTING);
    public static final HySetting HYPLUS_BETTERMSG_STYLE = new HySetting(HySettingType.STRING, "HYPLUS_BETTERMSG_STYLE", "Private message style", "Select the message style.", "SWITCH", "SWITCH", Material.PAPER);

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_BETTERMSG_TOGGLE.getDisplayName(), HYPLUS_BETTERMSG_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_BETTERMSG_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);

        }, HYPLUS_BETTERMSG_TOGGLE.getValueBoolean());
        toggle.setDescriptionText(HYPLUS_BETTERMSG_TOGGLE.getDescription());

        DropDownMenu<HyBetterMsgType> styleDropDown = new DropDownMenu<HyBetterMsgType>(HYPLUS_BETTERMSG_STYLE.getDisplayName(), 0, 0, 0, 0).fill(HyBetterMsgType.values());
        styleDropDown.setSelected(HyBetterMsgType.getByName(HYPLUS_BETTERMSG_STYLE.getValueString()));

        DropDownElement<HyBetterMsgType> styleElement = new DropDownElement<HyBetterMsgType>(HYPLUS_BETTERMSG_STYLE.getDisplayName(), styleDropDown);
        styleElement.setChangeListener((accept) -> {

            HYPLUS_BETTERMSG_STYLE.changeConfigValue(HyPlus.getInstance(), accept.name());
            checkConfig(false);

        });
        styleElement.setDescriptionText(HYPLUS_BETTERMSG_STYLE.getDescription());

        Settings bettermsg_sub = new Settings();
        bettermsg_sub.add(styleElement);

        toggle.setSubSettings(bettermsg_sub);

        moduleSettings.add(toggle);

        return moduleSettings;

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public String getModuleName() {
        return "BetterMsg";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_BETTERMSG_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_BETTERMSG_STYLE);

    }

    @Override
    public void onPrivateMessageSent(String message, String content) {

        String[] s2 = message.split(":");
        String name = s2[0].replaceFirst("§dTo ", "");


        StringBuilder builder = new StringBuilder();
        builder.append("§d§lPrivate: ");

        switch (HyBetterMsgType.getByName(HYPLUS_BETTERMSG_STYLE.getValueString())) {

            case ARROW: {

                builder.append("§f§l➤ ");
                builder.append(name);
                builder.append(" ");
                break;

            }
            case SWITCH: {

                builder.append("§7§lMe §f§l-> ");
                builder.append(name);
                builder.append(" ");
                break;

            }

        }


        builder.append("§d§l> §7");
        builder.append(message.substring(s2[0].length() + 4));

        HyPlus.getInstance().api.displayMessageInChat(builder.toString());

    }

    @Override
    public void onPrivateMessageReceived(String message, String content) {

        String[] s2 = message.split(":");
        String name = s2[0].replaceFirst("§dFrom ", "").trim();

        StringBuilder builder = new StringBuilder();
        builder.append("§d§lPrivate: ");

        switch (HyBetterMsgType.getByName(HYPLUS_BETTERMSG_STYLE.getValueString())) {

            case ARROW: {


                builder.append(name);
                builder.append(" §f§l➤ §d§l> §7");
                break;

            }
            case SWITCH: {

                builder.append("§7§lMe §f§l<- ");
                builder.append(name);
                builder.append(" ");
                break;

            }

        }

        builder.append("§d§l> §7");
        builder.append(message.substring((s2[0].length()) + 4));

        HyPlus.getInstance().api.displayMessageInChat(builder.toString());

    }

}
