package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class Suicide extends Module {
    int Timer;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> velo = sgGeneral.add(new DoubleSetting.Builder()
        .name("velo")
        .description("the velocity you are thrown at")
        .defaultValue(1)
        .min(0.1)
        .build()
    );

    private final Setting<Double> Timerc = sgGeneral.add(new DoubleSetting.Builder()
        .name("timer")
        .description("")
        .defaultValue(10)
        .min(1)
        .build()
    );

    public Suicide() {
        super(Addon.CATEGORY, "Suicide", "you fell from a high place");
    }

    @Override
    public void onActivate() {
        Timer = Timerc.get().intValue();

        mc.player.setVelocity(0, velo.get(), 0);
    }

    @EventHandler
    private void onTick(TickEvent.Pre event){
        if (Timer <= 0){
            mc.player.setVelocity(0, -velo.get(), 0);
            Timer = Timerc.get().intValue();
            toggle();
        } else {
            Timer--;
        }
    }
}
