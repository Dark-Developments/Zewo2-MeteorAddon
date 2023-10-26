package com.dark.zewo2.commands;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.netty.util.Signal;
import meteordevelopment.meteorclient.commands.Command;
import meteordevelopment.meteorclient.commands.arguments.PlayerArgumentType;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.meteorclient.utils.network.Http;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class MinefortJoin extends Command {

    public MinefortJoin() {
        super("minefort", "Join another player on minefort >> @just_jakob", "mf");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("join").then(argument("player", StringArgumentType.string()).executes(context -> {
            String player = StringArgumentType.getString(context, "player");

            info("Searching...");

            find(player).thenAccept((ser) -> {
                if(ser == null)
                    error("Player not found!");
                else {
                    info(Text.literal(player + " found on " + ser + ", attempting server switch!"));
                    ChatUtils.sendPlayerMsg("/server " + ser);
                }
            });

            return SINGLE_SUCCESS;
        })));

        builder.then(literal("find").then(argument("player", StringArgumentType.string()).executes(context -> {
            String player = StringArgumentType.getString(context, "player");

            info("Searching...");

            find(player).thenAccept((ser) -> {
                if(ser == null)
                    error("Player not found!");
                else
                    info(player + " is on " + ser);
            });

            return SINGLE_SUCCESS;
        })));
    }

    public CompletableFuture<String> find(String player) {
        return CompletableFuture.supplyAsync(() -> {
            try {

                String uuid = JsonParser.parseString(Http.get("https://playerdb.co/api/player/minecraft/" + player).sendString()).getAsJsonObject()
                    .getAsJsonObject("data").getAsJsonObject("player").get("id").getAsString();

                String str = Http.post("https://api.minefort.com/v1/servers/list").bodyJson("{\"pagination\": { \"skip\": 0, \"limit\": 500 }, \"sort\": { \"field\": \"players.online\", \"order\": \"desc\" }}").sendString();

                JsonArray obj = JsonParser.parseString(str).getAsJsonObject().get("result").getAsJsonArray();

                for (JsonElement jsonElement : obj) {
                    for (JsonElement p : jsonElement.getAsJsonObject().getAsJsonObject("players").getAsJsonArray("list")) {
                        if(p.getAsJsonObject().get("uuid").getAsString().equalsIgnoreCase(uuid)) {
                            return jsonElement.getAsJsonObject().get("serverName").getAsString();
                        }
                    }
                }

                return null;
            } catch (Exception e) {
                error("Something went wrong! " + e.getMessage());
                return null;
            }
        });
    }
}
