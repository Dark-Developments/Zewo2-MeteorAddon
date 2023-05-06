package com.nxyi.addon.gui.screens;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.gui.GuiTheme;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;

import static meteordevelopment.meteorclient.MeteorClient.ADDON;
import static meteordevelopment.meteorclient.MeteorClient.mc;

public class accounts extends WindowScreen {
    private final MultiplayerScreen multiplayerScreen;
    public accounts(GuiTheme theme, MultiplayerScreen multiplayerScreen) {
        super(theme, Addon.CATEGORY.toString());
        this.parent = multiplayerScreen;
        this.multiplayerScreen = multiplayerScreen;
    }

    @Override
    public void initWidgets() {
        // Add account
        WHorizontalList l = add(theme.horizontalList()).expandX().widget();

        addButton(l, "SessionLogin", () -> mc.setScreen(new SessionIDScreen(theme, multiplayerScreen, this)));
    }

    private void addButton(WContainer c, String text, Runnable action) {
        WButton button = c.add(theme.button(text)).expandX().widget();
        button.action = action;
    }

    @Override
    public boolean toClipboard() {
        return NbtUtils.toClipboard(Accounts.get());
    }

    @Override
    public boolean fromClipboard() {
        return NbtUtils.fromClipboard(Accounts.get());
    }
}
