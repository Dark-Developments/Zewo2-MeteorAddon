package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.misc.Keybind;
import meteordevelopment.meteorclient.utils.network.PacketUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PacketDelayer extends Module {
    final List<PacketEntry> S2CEntries = new ArrayList<>();
    final List<PacketEntry> C2SEntries = new ArrayList<>();
    private boolean IgnoreOutgoing = false;

    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("Mode")
        .description("Mode to use")
        .defaultValue(Mode.Delay)
        .build());

    private final Setting<Double> delay = sgGeneral.add(new DoubleSetting.Builder()
            .name("delay")
            .description("The delay.")
            .defaultValue(1000)
            .decimalPlaces(0)
            .sliderRange(0,5000)
            .min(0)
        .visible(() -> mode.get() == Mode.Delay)

        .build()
    );

    @SuppressWarnings("unused")
    private final Setting<Keybind> releaseC2S = sgGeneral.add(new KeybindSetting.Builder()
        .name("releaseC2S")
        .description("Release C2S packets.")
        .action(this::dumpC2SPackets)
            .visible(() -> mode.get() == Mode.Keybind)
        .defaultValue(Keybind.none())
        .build()
    );

    @SuppressWarnings("unused")
    private final Setting<Keybind> releaseS2C = sgGeneral.add(new KeybindSetting.Builder()
        .name("releaseS2C")
        .description("Release S2C packets.")
        .action(this::dumpS2CPackets)
        .visible(() -> mode.get() == Mode.Keybind)
        .defaultValue(Keybind.none())
        .build()
    );

    private final Setting<Set<Class<? extends Packet<?>>>> s2cPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("S2C-packets")
        .description("Server-to-client packets to cancel.")
        .filter(aClass -> PacketUtils.getS2CPackets().contains(aClass))
        .build()
    );

    private final Setting<Set<Class<? extends Packet<?>>>> c2sPackets = sgGeneral.add(new PacketListSetting.Builder()
        .name("C2S-packets")
        .description("Client-to-server packets to cancel.")
        .filter(aClass -> PacketUtils.getC2SPackets().contains(aClass))
        .build()
    );


    public PacketDelayer() {
        super(Addon.CATEGORY, "PacketDelayer", "Delay incoming packets");
    }

    @Override
    public void onActivate() {
        S2CEntries.clear();
        C2SEntries.clear();
        IgnoreOutgoing = false;
    }

    @EventHandler
    private void onTickPre(TickEvent.Pre event){
        if (mode.get() != Mode.Delay) return;

        long c = System.currentTimeMillis();

        for (PacketEntry entry : S2CEntries.toArray(new PacketEntry[0])) {
            if (entry == null) continue;

            if (entry.entryTime + entry.delay <= c) {
                S2CEntries.remove(entry);
                SendPacketToClient(entry.packet, mc.player.networkHandler.getConnection().getPacketListener());
            }
        }

        IgnoreOutgoing = true;
        for (PacketEntry entry : C2SEntries.toArray(new PacketEntry[0])) {
            if (entry == null) continue;

            if (entry.entryTime + entry.delay <= c) {
                C2SEntries.remove(entry);
                SendPacketToServer(entry.packet);
            }
        }
        IgnoreOutgoing = false;
    }

    @EventHandler
    private void onPacketIncoming(PacketEvent.Receive event){
        if (shouldDelayPacket(event.packet)) {

            PacketEntry entry = new PacketEntry(event.packet, delay.get(), System.currentTimeMillis());
            if (entry == null) return;

            event.setCancelled(true);
            S2CEntries.add(entry);
        }
    }

    @EventHandler
    private void onPacketOutgoing(PacketEvent.Send event){
        if (shouldDelayPacket(event.packet) && !IgnoreOutgoing) {

            PacketEntry entry = new PacketEntry(event.packet, delay.get(), System.currentTimeMillis());
            if (entry == null) return;

            event.setCancelled(true);
            C2SEntries.add(entry);
        }
    }

    boolean shouldDelayPacket(Packet<?> p) {
        if (s2cPackets.get().contains(p.getClass()) || c2sPackets.get().contains(p.getClass())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onDeactivate() {
        dumpS2CPackets();
        dumpC2SPackets();
    }

    public void dumpS2CPackets(){
        for (PacketEntry entry : S2CEntries.toArray(new PacketEntry[0])) {
            if (entry == null) continue;

            S2CEntries.remove(entry);
            SendPacketToClient(entry.packet, mc.player.networkHandler.getConnection().getPacketListener());
        }
    }

    public void dumpC2SPackets(){
        for (PacketEntry entry : C2SEntries.toArray(new PacketEntry[0])) {
            if (entry == null) continue;

            C2SEntries.remove(entry);
            SendPacketToServer(entry.packet);
        }
    }

    private static <T extends PacketListener> void SendPacketToClient(Packet<T> packet, PacketListener listener) {
        packet.apply((T) listener);
    }

    private void SendPacketToServer(Packet<?> packet){
        mc.getNetworkHandler().sendPacket(packet);
    }

    @Override
    public String getInfoString() {
        return "In: %s : Out: %s".formatted(S2CEntries.size(), C2SEntries.size());
    }

    record PacketEntry(Packet<?> packet, double delay, long entryTime) {

    }

    public enum Mode{
        Delay,
        Keybind;
    }

}
