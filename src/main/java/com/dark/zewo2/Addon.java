package com.dark.zewo2;

import com.dark.zewo2.commands.*;
import com.dark.zewo2.modules.*;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {

    public static String BOOTNAME;
    public static String BOOTUUID;
    public static String BOOTSESSION;

    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Zewo2");
    public static final HudGroup HUD_GROUP = new HudGroup("Zewo2");

    @Override
    public void onInitialize() {
        LOG.info("Kawaii Mode Activated");

        // Modules
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
        Modules.get().add(new Magnet());
        Modules.get().add(new NoSwing());
        Modules.get().add(new chatfilterbypass());
        Modules.get().add(new Airstrike());
        Modules.get().add(new Groupmessage());
        Modules.get().add(new RainbowArmor());
        Modules.get().add(new PenisESP());
        Modules.get().add(new EntityFly());
        Modules.get().add(new FakeAttack());
        Modules.get().add(new AutoHorn());
        Modules.get().add(new AntiSpawnpoint());
        Modules.get().add(new phase());
        Modules.get().add(new AntiBorder());
        Modules.get().add(new Bookchat());
        Modules.get().add(new StrongholdFinder());
        Modules.get().add(new Fling());
        Modules.get().add(new PermJukebox());
        Modules.get().add(new AntiNbtBypasser());


        // Commands
        Commands.add(new CheckCMD());
        Commands.add(new ClearInventoryCommand());
        Commands.add(new CrashItemCommand());
        Commands.add(new CreativeBanCommand());
        Commands.add(new CreativeKickAllCommand());
        Commands.add(new DesyncCommand());
        Commands.add(new DisableVehicleGrav());
        Commands.add(new HologramCommand());
        Commands.add(new ImageBookCommand());
        Commands.add(new ImageLoreCommand());
        Commands.add(new TrashCommand());
        Commands.add(new ReloadCapes());
        Commands.add(new SpamCommand());
        Commands.add(new CrackedOpSpamCommand());

        // HUD
//        Hud.get().register(HudExample.INFO);

        //for the sessionID login screen, so you can return to the account you started with
        String accessed = MinecraftClient.getInstance().getSession().getSessionId().replaceAll("token:", "");
        BOOTSESSION = accessed.split(":")[0];
        BOOTUUID = accessed.split(":")[1];
        BOOTNAME = MinecraftClient.getInstance().getSession().getUsername();
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.dark.zewo2";
    }
}
