package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;

public class AntiBorder extends Module {
    public AntiBorder() {
        super(Addon.CATEGORY, "AntiBorder", "Remove collisions from the world border");
    }
}
