package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.network.packet.c2s.handshake.HandshakeC2SPacket;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AntiSpawnpoint extends Module {

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> fakeuse = sgGeneral.add(new BoolSetting.Builder()
        .name("Fake Use")
        .description("Makes it look like you interacted with the block.")
        .defaultValue(false)
        .build()
    );
    public AntiSpawnpoint() {
        super(Addon.CATEGORY, "AntiSpawnpoint", "Stop you from setting your spawnpoint");
    }

    @EventHandler
    private void onclick(PacketEvent.Send event){
        if (event.packet instanceof PlayerInteractBlockC2SPacket packet) {
            BlockPos blockpos = packet.getBlockHitResult().getBlockPos();

            if (fakeuse.get()){
                mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
                mc.player.swingHand(Hand.MAIN_HAND);
            }

            if (mc.world.getDimension().bedWorks() && mc.world.getBlockState(blockpos).getBlock() instanceof BedBlock) {
                event.cancel();
            } else if (mc.world.getDimension().respawnAnchorWorks() && mc.world.getBlockState(blockpos).getBlock() instanceof RespawnAnchorBlock) {
                event.cancel();
            }
        }
    }
}
