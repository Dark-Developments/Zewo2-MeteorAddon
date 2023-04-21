package com.nxyi.addon.gui.screens;

import com.nxyi.addon.Addon;
import com.nxyi.addon.gui.settings.SetSession;
import com.nxyi.addon.login.easymc.AltTokenResponse;
import com.nxyi.addon.login.easymc.EasyMCRequest;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

public class EasyMCScreen extends WindowScreen {
    private final MultiplayerScreen multiplayerScreen;
    MinecraftClient mc = MinecraftClient.getInstance();

    public EasyMCScreen(GuiTheme theme, MultiplayerScreen multiplayerScreen, Screen parent) {
        super(theme, " EasyMC login");
        this.multiplayerScreen = multiplayerScreen;
        this.parent = parent;
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).expandX().widget();

        // Token
        t.add(theme.label("Token"));
        WTextBox TOKEN = t.add(theme.textBox("")).minWidth(220).expandX().widget();
        t.row();



        // Add
        t.add(theme.button("Done")).minWidth(220).expandX().widget().action = () -> {
            //starting new thread cause im lasy and if bad response it doesnt crash

            new Thread(() -> {
                if (TOKEN.get().isEmpty()) return;

                EasyMCRequest request = new EasyMCRequest();
                AltTokenResponse response = null;
                try {
                    response = request.getResponse(TOKEN.get());
                } catch (URISyntaxException | InterruptedException | IOException e) {
                    TOKEN.set("Error!");
                }

                SetSession.username = response.getMcName();
                SetSession.UUID = response.getUuid();
                SetSession.accessToken = response.getSession();

                SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
                SetSession.originalSession = false;
            }).start();
            mc.setScreen(new MultiplayerScreen(this.parent));
        };

        t.add(theme.button("Return ACC")).minWidth(220).expandX().widget().action = () -> {
            SetSession.username = Addon.BOOTNAME;
            SetSession.UUID = Addon.BOOTUUID;
            SetSession.accessToken = Addon.BOOTSESSION;

            SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
            SetSession.originalSession = true;
            mc.setScreen(new MultiplayerScreen(this.parent));
        };
    }
}
