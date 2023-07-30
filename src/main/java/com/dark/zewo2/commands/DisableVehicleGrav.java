/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import java.util.Objects;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class DisableVehicleGrav extends Command {
    // crappy hack to make it compile
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public DisableVehicleGrav() {
        super("antigrav", "Disables Vehicle Gravity");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            assert mc.player != null;   // impossible, but still
            if (mc.player.hasVehicle()){
                Objects.requireNonNull(mc.player.getVehicle()).setNoGravity(true);
            } else info("You need to have a vehicle");
            return SINGLE_SUCCESS;
        });
    }
}
