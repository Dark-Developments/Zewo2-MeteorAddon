package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.client.gui.screen.DemoScreen;
import net.minecraft.network.packet.s2c.play.CloseScreenS2CPacket;

public class AntiScreen extends Module {
    private long lastClosed = 0;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Boolean> endScreen = sgGeneral.add(new BoolSetting.Builder()
        .name("end-screen")
        .description("Removes the end screen after finishing the game.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> demoScreen = sgGeneral.add(new BoolSetting.Builder()
        .name("demo-screen")
        .description("Removes the demo screen.")
        .defaultValue(true)
        .build()
    );

    private final Setting<Boolean> noCloseScreen = sgGeneral.add(new BoolSetting.Builder()
        .name("Anti close")
        .description("Ignore close screen packets.")
        .defaultValue(true)
        .build()
    );

    // Constructor

    public AntiScreen() {
        super(Addon.CATEGORY, "anti-screen", "Removes certain screens in the game.");
    }

    // Getter

    public boolean cancelEndScreen() {
        return this.endScreen.get();
    }

    public boolean cancelDemoScreen() {
        return this.demoScreen.get();
    }

    @EventHandler
    private void kys(OpenScreenEvent event){
        if(endScreen.get() && event.screen instanceof CreditsScreen){
            event.cancel();
        }
        if(demoScreen.get() && event.screen instanceof DemoScreen){
            event.cancel();
        }
    }

    @EventHandler
    public void onPacket(PacketEvent.Receive event){
        if (event.packet instanceof CloseScreenS2CPacket packet && noCloseScreen.get()){
            if (System.currentTimeMillis() - lastClosed < 1000) event.cancel();

            lastClosed = System.currentTimeMillis(); // only stop packets if they are spammed
        }
    }
}
