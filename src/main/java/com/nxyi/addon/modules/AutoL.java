/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
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
            if (packet.getEntity(mc.world) instanceof PlayerEntity player){
                if (player.getHealth() <= 0){
                    if (player.equals(mc.player)) return;
                    String text = messages.get().get(Utils.random(0, messages.get().size()));

                    //replace syntax
                    text = text.replace("(target)", player.getEntityName());
                    text = text.replace("(target.coords)", player.getBlockPos().toString());

                    ChatUtils.sendPlayerMsg(text);
                }
            }
        }
    }
}
