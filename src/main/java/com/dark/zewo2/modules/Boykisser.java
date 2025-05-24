package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.systems.modules.Module;
import net.minecraft.util.Identifier;

public class Boykisser extends Module {
    public Boykisser() {
        super(Addon.CATEGORY, "Boykisser", "Makes everyone a boykisser :3");
    }

    public static final Identifier boykisser = Identifier.of("zewo2","boykisserskin.png");
}
