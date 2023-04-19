/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.commands.arguments.PlayerListEntryArgumentType;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;

import java.util.UUID;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class CreativeBanCommand extends Command {
    private static final SimpleCommandExceptionType NOT_IN_CREATIVE = new SimpleCommandExceptionType(Text.literal("You must be in creative mode to use this."));

    public CreativeBanCommand() {
        super("creative-ban", "Attempts to ban a player with an armor stand.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("player", PlayerListEntryArgumentType.create()).executes(ctx -> {
            ItemStack stack = new ItemStack(Items.ARMOR_STAND);
            PlayerListEntry player = PlayerListEntryArgumentType.get(ctx);
            int[] uuid = decodeUUID(player.getProfile().getId());
            stack.setNbt(StringNbtReader.parse("{EntityTag:{UUID:[I;" + uuid[0] + "," + uuid[1] + "," + uuid[2] + "," + uuid[3] + "],ArmorItems:[{},{},{},{id:\"minecraft:player_head\",Count:1b,tag:{SkullOwner:\"" + player.getProfile().getName() + "\"}}]}}"));
            stack.setCustomName(Text.of("§c" + player.getProfile().getName()));
            if (!mc.player.getAbilities().creativeMode) throw NOT_IN_CREATIVE.create();
            mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
            return SINGLE_SUCCESS;
        }));
    }

    private static int[] decodeUUID(UUID uuid) {
        long sigLeast = uuid.getLeastSignificantBits();
        long sigMost = uuid.getMostSignificantBits();
        return new int[]{(int) (sigMost >> 32), (int) sigMost, (int) (sigLeast >> 32), (int) sigLeast};
    }
}
