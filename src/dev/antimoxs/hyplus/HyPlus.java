package dev.antimoxs.hyplus;


import dev.antimoxs.hypixelapi.ApiBuilder;
import dev.antimoxs.hypixelapi.TBCHypixelApi;
import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.events.*;
import dev.antimoxs.hyplus.internal.discordapp.DiscordAppExtender;
import dev.antimoxs.hyplus.modules.*;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsg;
import dev.antimoxs.hyplus.modules.challengeTracker.HyQuestTracker;
import dev.antimoxs.hyplus.modules.challengeTracker.HyTrackboxGUI;
import dev.antimoxs.hyplus.modules.friends.HyFriend;
import dev.antimoxs.hyplus.modulesOLD.gameBadge.HyGameBadge;
import dev.antimoxs.hyplus.modules.partyManager.HyPartyManager;
import dev.antimoxs.hyplus.modules.playerTagCycle.HyPlayerTagExchanger;
import dev.antimoxs.hyplus.modules.quickplay.HyQuickPlay;
import dev.antimoxs.hyplus.modulesOLD.status.HyPresence;
import dev.antimoxs.hyplus.modules.HyAbout;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.awt.*;
import java.util.*;
import java.util.List;

public class HyPlus extends LabyModAddon {

    //public static String RSC_LOC = "textures/hyplus";

    private final String version = "0.3.130";
    private final String lastupdated = "17. September 2021";
    public HyAbout hyAbout = new HyAbout(this,
            new kvp("DevBuild :P", 3)
    );

    // settings:

    HyPlusConfig config;
    public TBCHypixelApi tbcHypixelApi;

    boolean running = false;
    boolean deactivated = false;

    private final ArrayList<HyModule> modules = new ArrayList<>();


    // HyPlus internals
    public final HyEventManager hyEventManager = new HyEventManager(this);
    public final HyModuleManager hyModuleManager = new HyModuleManager(this);
    public final HyConfigManager hyConfigManager = new HyConfigManager(this);
    public final DiscordAppExtender discordApp = new DiscordAppExtender(this);

    // HyPlus external listener
    public final HyListenerChatMessage hyChatMessageListener = new HyListenerChatMessage(this);
    public final HyListenerChatSend hyChatSendListener = new HyListenerChatSend(this);
    public final HyListenerPacket hyPacketListener = new HyListenerPacket(this);
    public final HyListenerQuit hyQuitListener = new HyListenerQuit(this);
    public final HyListenerGuiOpen hyListenerGuiOpen = new HyListenerGuiOpen();
    public final HyListenerKeyInput hyListenerKeyInput = new HyListenerKeyInput(this);
    public final HyListenerDevPacket hyListenerDevPacket = new HyListenerDevPacket(this);

    // Other
    public final ModuleCategory hyModuleCategory = new ModuleCategory(
            "HyPlus",
            true,
            new ControlElement.IconData("textures/hyplus/HyPlus2.png")
    );
    public final HyPresence HyPresence = new HyPresence(this);

    // NEW HyPlus Modules
    public final HyGeneral hyGeneral = new HyGeneral(this);
    public final HyAdvanced hyAdvanced = new HyAdvanced(this);
    public final Hypixel hypixel = new Hypixel(this);
    public final HyTablist hyTablist = new HyTablist(this);
    public final HyFriend hyFriend = new HyFriend(this);
    public final HyBetterMsg hyBetterMsg = new HyBetterMsg(this);
    public final HyLocationDetector hyLocationDetector = new HyLocationDetector(this);
    public final HyDiscordPresence hyDiscordPresence = new HyDiscordPresence(this);
    public final HyQuestTracker hyQuestTracker = new HyQuestTracker(this);
    public final HyPlayerTagExchanger hyPlayerTagExchanger = new HyPlayerTagExchanger(this);
    public final HyPartyManager hyPartyManager = new HyPartyManager(this);

    // old modules
    public final HyQuickPlay hyQuickPlay = new HyQuickPlay(this);
    public final HyGameBadge hyGameBadge = new HyGameBadge(this);

    // LabyGUI Modules
    public final HyTrackboxGUI hyTrackboxGUI = new HyTrackboxGUI(this);

    public String getVersion() {
        return this.version;
    }
    public String getLastUpdated() {
        return this.lastupdated;
    }

    @Override
    public void onEnable() {

        AtmxLogger.useShorting(true);


        AtmxLogger.log(AtmxLogType.SYSTEM, HyPlusConfig.name, "Starting up HyPlus....\n");

        System.out.println(
                "██╗  ██╗██╗   ██╗██████╗ ██╗     ██╗   ██╗███████╗\n" +
                "██║  ██║╚██╗ ██╔╝██╔══██╗██║     ██║   ██║██╔════╝\n" +
                "███████║ ╚████╔╝ ██████╔╝██║     ██║   ██║███████╗\n" +
                "██╔══██║  ╚██╔╝  ██╔═══╝ ██║     ██║   ██║╚════██║\n" +
                "██║  ██║   ██║   ██║     ███████╗╚██████╔╝███████║\n" +
                "╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚══════╝ ╚═════╝ ╚══════╝\n" +
                "      Version " + version + "           by Antimoxs\n" +
                "\n");

        try {
            startAddon();
            running = true;
        } catch (Exception e) {

            System.out.println("Error while starting HyPlus. Please report this to HyPlus staff.");
            e.printStackTrace();

        }

        //LabyMod.getInstance().getUserManager().getUser(uuid).setDailyEmoteFlat(true);


    }

    public void startAddon() {

        // HyPlus event listeners
        LabyMod.getInstance().getEventManager().register(hyChatMessageListener);
        LabyMod.getInstance().getEventManager().register(hyChatSendListener);
        LabyMod.getInstance().getEventManager().registerOnIncomingPacket(hyPacketListener);
        LabyMod.getInstance().getEventManager().registerOnAddonDevelopmentPacket(hyListenerDevPacket);
        LabyMod.getInstance().getEventManager().registerOnQuit(hyQuitListener);


        hyModuleManager.registerModule(hyGeneral);
        hyModuleManager.registerModule(hyAbout);
        hyModuleManager.registerModule(hypixel);
        hyModuleManager.registerModule(hyTablist);
        hyModuleManager.registerModule(hyFriend);
        hyModuleManager.registerModule(hyBetterMsg);
        hyModuleManager.registerModule(hyLocationDetector);
        hyModuleManager.registerModule(hyDiscordPresence);
        hyModuleManager.registerModule(hyQuestTracker);
        hyModuleManager.registerModule(hyPlayerTagExchanger);
        hyModuleManager.registerModule(hyPartyManager);
        hyModuleManager.registerModule(hyQuickPlay);
        hyModuleManager.registerModule(hyAdvanced);

        // load GUI Category
        ModuleCategoryRegistry.loadCategory(hyModuleCategory);
        LabyMod.getInstance().getLabyModAPI().registerModule(hyTrackboxGUI);


        this.getApi().registerForgeListener(hyListenerKeyInput);
        this.getApi().registerForgeListener(hyListenerGuiOpen);

        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Registered events.");

        this.getApi().registerServerSupport(this, hypixel);
        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Registered Hypixel ServerSupport.");


        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Indexing modules...");


        // HyLoop for all automated systems

        Thread thread = new Thread(() -> {

            TimerTask t = new TimerTask() {
                @Override
                public void run() {
                    loop();
                }
            };
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(t, 5000L, 1000L);

        });
        Runtime.getRuntime().addShutdownHook(thread);
        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Starting HyLoop...");
        thread.start();

        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Setting additional information...");
        hyPlayerTagExchanger.addSubtitle(HyPlusConfig.antimoxs());

        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Init HypixelAPI...");
        startAPI();

        AtmxLogger.log(AtmxLogType.COMPLETED, config.name, "Successfully started addon. Loading config file...");

    }


    public void startAPI() {

        try {

            System.out.println("Starting HypixelAPI...");
            ApiBuilder builder = new ApiBuilder();
            builder.addKey(hyGeneral.getApiKey());
            builder.setApplicationName("HyPlus");
            this.tbcHypixelApi = builder.build();

        } catch (Exception e) {

            this.tbcHypixelApi = null;

        }

    }

    @Override
    public void onDisable() {

        System.out.println("\n\nDISABLED HYPLUS ---\n\n");
        running = false;

    }

    @Override
    public void loadConfig() {

        hyConfigManager.loadConfig(0, false);

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

        list.add(new HeaderElement("HyPlus settings for version " + version));

        for (IHyPlusModule module : hyModuleManager.getModules()) {


            if (!module.showInSettings()) continue;
            try {
                list.addAll(module.getModuleSettings());
            } catch (Exception e) {

                try {

                    list.add(new ControlElement(module.getModuleName(), new ControlElement.IconData(Material.BARRIER)));

                } catch (Exception e2) {

                    list.add(new HeaderElement("Undefined module"));

                }
                System.err.println("Error while loading mod-settings");

            }

        }

        list.add(new HeaderElement("§4Danger Zone"));
        ButtonElement reset_values = new ButtonElement("Reset to default",
                new ControlElement.IconData(Material.BARRIER),
                buttonElement -> {

                    hyConfigManager.loadConfig(0, true);

                    Minecraft.getMinecraft().currentScreen.initGui();



                },
                "RESET",
                "Resets the config to default values",
                Color.RED
        );
        ButtonElement reload_values = new ButtonElement("Reload config",
                new ControlElement.IconData(Material.BARRIER),
                buttonElement -> {

                    loadConfig();
                    Minecraft.getMinecraft().currentScreen.initGui();

                },
                "RELOAD",
                "Reloads the HyPlus config",
                Color.GREEN
        );
        list.add(reset_values);
        list.add(reload_values);


    }

    private void loop() {

        // Check if we are enabled and loop is on.
        if (!hyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return;
        if (!hyGeneral.HYPLUS_GENERAL_LOOP_TOGGLE.getValueBoolean()) return;

        try {
            if (this.hypixel.checkOnServer()) {
                for (IHyPlusModule module : hyModuleManager.getModules()) {

                    if (!module.loop()) {

                        System.out.println("Error executing module loop for NEWmodule: " + module.getModuleName());

                    }

                }
            } else {

                if (!deactivated) {

                    //for (HyModule module : modules) { module.deactivate(); }
                    deactivated = true;

                }

            }

        }
        catch (Exception e) {

            System.out.println("An error occurred in the loop.");
            e.printStackTrace();

        }


    }

    public void displayIgMessage(String module, String text) {

        if (module == null) {

            api.displayMessageInChat(text);
            return;

        }
        StringBuilder builder = new StringBuilder();
        builder.append("§6§l[H+] ");
        builder.append("§b[").append(module).append("] ");
        builder.append("§f");
        builder.append(text);
        builder.append("§r");

        api.displayMessageInChat(builder.toString());

    }

    public void sendMessageIngameChat(String text) {

        try {
            System.out.println("[HyPlus] [OUT] [Chat]: " + text);
            LabyModCore.getMinecraft().getConnection().addToSendQueue(new C01PacketChatMessage(text));
        } catch (NullPointerException e) {

            System.out.println("Failed IG msg sent.");

        }

    }

}
