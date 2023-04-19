/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.commands;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.UpdateCommandBlockC2SPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public class CheckCMD extends Command {
    private static final SimpleCommandExceptionType ALWAYS_CHECKING = new SimpleCommandExceptionType((Message) Text.of("Already executing Command Check!"));

    private int checking = 0;

    public CheckCMD() {
        super("checkcmd", "Checks if commandblocks are active.", new String[0]);
    }

    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (this.checking > 0)
                throw ALWAYS_CHECKING.create();
            this.checking = 200;
            MeteorClient.EVENT_BUS.subscribe(this);
            mc.player.networkHandler.sendPacket((Packet<?>) new UpdateCommandBlockC2SPacket(mc.player.getBlockPos(), "", CommandBlockBlockEntity.Type.AUTO, false, false, false));
            ChatUtils.info("Checking..", new Object[0]);
            return 1;
        });
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof GameMessageS2CPacket))
            return;
        Text message = ((GameMessageS2CPacket)event.packet).content();
        if (message.getContent() instanceof TranslatableTextContent) {
            String key = ((TranslatableTextContent)message.getContent()).getKey();
            if (key.equals("advMode.notEnabled")) {
                ChatUtils.info("Command blocks are deactivated", new Object[0]);
                event.cancel();
                this.checking = 0;
            } else if (key.equals("advMode.notAllowed") || key.equals("advMode.setCommand.success")) {
                ChatUtils.info("Command blocks are activated", new Object[0]);
                event.cancel();
                this.checking = 0;
            }
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (this.checking == 1)
            ChatUtils.error("Server didnt send a response!", new Object[0]);
        if (this.checking < 1) {
            this.checking = 0;
            MeteorClient.EVENT_BUS.unsubscribe(this);
            return;
        }
        this.checking--;
    }
}
