package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.JinxUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

public class Fling extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> delay = sgGeneral.add(new DoubleSetting.Builder()
        .name("Power")
        .description("The power of the fling")
        .defaultValue(3)
        .sliderRange(1, 3)
        .decimalPlaces(0)
        .build());

    private final Setting<Double> updist = sgGeneral.add(new DoubleSetting.Builder()
        .name("Delay")
        .description("The delay before going back down")
        .defaultValue(250)
        .sliderRange(1, 500)
        .decimalPlaces(0)
        .build());
    boolean not = true;

    public Fling() {
        super(Addon.CATEGORY, "Fling", "Fling players in the air");
    }

    @EventHandler
    private void onpacket(PacketEvent.Send event){
        if (!not) return;
        if (event.packet instanceof PlayerInteractItemC2SPacket) {
            if (mc.player.getInventory().getMainHandStack().getItem() == Items.FISHING_ROD && (mc.player.fishHook != null)) {
                if (mc.player.fishHook.isRemoved()) return;
                mc.player.setVelocity(Vec3d.ZERO);
                event.setCancelled(true);
                new Thread(() -> {
                    double staticy = mc.player.getY();
                    for (int i = 0; i < updist.get(); i++) {
                        staticy = staticy + 9;
                        JinxUtils.sleep(5);
                        mc.player.setVelocity(Vec3d.ZERO);
                        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), staticy, mc.player.getZ(), true));
                    }
                    JinxUtils.sleep(delay.get().longValue());
                    not = false;
                    mc.player.networkHandler.getConnection().send(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0));
                    not = true;
                    JinxUtils.sleep(delay.get().longValue());
                    for (int i = 0; i < updist.get(); i++) {
                        staticy = staticy - 9;
                        JinxUtils.sleep(5);
                        mc.player.setVelocity(Vec3d.ZERO);
                        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), staticy, mc.player.getZ(), true));
                    }
                }).start();
            }
        }
    }
}
