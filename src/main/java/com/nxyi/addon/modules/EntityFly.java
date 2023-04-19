/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.entity.BoatMoveEvent;
import meteordevelopment.meteorclient.events.entity.LivingEntityMoveEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.mixininterface.IVec3d;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;;
import net.minecraft.network.packet.s2c.play.VehicleMoveS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

public class EntityFly extends Module {

    int kickdelay;
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal speed in blocks per second.")
        .defaultValue(10)
        .min(0)
        .sliderMax(50)
        .build()
    );

    private final Setting<Double> verticalSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-speed")
        .description("Vertical speed in blocks per second.")
        .defaultValue(6)
        .min(0)
        .sliderMax(20)
        .build()
    );

    private final Setting<Integer> bypass = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between antikick packet in ticks.")
        .defaultValue(20)
        .sliderRange(0, 20)
        .build()
    );

    private final Setting<Boolean> cancelServerPackets = sgGeneral.add(new BoolSetting.Builder()
        .name("cancel-server-packets")
        .description("Cancels incoming boat move packets.")
        .defaultValue(false)
        .build()
    );

    public EntityFly() {
        super(Addon.CATEGORY, "entity-fly", "Transforms your boat into a plane.");
    }

    public KeyBinding down = new KeyBinding("", GLFW.GLFW_KEY_LEFT_ALT, "");

    @EventHandler
    private void onEntityMove(TickEvent.Pre event) {
        if (!mc.player.hasVehicle()) return;

        Entity vehicle = mc.player.getVehicle();
        vehicle.setYaw(mc.player.getYaw());

        // Horizontal movement
        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.get());
        double velX = vel.getX();
        double velY = 0;
        double velZ = vel.getZ();

        // Vertical movement
        if (kickdelay <= 0) return;
        if (mc.options.jumpKey.isPressed()) velY += verticalSpeed.get() / 20;
        if (down.isPressed()) velY -= verticalSpeed.get() / 20;

        // Apply velocity
        ((IVec3d) vehicle.getVelocity()).set(velX, velY, velZ);
    }

    @EventHandler
    private void onBoatMove(BoatMoveEvent event) {
        if (event.boat.getFirstPassenger() != mc.player) return;

        event.boat.setYaw(mc.player.getYaw());

        // Horizontal movement
        Vec3d vel = PlayerUtils.getHorizontalVelocity(speed.get());
        double velX = vel.getX();
        double velY = 0;
        double velZ = vel.getZ();

        // Vertical movement
        if (kickdelay <= 0) return;
        if (mc.options.jumpKey.isPressed()) velY += verticalSpeed.get() / 20;
        if (down.isPressed()) velY -= verticalSpeed.get() / 20;

        // Apply velocity
        ((IVec3d) event.boat.getVelocity()).set(velX, velY, velZ);
    }

    @Override
    public void onActivate() {
        kickdelay = 15;
    }

    @EventHandler
    private void ontick(TickEvent.Post event){
        if (mc.player.hasVehicle()) {
            Entity vehicle = mc.player.getVehicle();
            vehicle.setOnGround(true);
            if (kickdelay <= 0) {
                vehicle.setPosition(vehicle.getX(), vehicle.getY() - 0.04, vehicle.getZ());
                kickdelay = bypass.get();
            } else {
                kickdelay--;
            }
        }
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (event.packet instanceof VehicleMoveS2CPacket && cancelServerPackets.get()) {
            event.cancel();
        }
    }
}
