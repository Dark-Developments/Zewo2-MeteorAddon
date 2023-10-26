package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.DeathMessageS2CPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class SitModule extends Module {
    private Entity target = null;
    int kickdelay = 25;
    public SitModule() {
        super(Addon.CATEGORY, "SitModule", "Sit on other players");
    }

    @Override
    public void onActivate() {
        target = null;
    }

    @EventHandler
    private void ontick(TickEvent.Pre event){
        if (target != null) {
            mc.player.setPosition(target.getPos().x, target.getPos().y + target.getHeight() + 0.04, target.getPos().z);
            mc.player.setVelocity(0,0,0);

            if (kickdelay <= 0) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 0.04, mc.player.getZ(), mc.player.isOnGround()));
                kickdelay = 25;
            } else {
                kickdelay--;
            }
        }

        if (mc.options.useKey.isPressed()){
            if (!mc.crosshairTarget.getType().equals(HitResult.Type.ENTITY)) return;
            EntityHitResult etr = (EntityHitResult) mc.crosshairTarget;
            target = etr.getEntity();
        }

        if (mc.options.sneakKey.isPressed()){
            target = null;
        }
    }

    @EventHandler
    private void onPacketReceive(PacketEvent.Receive event)  {
        if (event.packet instanceof DeathMessageS2CPacket packet) {
            Entity entity = mc.world.getEntityById(packet.getEntityId());
            target = null;
        }
    }
}
