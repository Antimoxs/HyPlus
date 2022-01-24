package dev.antimoxs.hyplus.events;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.modules.Hypixel;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.api.permissions.Permissions;
import net.labymod.api.protocol.liquid.FixedLiquidBucketProtocol;
import net.labymod.core.LabyModCore;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;
import net.labymod.utils.Consumer;
import net.labymod.utils.ServerData;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class HyListenerChatSend implements MessageSendEvent, Consumer<ServerData> {

    @Override
    public boolean onSend(String s) {

        // making sure we are on hypixel.
        if (!HyPlus.getInstance().hypixel.checkOnServer()) return false;

        // Check if we are enabled.
        if (!HyPlus.getInstance().hyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return false;

        String[] command = s.split(" ");

        switch (command[0]) {

            // Overwrite party commands when party manager is enabled.
            case "/stream":
            case "/pl":
            case "/p":
            case "/party": {

                HyPlus.getInstance().hyPartyManager.overriddenPartyCommands(s);
                return HyPlus.getInstance().hyPartyManager.HYPLUS_PM_TOGGLE.getValueBoolean();

            }
            // Tmporaly
            case "/#dx": {

                Hypixel.gulugulu(HyPlus.getInstance().hypixel);
                return true;

            }


            // HyPlus commands
            case "/assets/minecraft/hyplus": {

                return true;

            }
            case "/hydiscord": {

                HyPlus.getInstance().displayIgMessage("Discord", "https://discord.gg/ATdbUS4");
                return true;

            }
            case "/forceupdate": {


                return HyPlus.getInstance().hypixel.checkOnServer();

            }

            // Dev commands
            case "/#test": {

                HyPlus.getInstance().sendMessageIngameChat("/pc §4$lHI§r");
                return true;

            }

            case "/#banner": {

                //addon.hyGameDetector.sendTabList();
                return true;

            }

            case "/#dc": {

                System.out.println("HyPlus COMMAND");
                HyPlus.getInstance().HyPresence.updatePrecense(HyPlus.getInstance(), "poop");
                return true;

            }
            case "/#cs": {

                int percent = 25;
                long duration = 200L;

                if (command.length >= 2) {

                    percent = Integer.parseInt(command[1]);

                }
                if (command.length >= 3) {

                    duration = Long.parseLong(command[2]);

                }

                //Cinescopes.sendCineScope(percent, duration);
                return true;

            }
            case "/#csoff": {

                //Cinescopes.sendCineScope(0, 200L);
                return true;

            }
            case "/#sb": {

                Hypixel.checkScoreboard(HyPlus.getInstance().hypixel);
                return true;

            }
            case "/#sl": {

                Hypixel.listSB(HyPlus.getInstance().hypixel);
                return true;

            }
            case "/#dcs": {

                //addon.hyPlay.sendSecrets("antimoxs", "antimoxs", "mc.hypixel.net");
                return true;

            }
            case "/#dcp": {

                String uuidLeader = MojangRequest.getUUID("dev/antimoxs");
                uuidLeader = uuidLeader.substring(0,8)
                        + "-"
                        + uuidLeader.substring(8,12)
                        + "-"
                        + uuidLeader.substring(12,16)
                        + "-"
                        + uuidLeader.substring(16,20)
                        + "-"
                        + uuidLeader.substring(20,32);

                HyPlus.getInstance().HyPresence.updatePartyInfo(true, UUID.fromString(uuidLeader),1,2);
                return true;

            }
            case "/#nh": {

                return true;

            }


            case "/#perms": {

                JsonObject obj = new JsonObject();

                obj.addProperty("IMPROVED_LAVA", false);
                obj.addProperty("CROSSHAIR_SYNC", false);
                obj.addProperty("REFILL_FIX", false);


                obj.addProperty("GUI_ALL", true);
                obj.addProperty("GUI_POTION_EFFECTS", true);
                obj.addProperty("GUI_ARMOR_HUD", true);
                obj.addProperty("GUI_ITEM_HUD", true);

                obj.addProperty("BLOCKBUILD", false);
                obj.addProperty("TAGS", true);
                obj.addProperty("CHAT", true);
                obj.addProperty("ANIMATIONS", false);
                obj.addProperty("SATURATION_BAR", true);

                obj.addProperty("RANGE", false);
                obj.addProperty("SLOWDOWN", false);

                System.out.println(obj);

                System.out.println(obj.isJsonObject());

                LabyMod.getInstance().getEventManager().callServerMessage("PERMISSIONS", obj);



                return true;

            }

            case "/#perms2": {

                JsonObject obj = new JsonObject();

                obj.addProperty("IMPROVED_LAVA", false);
                obj.addProperty("CROSSHAIR_SYNC", false);
                obj.addProperty("REFILL_FIX", false);
                obj.addProperty("RANGE", false);
                obj.addProperty("SLOWDOWN", false);

                obj.addProperty("GUI_ALL", true);
                obj.addProperty("GUI_POTION_EFFECTS", true);
                obj.addProperty("GUI_ARMOR_HUD", true);
                obj.addProperty("GUI_ITEM_HUD", true);

                obj.addProperty("BLOCKBUILD", false);
                obj.addProperty("TAGS", true);
                obj.addProperty("CHAT", true);
                obj.addProperty("ANIMATIONS", false);
                obj.addProperty("SATURATION_BAR", true);



                System.out.println(obj);

                JsonObject permissionsObject = obj.getAsJsonObject();
                Iterator var4 = permissionsObject.entrySet().iterator();




                while(true) {
                    while(var4.hasNext()) {
                        System.out.println("HAS NEXT");
                        Map.Entry<String, JsonElement> jsonEntry = (Map.Entry)var4.next();
                        Permissions.Permission permission = Permissions.Permission.getPermissionByName((String)jsonEntry.getKey());
                        JsonPrimitive primitive = ((JsonElement)jsonEntry.getValue()).isJsonPrimitive() ? ((JsonElement)jsonEntry.getValue()).getAsJsonPrimitive() : null;
                        if (!LabyMod.getInstance().getServerManager().getPermissionMap().isEmpty()) LabyMod.getInstance().getServerManager().getPermissionMap().clear();
                        if (permission != null && primitive != null && primitive.isBoolean()) {
                            System.out.println("PERMISSION!");
                            LabyMod.getInstance().getServerManager().getPermissionMap().put(permission, primitive.getAsBoolean());
                            if (permission == Permissions.Permission.IMPROVED_LAVA) {
                                FixedLiquidBucketProtocol.onPermissionUpdate(primitive.getAsBoolean());
                            }
                        } else {
                            Debug.log(Debug.EnumDebugMode.PLUGINMESSAGE, "Permission " + (String)jsonEntry.getKey() + " is not supported!");
                        }
                    }

                    Permissions.getPermissionNotifyRenderer().checkChangedPermissions();
                    return true;
                }



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

            default:

                return false;

        }


    }

    @Override
    public void accept(ServerData serverData) {

    }


}
