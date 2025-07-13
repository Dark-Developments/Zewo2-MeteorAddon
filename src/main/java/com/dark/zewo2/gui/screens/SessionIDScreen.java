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
            if (ID.get().isEmpty() || USER.get().isEmpty()) return;
            if (UUID.get().isEmpty()){
                ID.set(ID.get().replaceAll("token:", ""));
                String tokenPart = ID.get().split(":")[0];
                String uuidPart = ID.get().split(":")[1];
                setSession(USER.get(), uuidPart, tokenPart);
            } else {
                setSession(USER.get(), UUID.get(), ID.get());
            }

            mc.setScreen(new MultiplayerScreen(this.parent));
        };

        t.add(theme.button("Return ACC")).minWidth(220).expandX().widget().action = () -> {
            setSession(Addon.BOOTNAME, Addon.BOOTUUID, Addon.BOOTSESSION);
            mc.setScreen(new MultiplayerScreen(this.parent));
        };
    }

    public void setSession(String name, String uuid, String token){
        try {
            Account.setSession(new Session(name, java.util.UUID.fromString(uuid), token, Optional.empty(), Optional.empty(), Session.AccountType.MSA));
        } catch (Exception e){
            // so we dont crash
        }
    }
}
