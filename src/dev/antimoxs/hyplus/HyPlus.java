package dev.antimoxs.hyplus;


import dev.antimoxs.hypixelapi.ApiBuilder;
import dev.antimoxs.hypixelapi.HypixelApi;
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
import dev.antimoxs.hyplus.modulesOLD.status.HyPresence;
import dev.antimoxs.hyplus.modules.HyAbout;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;
import net.labymod.addon.online.AddonInfoManager;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.*;
import java.util.List;

public class HyPlus extends LabyModAddon {

    /**
     * HyPlus by Antimoxs
     * https://addons.antimoxs.dev/hyplus
     *
     * Main class of the HyPlus Addon for LabyMod
     */

    private static HyPlus instance;

    private final String version = "0.5";
    private final String lastupdated = "19. January 2022";
    public HyAbout hyAbout = new HyAbout(
            new kvp("DevBuild :P", 3)
    );

    // settings:

    public HypixelApi hypixelApi;


    private final ArrayList<HyModule> modules = new ArrayList<>();

    // HyPlus internals
    public final HyEventManager hyEventManager = new HyEventManager();
    public final HyModuleManager hyModuleManager = new HyModuleManager();
    public final HyConfigManager hyConfigManager = new HyConfigManager();
    public final DiscordAppExtender discordApp = new DiscordAppExtender();

    // HyPlus external listener
    public final HyListenerChatMessage hyChatMessageListener = new HyListenerChatMessage();
    public final HyListenerChatSend hyChatSendListener = new HyListenerChatSend();
    public final HyListenerPacket hyPacketListener = new HyListenerPacket();
    public final HyListenerQuit hyQuitListener = new HyListenerQuit();
    public final HyListenerGuiOpen hyListenerGuiOpen = new HyListenerGuiOpen();
    public final HyListenerKeyInput hyListenerKeyInput = new HyListenerKeyInput();
    public final HyListenerDevPacket hyListenerDevPacket = new HyListenerDevPacket();

    // Other
    public final ModuleCategory hyModuleCategory = new ModuleCategory(
            "HyPlus",
            true,
            new ControlElement.IconData("textures/hyplus/HyPlus2.png")
    );
    public final HyPresence HyPresence = new HyPresence();

    // NEW HyPlus Modules
    public final HyGeneral hyGeneral = new HyGeneral();
    public final HyAdvanced hyAdvanced = new HyAdvanced();
    public final Hypixel hypixel = new Hypixel();
    public final HyTablist hyTablist = new HyTablist();
    public final HyFriend hyFriend = new HyFriend();
    public final HyBetterMsg hyBetterMsg = new HyBetterMsg();
    public final HyLocationDetector hyLocationDetector = new HyLocationDetector();
    public final HyDiscordPresence hyDiscordPresence = new HyDiscordPresence();
    public final HyQuestTracker hyQuestTracker = new HyQuestTracker();
    public final HyPlayerTagExchanger hyPlayerTagExchanger = new HyPlayerTagExchanger();
    public final HyPartyManager hyPartyManager = new HyPartyManager();

    // old modules
    // public final HyQuickPlay hyQuickPlay = new HyQuickPlay(this); // quickplay is not ready for publishing yet :(
    public final HyGameBadge hyGameBadge = new HyGameBadge();

    // LabyGUI Modules
    public HyTrackboxGUI hyTrackboxGUI;


    /**
     * Get the HyPlus version
     * @return The current version as String
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Get the date the addon was updated last
     * @return Date as String
     */
    public String getLastUpdated() {
        return this.lastupdated;
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

        // setup logger config
        AtmxLogger.useShorting(true);


        // cosmetic things - 'dies das'
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


        // register modules
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
        //hyModuleManager.registerModule(hyQuickPlay);
        hyModuleManager.registerModule(hyAdvanced);

        // load GUI Category
        ModuleCategoryRegistry.loadCategory(hyModuleCategory);
        LabyMod.getInstance().getLabyModAPI().registerModule(hyTrackboxGUI);


        // register forge event listeners
        this.getApi().registerForgeListener(hyListenerKeyInput);
        this.getApi().registerForgeListener(hyListenerGuiOpen);

        AtmxLogger.log(AtmxLogType.INFORMATION, HyPlusConfig.name, "Registered events.");

        // register server support for hypixel
        this.getApi().registerServerSupport(this, hypixel);
        AtmxLogger.log(AtmxLogType.INFORMATION, HyPlusConfig.name, "Registered Hypixel ServerSupport.");




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

        AtmxLogger.log(AtmxLogType.INFORMATION, HyPlusConfig.name, "Starting HyLoop...");

        AtmxLogger.log(AtmxLogType.INFORMATION, HyPlusConfig.name, "Setting additional information...");

        // add creator to tags
        hyPlayerTagExchanger.addSubtitle(HyPlusConfig.antimoxs());

        AtmxLogger.log(AtmxLogType.INFORMATION, HyPlusConfig.name, "Init HypixelAPI...");

        // start the hypixelAPI
        startAPI();

        AtmxLogger.log(AtmxLogType.COMPLETED, HyPlusConfig.name, "Successfully started addon. Loading config file...");

    }

    /**
     * Method to start the HypixelAPI
     */
    public void startAPI() {

        try {

            System.out.println("Starting HypixelAPI...");
            ApiBuilder builder = new ApiBuilder();
            builder.addKey(hyGeneral.getApiKey());
            builder.setApplicationName("HyPlus");
            this.hypixelApi = builder.build();

        } catch (Exception e) {

            this.hypixelApi = null;

        }

    }

    /**
     * Deprecated disable method
     */
    @Deprecated
    @Override
    public void onDisable() {

        System.out.println("\n\nDISABLED HYPLUS ---\n\n");

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
     * @param list
     */
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

                    AddonInfoManager.getInstance().createElementsForAddons();
                    Minecraft.getMinecraft().currentScreen.initGui();




                },
                "Reset",
                "Resets the config to default values",
                Color.RED
        );
        ButtonElement reload_values = new ButtonElement("Reload config",
                new ControlElement.IconData(Material.BARRIER),
                buttonElement -> {

                    loadConfig();
                    AddonInfoManager.getInstance().createElementsForAddons();
                    Minecraft.getMinecraft().currentScreen.initGui();

                },
                "Reload",
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

        // try-catch block to prevent any loop interruptions
        try {

            // lets make sure that we are on hypixel
            if (this.hypixel.checkOnServer()) {

                // module loop
                for (IHyPlusModule module : hyModuleManager.getModules()) {

                    if (!module.loop()) {

                        System.out.println("Error executing module loop for NEWmodule: " + module.getModuleName());

                    }

                }
            }

        }
        catch (Exception e) {

            // catch exception(s)
            System.out.println("An error occurred in the loop.");
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

    public void sendMessageIngameChat(String text) {

        try {
            System.out.println("[HyPlus] [OUT] [Chat]: " + text);
            LabyModCore.getMinecraft().getPlayer().sendChatMessage(text);
            //LabyModCore.getMinecraft().getConnection().addToSendQueue(new C01PacketChatMessage(text));
        } catch (NullPointerException e) {

            System.out.println("Failed IG msg sent.");

        }

    }

}
