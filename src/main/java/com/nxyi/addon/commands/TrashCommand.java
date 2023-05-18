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

public class TrashCommand extends Command {
    // crappy hack to make it compile
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public TrashCommand() {
        super("trash", "Destroys the item you are holding in your hand.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            assert mc.interactionManager != null && mc.player != null;   // impossible, but still
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, 36 + mc.player.getInventory().selectedSlot, 50, SlotActionType.SWAP, mc.player);

            return SINGLE_SUCCESS;
        });
    }
}
