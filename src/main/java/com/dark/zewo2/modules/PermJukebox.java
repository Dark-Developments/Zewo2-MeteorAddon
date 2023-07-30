package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;

public class PermJukebox extends Module {
    public PermJukebox() {
        super(Addon.CATEGORY, "PermJukebox", "Make the jukebox play permanently");
    }

    @EventHandler
    private void onpacket(PacketEvent.Send event){
        if (event.packet instanceof PlayerInteractBlockC2SPacket packet){
            if ((mc.world.getBlockState(packet.getBlockHitResult().getBlockPos()).getBlock() instanceof JukeboxBlock) && (mc.player.getInventory().getMainHandStack().getItem() instanceof MusicDiscItem)) {
                toggle();
                mc.player.networkHandler.sendPacket(packet);
                toggle();
            }
        }
    }
}
