package dev.antimoxs.hyplus;


import dev.antimoxs.hypixelapi.ApiBuilder;
import dev.antimoxs.hypixelapi.TBCHypixelApi;
import dev.antimoxs.hypixelapi.util.kvp;
import dev.antimoxs.hyplus.events.*;
import dev.antimoxs.hyplus.internal.discordapp.DiscordAppExtender;
import dev.antimoxs.hyplus.modules.HyLocationDetector;
import dev.antimoxs.hyplus.modules.HyTablist;
import dev.antimoxs.hyplus.modules.IHyPlusModule;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsg;
import dev.antimoxs.hyplus.modules.betterMsg.HyBetterMsgType;
import dev.antimoxs.hyplus.modules.challengeTracker.HyQuestTracker;
import dev.antimoxs.hyplus.modules.challengeTracker.HyTrackboxGUI;
import dev.antimoxs.hyplus.modulesOLD.discord.HyPlay;
import dev.antimoxs.hyplus.modules.friends.HyFriend;
import dev.antimoxs.hyplus.modulesOLD.gameBadge.HyGameBadge;
import dev.antimoxs.hyplus.modules.HyDiscordPresence;
import dev.antimoxs.hyplus.modules.partyDetector.HyPartyManager;
import dev.antimoxs.hyplus.modules.playerTagCycle.HyPlayerTagExchanger;
import dev.antimoxs.hyplus.modulesOLD.quickplay.HyQuickPlay;
import dev.antimoxs.hyplus.modulesOLD.status.HyPresence;
import dev.antimoxs.hyplus.modulesOLD.updateLog.HyAbout;
import dev.antimoxs.hyplus.objects.ButtonElement;
import dev.antimoxs.utilities.logger.AtmxLogType;
import dev.antimoxs.utilities.logger.AtmxLogger;
import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.ingamegui.ModuleCategory;
import net.labymod.ingamegui.ModuleCategoryRegistry;
import net.labymod.main.LabyMod;
import net.labymod.settings.LabyModAddonsGui;
import net.labymod.settings.elements.*;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C01PacketChatMessage;

import java.awt.*;
import java.util.*;
import java.util.List;

public class HyPlus extends LabyModAddon {

    //public static String RSC_LOC = "textures/hyplus";

    private final String version = "0.3.42";
    private final String lastupdated = "5. September 2021";
    public HyAbout hyAbout = new HyAbout(this,
            new kvp("DevBuild :P", 3)
    );

    // settings:

    HyPlusConfig config;
    public TBCHypixelApi tbcHypixelApi;

    boolean running = false;
    boolean updateCheck = false;
    boolean deactivated = false;

    private final ArrayList<HyModule> modules = new ArrayList<>();
    private final ArrayList<IHyPlusModule> NEWmodules = new ArrayList<>();

    // HyPlus internals
    public final HySettings hySettings = new HySettings(this);
    public final HyEventManager hyEventManager = new HyEventManager(this);
    public final DiscordAppExtender discordApp = new DiscordAppExtender(this);

    // HyPlus external listener
    public final HyListenerChatMessage hyChatMessageListener = new HyListenerChatMessage(this);
    public final HyListenerChatSend hyChatSendListener = new HyListenerChatSend(this);
    public final HyListenerPacket hyPacketListener = new HyListenerPacket(this);
    public final HyListenerQuit hyQuitListener = new HyListenerQuit(this);

    // Other
    public final Hypixel hypixel = new Hypixel(this);
    public final ModuleCategory hyModuleCategory = new ModuleCategory(
            "HyPlus",
            true,
            new ControlElement.IconData("textures/hyplus/HyPlus2.png")
    );
    public final HyPresence HyPresence = new HyPresence(this);
    public final HyPlay hyPlay = new HyPlay(this);

    // NEW HyPlus Modules
    public final HyTablist hyTablist = new HyTablist(this);
    public final HyFriend hyFriend = new HyFriend(this);
    public final HyBetterMsg hyBetterMsg = new HyBetterMsg(this);
    public final HyLocationDetector hyLocationDetector = new HyLocationDetector(this);
    public final HyDiscordPresence hyDiscordPresence = new HyDiscordPresence(this);
    public final HyQuestTracker hyQuestTracker = new HyQuestTracker(this);
    public final HyPlayerTagExchanger hyPlayerTagExchanger = new HyPlayerTagExchanger(this);

    // HyPlusCode Modules
    public final HyPartyManager hyPartyDetector = new HyPartyManager(this);

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
        LabyMod.getInstance().getEventManager().registerOnQuit(hyQuitListener);

        hyEventManager.register(hypixel);
        hyEventManager.register(hyTablist);
        hyEventManager.register(hyFriend);
        hyEventManager.register(hyBetterMsg);
        hyEventManager.register(hyLocationDetector);
        hyEventManager.register(hyDiscordPresence);
        hyEventManager.register(hyQuestTracker);
        hyEventManager.register(hyPlayerTagExchanger);
        hyEventManager.register(hyPartyDetector);

        NEWmodules.add(hyTablist);
        NEWmodules.add(hyFriend);
        NEWmodules.add(hyBetterMsg);
        NEWmodules.add(hyLocationDetector);
        NEWmodules.add(hyDiscordPresence);
        NEWmodules.add(hyQuestTracker);
        NEWmodules.add(hyPlayerTagExchanger);
        NEWmodules.add(hyPartyDetector);

        // load GUI Category
        ModuleCategoryRegistry.loadCategory(hyModuleCategory);
        LabyMod.getInstance().getLabyModAPI().registerModule(hyTrackboxGUI);


        this.getApi().registerForgeListener(hyQuickPlay);

        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Registered events.");

        this.getApi().registerServerSupport(this, hypixel);
        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Registered Hypixel ServerSupport.");


        //TBCLogger.log(TBCLoggingType.INFORMATION, config.name, "Enabling TBCHypixelAPI...");
        //startAPI();



        /*Runtime.getRuntime().addShutdownHook(new Thread(() -> {

            while (running) {

                System.out.println("loop");
                loop();

            }

        }));*/

        AtmxLogger.log(AtmxLogType.INFORMATION, config.name, "Indexing modules...");


        //modules.add(hyGameDetector);
        //modules.add(hyPartyDetector);
        //modules.add(hyFriend);
        modules.add(hyQuickPlay);
        //modules.add(hyBetterMsg);
        //modules.add(hyQuestTracker);
        //modules.add(hyPlayerTagExchanger);
        modules.add(hyGameBadge);

        // HyLoop for all automated systems

        Thread thread = new Thread(() -> {

            TimerTask t = new TimerTask() {
                @Override
                public void run() {

                    if (!hySettings.HYPLUS_GENERAL_LOOP_TOGGLE) return;
                    try {
                        loop();
                    } catch (Exception e) {

                        System.out.println("An error occurred in the loop.");
                        e.printStackTrace();

                    }
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


        AtmxLogger.log(AtmxLogType.COMPLETED, config.name, "Successfully started addon. Loading config file...");

    }


    public void startAPI() {

        try {

            System.out.println("Starting HypixelAPI...");
            ApiBuilder builder = new ApiBuilder();
            builder.addKey(hySettings.HYPLUS_GENERAL_APIKEY);
            builder.setApplicationName("HyPlus");
            this.tbcHypixelApi = builder.build();

        } catch (Exception e) {

            this.tbcHypixelApi = null;

        }

    }

    @Override
    public void onDisable() {

        System.out.println("DISABLED");
        running = false;

    }

    @Override
    public void loadConfig() {

        loadConfig(0, false);

    }

    public void loadConfig(int retry, boolean reset) {

        if (retry >= 10) {

            onDisable();
            System.out.println("--- Unable to properly read HyPlus config file. ---");
            return;

        }

        // Check if all needed config entries are set to prevent errors
        checkConfig(reset, "HYPLUS_GENERAL_TOGGLE", true);
        checkConfig(reset, "HYPLUS_GENERAL_LOOP_TOGGLE", true);
        checkConfig(reset, "HYPLUS_GENERAL_APIKEY", "");
        checkConfig(reset, "HYPLUS_API_TOGGLE", false);
        checkConfig(reset, "HYPLUS_ABOUT_UPDATE", true);
        checkConfig(reset, "HYPLUS_ABOUT", 0);
        checkConfig(reset, "HYPLUS_VERSION", "-");
        checkConfig(reset, "HYPLUS_STATUS_INTERVAL", 5);
        checkConfig(reset, "HYPLUS_STATUS_ENABLE", true);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_TOGGLE", true);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_NON", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_VIP", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_VIPP", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVP", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVPP", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_MVPPP", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_YT", 0);
        checkConfig(reset, "HYPLUS_AUTOFRIEND_AA_STAFF", 0);
        checkConfig(reset, "HYPLUS_DUELS_ANTIBLUR", 0);
        checkConfig(reset, "HYPLUS_QUICKPLAY_TOGGLE", true);
        checkConfig(reset, "HYPLUS_QUICKPLAY_GUIKEY", -1);
        checkConfig(reset, "HYPLUS_AUTOGG_TOGGLE", true);
        checkConfig(reset, "HYPLUS_BETTERMSG_TOGGLE", true);
        checkConfig(reset, "HYPLUS_BETTERMSG_STYLE", "SWITCH");

        checkConfig(reset, "HYPLUS_LD_TOGGLE", true);
        checkConfig(reset, "HYPLUS_LD_API", false);

        checkConfig(reset, "HYPLUS_DP_TOGGLE", true);
        checkConfig(reset, "HYPLUS_DP_DELAY", 0);
        checkConfig(reset, "HYPLUS_DP_GAME", true);
        checkConfig(reset, "HYPLUS_DP_MODE", true);
        checkConfig(reset, "HYPLUS_DP_LOBBY", true);
        checkConfig(reset, "HYPLUS_DP_MAP", true);
        checkConfig(reset, "HYPLUS_DP_TIME", true);
        checkConfig(reset, "HYPLUS_DP_SPECIFIC", true);

        checkConfig(reset, "HYPLUS_HPD_TOGGLE", false);
        checkConfig(reset, "HYPLUS_CTR_TOGGLE", true);
        checkConfig(reset, "HYPLUS_CTR_DAILY", true);
        checkConfig(reset, "HYPLUS_CTR_WEEKLY", true);
        checkConfig(reset, "HYPLUS_CTR_COMPLETED", true);
        checkConfig(reset, "HYPLUS_CTR_SORTORDER", true);
        checkConfig(reset, "HYPLUS_CTR_DP_DAILY", true);
        checkConfig(reset, "HYPLUS_CTR_DP_WEEKLY", true);
        checkConfig(reset, "HYPLUS_PTC_TOGGLE", true);
        checkConfig(reset, "HYPLUS_PTC_CHANGER", true);
        checkConfig(reset, "HYPLUS_PTC_INTERVAL", 3);

        if (updateCheck) {

            updateCheck = false;
            System.out.println("Some properties updated, reloading!");
            loadConfig();
            return;

        }

        try {


            // transferring values from config to settings-class

            hySettings.HYPLUS_GENERAL_TOGGLE = getConfig().get("HYPLUS_GENERAL_TOGGLE").getAsBoolean();
            hySettings.HYPLUS_GENERAL_LOOP_TOGGLE = getConfig().get("HYPLUS_GENERAL_LOOP_TOGGLE").getAsBoolean();


            if (getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString().equals("")) {

                hySettings.HYPLUS_GENERAL_APIKEY = null;

            } else if (!getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString().equals(hySettings.HYPLUS_GENERAL_APIKEY)) {

                hySettings.HYPLUS_GENERAL_APIKEY = getConfig().get("HYPLUS_GENERAL_APIKEY").getAsString();
                startAPI();

            }

            // External API
            hySettings.HYPLUS_API_TOGGLE = getConfig().get("HYPLUS_API_TOGGLE").getAsBoolean();


            // About
            hySettings.HYPLUS_ABOUT_UPDATE = true;


            // AutoFriend module
            hyFriend.HYPLUS_AUTOFRIEND_TOGGLE = getConfig().get("HYPLUS_AUTOFRIEND_TOGGLE").getAsBoolean();


            hyFriend.HYPLUS_AUTOFRIEND_AA_NON = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_NON");
            hyFriend.HYPLUS_AUTOFRIEND_AA_VIP = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_VIP");
            hyFriend.HYPLUS_AUTOFRIEND_AA_VIPP = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_VIPP");
            hyFriend.HYPLUS_AUTOFRIEND_AA_MVP = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_MVP");
            hyFriend.HYPLUS_AUTOFRIEND_AA_MVPP = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_MVPP");
            hyFriend.HYPLUS_AUTOFRIEND_AA_MVPPP = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_MVPPP");
            hyFriend.HYPLUS_AUTOFRIEND_AA_YT = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_YT");
            hyFriend.HYPLUS_AUTOFRIEND_AA_STAFF = HyUtilities.validateCheckbox(this, "HYPLUS_AUTOFRIEND_AA_STAFF");

            // Duels
            hySettings.HYPLUS_DUELS_ANTIBLUR = getConfig().get("HYPLUS_DUELS_ANTIBLUR").getAsBoolean();

            // Quickplay
            hySettings.HYPLUS_QUICKPLAY_TOGGLE = getConfig().get("HYPLUS_QUICKPLAY_TOGGLE").getAsBoolean();
            hySettings.HYPLUS_QUICKPLAY_GUIKEY = getConfig().get("HYPLUS_QUICKPLAY_GUIKEY").getAsInt();

            // BetterMSG
            hyBetterMsg.HYPLUS_BETTERMSG_TOGGLE = getConfig().get("HYPLUS_BETTERMSG_TOGGLE").getAsBoolean();
            hyBetterMsg.HYPLUS_BETTERMSG_STYLE = HyBetterMsgType.getByName(getConfig().get("HYPLUS_BETTERMSG_STYLE").getAsString());

            // GameDetector
            hyDiscordPresence.HYPLUS_DP_TOGGLE =    getConfig().get("HYPLUS_DP_TOGGLE").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_DELAY =     getConfig().get("HYPLUS_DP_DELAY").getAsInt();
            hyDiscordPresence.HYPLUS_DP_GAME =      getConfig().get("HYPLUS_DP_GAME").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_MODE =      getConfig().get("HYPLUS_DP_MODE").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_LOBBY =     getConfig().get("HYPLUS_DP_LOBBY").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_MAP =       getConfig().get("HYPLUS_DP_MAP").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_TIME =      getConfig().get("HYPLUS_DP_TIME").getAsBoolean();
            hyDiscordPresence.HYPLUS_DP_SPECIFIC =  getConfig().get("HYPLUS_DP_SPECIFIC").getAsBoolean();

            // LocationDetection
            hyLocationDetector.HYPLUS_LD_TOGGLE =   getConfig().get("HYPLUS_LD_TOGGLE").getAsBoolean();
            hyLocationDetector.HYPLUS_LD_API =      getConfig().get("HYPLUS_LD_API").getAsBoolean();

            // PartyDetector
            hySettings.HYPLUS_HPD_TOGGLE = getConfig().get("HYPLUS_HPD_TOGGLE").getAsBoolean();

            // ChallengeTracker
            hyQuestTracker.HYPLUS_CTR_TOGGLE = getConfig().get("HYPLUS_CTR_TOGGLE").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_DAILY = getConfig().get("HYPLUS_CTR_DAILY").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_WEEKLY = getConfig().get("HYPLUS_CTR_WEEKLY").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_COMPLETED = getConfig().get("HYPLUS_CTR_COMPLETED").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_SORTORDER = getConfig().get("HYPLUS_CTR_SORTORDER").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_DP_DAILY = getConfig().get("HYPLUS_CTR_DP_DAILY").getAsBoolean();
            hyQuestTracker.HYPLUS_CTR_DP_WEEKLY = getConfig().get("HYPLUS_CTR_DP_WEEKLY").getAsBoolean();

            // PlayerTagChanger
            hyPlayerTagExchanger.HYPLUS_PTC_TOGGLE = getConfig().get("HYPLUS_PTC_TOGGLE").getAsBoolean();
            hyPlayerTagExchanger.HYPLUS_PTC_CHANGER = getConfig().get("HYPLUS_PTC_CHANGER").getAsBoolean();
            hyPlayerTagExchanger.HYPLUS_PTC_INTERVAL = getConfig().get("HYPLUS_PTC_INTERVAL").getAsInt();


        } catch (Exception e) {

            System.out.println("Can't load one of the fields, retrying. If this keeps happening restart your client.");
            loadConfig(retry + 1, false);
            return;

        }


        System.out.println("Config loaded.");

        //this.onDisable();
        //this.onEnable();

        //"f0678253-89db-45bb-9730-b5eb952481ea"


    }


    private void checkConfig(boolean reset, String property, Object defaultValue) {

        //System.out.println("Checking for '" + property + "'...");

        if (getConfig().has(property)) {

            //TBCLogger.log(TBCLoggingType.INFORMATION, config.name, "Loaded '" + property + "'");
            if (reset) {

                getConfig().remove(property);
                checkConfig(true, property, defaultValue);
                return;

            }

            System.out.println("Loaded '" + property + "'.");

        } else {

            if (reset) {

                System.out.println("Property '" + property + "' were put to default.");

            }
            else {

                System.out.println("Property '" + property + "' not yet in config, creating!");

            }


            if (defaultValue instanceof String) {
                getConfig().addProperty(property, (String) defaultValue);
            } else if (defaultValue instanceof Integer) {
                getConfig().addProperty(property, (int) defaultValue);
            } else if (defaultValue instanceof Boolean) {
                getConfig().addProperty(property, (boolean) defaultValue);
            } else if (defaultValue instanceof Character) {
                getConfig().addProperty(property, (char) defaultValue);
            } else {
                System.out.println("uhm this is not indented nor wanted. fix asap? " + property + " " + defaultValue);
            }
            updateCheck = true;

        }

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {

        list.add(new HeaderElement("HyPlus settings for version " + version));
        list.add(new BooleanElement("All features", this, new ControlElement.IconData(Material.REDSTONE), "HYPLUS_GENERAL_TOGGLE", true));
        list.add(new BooleanElement("HyLoop", this, new ControlElement.IconData(Material.FISHING_ROD), "HYPLUS_GENERAL_LOOP_TOGGLE", true));
        list.add(new StringElement("Hypixel API-Key", this, new ControlElement.IconData(Material.REDSTONE), "HYPLUS_GENERAL_APIKEY", hySettings.HYPLUS_GENERAL_APIKEY));
        list.addAll(this.hyAbout.getModuleSettings());
        list.add(new HeaderElement("HyModules:"));

        for (HyModule module : modules) {

            //list.add(new HeaderElement(module.getModuleName()));
            try {
                //list.addAll(module.getModuleSettings());
            } catch (Exception e) {

                try {

                    list.add(new ControlElement(module.getModuleName(), new ControlElement.IconData(Material.BARRIER)));

                } catch (Exception e2) {

                    list.add(new HeaderElement("Undefined module"));

                }
                System.err.println("Error while loading mod-settings");

            }

        }

        list.add(new HeaderElement("Updated Modules"));
        for (IHyPlusModule module : NEWmodules) {


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

                    loadConfig(0, true);
                    loadConfig();
                    Minecraft.getMinecraft().displayGuiScreen(new LabyModAddonsGui());



                },
                "RESET",
                "Resets the config to default values",
                Color.RED
        );
        ButtonElement reload_values = new ButtonElement("Reload config",
                new ControlElement.IconData(Material.BARRIER),
                buttonElement -> loadConfig(),
                "RELOAD",
                "Reloads the HyPlus config",
                Color.GREEN
        );
        list.add(reset_values);
        list.add(reload_values);


    }

    private void loop() {

        if (this.hypixel.checkOnServer()) {
            for (HyModule module : modules) {

                deactivated = false;


                if (!module.loop()) {

                    System.out.println("Error executing module loop for module: " + module.getModuleName());

                }

            }
            for (IHyPlusModule module : NEWmodules) {

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

    public void changeConfigValue(String property, Object value) {

        if (getConfig().has(property)) {

            getConfig().remove(property);

        }


        if (value instanceof String) {
            getConfig().addProperty(property, (String) value);
        } else if (value instanceof Integer) {
            getConfig().addProperty(property, (int) value);
        } else if (value instanceof Boolean) {
            getConfig().addProperty(property, (boolean) value);
        } else if (value instanceof Character) {
            getConfig().addProperty(property, (char) value);
        } else {
            System.out.println("uhm this is not indented neither wanted. fix asap? (changer)" + property + " " + value);
        }


    }

}
