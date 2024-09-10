package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TotemNotifier extends Module {
    final Map<UUID, Integer> popMap = new HashMap<>();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private Setting<Boolean> announce = sgGeneral.add(new BoolSetting.Builder()
            .name("Announce")
            .description("Announce the pop in chat")
            .defaultValue(false)
            .build());

    private Setting<Boolean> self = sgGeneral.add(new BoolSetting.Builder()
        .name("Self")
        .description("Include yourself in the pops.")
        .defaultValue(false)
        .build());

    public TotemNotifier() {
        super(Addon.CATEGORY, "TotemNotifier", "Notify you when someone pops a totem");
    }

    @Override
    public void onActivate() {
        popMap.clear();
    }

    @EventHandler
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket packet)) return;

        if (packet.getStatus() != 35) return;

        Entity entity = packet.getEntity(mc.world);

        if (entity == mc.player && !self.get()) return;

        if (!(entity instanceof PlayerEntity player)) return;

        int pops = popMap.getOrDefault(player.getUuid(), 0);
        popMap.put(player.getUuid(), ++pops);

        String message = "%s has popped their %s totem".formatted(player.getGameProfile().getName(), pops);

        if (announce.get()) ChatUtils.sendPlayerMsg(message);
        else ChatUtils.info(message);
    }
}
