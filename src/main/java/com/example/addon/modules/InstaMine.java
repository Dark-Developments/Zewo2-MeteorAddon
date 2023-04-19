/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import com.example.addon.mixin.Interface.INClientPlayerInteractionManagerMixin;
import meteordevelopment.meteorclient.events.entity.player.StartBreakingBlockEvent;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.utils.world.BlockUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Objects;

public class InstaMine extends Module {
    private final BlockPos.Mutable blockPos = new BlockPos.Mutable(0, -1, 0);
    private Direction direction;
    BlockPos last;
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final SettingGroup sgRender = settings.createGroup("Render");

    private final Setting<Boolean> rotate = sgGeneral.add(new BoolSetting.Builder()
        .name("rotate")
        .description("Faces the blocks being mined server side.")
        .defaultValue(true)
        .build()
    );

    // Render

    private final Setting<Boolean> render = sgRender.add(new BoolSetting.Builder()
        .name("render")
        .description("Renders a block overlay on the block being broken.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> armswing = sgRender.add(new BoolSetting.Builder()
        .name("armswing")
        .description("send armswing packet.")
        .defaultValue(true)
        .build()
    );

    private final Setting<ShapeMode> shapeMode = sgRender.add(new EnumSetting.Builder<ShapeMode>()
        .name("shape-mode")
        .description("How the shapes are rendered.")
        .defaultValue(ShapeMode.Both)
        .build()
    );

    private final Setting<SettingColor> sideColor = sgRender.add(new ColorSetting.Builder()
        .name("side-color")
        .description("The color of the sides of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 10))
        .build()
    );

    private final Setting<SettingColor> lineColor = sgRender.add(new ColorSetting.Builder()
        .name("line-color")
        .description("The color of the lines of the blocks being rendered.")
        .defaultValue(new SettingColor(204, 0, 0, 255))
        .build()
    );


    public InstaMine() {
        super(Addon.CATEGORY, "insta-mine", "Attempts to instantly mine blocks.");
    }

    @Override
    public void onActivate() {
        last = new BlockPos(0, -128, 0);
    }

    @EventHandler
    private void onStartBreakingBlock(StartBreakingBlockEvent event) {
        direction = event.direction;
        blockPos.set(event.blockPos);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (Objects.requireNonNull(mc.interactionManager).isBreakingBlock()) {
            last = ((INClientPlayerInteractionManagerMixin) mc.interactionManager).getCurrentBreakingPos();
        }
        if (last.getY() == -128) return;
        mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, last, direction));

        if (armswing.get()) mc.getNetworkHandler().sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }

    private boolean shouldMine() {
        if (blockPos.getY() == -128) return false;
        if (!BlockUtils.canBreak(blockPos)) return false;
        return true;
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (!render.get() || !shouldMine()) return;
        event.renderer.box(blockPos, sideColor.get(), lineColor.get(), shapeMode.get(), 0);
    }
}
