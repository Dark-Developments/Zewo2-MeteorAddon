package com.dark.zewo2.gui.screens;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.gui.WindowScreen;
import meteordevelopment.meteorclient.gui.widgets.containers.WContainer;
import meteordevelopment.meteorclient.gui.widgets.containers.WHorizontalList;
import meteordevelopment.meteorclient.gui.widgets.pressable.WButton;
import meteordevelopment.meteorclient.systems.accounts.Accounts;
import meteordevelopment.meteorclient.utils.misc.NbtUtils;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class accounts extends WindowScreen {
    public accounts() {
        super(GuiThemes.get(), Addon.CATEGORY.toString());
    }

    @Override
    public void initWidgets() {
        // Add account
        WHorizontalList l = add(theme.horizontalList()).expandX().widget();

        addButton(l, "SessionLogin", () -> mc.setScreen(new SessionIDScreen()));
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
