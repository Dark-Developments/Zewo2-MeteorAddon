/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.List;

public class AutoL extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<List<String>> messages = sgGeneral.add(new StringListSetting.Builder()
        .name("Messages")
        .description("Messages to send when killing enemy")
        .defaultValue(List.of("L (target)", "bozo (target)"))
        .build()
    );

    public AutoL() {
        super(Addon.CATEGORY, "AutoL", "Disgraceful");
    }

    @EventHandler
    private void OnPacket(PacketEvent.Receive event) {
        if (event.packet instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() != 3) return;

            Entity entity = packet.getEntity(mc.world);
            if (entity instanceof PlayerEntity player){
                if (player.equals(mc.player)) return;

                String text = messages.get().get(Utils.random(0, messages.get().size()));
                //replace syntax
                text = text.replace("(target)", player.getGameProfile().getName());
                text = text.replace("(target.coords)", player.getBlockPos().toString());
                ChatUtils.sendPlayerMsg(text);

            }
        }
    }
}
