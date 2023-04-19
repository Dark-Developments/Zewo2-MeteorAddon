/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class DisableVehicleGrav extends Command {
    public DisableVehicleGrav() {
        super("antigrav", "Disables Vehicle Gravity");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            if (mc.player.hasVehicle()){
                mc.player.getVehicle().setNoGravity(true);
            } else info("You need to have a vehicle");
            return SINGLE_SUCCESS;
        });
    }
}
