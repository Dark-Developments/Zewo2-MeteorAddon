/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class Airstrike extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> radius = sgGeneral.add(new IntSetting.Builder()
        .name("radius")
        .defaultValue(30)
        .sliderRange(1, 100)
        .build());

    private final Setting<Integer> power = sgGeneral.add(new IntSetting.Builder()
        .name("power")
        .defaultValue(10)
        .sliderRange(1, 127)
        .build());

    private final Setting<Integer> height = sgGeneral.add(new IntSetting.Builder()
        .name("height")
        .description("y level they spawn")
        .defaultValue(100)
        .sliderRange(-63, 320)
        .build());

    private final Setting<Integer> speed = sgGeneral.add(new IntSetting.Builder()
        .name("speed")
        .defaultValue(10)
        .sliderRange(1, 10)
        .build());

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("its in ticks")
        .defaultValue(2)
        .sliderRange(0, 20)
        .build());

    public Airstrike() {
        super(Addon.CATEGORY, "airstrike", "nuke the area around you");
    }

    Vec3d origin = null;
    int i = 0;

    private Vec3d pickRandomPos() {
        double x = new Random().nextDouble(radius.get() * 2) - radius.get() + origin.x;
        double y = height.get();
        double z = new Random().nextDouble(radius.get() * 2) - radius.get() + origin.z;
        return new Vec3d(x, y, z);
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {
        origin = mc.player.getPos();
        ItemStack bomb = new ItemStack(Items.SALMON_SPAWN_EGG);
        ItemStack bfr = mc.player.getMainHandStack();
        BlockHitResult bhr = new BlockHitResult(mc.player.getPos(), Direction.DOWN, new BlockPos(BlockPos.ofFloored(mc.player.getPos())), false);
        i++;
        if (mc.player.getAbilities().creativeMode) {
            if (i >= delay.get()) {
                Vec3d cpos = pickRandomPos();

                NbtCompound tag = new NbtCompound();
                NbtList speedlist = new NbtList();
                NbtList pos = new NbtList();
                speedlist.add(NbtDouble.of(0));
                speedlist.add(NbtDouble.of(-speed.get()));
                speedlist.add(NbtDouble.of(0));
                pos.add(NbtDouble.of(cpos.x));
                pos.add(NbtDouble.of(height.get()));
                pos.add(NbtDouble.of(cpos.z));
                tag.put("ExplosionPower", NbtDouble.of(power.get()));
                tag.put("power", speedlist);
                tag.put("Pos", pos);
                tag.putString("id", "minecraft:fireball");
                bomb.setSubNbt("EntityTag", tag);
                mc.interactionManager.clickCreativeStack(bomb, 36 + mc.player.getInventory().selectedSlot);
                mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, bhr);
                mc.interactionManager.clickCreativeStack(bfr, 36 + mc.player.getInventory().selectedSlot);
                i = 0;
            }
        } else {
            error("You need to be in creative mode.");
            toggle();
        }
    }
}
