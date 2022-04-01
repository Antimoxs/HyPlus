package dev.antimoxs.hyplus.internal.discordSdk;

import com.jagrosh.discordipc.IPCClient;
import com.jagrosh.discordipc.IPCListener;
import com.jagrosh.discordipc.entities.DiscordBuild;
import com.jagrosh.discordipc.entities.RichPresence;
import com.jagrosh.discordipc.entities.pipe.PipeStatus;
import com.jagrosh.discordipc.exceptions.NoDiscordClientException;
import dev.antimoxs.hyplus.HyPlus;
import net.labymod.main.LabyMod;
import net.labymod.support.util.Debug;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DiscordManager {

    public static File discordLibrary;

    static {
        try {
            discordLibrary = downloadDiscordLibrary();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final ModRichPresenceExtender modRichPresenceExtender = new ModRichPresenceExtender(this);
    private final IPCListener handlers = new DiscordJoinEventListener(this);

    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private static final long applicationID = 824252829404233729L;
    private final IPCClient discordClient = new IPCClient(applicationID);

    private static File downloadDiscordLibrary() throws IOException {
        // Find out which name Discord's library has (.dll for Windows, .so for Linux)
        String name = "discord_game_sdk";
        String suffix;

        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        if(osName.contains("windows"))
        {
            suffix = ".dll";
        }
        else if(osName.contains("linux"))
        {
            suffix = ".so";
        }
        else if(osName.contains("mac os"))
        {
            suffix = ".dylib";
        }
        else
        {
            throw new RuntimeException("cannot determine OS type: "+osName);
        }

		/*
		Some systems report "amd64" (e.g. Windows and Linux), some "x86_64" (e.g. Mac OS).
		At this point we need the "x86_64" version, as this one is used in the ZIP.
		 */
        if(arch.equals("amd64"))
            arch = "x86_64";

        // Path of Discord's library inside the ZIP
        String zipPath = "lib/"+arch+"/"+name+suffix;

        // Open the URL as a ZipInputStream
        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");

        URLConnection openConnection = downloadUrl.openConnection();
        openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

        ZipInputStream zin = new ZipInputStream(openConnection.getInputStream());

        // Search for the right file inside the ZIP
        ZipEntry entry;
        while((entry = zin.getNextEntry())!=null)
        {
            if(entry.getName().equals(zipPath))
            {
                // Create a new temporary directory
                // We need to do this, because we may not change the filename on Windows
                File tempDir = new File(System.getProperty("java.io.tmpdir"), "java-"+name+System.nanoTime());
                if(!tempDir.mkdir())
                    throw new IOException("Cannot create temporary directory");
                tempDir.deleteOnExit();

                // Create a temporary file inside our directory (with a "normal" name)
                File temp = new File(tempDir, name+suffix);
                temp.deleteOnExit();

                // Copy the file in the ZIP to our temporary file
                Files.copy(zin, temp.toPath());

                // We are done, so close the input stream
                zin.close();

                // Return our temporary file
                return temp;
            }
            // next entry
            zin.closeEntry();
        }
        zin.close();
        // We couldn't find the library inside the ZIP
        return null;
    }

    private boolean connected = false;

    public DiscordManager() {

        Runtime.getRuntime().addShutdownHook(new Thread(this.discordClient::close));

    }

    public ModRichPresenceExtender getRichPresence() {

        return this.modRichPresenceExtender;

    }

    public boolean startInstance() {

            try {
                this.discordClient.connect(DiscordBuild.ANY);
                this.discordClient.subscribe(IPCClient.Event.ACTIVITY_JOIN);
                this.discordClient.subscribe(IPCClient.Event.ACTIVITY_JOIN_REQUEST);
                this.discordClient.setListener(handlers);
                connected = true;
                return true;
            } catch (Exception e) {
                this.connected = this.discordClient.getStatus() == PipeStatus.CONNECTED;
                HyPlus.debugLog("Can't start discord rpc: " + e.getMessage());
                return false;
            }

    }

    public void shutdown() {

        if (!connected) startInstance();
        try {

            this.discordClient.close();
        }
        catch (Exception e) {

            HyPlus.debugLog("Can't stop discord rpc: " + e.getMessage());

        }

    }

    public void respond(long userID, boolean accept) {

        if (!connected) startInstance();
        try {
            this.discordClient.reply(userID, accept);
        }
        catch (Exception e) {

            HyPlus.debugLog("Can't respond to discord rpc: " + e.getMessage());

        }

    }

    public void updateActivity(RichPresence activity) {

        if (!connected) startInstance();
        try {
            this.discordClient.sendRichPresence(activity);
        }
        catch (Exception e) {

            HyPlus.debugLog("Can't update discord rpc: " + e.getMessage());

        }


    }

    public void clearActivity() {

        if (!connected) startInstance();
        try {
            this.discordClient.sendRichPresence(null);
        }
        catch (Exception e) {

            HyPlus.debugLog("Can't clear discord rpc: " + e.getMessage());

        }


    }

    public void redeemJoinKey(UUID joinKey, String serverDomain) {

        HyPlus.getInstance().log("Joining Hypixel via Discord key...");
        Debug.log(Debug.EnumDebugMode.DISCORD, "Redeem join secret key: " + joinKey.toString() + " on " + serverDomain);

        if (!LabyMod.getInstance().switchServer("mc.hypixel.net", true)) {

            String name = HyPlus.getInstance().hyPartyManager.rematchInvite(joinKey);
            HyPlus.getInstance().sendMessageIngameChat("/party join " + name);
            HyPlus.getInstance().hyPartyManager.getParty().setPublic(true);
            HyPlus.getInstance().sendMessageIngameChat("/pl"); // <- update the party :)

        }

    }

    public void runCallbacks() {

        // not needed in current implementation :D

        /*
        if (discordCore == null) startInstance();
        this.discordCore.run_callbacks.apply(this.discordCore);

         */

    }

}
