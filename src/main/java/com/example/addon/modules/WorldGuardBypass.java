/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class WorldGuardBypass extends Module {
    public WorldGuardBypass() {
        super(Addon.CATEGORY, "WG-Bypass", "Bypass world guard");
    }

    ClientPlayerEntity player = mc.player;

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        double hspeed = 0.0625D;
        double vspeed = 0.0625D;

        Vec3d forward = new Vec3d(0, 0, hspeed).rotateY(-(float) Math.toRadians(Math.round(mc.player.getYaw() / 90) * 90));
        Vec3d moveVec = Vec3d.ZERO;


        if (mc.options.forwardKey.isPressed()) {
            moveVec = moveVec.add(forward);
            mc.player.setVelocity(0, 0, 0);
        } else if (mc.options.backKey.isPressed()) {
            moveVec = moveVec.add(forward.negate());
            mc.player.setVelocity(0, 0, 0);
        }

        else if (mc.options.leftKey.isPressed()) {
            mc.player.setVelocity(0, 0, 0);
            moveVec = moveVec.add(forward.rotateY((float) Math.toRadians(90)));
        }else if (mc.options.rightKey.isPressed()) {
            moveVec = moveVec.add(forward.rotateY((float) -Math.toRadians(90)));
            mc.player.setVelocity(0, 0, 0);
        }

        else if (mc.options.jumpKey.isPressed()) {
            moveVec = moveVec.add(0, vspeed, 0);
            mc.player.setVelocity(0, 0, 0);
        }else if (mc.options.sneakKey.isPressed()) {
            moveVec = moveVec.add(0, -vspeed, 0);
            mc.player.setVelocity(0, 0, 0);
        }

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
            mc.player.getX() + moveVec.x, mc.player.getY() + moveVec.y, mc.player.getZ() + moveVec.z, false));

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
            mc.player.getX() + moveVec.x, mc.player.getY() -100, mc.player.getZ() + moveVec.z, true));
    }
}
