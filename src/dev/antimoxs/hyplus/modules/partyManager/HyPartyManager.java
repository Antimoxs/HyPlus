package dev.antimoxs.hyplus.modules.partyManager;

import com.google.gson.JsonObject;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.HyUtilities;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.objects.HyServerLocation;
import dev.antimoxs.hyplus.objects.HySetting;
import dev.antimoxs.hyplus.objects.HySettingType;
import dev.antimoxs.hyplus.objects.HySimplePlayer;
import dev.antimoxs.utilities.time.wait;
import net.labymod.labyconnect.packets.PacketAddonDevelopment;
import net.labymod.main.LabyMod;
import net.labymod.settings.Settings;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import scala.tools.nsc.Global;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HyPartyManager implements IHyPlusModule, IHyPlusEvent {

    private final HyPlus hyPlus;

    private HyParty party = new HyParty();

    public final HySetting HYPLUS_PM_TOGGLE = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_TOGGLE", "PartyManager", "Toggle the party-manager.", true, true, Material.CAKE);
    public final HySetting HYPLUS_PM_SHOW = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_SHOW", "Show in DiscordPresence", "Toggle the display of the current party state in the Discord.", true, true, Material.PAPER);
    public final HySetting HYPLUS_PM_JOIN = new HySetting(HySettingType.BOOLEAN, "HYPLUS_PM_JOIN", "Allow party joins.", "Allow players to join ur party. (Only public party)", true, true, Material.GOLD_BOOTS);

    public HyPartyManager(HyPlus HyPlus) {

        this.hyPlus = HyPlus;

    }


    @Override
    public String getModuleName() {
        return "PartyManager";
    }

    @Override
    public void checkConfig(boolean reset) {

        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_PM_TOGGLE);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_PM_SHOW);
        hyPlus.hyConfigManager.checkConfig(reset, HYPLUS_PM_JOIN);

    }

    @Override
    public boolean showInSettings() {
        return true;
    }

    @Override
    public List<SettingsElement> getModuleSettings() {

        List<SettingsElement> moduleSettings = new ArrayList<>();

        BooleanElement toggle = new BooleanElement(HYPLUS_PM_TOGGLE.getDisplayName(), HYPLUS_PM_TOGGLE.getIcon(), (booleanElement) -> {

            HYPLUS_PM_TOGGLE.changeConfigValue(hyPlus, booleanElement);
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

            HYPLUS_PM_SHOW.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_SHOW.getValueBoolean());
        show.setDescriptionText(HYPLUS_PM_SHOW.getDescription());

        BooleanElement join = new BooleanElement(HYPLUS_PM_JOIN.getDisplayName(), HYPLUS_PM_JOIN.getIcon(), (booleanElement) -> {

            HYPLUS_PM_JOIN.changeConfigValue(hyPlus, booleanElement);
            checkConfig(false);
            updateParty(false);

        }, HYPLUS_PM_JOIN.getValueBoolean());
        join.setDescriptionText(HYPLUS_PM_JOIN.getDescription());

        subSettings.add(show);
        subSettings.add(join);

        return subSettings;

    }

    @Override
    public void onLocationChange(HyServerLocation location) {

        if (!HYPLUS_PM_TOGGLE.getValueBoolean()) return;
        hyPlus.sendMessageIngameChat("/pl");

    }

    @Override
    public boolean loop() {

        hyPlus.discordApp.getRichPresence().runCallbacks();
        return true;

    }

    // methods

    public void overriddenPartyCommands(String s) {

        // accept invite list leave warp disband promote demote transfer kick kickoffline settings poll chat mute private pc

        if (s.startsWith("/pl")) {

            s = s.replaceFirst("/pl", "/party list");

        }

        if (s.startsWith("/stream")) {

            hyPlus.sendMessageIngameChat(s); return;

        }

        String[] command = s.split(" ");

        StringBuilder sb = new StringBuilder();
        sb.append("§9§m------------------------------§r");

        if (command.length <=1) return;

        switch (command[1]) {

            case "accept": { hyPlus.sendMessageIngameChat(s); break; }
            case "invite": { hyPlus.sendMessageIngameChat(s); break; }
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

                hyPlus.displayIgMessage(null, sb.toString());
                break;



            }
            case "leave": { hyPlus.sendMessageIngameChat(s); break; }
            case "warp": { hyPlus.sendMessageIngameChat(s); break; }
            case "disband": { hyPlus.sendMessageIngameChat(s); break; }
            case "promote": { hyPlus.sendMessageIngameChat(s); break; }
            case "demote": { hyPlus.sendMessageIngameChat(s); break; }
            case "transfer": { hyPlus.sendMessageIngameChat(s); break; }
            case "kick": { hyPlus.sendMessageIngameChat(s); break; }
            case "kickoffline": { hyPlus.sendMessageIngameChat(s); break; }
            case "settings": { hyPlus.sendMessageIngameChat(s); break; }
            case "poll": { hyPlus.sendMessageIngameChat(s); break; }
            case "chat": { hyPlus.sendMessageIngameChat(s); break; }
            case "mute": { hyPlus.sendMessageIngameChat(s); break; }
            case "private": { hyPlus.sendMessageIngameChat(s); break; }
            case "join": { hyPlus.sendMessageIngameChat(s); break; }
            case "answer": { hyPlus.sendMessageIngameChat(s); break; }
            default: overriddenPartyCommands("/party invite " + s.replaceFirst(command[0], ""));

        }

        Thread updater = new Thread(() -> {

            wait.sc(3L);
            this.hyPlus.sendMessageIngameChat("/pl");
            wait.sc(3L);
            updateParty(false);

        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();


    }

    public HyParty getParty() { return this.party; }

    @Override
    public void onPartyDataPacket(HyParty party) {

        // updating party over packet
        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Updated party via packet.");
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
                    party.setCount(count);
                    updateParty(false);
                    return;

                }
                case LIST_LEADER: {

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
                    party.setPartyLeader(new HySimplePlayer(name, rank, ""));
                    updateParty(true);
                    return;

                }
                case LIST_MEMBERS: {

                    // "§eParty Members: §r§b[MVP§r§2+§r§b] cuddlig§r§a ● §r§b[MVP§r§c+§r§b] valentinsan§r§a ● §r"
                    String cropped = message.substring(17);
                    String[] members = cropped.split("●");
                    party.setExists(true);
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
                    for (String member : members) {

                        party.addMod(getPlayerFromString(member, cropped));

                    }
                    updateParty(false);
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
                    party.setPublic(true);
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
                case PGAMES_ON: {

                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Enabled private games.");
                    party.setExists(true);
                    party.setPGames(true);
                    return;

                }
                case PGAMES_OFF: {

                    LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("PartyDetector", "Disabled private games.");
                    party.setExists(true);
                    party.setPGames(false);
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
                case UPDATE:
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

            this.hyPlus.sendMessageIngameChat("/pl");
            wait.sc(3L);
            updateParty(true);

        });
        Runtime.getRuntime().addShutdownHook(partyT);
        partyT.start();


    }

    private HySimplePlayer getPlayerFromString(String member, String cropped) {

        String rank;
        String name;

        if (member.trim().toCharArray().length < 6) {

            System.out.println("SMALL: " + member + " | " + cropped);
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
        return new HySimplePlayer(name, rank, "");

    }

    public void updateParty(boolean sendPacket) {

        Thread updater = new Thread(() -> {

            if (!hyPlus.hyDiscordPresence.presenceCheck()) return;

            if (!this.party.doesExist() || !HYPLUS_PM_SHOW.getValueBoolean()) {

                hyPlus.discordApp.getRichPresence().updateParty(false, 0, 0, "");
                hyPlus.discordApp.getRichPresence().updateJoinSecret(false, null);
                hyPlus.discordApp.getRichPresence().updateRichPresence();
                return;

            }

            int max = this.party.isPublic() ? this.party.getCap() : this.party.getCount();
            int count = this.party.getCount();
            String name = this.party.getPartyLeader().getName();
            System.out.println("LEADERNAME: '" + name + "'");

            if (name.equals("#UndefinedPlayer#")) return;

            String id = MojangRequest.getUUID(name);
            hyPlus.discordApp.getRichPresence().updateParty(true, max, count, id);

            if (this.party.isPublic() && HYPLUS_PM_JOIN.getValueBoolean()) {

                generateInvite(id);

            }
            else {

                hyPlus.discordApp.getRichPresence().updateJoinSecret(false, null);

            }

            hyPlus.discordApp.getRichPresence().updateRichPresence();

            if (sendPacket) {

                System.out.println("Sending party update packet to all members :)");
                ArrayList<UUID> uuids = new ArrayList<>();
                for (HySimplePlayer player : this.party.getAllMembers()) {

                    String uuid = MojangRequest.getUUID(player.getName());
                    System.out.println("REQUEST FOR '" + player.getName() + "' returned: " + uuid);
                    try {
                        uuids.add(UUID.fromString(uuid));
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


        });
        Runtime.getRuntime().addShutdownHook(updater);
        updater.start();

    }

    private void generateInvite(String uuidJoin) {

        System.out.println(uuidJoin);
        uuidJoin = uuidJoin.substring(0,8)
                + "-"
                + uuidJoin.substring(8,12)
                + "-"
                + uuidJoin.substring(12,16)
                + "-"
                + uuidJoin.substring(16,20)
                + "-"
                + uuidJoin.substring(20,32);
        hyPlus.discordApp.getRichPresence().updateJoinSecret(true, uuidJoin);

    }


    // do we need this?
    public void sendSecrets(String matchSecret, String joinSecret, String domain) {

        // The LabyMod client sends a "INFO" message in the "LMC" channel on join
        JsonObject obj = new JsonObject();

        String uuidMatch = MojangRequest.getUUID(matchSecret);
        System.out.println(uuidMatch);
        uuidMatch = uuidMatch.substring(0,8)
                + "-"
                + uuidMatch.substring(8,12)
                + "-"
                + uuidMatch.substring(12,16)
                + "-"
                + uuidMatch.substring(16,20)
                + "-"
                + uuidMatch.substring(20,32);
        System.out.println(uuidMatch);
        String uuidJoin = MojangRequest.getUUID(joinSecret);
        System.out.println(uuidJoin);
        uuidJoin = uuidJoin.substring(0,8)
                + "-"
                + uuidJoin.substring(8,12)
                + "-"
                + uuidJoin.substring(12,16)
                + "-"
                + uuidJoin.substring(16,20)
                + "-"
                + uuidJoin.substring(20,32);

        System.out.println(uuidJoin);

        // Add all secrets
        addSecret( obj, "hasMatchSecret", "matchSecret", UUID.randomUUID(), domain );
        //addSecret( obj, "hasSpectateSecret", "spectateSecret", user.getSpectateSecret(), domain );
        addSecret( obj, "hasJoinSecret", "joinSecret", UUID.randomUUID(), domain );

        LabyMod.getInstance().getDiscordApp().onServerMessage("discord_rpc", obj);

    }

    public JsonObject addSecret(JsonObject jsonObject, String hasKey, String key, UUID secret, String domain) {
        jsonObject.addProperty( hasKey, true );
        jsonObject.addProperty( key, secret.toString() + ":" + domain );
        return jsonObject;
    }


    public String rematchInvite(UUID invite) {

        String r = MojangRequest.getName(invite.toString());
        System.out.println("JOINING TO: " + r);
        return r;

    }

}
