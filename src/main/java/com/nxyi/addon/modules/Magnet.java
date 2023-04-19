/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class Magnet extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> range = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Range, in which items will be sucked.")
        .defaultValue(3.5)
        .range(1, 128)
        .sliderRange(1, 10)
        .build()
    );

    BlockPos pos = null;

    public Magnet() {
        super(Addon.CATEGORY, "Magnet", "Sucks up all items on the ground.");
    }

    //clear pos
    @Override
    public void onActivate() {pos = null;}
    @EventHandler
    private void onGameLeft(GameLeftEvent event) {pos = null;}

    @EventHandler
    public void onMove(PlayerMoveEvent event) {

    }

    //Bad system and gets stuck ALOT will fix
    @EventHandler
    private void onTick(TickEvent.Pre event) {
        for (Entity entity : mc.world.getEntities()) {
            if (Objects.equals(entity.getType().toString(), "entity.minecraft.item") && mc.player.distanceTo(entity) <= range.get()) {
                if (mc.player == null || mc.world == null) return;
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(entity.getX(), entity.getY(), entity.getZ(), true));
            }
        }
    }
}
