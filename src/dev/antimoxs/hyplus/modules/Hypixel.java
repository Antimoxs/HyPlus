package dev.antimoxs.hyplus.modules;

import dev.antimoxs.hypixelapi.exceptions.ApiRequestException;
import dev.antimoxs.hypixelapi.objects.player.statGames.PSGDuels;
import dev.antimoxs.hypixelapi.requests.MojangRequest;
import dev.antimoxs.hypixelapi.response.PlayerResponse;
import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.modules.playerTagCycle.HyPlayerTag;
import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.IHyPlusEvent;
import net.labymod.api.events.TabListEvent;
import net.labymod.main.LabyMod;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;

import java.util.List;
import java.util.UUID;

public class Hypixel extends Server implements IHyPlusModule, IHyPlusEvent {

    public boolean onServer = false;

    public Hypixel() {
        super("hypixel", "mc.hypixel.net", "*.hypixel.net");
    }

    public static void checkScoreboard(Hypixel hypixel) {

        if (hypixel.onServer) {

            Scoreboard scoreboard = Minecraft.getMinecraft().theWorld.getScoreboard();




            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {

                try {
                    info.setDisplayName(new ChatComponentText(info.getGameProfile().getName()));
                    //hypixel.HyPlus.displayIgMessage("Custom", info.getResponseTime() + "");
                }
                catch (NullPointerException e) {

                    //hypixel.HyPlus.displayIgMessage("Custom", "null");

                }

            }

        }

    }

    public static void listSB(Hypixel hypixel) {

        Scoreboard sb = Minecraft.getMinecraft().theWorld.getScoreboard();
        HyPlus.getInstance().displayIgMessage("sb", "Teams:");
        for (ScorePlayerTeam team : sb.getTeams()) {

            HyPlus.getInstance().displayIgMessage("reg name", team.getRegisteredName());
            HyPlus.getInstance().displayIgMessage("name", team.getTeamName());
            HyPlus.getInstance().displayIgMessage("prefix", team.getColorPrefix());
            HyPlus.getInstance().displayIgMessage("suffix", team.getColorSuffix());
            HyPlus.getInstance().displayIgMessage("ß", "---");

        }
        HyPlus.getInstance().displayIgMessage("sb", "Objectives:");
        for (ScoreObjective ob : sb.getScoreObjectives()) {

            HyPlus.getInstance().displayIgMessage("display name", ob.getDisplayName());
            HyPlus.getInstance().displayIgMessage("name", ob.getName());
            HyPlus.getInstance().displayIgMessage("cri", ob.getCriteria().getName());
            HyPlus.getInstance().displayIgMessage("rt", ob.getRenderType().name());
            HyPlus.getInstance().displayIgMessage("ß","---");

        }
        HyPlus.getInstance().displayIgMessage("sb", "Scores:");
        for (Score s : sb.getScores()) {

            HyPlus.getInstance().displayIgMessage("name",s.getPlayerName());
            HyPlus.getInstance().displayIgMessage("points",s.getScorePoints() + "");
            HyPlus.getInstance().displayIgMessage("objective name",s.getObjective().getName());
            HyPlus.getInstance().displayIgMessage("ß","---");

        }

    }

    public static void gulugulu(Hypixel hypixel) {

        Thread gulu = new Thread(() -> {

            ThreadGroup group = new ThreadGroup("gulug");
            StringBuilder pchat = new StringBuilder();
            for (NetworkPlayerInfo info : Minecraft.getMinecraft().getNetHandler().getPlayerInfoMap()) {

                String name = MojangRequest.getName(info.getGameProfile().getId().toString());
                try {

                    info.setDisplayName(new ChatComponentText(name));

                    Thread req = new Thread(group, () -> {

                        if (HyPlus.getInstance().hypixelApi == null) {

                            HyPlus.getInstance().displayIgMessage(null, "§6§l[HyPlus]§4§l [WARN]: §7You don't have an API-Key set. Not every function is enabled.§r");
                            return;

                        }

                        try {
                            PlayerResponse res = HyPlus.getInstance().hypixelApi.createPlayerRequestUUID(info.getGameProfile().getId().toString());
                            if (res.success) {

                                StringBuilder builder = new StringBuilder();

                                PSGDuels duels = res.getPlayer().stats.Duels;
                                int plyd = duels.uhc_doubles_rounds_played;
                                int loss = duels.uhc_doubles_losses;
                                int strk = duels.uhc_doubles_winstreak_current;

                                double wl;

                                if (plyd == -1 || plyd == 0) {
                                    wl = -1.0;
                                }
                                else {
                                    if (loss == -1 || loss == 0) {

                                        wl = plyd;
                                    }
                                    else {

                                        wl = (((double)plyd-(double)loss)/(double)plyd) * 100.0;

                                    }

                                }

                                builder.append(name);
                                builder.append("(");
                                builder.append("WP: ");
                                if (wl > 30.0) builder.append("!!! ");
                                builder.append(Math.floor(wl*100)/100);
                                builder.append("% | ");
                                if (strk > 5) builder.append("!!! ");
                                builder.append("S: " + strk);

                                HyPlayerTag tag = HyPlus.getInstance().hyPlayerTagExchanger.getTagForPlayer(UUID.fromString(info.getGameProfile().getId().toString()));
                                tag.setValue(new kvp(builder.toString(), 1));
                                if (name.toLowerCase().equals("dev/antimoxs") || name.toLowerCase().equals("_ghettofish_")) {
                                    return;
                                }
                                pchat.append(builder.toString() + ")/ ");
                                //hypixel.HyPlus.sendMessageIngameChat("/pc " + builder.toString());


                            } else {

                                throw new ApiRequestException("request success is false");

                            }
                        } catch (ApiRequestException e) {
                            HyPlus.getInstance().displayIgMessage("Lookup", "Request failed for player '" + info.getGameProfile().getName() + "'. Nicked? (" + e.getReason() + ")");
                            e.printStackTrace();
                        }

                    });

                    Runtime.getRuntime().addShutdownHook(req);
                    req.start();


                    //hypixel.HyPlus.displayIgMessage("Custom", info.getResponseTime() + "");
                } catch (NullPointerException e) {

                    //hypixel.HyPlus.displayIgMessage("Custom", "null");

                }


            }

            while (group.activeCount() != 0) {}

            HyPlus.getInstance().sendMessageIngameChat("/pc " + pchat);

            //count = -1;

        });
        Runtime.getRuntime().addShutdownHook(gulu);
        gulu.start();

    }

    public static String buildSB(ScorePlayerTeam team) {

        return team.getRegisteredName() + " " + team.getColorPrefix() + " " + team.getColorSuffix();

    }

    public static String buildSB(Score obj) {

        return obj.getObjective().getDisplayName() + " " + obj.getPlayerName();

    }

    @Override
    public void onJoin(net.minecraft.client.multiplayer.ServerData serverData) {

        if (!Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().endsWith("hypixel.net")) {

            System.out.println("This is not Hypixel.");
            return;

        }

        //a5ac827a-b0ef-46df-b356-247451b6fd10

        //HyPlus.displayIgMessage(null, "Enabled Hypixel ServerSupport.");
        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("https://i.imgur.com/twFCNxQ.png", "Welcome to Hypixel!", "Enabled HyPlus server support.");

        HyPlus.getInstance().hyEventManager.callHypixelJoin();

        //HyPlus.discordApp.updateServer(serverData);


    }

    @Override
    public ChatDisplayAction handleChatMessage(String s, String s1) throws Exception {
        ChatDisplayAction cda = ChatDisplayAction.NORMAL;
        return cda;
    }

    @Override
    public void handlePluginMessage(String s, PacketBuffer packetBuffer) throws Exception {

    }

    @Override
    public void handleTabInfoMessage(TabListEvent.Type type, String s, String s1) throws Exception {

    }

    @Override
    public void fillSubSettings(List<SettingsElement> list) {



    }

    @Override
    public void addModuleLines(List<DisplayLine> lines) {
        super.addModuleLines(lines);
    }

    @Override
    public void sendPluginMessage(String channelName, Consumer<PacketBuffer> packetBufferConsumer) {
        super.sendPluginMessage(channelName, packetBufferConsumer);
    }

    @Override
    public void loopSecond() {

        if (HyPlus.getInstance() == null) { return; }

        if (checkOnServer()) {

            // We are on Hypixel

        }
        else {

            // Not on Hypixel

        }

    }

    public boolean checkOnServer() {

        if (Minecraft.getMinecraft().getCurrentServerData() == null) { return false; }

        return Minecraft.getMinecraft().getCurrentServerData().serverIP.toLowerCase().endsWith("hypixel.net");
    }

    // functions as module for general settings

    @Override
    public String getModuleName() {
        return "Hypixel - General";
    }

    @Override
    public void checkConfig(boolean reset) {

    }

    @Override
    public boolean showInSettings() {
        return IHyPlusModule.super.showInSettings();
    }

    @Override
    public List<SettingsElement> getModuleSettings() {
        return IHyPlusModule.super.getModuleSettings();
    }
}
