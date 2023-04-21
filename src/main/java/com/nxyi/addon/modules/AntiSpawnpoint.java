package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;

public class AntiSpawnpoint extends Module {
    public AntiSpawnpoint() {
        super(Addon.CATEGORY, "AntiSpawnpoint", "Stop you from setting your spawnpoint");
    }

    @EventHandler
    private void onclick(PacketEvent.Send event){
        if (event.packet instanceof PlayerInteractBlockC2SPacket packet) {
            if (mc.world.getBlockState(packet.getBlockHitResult().getBlockPos()).getBlock().getName().contains(Text.of("Bed"))) {
                event.cancel();
            }
        }
    }
}
