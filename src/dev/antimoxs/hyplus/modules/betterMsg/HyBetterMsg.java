package dev.antimoxs.hyplus.modules.betterMsg;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
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

    private final HyPlus hyPlus;

    public HyBetterMsg(HyPlus hyPlus) {

        this.hyPlus = hyPlus;

    }

    // BetterMsg
    public boolean HYPLUS_BETTERMSG_TOGGLE = true;
    public HyBetterMsgType HYPLUS_BETTERMSG_STYLE = HyBetterMsgType.SWITCH;

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("Better MSG", hyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_BETTERMSG_TOGGLE", true);

        final DropDownMenu<HyBetterMsgType> styleDropDown = new DropDownMenu<HyBetterMsgType>( "Private message style" /* Display name */, 0, 0, 0, 0 ).fill(HyBetterMsgType.values());
        styleDropDown.setSelected(HYPLUS_BETTERMSG_STYLE);

        DropDownElement<HyBetterMsgType> element = new DropDownElement<HyBetterMsgType>( "Select style", styleDropDown );
        element.setChangeListener( new Consumer<HyBetterMsgType>() {
            @Override
            public void accept( HyBetterMsgType alignment ) {
                hyPlus.changeConfigValue("HYPLUS_BETTERMSG_STYLE", alignment.name());
                hyPlus.loadConfig();
            }
        });

        Settings bettermsg_sub = new Settings();
        bettermsg_sub.add(element);

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
    public void onPrivateMessageSent(String message, String content) {

        String[] s2 = message.split(":");
        String name = s2[0].replaceFirst("§dTo ", "");


        StringBuilder builder = new StringBuilder();
        builder.append("§d§lPrivate: ");

        switch (HYPLUS_BETTERMSG_STYLE) {

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

        hyPlus.api.displayMessageInChat(builder.toString());

    }

    @Override
    public void onPrivateMessageReceived(String message, String content) {

        String[] s2 = message.split(":");
        String name = s2[0].replaceFirst("§dFrom ", "").trim();

        StringBuilder builder = new StringBuilder();
        builder.append("§d§lPrivate: ");

        switch (HYPLUS_BETTERMSG_STYLE) {

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

        hyPlus.api.displayMessageInChat(builder.toString());

    }

}
