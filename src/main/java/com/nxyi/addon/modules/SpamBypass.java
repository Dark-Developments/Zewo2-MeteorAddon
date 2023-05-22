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

    private final Setting<String> bc = sgGeneral.add(new StringSetting.Builder().name("Command").description("Bypass Command").defaultValue("/skill").build());

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

    private final Setting<Boolean> rndString = sgGeneral.add(new BoolSetting.Builder()
        .name("rndString")
        .description("add random string to the end.")
        .defaultValue(true)
        .build()
    );
    private final Setting<Integer> length = sgGeneral.add(new IntSetting.Builder()
        .name("length")
        .description("The length of the random string.")
        .defaultValue(4)
        .sliderRange(1, 10)
        .visible(() -> rndString.get())
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
                    mc.player.networkHandler.sendChatMessage(bc.get() + " " + message + (((rndString.get()) ? " " + JinxUtils.randomString(length.get()) : "")));
                    timer = delay.get();
                } else {
                    timer--;
                }
            }

            case AmountPerTick -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.networkHandler.sendChatMessage(bc.get() + " " + message + (((rndString.get()) ? " " + JinxUtils.randomString(length.get()) : "")));
                }
            }
        }
    }

    public enum Modes{
        DelayPerTick,
        AmountPerTick
    }
}
