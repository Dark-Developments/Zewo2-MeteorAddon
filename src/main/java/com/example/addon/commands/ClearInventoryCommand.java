/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.config.Config;
import net.minecraft.command.CommandSource;
import net.minecraft.screen.slot.SlotActionType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ClearInventoryCommand extends Command {
    public ClearInventoryCommand() {
        super("clear-inventory", "Attempts to clear your inventory.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            warning("Are you sure that you want to clear your inventory? if yes, use " + Config.get().prefix.get() + "clear-inventory confirm.");
            return SINGLE_SUCCESS;
        });
        builder.then(literal("confirm").executes(ctx -> {
            for (int i = 9; i < 45; i++) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 120, SlotActionType.SWAP, mc.player);
            return SINGLE_SUCCESS;
        }));
    }
}
