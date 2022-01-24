package dev.antimoxs.hyplus.events;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.modules.friends.HyFriendRequest;
import dev.antimoxs.hyplus.modules.partyManager.HyPartyMessageType;
import net.labymod.api.events.MessageReceiveEvent;

public class HyListenerChatMessage implements MessageReceiveEvent {

    @Override
    public boolean onReceive(String s, String s1) {

        System.out.println("[AdvancedChatLog][" + s.replaceAll("\n","{\\n}") + "]");

        // Check if we are enabled.
        if (!HyPlus.getInstance().hyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return false;

        // Making sure we are on hypixel.
        if (HyPlus.getInstance().hypixel.checkOnServer()) {

            // Checking if we have API-mode disabled and detection activated.
            if ((!HyPlus.getInstance().hyLocationDetector.HYPLUS_LD_API.getValueBoolean()) && HyPlus.getInstance().hyLocationDetector.HYPLUS_LD_TOGGLE.getValueBoolean()) {

                // Filtering for our locraw message
                if (s.startsWith("§f{\"")) {

                    // calling location event
                    HyPlus.getInstance().hyEventManager.callLocationResponse(s1);
                    return true;

                }

                // Check for Atlas
                if (s.startsWith("§r§aTeleporting you to Suspect§r")) {

                    HyPlus.getInstance().hyEventManager.callLocationResponse(HyPlus.getInstance().hyLocationDetector.getCurrentLocation().getJson());
                    return false;

                }

            }

            // Checking for MSG message
            if (HyPlus.getInstance().hyBetterMsg.HYPLUS_BETTERMSG_TOGGLE.getValueBoolean()) {

                if (s.startsWith("§dFrom ")) {

                    // calling msg event
                    HyPlus.getInstance().hyEventManager.callPrivateMessage(true, s, s1);
                    return true;

                }
                else if (s.startsWith("§dTo ")) {

                    // calling msg event
                    HyPlus.getInstance().hyEventManager.callPrivateMessage(false, s, s1);
                    return true;


                }

            }

            // Checking if it's a friend request
            if (HyPlus.getInstance().hyFriend.HYPLUS_AUTOFRIEND_TOGGLE.getValueBoolean()) {



                // Filtering friend request message
                //                §m----------------------------------------------------

                //System.out.println("[HyFriend] [" + s + "][" + s1 + "]");

                if (s.startsWith("§9§m----------------------------------------------------§r§9")) {

                    System.out.println("[HyFriend] It's a friend request bois!!!");

                    String s2 = HyUtilities.matchOutColorCode(s);

                    s2 = s2.replaceFirst("----------------------------------------------------", " ");
                    String[] lines = s2.split("\n");
                    if (lines.length < 2) return false;
                    String line = lines[1];
                    String[] lineSplit = line.split(" ");

                    String name = lineSplit[lineSplit.length - 1];
                    String rank = lineSplit[lineSplit.length - 2];

                    if (name.equals(">>")) {
                        return false;
                    }

                    // calling new friend request event
                    return HyPlus.getInstance().hyEventManager.callFriendRequest(new HyFriendRequest(name, rank));

                }


            }

            // Checking for party message
            if (HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getValueBoolean()) {

                boolean hideMsg = HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getValueBoolean();

                /*
                * Disgusting if chain :[
                * */

                // "§9§m-----------------------------§r"
                if (s.equals("§9§m-----------------------------§r")) {

                    System.out.println("###PARTYLINE");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.LINE);
                    return hideMsg;
                    //return true;

                }

                // "§cYou are not currently in a party.§r"
                if (s.toLowerCase().startsWith("§cyou are not currently in a party.§r")) {

                    System.out.println("###NOPARTY");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.EMPTY);
                    return hideMsg;
                    //return true;

                }
                // "§aCreated a public party! Players can join with §r§6§l/party join Antimoxs§r"
                if (s.toLowerCase().startsWith("§acreated a public party! players can join with §r§6§l/party join")) {

                    System.out.println("###PUBLIC");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PUBLIC_CREATED);
                    return false;
                    //return true;

                }
                // "§eParty is capped at 25 players.§r"
                if (s.toLowerCase().startsWith("§eparty is capped at")) {

                    System.out.println("###CAPPED");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PUBLIC_CAPPED);
                    return false;
                    //return true;

                }
                // §eThe party is no longer public§r
                if (s.toLowerCase().startsWith("§ethe party is no longer public§r")) {

                    System.out.println("###PUBLICOFF");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PUBLIC_OFF);
                    return false;
                    //return true;

                }
                // "§6Party Members (1)§r"
                if (s.toLowerCase().startsWith("§6party members (")) {

                    System.out.println("###MEMBERSCOUNT");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.LIST_COUNT);
                    return hideMsg;
                    //return true;

                }
                // "§eParty Leader: §r§6[MVP§r§5++§r§6] Antimoxs §r§a●§r"
                if (s.toLowerCase().startsWith("§eparty leader:")) {

                    System.out.println("###LEADER");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.LIST_LEADER);
                    return hideMsg;
                    //return true;

                }
                // "§eParty Members: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                if (s.toLowerCase().startsWith("§eparty members:")) {

                    System.out.println("###MEMBERS");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.LIST_MEMBERS);
                    return hideMsg;
                    //return true;

                }
                // "§eParty Moderators: §r§b[MVP] nobodycared§r§a ● §r"
                if (s.toLowerCase().startsWith("§eparty moderators:")) {

                    System.out.println("###MODERATORS");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.LIST_MODS);
                    return hideMsg;
                    //return true;

                }
                // §b[MVP] nobodycared §r§ejoined the party.§r
                if (s.toLowerCase().endsWith("§r§ejoined the party.§r")) {

                    System.out.println("###JOINED");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PLAYER_JOINED);
                    return false;
                    //return true;

                }
                // §cThe party is now muted. §r
                if (s.toLowerCase().startsWith("§cthe party is now muted. §r")) {

                    System.out.println("###MUTED");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.MUTED_ON);
                    return false;
                    //return true;

                }
                // TODO: does this work?
                // §aDie Party ist nicht mehr länger stummgeschaltet.§r
                if (s.startsWith("§a") && s.toLowerCase().contains("party") && !s.contains("§r§6§l/party join")) {

                    System.out.println("###UNMUTED");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.MUTED_OFF);
                    return false;
                    //return true;

                }
                // §cThe party was disbanded because the party leader disconnected.§r
                if (s.toLowerCase().startsWith("§cthe party was disbanded because the party leader disconnected.§r")) {

                    System.out.println("###DISBAND");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.DISBAND);
                    return false;
                    //return true;

                }
                // §eThe party was transferred to §r§b[MVP§r§2+§r§b] cuddlig §r§eby §r§6[MVP§r§5++§r§6] Antimoxs§r
                if (s.toLowerCase().startsWith("§ethe party was transferred to ")) {

                    System.out.println("###TRANSFERRED");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.TRANSFERRED);
                    return false;
                    //return true;

                }
                // §6[MVP§r§5++§r§6] Antimoxs §r§ahat die Option All Invite aktiviert§r
                if (s.contains("§r§a") && s.toLowerCase().contains("all invite")) {

                    System.out.println("#ALLINVON");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.ALLINV_ON);
                    return false;

                }
                // §6[MVP§r§5++§r§6] Antimoxs §r§chat All Invite deaktiviert§r
                if (s.contains("§r§c") && s.toLowerCase().contains("all invite")) {

                    System.out.println("#ALLINVOFF");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.ALLINV_OFF);
                    return false;

                }
                // private
                if (s.contains("§r§a") && s.toLowerCase().contains("private game")) {

                    System.out.println("#PGAMEON");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PGAMES_ON);
                    return false;

                }
                // §6[MVP§r§5++§r§6] Antimoxs §r§chat All Invite deaktiviert§r
                if (s.contains("§r§c") && s.toLowerCase().contains("private game")) {

                    System.out.println("#PGAMEOFF");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.PGAMES_OFF);
                    return false;

                }
                // general update
                if (s.contains("§e") && s.toLowerCase().contains("party") && s.endsWith("§r")) {

                    System.out.println("#PARTYUPDATE");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.UPDATE);

                }
                else if (s.contains("§c") && s.toLowerCase().contains("party") && s.endsWith("§r")) {

                    System.out.println("#PARTYEMPTY?");
                    HyPlus.getInstance().hyEventManager.callPartyMessageAsync(s, HyPartyMessageType.EMPTY);

                }



                //--- §6[MVP§r§5++§r§6] Antimoxs §r§einvited §r§b[MVP§r§2+§r§b] cuddlig §r§eto the party! They have §r§c60 §r§eseconds to accept.§r
                // §6[MVP§r§5++§r§6] Antimoxs§r§e hat §r§b[MVP§r§2+§r§b] cuddlig §r§ezum Party Moderator befördert§r

                // §6[MVP§r§5++§r§6] Antimoxs§r§e が §r§b[MVP§r§2+§r§b] cuddlig§r§e を Party モデレーターに昇格させました§r
                // §6[MVP§r§5++§r§6] Antimoxs§r§e が §r§b[MVP§r§2+§r§b] cuddlig§r§e を Party メンバーに降格させました§r

                //--- §eOnly Party Mods, Staff and the Leader will be able to chat.§r
                // §cKeinen Spieler mit diesem Namen gefunden!§r
                // §cDu kannst diesen Spieler nicht einladen, da er nicht online ist.§r
                //--- §aChanged the maximum party size!§r
                // §b[MVP§r§c+§r§b] valentinsan §r§ewurde aus der Party entfernt.§r
                // §eThe party was transferred to §r§b[MVP§r§2+§r§b] cuddlig §r§eby §r§6[MVP§r§5++§r§6] Antimoxs§r
                // §eThe party leader, §r§b[MVP§r§2+§r§b] cuddlig §r§ehas disconnected, they have §r§c5 §r§eminutes to rejoin before the party is disbanded.§r
                // §cThe party was disbanded because the party leader disconnected.§r

            }

        }

        return false;

    }

}
