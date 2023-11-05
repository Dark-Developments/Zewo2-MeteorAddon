package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.common.KeepAliveC2SPacket;

import java.util.ArrayList;
import java.util.List;

public class PingSpoof extends Module {
    final List<PacketEntry> entries = new ArrayList<>();
    final List<Packet<?>> dontRepeat = new ArrayList<>();

    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The mode to use.")
        .defaultValue(Mode.Spoof)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay.")
        .defaultValue(50)
        .min(0)
        .sliderMax(1000)
        .build()
    );

    public PingSpoof() {
        super(Addon.CATEGORY, "PingSpoof", "Spoof your ping to the given value");
    }

    @Override
    public void onActivate() {
        entries.clear();
        dontRepeat.clear();
    }

    @EventHandler
    private void onsendpacket(PacketEvent.Send event){
        if (!dontRepeat.contains(event.packet) && shouldDelayPacket(event.packet)) {
            event.setCancelled(true);
            entries.add(new PacketEntry(event.packet, delay.get(), System.currentTimeMillis()));
        } else {
            dontRepeat.remove(event.packet);
        }
    }

    boolean shouldDelayPacket(Packet<?> p) {
        if (mode.get() == Mode.Delay) {
            return true; // if we want to delay everything, say yes
        } else {
            return p instanceof KeepAliveC2SPacket; // if we want to fake it, say yes if its a pong or keepalive
        }
    }

    @EventHandler
    private void ontick(TickEvent.Pre event){
        if (mc.getNetworkHandler() == null) {
            toggle();
            return;
        }
        long c = System.currentTimeMillis();
        for (PacketEntry entry : entries.toArray(new PacketEntry[0])) {
            if (entry.entryTime + entry.delay <= c) {
                dontRepeat.add(entry.packet);
                entries.remove(entry);
                mc.getNetworkHandler().sendPacket(entry.packet);
            }
        }
    }

    public enum Mode{
        Delay, Spoof
    }

    record PacketEntry(Packet<?> packet, double delay, long entryTime) {

    }
}
