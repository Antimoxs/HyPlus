package dev.antimoxs.hyplus;

import dev.antimoxs.hyplus.events.IHyPlusEvent;
import dev.antimoxs.hyplus.objects.HyGameStatus;
import dev.antimoxs.hyplus.api.location.HyServerLocation;
import net.labymod.api.events.TabListEvent;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.moduletypes.ColoredTextModule;
import net.labymod.main.LabyMod;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class Hypixel extends Server implements IHyPlusEvent {

    // TODO: convert to non mc class (PacketBuffer, Scoreboard)

    public boolean onServer = false;

    public Hypixel() {
        super("hypixel", "hypixel.net", "hypixel.net:25565");
    }

    // dev function for testing; not used in normal use
    public static void listSB(Hypixel hypixel) {

        Scoreboard sb = LabyModCore.getMinecraft().getWorld().getScoreboard();
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

    public static HyGameStatus getGameStatus(HyServerLocation location) {

        try {

            Scoreboard sb = LabyModCore.getMinecraft().getWorld().getScoreboard();

            HyGameStatus status = new HyGameStatus();

            // setting state
            String sbName = "";
            for (ScoreObjective objective : sb.getScoreObjectives()) {

                sbName = objective.getName();


            }
            if (sbName.equals("PreScoreboard")) {

                status.state = HyGameStatus.State.PREGAME;

            } else if (location.isLobby()) {

                status.state = HyGameStatus.State.LOBBY;

            } else {

                status.state = HyGameStatus.State.INGAME;

            }

            // set timestamps
            for (ScorePlayerTeam team : sb.getTeams()) {

                if (team.getRegisteredName().startsWith("team_")) {

                    String content = team.getColorPrefix() + team.getColorSuffix();


                    if (content.contains("§a")) {

                        int idx = content.indexOf("§a");

                        //HyPlus.getInstance().displayIgMessage("Checking sb: ", content.substring(idx));

                        // checking for letters (not all cause all words used in this context are not abstract; ex. maps contain always a vowel)
                        String stripped = HyUtilities.matchOutColorCode(content.substring(idx));
                        if (stripped.contains("a")) continue;
                        if (stripped.contains("e")) continue;
                        if (stripped.contains("i")) continue;
                        if (stripped.contains("o")) continue;
                        if (stripped.contains("u")) continue;
                        if (stripped.contains("t")) continue;
                        if (stripped.contains("m")) continue;
                        if (stripped.contains("k")) continue;
                        if (stripped.contains("l")) continue;

                        String time = HyUtilities.matchOutColorCode(content.substring(idx).split(" ")[0]);

                        if (stripped.contains(":")) {

                            //HyPlus.getInstance().displayIgMessage("TimeDetection stamp", time);
                            int mins = Integer.parseInt(time.substring(0, time.indexOf(":")));
                            int secs = Integer.parseInt(time.substring(time.indexOf(":") + 1));
                            status.endingTimestamp = status.state == HyGameStatus.State.PREGAME ? 0L : System.currentTimeMillis() + ((long) mins * 60 * 1000) + (secs * 1000L);
                            status.startingTimestamp = status.state != HyGameStatus.State.PREGAME ? 0L : System.currentTimeMillis() + ((long) mins * 60 * 1000) + (secs * 1000L);

                        } else if (stripped.contains("s")) {

                            //HyPlus.getInstance().displayIgMessage("TimeDetection sec", time);
                            int secs = Integer.parseInt(time.substring(0, time.indexOf("s")));
                            status.endingTimestamp = status.state == HyGameStatus.State.PREGAME ? 0L : System.currentTimeMillis() + (secs * 1000L);
                            status.startingTimestamp = status.state != HyGameStatus.State.PREGAME ? 0L : System.currentTimeMillis() + (secs * 1000L);

                        }

                    }

                }


            }

            return status;

        }
        catch (NullPointerException e) {

            return new HyGameStatus();

        }

    }

    @Override
    public void onJoin(net.minecraft.client.multiplayer.ServerData serverData) {

        String mcA = Minecraft.getMinecraft().getCurrentServerData().serverIP;
        String evA = serverData.serverIP;

        if (!evA.equals(mcA)) HyPlus.debugLog("mca and eva are unequal.");

        if (!checkServerAddress(serverData.serverIP)) {

            HyPlus.debugLog("This is not Hypixel.");
            return;

        }

        //a5ac827a-b0ef-46df-b356-247451b6fd10

        //HyPlus.displayIgMessage(null, "Enabled Hypixel ServerSupport.");
        LabyMod.getInstance().getGuiCustomAchievement().displayAchievement("https://i.imgur.com/twFCNxQ.png", "Welcome to Hypixel!", "Enabled HyPlus server support.");

        HyPlus.getInstance().hyEventManager.callHypixelJoin();

        //HyPlus.discordApp.updateServer(serverData);


    }

    @Override
    public ChatDisplayAction handleChatMessage(String s, String s1) {
        return ChatDisplayAction.NORMAL;
    }

    @Override
    public void handlePluginMessage(String s, PacketBuffer packetBuffer) {

    }

    @Override
    public void handleTabInfoMessage(TabListEvent.Type type, String s, String s1) {



    }

    @Override
    public void fillSubSettings(List<SettingsElement> list) {



    }

    @Override
    public void addModuleLines(List<DisplayLine> lines) {
        ArrayList<ColoredTextModule.Text> texts = new ArrayList<>();
        texts.add(ColoredTextModule.Text.getText("HyPlus"));
        lines.add(new DisplayLine("Server support: ", texts));
        super.addModuleLines(lines);
    }

    @Override
    public void sendPluginMessage(String channelName, Consumer<PacketBuffer> packetBufferConsumer) {
        super.sendPluginMessage(channelName, packetBufferConsumer);
    }

    @Override
    public void loopSecond() {



    }

    public boolean checkOnServer() {

        if (Minecraft.getMinecraft().getCurrentServerData() == null) { return false; }
        return checkServerAddress(Minecraft.getMinecraft().getCurrentServerData().serverIP);

    }

    /**
     * Check function to combine all checks into one
     * @param address current server address as {@link String}
     * @return Whether we are currently on hypixel
     */
    public static boolean checkServerAddress(String address) {

        //HyPlus.debugLog("Checking for Hypixel: '" + address + "'"); do not contain in live build :)
        if (address.contains(":")) {
            address = address.split(":")[0];

        }
        return address.toLowerCase().endsWith("hypixel.net");

    }

}
