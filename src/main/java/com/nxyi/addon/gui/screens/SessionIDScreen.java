package com.nxyi.addon.gui.screens;

import com.nxyi.addon.Addon;
import com.nxyi.addon.gui.settings.SetSession;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

public class SessionIDScreen extends WindowScreen {
    private final MultiplayerScreen multiplayerScreen;
    MinecraftClient mc = MinecraftClient.getInstance();

    public SessionIDScreen(GuiTheme theme, MultiplayerScreen multiplayerScreen, Screen parent) {
        super(theme, "Session ID login");
        this.multiplayerScreen = multiplayerScreen;
        this.parent = parent;
    }

    @Override
    public void initWidgets() {
        WTable t = add(theme.table()).expandX().widget();

        // Token
        t.add(theme.label("UserName"));
        WTextBox USER = t.add(theme.textBox("")).minWidth(220).expandX().widget();
        t.row();

        t.add(theme.label("UUID"));
        WTextBox UUID = t.add(theme.textBox("")).minWidth(220).expandX().widget();
        t.row();

        t.add(theme.label("Session"));
        WTextBox ID = t.add(theme.textBox("")).minWidth(220).expandX().widget();
        t.row();

        // Add
        t.add(theme.button("Done")).minWidth(220).expandX().widget().action = () -> {
            if (ID.get().isEmpty() || UUID.get().isEmpty() || USER.get().isEmpty()) return;

            SetSession.username = USER.get();
            SetSession.UUID = UUID.get();
            SetSession.accessToken = ID.get();

            SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
            SetSession.originalSession = false;
            mc.setScreen(new MultiplayerScreen(this.parent));
        };

        t.add(theme.button("Return ACC")).minWidth(220).expandX().widget().action = () -> {
            SetSession.username = Addon.BOOTNAME;
            SetSession.UUID = Addon.BOOTUUID;
            SetSession.accessToken = Addon.BOOTSESSION;

            SetSession.sessionid = "token:" + SetSession.accessToken + ":" + SetSession.UUID;
            SetSession.originalSession = false;
            mc.setScreen(new MultiplayerScreen(this.parent));
        };
    }
}
