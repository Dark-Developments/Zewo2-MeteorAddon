/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;
import net.minecraft.screen.slot.SlotActionType;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ClearInventoryCommand extends Command {
    // crappy hack to make it compile
    private final MinecraftClient mc = MinecraftClient.getInstance();

    boolean confirm = false;
    public ClearInventoryCommand() {
        super("clear-inventory", "clear your inventory.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(ctx -> {
            assert mc.interactionManager != null && mc.player != null;  // impossible, but still
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
