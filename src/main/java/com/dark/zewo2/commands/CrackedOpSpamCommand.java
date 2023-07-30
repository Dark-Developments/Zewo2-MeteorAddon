package com.dark.zewo2.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

public class CrackedOpSpamCommand extends Command {
    public CrackedOpSpamCommand() {
        super("CrackedOpSpam", "Spam op accounts on cracked servers", "cop", "crackedopspam");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("name", StringArgumentType.string())
            .then(argument("amount", IntegerArgumentType.integer(0)).executes(ctx -> {
                int amount = IntegerArgumentType.getInteger(ctx, "amount");
                String name = StringArgumentType.getString(ctx, "name");

                //make new thread to try to prevent some lag
                new Thread(() -> {
                    for (int i = 0; i < amount; i++){
                        MinecraftClient.getInstance().player.networkHandler.sendChatCommand("op " + name + i);
                        sleep(500);
                    }
                }).start();

                return 1;
            })));
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }
}
