package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import com.nxyi.addon.Utils.JinxUtils;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.Utils;
import meteordevelopment.orbit.EventHandler;

public class SpamBypass extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();


    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder().name("Message").description("Message To Spam").defaultValue("Jinx").build());

    private final Setting<Bypass> bypass = sgGeneral.add(new EnumSetting.Builder<Bypass>()
        .name("bypass")
        .description("bypass")
        .defaultValue(Bypass.Command)
        .build()
    );
    private final Setting<String> bc = sgGeneral.add(new StringSetting.Builder().name("Command").description("Bypass Command").defaultValue("/skill").visible(() -> bypass.get().equals(Bypass.Command)).build());

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("modes")
        .defaultValue(Modes.DelayPerTick)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between specified messages in ticks.")
        .defaultValue(20)
        .sliderRange(0, 20)
        .visible(() -> !mode.get().equals(Modes.AmountPerTick))
        .build()
    );

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount")
        .description("The Amount of messages to send / tick")
        .defaultValue(20)
        .sliderRange(0, 20)
        .visible(() -> !mode.get().equals(Modes.DelayPerTick))
        .build()
    );


    private int timer;

    public SpamBypass() {
        super(Addon.CATEGORY,"Spam-Bypass", "Better than spam.");
    }

    @Override
    public void onActivate() {
        timer = delay.get();
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        switch (mode.get()) {

            case DelayPerTick -> {
                if (timer <= 0) {
                    mc.player.networkHandler.sendChatMessage(((bypass.get() == Bypass.Command) ? bc.get() : "") + " " + message + (((bypass.get() == Bypass.RndString) ? " " + JinxUtils.randomString(4) : "")));
                    timer = delay.get();
                } else {
                    timer--;
                }
            }

            case AmountPerTick -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.networkHandler.sendChatMessage(((bypass.get() == Bypass.Command) ? bc.get() : "") + " " + message + (((bypass.get() == Bypass.RndString) ? " " + JinxUtils.randomString(4) : "")));
                }
            }
        }
    }

    public enum Modes{
        DelayPerTick,
        AmountPerTick
    }

    public enum Bypass{
        Command,
        RndString
    }
}
