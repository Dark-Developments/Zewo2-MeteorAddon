package com.nxyi.addon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nxyi.addon.perk.Capes;
import meteordevelopment.meteorclient.renderer.Fonts;
import meteordevelopment.meteorclient.systems.Systems;
import meteordevelopment.meteorclient.systems.commands.Command;
import meteordevelopment.meteorclient.systems.friends.Friend;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ReloadCapes extends Command {
    public ReloadCapes() {
        super("reloadCapes", "Reloads cape textures.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            warning("Reloading capes...");

            Capes.init();

            return SINGLE_SUCCESS;
        });
    }
}
