package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.Utils;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

public class AutoWordReact extends Module {
    public AutoWordReact() {
        super(Addon.CATEGORY, "AutoWordReact", "Automatically say word reactions");
    }

    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final Setting<String> text = sgGeneral.add(
        new StringSetting.Builder().name("text").description("unformatted text before word to look for").defaultValue("").build());

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay. (ms)")
        .defaultValue(0)
        .min(0)
        .sliderMax(1000)
        .build()
    );
    @EventHandler
    private void onmessage(ReceiveMessageEvent event){
        String check = String.valueOf(Text.literal(event.getMessage().getString()));
        String message = check.substring(check.indexOf(text.get()) + text.get().length() + 1, check.length()).split(" ")[0];

        if (check.contains(text.get())) {
            new Thread(() -> {
                try {
                    Thread.sleep(delay.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                mc.player.networkHandler.sendChatMessage(message);
            }).start();
        }
    }
}
