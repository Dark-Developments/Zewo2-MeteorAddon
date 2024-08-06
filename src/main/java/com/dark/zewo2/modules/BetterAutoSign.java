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
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.item.DyeItem;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Item;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.CloseHandledScreenC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.reflections.vfs.Vfs;

import java.util.List;

public class BetterAutoSign extends Module {
    boolean listen;

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

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

    @Override
    public void onActivate() {
        listen = true;
    }

    @EventHandler
    private void packet(PacketEvent.Receive event){
        if (!listen) return;
        if (event.packet instanceof SignEditorOpenS2CPacket packet){
            event.cancel();

            listen = false;
            boolean sneaking = mc.player.isSneaking();
            if (sneaking) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY));

            BlockPos sign = new BlockPos(packet.getPos());

            if (mode.get().equals(sides.front)) {
                mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
            }
            if (mode.get().equals(sides.back)) {
                mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
            }

            if (mode.get().equals(sides.both)){
                mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND ,new BlockHitResult(new Vec3d(sign.getX(), sign.getY(), sign.getZ()), mc.player.getHorizontalFacing().getOpposite(), sign, false), 0));
                mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
            }

            if (sneaking) mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY));

            listen = true;
        }
    }

    private String isempty(String text){
        if (text.isEmpty()) return "";
        else return text;
    }

    private boolean itemfilter(Item item) {
        return item instanceof DyeItem;
    }

    public enum sides{
        front,
        back,
        both
    }
}
