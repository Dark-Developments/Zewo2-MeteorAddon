package com.nxyi.addon;

import com.nxyi.addon.commands.*;
import com.nxyi.addon.hud.HudExample;
import com.nxyi.addon.modules.*;
import com.mojang.logging.LogUtils;
import com.nxyi.addon.commands.*;
import com.nxyi.addon.modules.*;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Kapuncino");
    public static final HudGroup HUD_GROUP = new HudGroup("Kapuncino");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // Modules
        Modules.get().add(new SpamBypass());
        Modules.get().add(new SoundCoordLogger());
        Modules.get().add(new InstaMine());
        Modules.get().add(new NoChatFormatting());
        Modules.get().add(new NoChatNormalisation());
        Modules.get().add(new NoClearChat());
        Modules.get().add(new AntiScreen());
        Modules.get().add(new GhostBlockFly());
        Modules.get().add(new GhostMode());
        Modules.get().add(new GMnotifier());
        Modules.get().add(new Suicide());
        Modules.get().add(new WorldGuardBypass());
        Modules.get().add(new BetterAutoSign());
        Modules.get().add(new AutoL());
        Modules.get().add(new PacketLogger());
        Modules.get().add(new Magnet());
        Modules.get().add(new NoSwing());
        Modules.get().add(new chatfilterbypass());

        // Commands
        Commands.get().add(new CheckCMD());
        Commands.get().add(new ClearInventoryCommand());
        Commands.get().add(new CrashItemCommand());
        Commands.get().add(new CreativeBanCommand());
        Commands.get().add(new CreativeKickAllCommand());
        Commands.get().add(new DesyncCommand());
        Commands.get().add(new DisableVehicleGrav());
        Commands.get().add(new HologramCommand());
        Commands.get().add(new ImageBookCommand());
        Commands.get().add(new ImageLoreCommand());
        Commands.get().add(new TrashCommand());

        // HUD
        Hud.get().register(HudExample.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.nxyi.addon";
    }
}
