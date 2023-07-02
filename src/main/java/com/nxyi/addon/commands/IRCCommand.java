package com.nxyi.addon.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nxyi.addon.Addon;
import com.sun.jdi.connect.Connector;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;

public class IRCCommand extends Command {
    public IRCCommand() {
        super("irc", "Send messages to the irc client");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("text", StringArgumentType.greedyString()).executes(ctx -> {
            String text = StringArgumentType.getString(ctx, "text");
            if (Addon.getIrc().isConnected()){
                Addon.getIrc().getReceiver().getSender().addMessage(text);
            }
            else error("You are not connected to the IRC");

            return 0;
        }));
    }
}
