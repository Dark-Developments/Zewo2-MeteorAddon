package com.nxyi.addon.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandSource;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class SpamCommand extends Command {
    public SpamCommand() {
        super("Spam", "Spam a message in chat", "spawm");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("amount" ,IntegerArgumentType.integer(0))
            .then(argument("text", StringArgumentType.string())).executes(ctx -> {
                int amount = IntegerArgumentType.getInteger(ctx, "amount");
                String text = StringArgumentType.getString(ctx, "text");

                for (int i = 0; i < amount; i++){
                    MinecraftClient.getInstance().player.networkHandler.sendChatMessage(text);
                }

                return SINGLE_SUCCESS;
            }));
    }
}
