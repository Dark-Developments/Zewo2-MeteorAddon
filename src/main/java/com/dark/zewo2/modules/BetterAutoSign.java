/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixin.AbstractSignEditScreenAccessor;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;

public class BetterAutoSign extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<String> line1 = sgGeneral.add(new StringSetting.Builder().name("line 1").description("line 1").defaultValue("").build());

    private final Setting<String> line2 = sgGeneral.add(new StringSetting.Builder().name("line 2").description("line 2").defaultValue("").build());
    private final Setting<String> line3 = sgGeneral.add(new StringSetting.Builder().name("line 3").description("line 3").defaultValue("").build());

    private final Setting<String> line4 = sgGeneral.add(new StringSetting.Builder().name("line 4").description("line 4").defaultValue("").build());

    public BetterAutoSign() {
        super(Addon.CATEGORY, "Auto-Sign+", "Auto Sign");
    }

    @EventHandler
    private void onSendPacket(PacketEvent.Send event) {
        if (!(event.packet instanceof UpdateSignC2SPacket)) return;
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (!(event.screen instanceof SignEditScreen) || line1 == null || line2 == null || line3 == null || line4 == null) return;

        SignBlockEntity sign = ((AbstractSignEditScreenAccessor) event.screen).getSign();

        mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign.getPos(), line1.get(), line2.get(), line3.get(), line4.get()));

        event.cancel();
    }
}
