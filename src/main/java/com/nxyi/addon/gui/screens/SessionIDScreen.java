package com.nxyi.addon.gui.screens;

import com.nxyi.addon.Addon;
import com.nxyi.addon.gui.settings.SetSession;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.systems.accounts.Account;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.util.Session;

import java.util.Optional;

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

            Account.setSession(new Session(USER.get(), UUID.get(), ID.get(), Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));

            mc.setScreen(new MultiplayerScreen(this.parent));
        };

        t.add(theme.button("Return ACC")).minWidth(220).expandX().widget().action = () -> {
            Account.setSession(new Session(Addon.BOOTNAME, Addon.BOOTUUID, Addon.BOOTSESSION, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));

            mc.setScreen(new MultiplayerScreen(this.parent));
        };
    }
}
