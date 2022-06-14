package dev.antimoxs.hyplus;


import dev.antimoxs.hypixelapiHP.ApiBuilder;
import dev.antimoxs.hypixelapiHP.HypixelApi;
import dev.antimoxs.hypixelapiHP.util.kvp;
import dev.antimoxs.hyplus.events.*;
import dev.antimoxs.hyplus.internal.discordSdk.DiscordManager;
import dev.antimoxs.hyplus.listener.*;
import dev.antimoxs.hyplus.modules.*;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsg;
import dev.antimoxs.hyplus.modules.headStats.HyHeadStats;
import dev.antimoxs.hyplus.modules.quests.HyQuestTracker;
import dev.antimoxs.hyplus.modules.quests.HyTrackboxGUI;
import dev.antimoxs.hyplus.modules.friends.HyFriend;
import dev.antimoxs.hyplus.modules.party.HyPartyManager;
import dev.antimoxs.hyplus.modules.headStats.HyPlayerTagExchanger;
import dev.antimoxs.hyplus.modules.HyAbout;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;

import java.util.*;
import java.util.List;

public class HyPlus extends LabyModAddon {

    /**
     * HyPlus by Antimoxs
     * <a href="https://addons.antimoxs.dev/hyplus">https://addons.antimoxs.dev/hyplus</a>
     * Main class of the HyPlus Addon for LabyMod
     */
    private static HyPlus instance;

    private static final String VERSION = "1.0.16";
    private static final String LASTUPDATED = "14. June 2022";
    public HyAbout hyAbout = new HyAbout(
            new kvp("Initial Release", 3)
    );
    public HypixelApi hypixelApi;

    public DiscordManager discordManager = new DiscordManager();

    // HyPlus internals
    public final HyEventManager hyEventManager = new HyEventManager();
    public final HyModuleManager hyModuleManager = new HyModuleManager();
    public final HyConfigManager hyConfigManager = new HyConfigManager();

    // old discord
    //public final DiscordAppExtender discordApp = new DiscordAppExtender();

    // HyPlus external listener
    public final HyListenerChatMessage hyChatMessageListener = new HyListenerChatMessage();
    public final HyListenerChatSend hyChatSendListener = new HyListenerChatSend();
    public final HyListenerPacket hyPacketListener = new HyListenerPacket();
    public final HyListenerQuit hyQuitListener = new HyListenerQuit();
    public final HyListenerGuiOpen hyListenerGuiOpen = new HyListenerGuiOpen();
    public final HyListenerKeyInput hyListenerKeyInput = new HyListenerKeyInput();
    public final HyListenerDevPacket hyListenerDevPacket = new HyListenerDevPacket();
    public final HyListenerPluginMessage hyListenerPluginMessage = new HyListenerPluginMessage();

    // Other
    public final Hypixel hypixel = new Hypixel();
    public final ModuleCategory hyModuleCategory = new ModuleCategory(
            "HyPlus",
            true,
            new ControlElement.IconData("textures/hyplus/HyPlus2.png")
    );

    // NEW HyPlus Modules
    public final HyGeneral hyGeneral = new HyGeneral();
    public final HyAdvanced hyAdvanced = new HyAdvanced();
    public final HyTablist hyTablist = new HyTablist();
    public final HyFriend hyFriend = new HyFriend();
    public final HyBetterMsg hyBetterMsg = new HyBetterMsg();
    public final HyLocationDetector hyLocationDetector = new HyLocationDetector();
    public final HyDiscordPresence hyDiscordPresence = new HyDiscordPresence();
    public final HyQuestTracker hyQuestTracker = new HyQuestTracker();
    public final HyPlayerTagExchanger hyPlayerTagExchanger = new HyPlayerTagExchanger(); // <- next update should fixes some general qol thingys
    public final HyPartyManager hyPartyManager = new HyPartyManager();
    public final HyHeadStats hyHeadStats = new HyHeadStats(); // <- beta

    // old modules
    // public final HyQuickPlay hyQuickPlay = new HyQuickPlay(this); // quickplay is not ready for publishing yet :(

    // LabyGUI Modules
    public HyTrackboxGUI hyTrackboxGUI;

    /**
     * Get the HyPlus version
     * @return The current version as String
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * Get the date the addon was updated last
     * @return Date as String
     */
    public String getLastUpdated() {
        return LASTUPDATED;
    }

    /**
     * Get the HyPlus instance
     * @return {@link HyPlus}
     */
    public static HyPlus getInstance() { return instance; }

    /**
     * Default method called on addon init
     */
    @Override
    public void onEnable() {

        // set the instance
        instance = this;


        // cosmetic things - 'dies das'
        System.out.println(
                "██╗  ██╗██╗   ██╗██████╗ ██╗     ██╗   ██╗███████╗\n" +
                "██║  ██║╚██╗ ██╔╝██╔══██╗██║     ██║   ██║██╔════╝\n" +
                "███████║ ╚████╔╝ ██████╔╝██║     ██║   ██║███████╗\n" +
                "██╔══██║  ╚██╔╝  ██╔═══╝ ██║     ██║   ██║╚════██║\n" +
                "██║  ██║   ██║   ██║     ███████╗╚██████╔╝███████║\n" +
                "╚═╝  ╚═╝   ╚═╝   ╚═╝     ╚══════╝ ╚═════╝ ╚══════╝\n" +
                "      Version " + VERSION + "           by Antimoxs\n" +
                "\n");

        // let's try starting the addon
        try {
            startAddon();
        } catch (Exception e) {

            System.out.println("Error while starting HyPlus. Please report this to HyPlus staff.");
            e.printStackTrace();

        }


    }





    /**
     * addon start method
     */
    public void startAddon() {

        // setting classes that need the HyPlus instance on init
        this.hyTrackboxGUI = new HyTrackboxGUI();

        // register laby event listeners
        LabyMod.getInstance().getEventManager().register(hyChatMessageListener);
        LabyMod.getInstance().getEventManager().register(hyChatSendListener);
        LabyMod.getInstance().getEventManager().registerOnIncomingPacket(hyPacketListener);
        LabyMod.getInstance().getEventManager().registerOnAddonDevelopmentPacket(hyListenerDevPacket);
        LabyMod.getInstance().getEventManager().registerOnQuit(hyQuitListener);
        LabyMod.getInstance().getEventManager().register(hyListenerPluginMessage);


        // register modules
        hyModuleManager.registerModule(hyGeneral);
        hyModuleManager.registerModule(hyAbout);
        hyModuleManager.registerModule(hyTablist);
        hyModuleManager.registerModule(hyFriend);
        hyModuleManager.registerModule(hyBetterMsg);
        hyModuleManager.registerModule(hyLocationDetector);
        hyModuleManager.registerModule(hyDiscordPresence);
        hyModuleManager.registerModule(hyQuestTracker);
        hyModuleManager.registerModule(hyPlayerTagExchanger);
        hyModuleManager.registerModule(hyPartyManager);
        hyModuleManager.registerModule(hyHeadStats);
        //hyModuleManager.registerModule(hyQuickPlay); disabled because the gui is not done yet
        hyModuleManager.registerModule(hyAdvanced);

        // load GUI Category
        ModuleCategoryRegistry.loadCategory(hyModuleCategory);
        LabyMod.getInstance().getLabyModAPI().registerModule(hyTrackboxGUI);


        // register forge event listeners
        this.getApi().registerForgeListener(hyListenerKeyInput);
        this.getApi().registerForgeListener(hyListenerGuiOpen);

        log("Registered events.");

        // register server support for hypixel
        this.getApi().registerServerSupport(this, hypixel);
        log("Registered Hypixel ServerSupport.");

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

        // add shutdown hook to make sure our loop gets closed
        Runtime.getRuntime().addShutdownHook(thread);
        thread.start();

        log("Starting HyLoop...");

        log("Setting additional information...");

        // add creator to tags
        hyPlayerTagExchanger.addSubtitle(HyPlusConfig.antimoxs());

        log("Init HypixelAPI...");

        // start the hypixelAPI
        startAPI();

        log("Successfully started addon. Loading config file...");

    }

    /**
     * Method to start the HypixelAPI
     */
    public void startAPI() {

        try {

            log("Starting HypixelAPI...");
            ApiBuilder builder = new ApiBuilder();
            builder.addKey(hyGeneral.getApiKey());
            builder.setApplicationName("HyPlus");
            this.hypixelApi = builder.build(); // <- known to cause trouble for unknown reasons lol; just build it again


        } catch (Exception e) {

            this.hypixelApi = null;

        }

    }

    /**
     * Loads the HyPlus config
     */
    @Override
    public void loadConfig() {

        hyConfigManager.loadConfig(0, false);

    }

    /**
     * Fills list with the addon-settings for laby
     */
    @Override
    protected void fillSettings(List<SettingsElement> list) {

        list.add(new HeaderElement("HyPlus settings for version " + VERSION));

        for (IHyPlusModule module : hyModuleManager.getModules()) {


            if (!module.showInSettings()) continue;
            try {
                list.addAll(module.getModuleSettings());
            } catch (Exception e) {

                try {

                    list.add(new ControlElement(module.getModuleName(), new ControlElement.IconData(Material.BARRIER)));

                } catch (Exception e2) {

                    list.add(new ControlElement("Undefined module" , new ControlElement.IconData(Material.BARRIER)));

                }
                System.err.println("Error while loading mod-settings");

            }

        }

    }

    private void loop() {

        // Check if we are enabled and loop is on.
        if (!HyGeneral.HYPLUS_GENERAL_TOGGLE.getValue()) return;
        if (!HyGeneral.HYPLUS_GENERAL_LOOP_TOGGLE.getValue()) return;

        // try-catch block to prevent any loop interruptions
        try {

            // lets make sure that we are on hypixel
            if (this.hypixel.checkOnServer()) {

                // module loop
                for (IHyPlusModule module : hyModuleManager.getModules()) {

                    if (!module.loop()) {

                        log("Error executing module loop for NEWmodule: " + module.getModuleName());

                    }

                }
            }

        }
        catch (Exception e) {

            // catch exception(s)
            log("An error occurred in the loop.");
            e.printStackTrace();

        }


    }

    // formatted display
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

    // display sth in chat
    public void sendMessageIngameChat(String text) {

        try {
            log("[OUT] [Chat]: " + text);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(text); // <- fancier lol
            //LabyModCore.getMinecraft().getConnection().addToSendQueue(new C01PacketChatMessage(text)); if code above works -> remove
        } catch (NullPointerException e) {

            log("Failed IG msg sent.");

        }

    }

    // log to console
    public void log(String s) {

        System.out.println("[HyPlus] " + s);

    }

    /**
     *
     * Debug log :)
     *
     */
    public static void debugLog(String s) {

        if (HyAdvanced.HYPLUS_ADVANCED_DEBUGLOG.getValue()) {

            HyPlus.getInstance().log(s);

        }

    }

}
