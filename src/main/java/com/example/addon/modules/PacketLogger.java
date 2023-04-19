/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.PacketListSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.PacketUtils;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PacketLogger extends Module{
    public PacketLogger() {
        super(Addon.CATEGORY, "Packet-Logger", "Tells you incoming packet info.");
    }

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Set<Class<? extends Packet<?>>>> s2cPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("S2C-packets")
        .description("Server-to-client packets to log.")
        .filter(aClass -> PacketUtils.getS2CPackets().contains(aClass))
        .build()
    );

    @EventHandler(priority = EventPriority.HIGHEST + 1)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (s2cPackets.get().contains(event.packet.getClass())) info(event.packet.getClass().toString() + " §a->§r " + event.packet);
    }
}

