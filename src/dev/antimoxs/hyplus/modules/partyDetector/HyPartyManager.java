package dev.antimoxs.hyplus.modules.partyDetector;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySimplePlayer;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;

import java.util.ArrayList;
import java.util.List;

public class HyPartyManager implements IHyPlusModule, IHyPlusEvent {

    HyPlus HyPlus;
    boolean active = true;
    public int delay = 0;
    public boolean update = true;

    private HyParty party = new HyParty();

    public HyPartyManager(HyPlus HyPlus) {

        this.HyPlus = HyPlus;

    }


    @Override
    public String getModuleName() {
        return "PartyManager";
    }

    @Override
    public boolean showInSettings() {
        return true; // change to appear
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        SettingsElement toggle = new BooleanElement("PartyDetector", HyPlus, new ControlElement.IconData(Material.LEVER), "HYPLUS_HPD_TOGGLE", true);
        moduleSettings.add(toggle);

        return moduleSettings;

    }

    @Override // maybe we can delete this
    public boolean loop() {

        /*
        if (HyPlus.hySettings.HYPLUS_HPD_TOGGLE) {

            if (!active) activate();


        }
        else {

            if (active) deactivate();

        }

        if (active) {

            if (delay <= 0) {
                delay = HyPlus.hySettings.HYPLUS_HPD_INTERVAL;
                if (!HyPlus.hypixel.checkOnServer()) {
                    return true;
                }

                HyPlus.sendMessageIngameChat("/pl");

            }
            else {

                delay--;


            }

        }


        return true;
        */
        return true;

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        HyPlus.sendMessageIngameChat("/pl");

    }

    // methods

    public void overriddenPartyCommands(String s) {

        // accept invite list leave warp disband promote demote transfer kick kickoffline settings poll chat mute private pc

        if (s.startsWith("/pl")) {

            s = s.replaceFirst("/pl", "/party list");

        }

        String[] command = s.split(" ");

        StringBuilder sb = new StringBuilder();
        sb.append("§9§m------------------------------§r");



        switch (command[1]) {

            case "accept": { HyPlus.sendMessageIngameChat(s); break; }
            case "invite": { HyPlus.sendMessageIngameChat(s); break; }
            case "list": {

                /*
                if (!partyOwner.equals("")) {
                    sb.append("\n§eParty Leader: " + partyOwner);

                    if (partyMods != null) {
                        sb.append("\n§eParty Moderators: §r");
                        for (String mod : partyMods) {

                            sb.append(mod + " ");

                        }
                    }

                    if (partyMems != null) {
                        sb.append("\n§eParty Members: §r");
                        for (String mem : partyMems) {

                            sb.append(mem + "● ");

                        }
                    } else {

                        sb.append("\n§6No members.");

                    }
                }
                else {

                    sb.append("\n§4You are currently not in a party.");

                }
                sb.append("\n§9§m------------------------------§r");

                HyPlus.displayIgMessage(null, sb.toString());
                break;

                 */

            }
            case "leave": { HyPlus.sendMessageIngameChat(s); break; }
            case "warp": { HyPlus.sendMessageIngameChat(s); break; }
            case "disband": { HyPlus.sendMessageIngameChat(s); break; }
            case "promote": { HyPlus.sendMessageIngameChat(s); break; }
            case "demote": { HyPlus.sendMessageIngameChat(s); break; }
            case "transfer": { HyPlus.sendMessageIngameChat(s); break; }
            case "kick": { HyPlus.sendMessageIngameChat(s); break; }
            case "kickoffline": { HyPlus.sendMessageIngameChat(s); break; }
            case "settings": { HyPlus.sendMessageIngameChat(s); break; }
            case "poll": { HyPlus.sendMessageIngameChat(s); break; }
            case "chat": { HyPlus.sendMessageIngameChat(s); break; }
            case "mute": { HyPlus.sendMessageIngameChat(s); break; }
            case "private": { HyPlus.sendMessageIngameChat(s); break; }
            default: overriddenPartyCommands("/party invite " + s.replaceFirst(command[0], ""));

        }

    }

    public HyParty getParty() { return this.party; }

    @Override
    public void onInternalPartyMessage(String message, HyPartyMessageType type) {



        switch (type) {

            case EMPTY:
            case DISBAND: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Removed party.");
                party.reset();
                return;

            }
            case LINE: {

                return;

            }

            case LIST_COUNT: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated party.");
                // "§6Party Members (1)§r"
                int count = Integer.parseInt(message.substring(17, message.length()-3));
                party.setExists(true);
                party.setCount(count);
                return;

            }
            case LIST_LEADER: {

                // "§eParty Leader: §r§6[MVP§r§5++§r§6] Antimoxs §r§a●§r"
                String cropped = message.substring(16,message.length()-8);
                String rank;
                String name;
                if (message.charAt(20) == '[') {

                    rank = cropped.split(" ")[0];
                    name = cropped.split(" ")[1];

                }
                else {

                    rank = null;
                    name = cropped;

                }
                party.setExists(true);
                party.setPartyLeader(new HySimplePlayer(name, rank));
                return;

            }
            case LIST_MEMBERS: {

                // "§eParty Members: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                String cropped = message.substring(17);
                String[] members = cropped.split("●");
                party.setExists(true);
                party.clearMembers();
                for (String member : members) {

                    party.addPlayer(getPlayerFromString(member, cropped));

                }
                return;

            }
            case LIST_MODS: {

                // "§eParty Moderators: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                String cropped = message.substring(20);
                String[] members = cropped.split("●");
                party.setExists(true);
                party.clearMembers();
                for (String member : members) {

                    party.addMod(getPlayerFromString(member, cropped));

                }
                return;

            }

            case PUBLIC_CREATED: {

                // "§aCreated a public party! Players can join with §r§6§l/party join Antimoxs§r"
                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now public.");
                party.setExists(true);
                party.setPublic(true);
                break;

            }
            case PUBLIC_CAPPED: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated player cap.");
                // "§eParty is capped at 25 players.§r"
                String cropped = message.substring(21, message.length()-11);
                int cap = Integer.parseInt(cropped);
                party.setExists(true);
                party.setCap(cap);
                break;

            }

            case ALLINV_ON: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Enabled all-invite.");
                party.setExists(true);
                party.setAllInvite(true);
                return;

            }
            case ALLINV_OFF: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Disabled all-invite.");
                party.setExists(true);
                party.setAllInvite(false);
                return;

            }
            case PUBLIC_OFF: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is no longer public.");
                party.setExists(true);
                party.setPublic(false);
                party.setCap(-1);
                break;

            }
            case TRANSFERRED: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Transferred party.");
                break;

            }
            case PLAYER_LEFT:
            case PLAYER_JOINED:
            case PLAYER_KICKED:
            case PLAYER_DISCONNECT: {

                // update the party
                break;

            }
            case MUTED_ON: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now muted.");
                party.setExists(true);
                party.setMuted(true);
                return;

            }
            case MUTED_OFF: {

                LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now unmuted.");
                party.setExists(true);
                party.setMuted(false);
                return;

            }

        }

        this.HyPlus.sendMessageIngameChat("/pl");

    }

    private HySimplePlayer getPlayerFromString(String member, String cropped) {

        String rank;
        String name;
        if (member.trim().charAt(5) == '[') {

            rank = cropped.split(" ")[0];
            name = cropped.split(" ")[1];

        }
        else {

            rank = null;
            name = cropped;

        }
        return new HySimplePlayer(HyUtilities.matchOutColorCode(name), rank);

    }

}
