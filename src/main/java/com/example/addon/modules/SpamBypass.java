package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.entity.Target;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;

public class SpamBypass extends Module {

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<String> bc = sgGeneral.add(new StringSetting.Builder().name("Command").description("Bypass Command").defaultValue("/skill").build());

    private final Setting<String> message = sgGeneral.add(new StringSetting.Builder().name("Message").description("Message To Spam").defaultValue("Jinx").build());

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
                    mc.player.networkHandler.sendChatMessage(bc + " " + message);
                    timer = delay.get();
                } else {
                    timer--;
                }
            }

            case AmountPerTick -> {
                for (int i = 0; i < amount.get(); i++) {
                    mc.player.networkHandler.sendChatMessage(bc + " " + message);
                }
            }

        }
    }

    public enum Modes{
        DelayPerTick,
        AmountPerTick
    }
}
