package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.world.GameMode;

public class GMnotifier extends Module {

    public GMnotifier() {
        super(Addon.CATEGORY, "GM-notifier", "Tells you if someone switches GM");
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event) {
        if (event.packet instanceof PlayerListS2CPacket packet) {
            for (PlayerListS2CPacket.Entry gamemode : packet.getEntries()) {
                if (!packet.getActions().contains(PlayerListS2CPacket.Action.UPDATE_GAME_MODE)) continue;
                PlayerListEntry entry1 = mc.getNetworkHandler().getPlayerListEntry(gamemode.profileId());
                if (entry1 == null) continue;
                GameMode gameMode = gamemode.gameMode();
                if (entry1.getGameMode() != gameMode) {
                    info("Player %s changed gamemode to %s", entry1.getProfile().getName(), gamemode.gameMode());
                }
            }
        }
    }
}
