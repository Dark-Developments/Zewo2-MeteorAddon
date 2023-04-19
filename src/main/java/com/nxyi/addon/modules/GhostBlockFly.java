/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.*;
import net.minecraft.util.math.BlockPos;

public class GhostBlockFly extends Module {
    public GhostBlockFly() {
        super(Addon.CATEGORY, "GhostBlockFly", "Fly Using Ghost Blocks");
    }

    BlockState state = null;
    BlockPos pos = null;

    @EventHandler
    public void onTick(TickEvent.Post event) {
        assert mc.world != null;
        assert mc.player != null;
        if (mc.player.getBlockPos().add(0, -1, 0) != pos && pos != null && state != null) {
            mc.world.setBlockState(pos, state);
        }

        pos = mc.player.getBlockPos().add(0, -1, 0);
        state = mc.world.getBlockState(pos);

        if (!mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, Blocks.BARRIER.getDefaultState());
        }

        if (mc.options.sneakKey.isPressed() && mc.world.getBlockState(pos).getBlock() instanceof AirBlock && pos != null) {
            mc.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void onDeactivate() {
        assert mc.world != null;
        mc.world.setBlockState(pos, state);
        pos = null;
        BlockState state = null;
    }
}
