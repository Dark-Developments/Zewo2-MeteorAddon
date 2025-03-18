package com.dark.zewo2.gui.screens;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WTable;
import meteordevelopment.meteorclient.gui.widgets.input.WTextBox;
import meteordevelopment.meteorclient.systems.accounts.Account;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.session.Session;

import java.util.Optional;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class SessionIDScreen extends WindowScreen {

    public SessionIDScreen() {
        super(GuiThemes.get(), "Session ID login");
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

            Account.setSession(new Session(USER.get(), java.util.UUID.fromString(UUID.get()), ID.get(), Optional.empty(), Optional.empty(), Session.AccountType.MSA));

            mc.setScreen(new MultiplayerScreen(this.parent));
        };

        t.add(theme.button("Return ACC")).minWidth(220).expandX().widget().action = () -> {
            Account.setSession(new Session(Addon.BOOTNAME, java.util.UUID.fromString(Addon.BOOTUUID), Addon.BOOTSESSION, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));

            mc.setScreen(new MultiplayerScreen(this.parent));
        };
    }
}
