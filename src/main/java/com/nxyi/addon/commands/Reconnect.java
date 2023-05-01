package com.nxyi.addon.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.nxyi.addon.Addon;
import com.nxyi.addon.gui.settings.SetSession;
import com.nxyi.addon.login.easymc.AltTokenResponse;
import com.nxyi.addon.login.easymc.EasyMCRequest;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;

public class Reconnect extends Command {
    public Reconnect() {
        super("rejoin", "Reconnect you with different things", "Reconnect");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("easymc")
            .then(argument("token", StringArgumentType.greedyString()).executes(ctx -> {
                String TOKEN = StringArgumentType.getString(ctx ,"token");
                new Thread(() -> {
                    if (TOKEN.isEmpty()) return;

                    EasyMCRequest request = new EasyMCRequest();
                    AltTokenResponse response = null;
                    try {
                        response = request.getResponse(TOKEN);
                    } catch (URISyntaxException | InterruptedException | IOException e) {
                        //error
                    }

                    SetSession.username = response.getMcName();
                    SetSession.UUID = response.getUuid();
                    SetSession.accessToken = response.getSession();

                    SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
                    SetSession.originalSession = false;
                }).start();
                rejoin();

                return 0;
            })));

        builder.then(literal("return").executes(context -> {
            SetSession.username = Addon.BOOTNAME;
            SetSession.UUID = Addon.BOOTUUID;
            SetSession.accessToken = Addon.BOOTSESSION;

            SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
            SetSession.originalSession = true;
            rejoin();
            return 0;
        }));
    }

    private void rejoin(){
        ServerInfo lastServerInfo = mc.isInSingleplayer() ? null : mc.getCurrentServerEntry();
        mc.player.networkHandler.getConnection().disconnect(Text.literal(""));
        ConnectScreen.connect(null, mc, ServerAddress.parse(lastServerInfo.address), lastServerInfo);
    }
}
