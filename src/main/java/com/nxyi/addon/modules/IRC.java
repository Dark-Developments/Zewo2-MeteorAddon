package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.data.client.Model;

public class IRC extends Module {
    public IRC() {
        super(Addon.CATEGORY, "IRC", "chat with other people on the client");
    }

    @Override
    public void onActivate() {
        Addon.startIRC(mc.getSession().getUsername());
    }
}
