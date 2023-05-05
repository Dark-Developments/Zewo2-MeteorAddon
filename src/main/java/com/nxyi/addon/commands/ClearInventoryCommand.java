/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.config.Config;
import net.minecraft.command.CommandSource;
import net.minecraft.screen.slot.SlotActionType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ClearInventoryCommand extends Command {
    boolean confirm = false;
    public ClearInventoryCommand() {
        super("clear-inventory", "clear your inventory.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            if(!confirm) {
                warning("Are you sure that you want to clear your inventory? if yes, use the command again.");
                confirm = true;
            } else {
                for (int i = 9; i < 45; i++) mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 120, SlotActionType.SWAP, mc.player);
                confirm = false;
            }
            return SINGLE_SUCCESS;
        });
    }
}
