package dev.antimoxs.hyplus.listener;

import dev.antimoxs.hyplus.HyPlus;
import dev.antimoxs.hyplus.events.PlayerSpawnEvent;
import dev.antimoxs.hyplus.modules.HyGeneral;
import net.labymod.utils.Consumer;
import net.minecraft.network.play.server.*;

public class HyListenerPacket implements Consumer<Object> {

    @Override
    public void accept(Object o) {

        // Check if we are enabled.
        if (!HyGeneral.HYPLUS_GENERAL_TOGGLE.getValueBoolean()) return;

        if (HyPlus.getInstance().hypixel.checkOnServer()) {

            if (o instanceof S01PacketJoinGame) {

                // joined new game/ server
                HyPlus.getInstance().hyEventManager.callPacketJoinGame((S01PacketJoinGame) o);

            }
            else if (o instanceof S3BPacketScoreboardObjective) {

                // scoreboard changed
                HyPlus.getInstance().hyEventManager.callGameStatusChange("SBO: " + ((S3BPacketScoreboardObjective) o).func_149339_c());

            }
            else if (o instanceof S3CPacketUpdateScore) {

                // scoreboard changed
                HyPlus.getInstance().hyEventManager.callGameStatusChange("Update: " + ((S3CPacketUpdateScore) o).getObjectiveName());

            }
            else if (o instanceof S3DPacketDisplayScoreboard) {

                // scoreboard changed
                HyPlus.getInstance().hyEventManager.callGameStatusChange("Display");

            }
            else if (o instanceof S0CPacketSpawnPlayer) {

                try {
                    S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer) o;
                    PlayerSpawnEvent event = new PlayerSpawnEvent();
                    event.playerUUID = packet.getPlayer();
                    event.X = packet.getX();
                    event.Y = packet.getY();
                    event.Z = packet.getZ();
                    event.pitch = packet.getPitch();
                    event.yaw = packet.getYaw();

                    HyPlus.getInstance().hyEventManager.callPlayerSpawn(event);
                }
                catch (Exception e) {

                    // ignored lol
                    HyPlus.getInstance().log("Failed to pass player join event towards HyPlus event system.");

                }

            }

        }

    }

}
