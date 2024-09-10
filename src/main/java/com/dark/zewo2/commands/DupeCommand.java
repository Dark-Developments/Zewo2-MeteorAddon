package com.dark.zewo2.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.screen.slot.SlotActionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class DupeCommand extends Command {
    public DupeCommand() {
        super("dupe", "popbobsexdupe");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            ArrayList<String> pages = new ArrayList<>();

            mc.player.networkHandler.sendPacket(new BookUpdateC2SPacket(mc.player.getInventory().selectedSlot, pages, Optional.of("discord.gg/kja3YYV7R9 join now :3")));
            return 1;
        });
    }
}
