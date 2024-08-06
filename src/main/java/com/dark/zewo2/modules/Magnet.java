/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.JinxUtils;
import com.dark.zewo2.Utils.Utils;
import meteordevelopment.meteorclient.events.entity.player.PlayerMoveEvent;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
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

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof ItemEntity){
                if (mc.player.distanceTo(entity) > range.get()) return;

                if (!Utils.isABFree(mc.player.getPos(), entity.getPos())) return;
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(entity.getX(), entity.getY(), entity.getZ(), true));
            }
        }
    }
}
