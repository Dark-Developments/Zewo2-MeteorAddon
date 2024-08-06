package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.JinxUtils;
import com.dark.zewo2.Utils.Utils;
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

import java.util.concurrent.CompletableFuture;

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
    public void onPacket(PacketEvent.Send event){
        if (event.packet instanceof PlayerInteractItemC2SPacket) {
            if (mc.player.getMainHandStack().getItem() != Items.FISHING_ROD || mc.player.fishHook == null || mc.player.fishHook.getHookedEntity() == null) return;

            if (mc.player.fishHook.isRemoved()) return;
            event.cancel();

            double eDistance2Player = mc.player.distanceTo(mc.player.fishHook.getHookedEntity());
            double distance = (11 * updist.get()) - (eDistance2Player > 2 ? eDistance2Player : 0);

            int packetsRequired = (int) Math.ceil(Math.abs(distance / 10));

            new Thread(() -> {
                buildTpRange(packetsRequired);

                Vec3d pos = mc.player.getPos();
                moveTo(pos.add(0, distance, 0));

                JinxUtils.sleep(250);

                toggle(); // i cannot be bothered to find if meteor has an eventLess packet util.
                mc.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, 0, 0));
                toggle();

                JinxUtils.sleep(250);

                buildTpRange(packetsRequired); // "minecraft resets your movement charge like every tick"
                mc.player.setVelocity(Vec3d.ZERO);
                moveTo(pos.add(0, 0.01, 0));
            }).start();

        }
    }

    private void buildTpRange(int amount){
        Vec3d p = mc.player.getPos();

        for (int i = 0; i < amount; i++){
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y, p.z, true));
        }

    }

    private void moveTo(Vec3d p){
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(p.x, p.y, p.z, true));
        mc.player.setPosition(p);
    }
}
