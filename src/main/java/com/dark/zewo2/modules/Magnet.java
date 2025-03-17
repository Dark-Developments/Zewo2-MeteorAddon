package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
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
import net.minecraft.util.math.Vec3d;

import java.util.*;
import java.util.stream.StreamSupport;

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
        List<Entity> entities = new ArrayList<>(StreamSupport.stream(mc.world.getEntities().spliterator(), true)
            .filter(entity -> entity instanceof ItemEntity)
            .filter(entity -> entity.distanceTo(mc.player) <= range.get())
            .filter(entity -> Utils.isABFree(mc.player.getPos(), entity.getPos()))
            .toList());

        if (entities.isEmpty()) return;

        entities.sort(Comparator.comparingDouble(entity -> entity.distanceTo(mc.player)));
        Entity closest = entities.get(0);

        if (closest.distanceTo(mc.player) > 1) mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(closest.getX(), closest.getY(), closest.getZ(), mc.player.isOnGround(), false));
    }
}
