package dev.antimoxs.hyplus.modules.party;

import dev.antimoxs.hypixelapiHP.requests.MojangRequest;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.api.party.HyParty;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import dev.antimoxs.hyplus.api.player.HySimplePlayer;
import dev.antimoxs.utilities.time.wait;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.settings.elements.SliderElement;
import net.labymod.utils.Material;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HyPartyManager implements IHyPlusModule, IHyPlusEvent {

    public static final HySetting HYPLUS_PM_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_TOGGLE", "PartyManager", "Toggle the party-manager.", true, true, Material.CAKE);
    public static final HySetting HYPLUS_PM_SHOW = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_SHOW", "Show in DiscordPresence", "Toggle the display of the current party state in the Discord.", true, true, Material.PAPER);
    public static final HySetting HYPLUS_PM_JOIN = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_JOIN", "Allow party joins.", "Allow players to join ur party. (Only public party)", true, true, Material.GOLD_BOOTS);
    public static final HySetting HYPLUS_PM_DC_UPDATE = new HySetting(HySettingType.INT, "HYPLUS_PM_DC_UPDATE", "Discord Callback interval.", "Set the interval for Callback checks.", 2, 2, Material.WATCH);

    public static final HySetting HYPLUS_PM_MESSAGE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_MESSAGE", "Display Update-Message", "Display the party update message", true, true, Material.BOOK_AND_QUILL);

    private HyParty party = new HyParty();

    @Override
    public String getModuleName() {
        return "PartyManager";
    }

    @Override
    public void checkConfig(boolean reset) {

        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PM_TOGGLE);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PM_SHOW);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PM_JOIN);
        HyPlus.getInstance().hyConfigManager.checkConfig(reset, HYPLUS_PM_MESSAGE);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_PM_TOGGLE.getDisplayName(), HYPLUS_PM_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_PM_TOGGLE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_TOGGLE.getValueBoolean());
        toggle.setDescriptionText(HYPLUS_PM_TOGGLE.getDescription());

        Settings subs = new Settings();
        subs.addAll(getSubSettings());

        toggle.setSubSettings(subs);

        moduleSettings.add(toggle);

        return moduleSettings;

    }

    public ArrayList<SettingsElement> getSubSettings() {

        ArrayList<SettingsElement> subSettings = new ArrayList<>();
        BooleanElement show = new BooleanElement(HYPLUS_PM_SHOW.getDisplayName(), HYPLUS_PM_SHOW.getIcon(), (booleanElement) -> {

            HYPLUS_PM_SHOW.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_SHOW.getValueBoolean());
        show.setDescriptionText(HYPLUS_PM_SHOW.getDescription());

        BooleanElement join = new BooleanElement(HYPLUS_PM_JOIN.getDisplayName(), HYPLUS_PM_JOIN.getIcon(), (booleanElement) -> {

            HYPLUS_PM_JOIN.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_JOIN.getValueBoolean());
        join.setDescriptionText(HYPLUS_PM_JOIN.getDescription());

        BooleanElement msg = new BooleanElement(HYPLUS_PM_MESSAGE.getDisplayName(), HYPLUS_PM_MESSAGE.getIcon(), (booleanElement) -> {

            HYPLUS_PM_MESSAGE.changeConfigValue(HyPlus.getInstance(), booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_MESSAGE.getValueBoolean());
        msg.setDescriptionText(HYPLUS_PM_MESSAGE.getDescription());

        SliderElement interval = new SliderElement(HYPLUS_PM_DC_UPDATE.getDisplayName(), HYPLUS_PM_DC_UPDATE.getIcon(), HYPLUS_PM_DC_UPDATE.getValueInt());
        interval.addCallback((sliderElement) -> {

            HYPLUS_PM_DC_UPDATE.changeConfigValue(HyPlus.getInstance(), sliderElement);
            checkConfig(false);
            updateParty(false);

        });
        interval.setMinValue(1);
        interval.setMaxValue(50);
        interval.setSteps(1);


        subSettings.add(show);
        subSettings.add(join);
        subSettings.add(msg);
        subSettings.add(interval);

        return subSettings;

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        if (!HYPLUS_PM_TOGGLE.getValueBoolean()) return;
        HyPlus.getInstance().sendMessageIngameChat("/pl");

    }

    private int interval = 0;

    @Override
    public boolean loop() {

        if (interval > HYPLUS_PM_DC_UPDATE.getValueInt()) {

            interval = 0;

            Thread t = new Thread(() -> {

                HyPlus.getInstance().discordManager.runCallbacks();

            });
            Runtime.getRuntime().addShutdownHook(t);
            t.start();

        }
        else {

            interval++;

        }
        return true;

    }

    // methods

    public void overriddenPartyCommands(String s) {

        // accept invite list leave warp disband promote demote transfer kick kickoffline settings poll chat mute private pc

        if (s.startsWith("/pl")) {

            s = s.replaceFirst("/pl", "/party list");

        }

        if (s.startsWith("/stream")) {

            HyPlus.getInstance().sendMessageIngameChat(s); return;

        }

        String[] command = s.split(" ");

        StringBuilder sb = new StringBuilder();
        sb.append("§9§m------------------------------§r");

        if (command.length <=1) {
            HyPlus.getInstance().sendMessageIngameChat("/party");
            return;
        }

        switch (command[1]) {

            case "accept":
            case "transfer":
            case "demote":
            case "kickoffline":
            case "promote":
            case "disband":
            case "warp":
            case "poll":
            case "chat":
            case "private":
            case "join":
            case "settings":
            case "mute":
            case "invite":
            case "leave":
            case "kick":
            case "answer": { HyPlus.getInstance().sendMessageIngameChat(s); break; }
            case "list": {


                if (this.party.doesExist()) {
                    sb.append("\n§6Party Members (" + this.party.getCount() + (this.party.isPublic() ? "/" + this.party.getCap() : "") + ")");
                    sb.append("\n§eParty Leader: ").append(this.party.getPartyLeader().getPlayer());

                    if (!party.getPartyMods().isEmpty()) {
                        sb.append("\n§eParty Moderators: §r");
                        for (HySimplePlayer mod : this.party.getPartyMods().values()) {

                            sb.append(mod.getPlayer()).append(" ");

                        }
                    }

                    if (!this.party.getPartyMembers().isEmpty()) {
                        sb.append("\n§eParty Members:§r");
                        for (HySimplePlayer mem : this.party.getPartyMembers().values()) {

                            sb.append(mem.getPlayer());

                        }
                    } else {

                        sb.append("\n§6No members.");

                    }
                }
                else {

                    sb.append("\n§cYou are currently not in a party.");

                }
                sb.append("\n§9§m------------------------------§r");

                HyPlus.getInstance().displayIgMessage(null, sb.toString());
                break;



            }
            default: overriddenPartyCommands("/party invite " + s.replaceFirst(command[0], ""));

        }

        Thread updater = new Thread(() -> {

            wait.sc(3L);
            HyPlus.getInstance().sendMessageIngameChat("/pl");
            wait.sc(3L);
            updateParty(false);

        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();


    }

    public HyParty getParty() { return this.party; }

    @Override
    public void onPartyDataPacket(HyParty party) {

        if (!party.isInParty(LabyMod.getInstance().getPlayerName())) return; // we're not in that party :sob:
        // updating party over packet
        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated party via packet.");
        // check if we even in the party lol

        this.party = party;
        updateParty(false);

    }

    @Override
    public void onInternalPartyMessage(String message, HyPartyMessageType type) {


        Thread partyT = new Thread(() -> {

            switch (type) {

                case EMPTY:
                case DISBAND: {

                    // this is spam :(
                    //LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Removed party.");
                    party.reset();
                    updateParty(true);
                    return;

                }
                case LINE: {

                    return;

                }

                case LIST_COUNT: {

                    // spam too :(
                    //LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated party.");
                    // "§6Party Members (1)§r"
                    int count = Integer.parseInt(message.substring(17, message.length()-3));
                    party.setExists(true);

                    if (count == party.getCount()) return;

                    party.setCount(count);
                    updateParty(false);
                    return;

                }
                case LIST_LEADER: {

                    // PACKET EXCHANGE
                    party.clearMembers();
                    party.clearMods();
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

                    System.out.println("PL: '" + rank + "' | '" + name + "'");
                    System.out.println(cropped);
                    party.setExists(true);

                    // if (name.equals(party.getPartyLeader().getName())) return; -- update it every time >:O

                    party.setPartyLeader(new HySimplePlayer(name, rank, "", false));
                    updateParty(true);
                    return;

                }
                case LIST_MEMBERS: {

                    // "§eParty Members: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                    String cropped = message.substring(17);
                    String[] members = cropped.split("●");
                    party.setExists(true);

                    if (members.length == party.getPartyMembers().size()) return;

                    for (String member : members) {

                        party.addPlayer(getPlayerFromString(member, cropped));

                    }
                    updateParty(false);
                    return;

                }
                case LIST_MODS: {

                    // "§eParty Moderators: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                    String cropped = message.substring(20);
                    String[] members = cropped.split("●");
                    party.setExists(true);

                    if (members.length == party.getPartyMods().size()) return;

                    for (String member : members) {

                        party.addMod(getPlayerFromString(member, cropped));

                    }
                    updateParty(false);
                    return;

                }

                case PUBLIC_CREATED: {

                    // DETECTED CLIENTSIDE
                    // "§aCreated a public party! Players can join with §r§6§l/party join Antimoxs§r"
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now public.");
                    party.setExists(true);

                    party.setPublic(true);
                    updateParty(false);
                    return;

                }
                case PUBLIC_CAPPED: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated player cap.");
                    // "§eParty is capped at 25 players.§r"
                    String cropped = message.substring(21, message.length()-11);
                    int cap = Integer.parseInt(cropped);
                    party.setExists(true);
                    party.setPublic(true);

                    if (cap == party.getCap()) return;

                    party.setCap(cap);
                    HyPlus.getInstance().sendMessageIngameChat("/pl");
                    return;

                }

                case ALLINV_ON: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Enabled all-invite.");
                    party.setExists(true);

                    //if (party.getAllInvite()) return;

                    party.setAllInvite(true);
                    return;

                }
                case ALLINV_OFF: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Disabled all-invite.");
                    party.setExists(true);

                    //if (!party.getAllInvite()) return;

                    party.setAllInvite(false);
                    return;

                }
                case PGAMES_ON: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Enabled private games.");
                    party.setExists(true);

                    //if (party.getPGames()) return;

                    party.setPGames(true);
                    return;

                }
                case PGAMES_OFF: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Disabled private games.");
                    party.setExists(true);

                    //if (!party.getPGames()) return;

                    party.setPGames(false);
                    return;

                }
                case PUBLIC_OFF: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is no longer public.");
                    party.setExists(true);
                    party.setPublic(false);
                    party.setCap(-1);
                    break;

                }
                case TRANSFERRED: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Transferred party.");
                    break;

                }
                case UPDATE:
                case PLAYER_LEFT:
                case PLAYER_JOINED:
                case PLAYER_KICKED:
                case PLAYER_DISCONNECT: {

                    // just update the party via '/party list'
                    break;

                }
                case MUTED_ON: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now muted.");
                    party.setExists(true);
                    party.setMuted(true);
                    return;

                }
                case MUTED_OFF: {

                    // DETECTED CLIENTSIDE
                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Party is now unmuted.");
                    party.setExists(true);
                    party.setMuted(false);
                    return;

                }

            }

            HyPlus.getInstance().sendMessageIngameChat("/pl");
            //wait.sc(3L);
            //updateParty(true);

        });
        Runtime.getRuntime().addShutdownHook(partyT);
        partyT.start();


    }

    private HySimplePlayer getPlayerFromString(String member, String cropped) {

        String rank;
        String name;

        if (member.trim().toCharArray().length < 6) {

            HyPlus.debugLog("SMALL: " + member + " | " + cropped);
            rank = null;
            name = cropped;

        }
        else if (member.trim().charAt(5) == '[') {

            rank = cropped.split(" ")[0];
            name = cropped.split(" ")[1];

        }
        else {

            rank = null;
            name = cropped;

        }
        return new HySimplePlayer(name, rank, "", false);

    }

    public void updateParty(boolean sendPacket) {

        Thread updater = new Thread(() -> {

            if (!HyPlus.getInstance().hyDiscordPresence.presenceCheck()) return;

            if (!this.party.doesExist() || !HYPLUS_PM_SHOW.getValueBoolean()) {

                HyPlus.getInstance().discordManager.getRichPresence().updateParty(false, 0, 0, "");
                HyPlus.getInstance().discordManager.getRichPresence().updateJoinSecret(false, null);
                HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();
                return;

            }

            int max = this.party.isPublic() ? this.party.getCap() : this.party.getCount();
            int count = this.party.getCount();
            String name = this.party.getPartyLeader().getPlayerBlank();
            HyPlus.debugLog("LEADERNAME: '" + name + "'");

            if (name.equals("#UndefinedPlayer#")) return;


            String id = MojangRequest.getUUID(name);
            HyPlus.getInstance().discordManager.getRichPresence().updateParty(true, max, count, id);

            if (this.party.isPublic() && HYPLUS_PM_JOIN.getValueBoolean()) {

                generateInvite(id);

            }
            else {

                HyPlus.getInstance().discordManager.getRichPresence().updateJoinSecret(false, null);

            }

            HyPlus.getInstance().discordManager.getRichPresence().updateRichPresence();

            if (sendPacket) {

                HyPlus.debugLog("Sending party update packet to all members :)");
                ArrayList<UUID> uuids = new ArrayList<>();
                for (HySimplePlayer player : this.party.getAllMembers()) {

                    String uuid = MojangRequest.getUUID(player.getPlayerBlank());
                    HyPlus.debugLog("REQUEST FOR '" + player.getPlayerBlank() + "' returned: " + uuid);
                    try {
                        uuids.add(UUID.fromString(HyUtilities.dashUUID(uuid)));
                    }
                    catch (Exception e) {

                        e.printStackTrace();

                    }


                }

                UUID[] uuidsA = new UUID[uuids.size()];

                for (int i = 0; i < uuids.size(); i++) {

                    uuidsA[i] = uuids.get(i);

                }

                String jsonBytes = this.party.getJson();
                PacketAddonDevelopment pad = new PacketAddonDevelopment(
                        LabyMod.getInstance().getPlayerUUID(), uuidsA,
                        "hyplus:partydata",
                        jsonBytes.getBytes(StandardCharsets.UTF_8)
                );
                LabyMod.getInstance().getLabyModAPI().sendAddonDevelopmentPacket(pad);

            }

            if (HYPLUS_PM_MESSAGE.getValueBoolean()) HyPlus.getInstance().displayIgMessage("PartyManager", "Updating your party <3");


        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();

    }

    private void generateInvite(String uuidJoin) {

        System.out.println(uuidJoin);
        // DEBUG HyPlus.getInstance().displayIgMessage("PublicPartyUUID", uuidJoin);
        HyPlus.getInstance().discordManager.getRichPresence().updateJoinSecret(true, HyUtilities.dashUUID(uuidJoin));

    }

    public String rematchInvite(UUID invite) {

        String r = MojangRequest.getName(invite.toString());
        HyPlus.debugLog("JOINING TO: " + r);
        return r;

    }

}
