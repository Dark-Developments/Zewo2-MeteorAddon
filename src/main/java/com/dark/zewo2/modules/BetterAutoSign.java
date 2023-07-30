/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.AbstractSignEditScreenAccessor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.reflections.vfs.Vfs;

public class BetterAutoSign extends Module {

    private final SettingGroup sgGeneral = this.settings.createGroup("Front");

    private final Setting<BetterAutoSign.sides> mode = sgGeneral.add(new EnumSetting.Builder<BetterAutoSign.sides>()
        .name("mode")
        .description("modes")
        .defaultValue(sides.front)
        .build()
    );

    private final Setting<String> line1 = sgGeneral.add(
        new StringSetting.Builder().name("line 1").description("line 1").defaultValue("").build());

    private final Setting<String> line2 = sgGeneral.add(
        new StringSetting.Builder().name("line 2").description("line 2").defaultValue("").build());
    private final Setting<String> line3 = sgGeneral.add(
        new StringSetting.Builder().name("line 3").description("line 3").defaultValue("").build());

    private final Setting<String> line4 = sgGeneral.add(
        new StringSetting.Builder().name("line 4").description("line 4").defaultValue("").build());

    public BetterAutoSign() {
        super(Addon.CATEGORY, "Auto-Sign+", "Auto Sign");
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (!(event.screen instanceof SignEditScreen)) return;

        SignBlockEntity sign = ((AbstractSignEditScreenAccessor) event.screen).getSign();

        if (mode.get().equals(sides.front)) mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
        if (mode.get().equals(sides.back)) mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));

        if (mode.get().equals(sides.both)){
            mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
            mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND ,new BlockHitResult(new Vec3d(sign.getPos().getX(), sign.getPos().getY(), sign.getPos().getZ()), getop(mc.player.getHorizontalFacing()), sign.getPos(), false), 0));
            mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
        }

        event.cancel();
    }

    private String isempty(String text){
        if (text.isEmpty()) return "";
        else return text;
    }

    private Direction getop(Direction dir){
        if (dir.equals(Direction.NORTH)) return Direction.SOUTH;
        else if (dir.equals(Direction.EAST)) return Direction.WEST;
        else if (dir.equals(Direction.SOUTH)) return Direction.NORTH;
        else return Direction.EAST;
    }

    public enum sides{
        back,
        front,
        both
    }
}
