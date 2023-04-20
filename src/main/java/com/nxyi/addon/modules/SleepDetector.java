package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;

public class SleepDetector extends Module {
    public SleepDetector() {
        super(Addon.CATEGORY, "SleepDetector", "Detect if someone hops in a bed from anywhere in the server :OOO");
    }

    @EventHandler
    private void onsleep(PacketEvent.Receive event){
        if (event.packet instanceof GameMessageS2CPacket packet){
            if (packet.content().contains(Text.of("sleep.players_sleeping"))){
                ChatUtils.info("Someone is sleeping");
            }
        }
    }
}
