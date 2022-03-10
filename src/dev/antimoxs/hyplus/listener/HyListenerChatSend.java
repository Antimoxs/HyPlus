package dev.antimoxs.hyplus.listener;


import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.Hypixel;
import dev.antimoxs.hyplus.modules.HyGeneral;
import dev.antimoxs.hyplus.modules.party.HyPartyManager;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

public class HyListenerChatSend implements MessageSendEvent, Consumer<ServerData> {

    @Override
    public boolean onSend(String s) {

        // making sure we are on hypixel.
        if (!HyPlus.getInstance().hypixel.checkOnServer()) return false;

        // Check if we are enabled.
        if (!HyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return false;

        String[] command = s.split(" ");

        switch (command[0]) {

            // Overwrite party commands when party manager is enabled.
            case "/stream":
            case "/pl":
            case "/p":
            case "/party": {

                HyPlus.getInstance().hyPartyManager.overriddenPartyCommands(s);
                return HyPartyManager.HYPLUS_PM_TOGGLE.getValueBoolean();

            }

            // HyPlus commands
            case "/hydiscord": {

                HyPlus.getInstance().displayIgMessage("Discord", "https://discord.gg/ATdbUS4");
                return true;

            }

            /*


            // Dev commands


            case "/#sb": {

                Hypixel.checkScoreboard(HyPlus.getInstance().hypixel);
                return true;

            }
            case "/#sl": {

                Hypixel.listSB(HyPlus.getInstance().hypixel);
                return true;

            }
            case "/#reloadfl" : {

                HyPlus.getInstance().hyFriend.reloadFL();
                return true;

            }

            case "/#showparty": {

                HyPlus.getInstance().displayIgMessage("Party1", HyPlus.getInstance().hyPartyManager.getParty().doesExist() + " (exists)");
                HyPlus.getInstance().displayIgMessage("Party2", HyPlus.getInstance().hyPartyManager.getParty().getCount() + " (count)");
                HyPlus.getInstance().displayIgMessage("Party3", HyPlus.getInstance().hyPartyManager.getParty().getPartyLeader().getPlayer() + " (leader)");
                HyPlus.getInstance().displayIgMessage("Party4", HyPlus.getInstance().hyPartyManager.getParty().getPartyMembers().toString() + "(members)");
                HyPlus.getInstance().displayIgMessage("Party5", HyPlus.getInstance().hyPartyManager.getParty().getPartyMods().toString() + "(mods)");
                HyPlus.getInstance().displayIgMessage("Party6", HyPlus.getInstance().hyPartyManager.getParty().getAllInvite() + "(allinvite)");
                HyPlus.getInstance().displayIgMessage("Party7", HyPlus.getInstance().hyPartyManager.getParty().isPublic() + "(public)");
                HyPlus.getInstance().displayIgMessage("Party8", HyPlus.getInstance().hyPartyManager.getParty().getCap() + "(cap)");

                return false;

            }

            case "/#status": {

                Hypixel.getGameStatus(HyPlus.getInstance().hyLocationDetector.getCurrentLocation());
                return true;

            }


             */

            default:

                return false;

        }


    }

    @Override
    public void accept(ServerData serverData) {

    }


}
