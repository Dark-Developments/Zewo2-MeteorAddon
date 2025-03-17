package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.InvUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.CompletableFuture;

public class MaceInstaKill extends Module {
    private boolean ignorePacket = false;

    SettingGroup settingGroup = settings.getDefaultGroup();

    final private Setting<Boolean> silentSwitch = settingGroup.add(new BoolSetting.Builder()
            .name("Silent-Switch")
            .defaultValue(false)
            .description("Silently switch to mace if in hotbar")
            .build());

    final private Setting<Boolean> autoHeight = settingGroup.add(new BoolSetting.Builder()
            .name("Auto-Height")
            .description("Automatically calculate height")
            .defaultValue(true)
            .build());

    final private Setting<Double> height = settingGroup.add(new DoubleSetting.Builder()
            .name("Range")
            .description("Height to attack from")
            .defaultValue(20)
            .sliderRange(10,100)
            .max(100)
            .min(0)
            .decimalPlaces(0)
            .visible(() -> !autoHeight.get())
            .build());

    final private Setting<Boolean> preDamage = settingGroup.add(new BoolSetting.Builder()
            .name("Pre-Dmg")
            .description("damage yourself before. to have damage tick invulnerability")
            .defaultValue(true)
            .build());

    public MaceInstaKill() {
        super(Addon.CATEGORY,"MaceInstaKill", "InstaKill people");
    }

    @Override
    public void onActivate() {
        ignorePacket = false;
    }

    @EventHandler
    public void onPacketSend(PacketEvent.Send event) {
        if (ignorePacket) return;
        if (event.packet instanceof IPlayerInteractEntityC2SPacket packet && packet.meteor$getType()  == PlayerInteractEntityC2SPacket.InteractType.ATTACK.ATTACK && (mc.player.getMainHandStack().getItem() instanceof MaceItem || silentSwitch.get())){
            event.cancel();

            Entity victim = packet.meteor$getEntity();
            if (victim == null) return;
            if (!(victim instanceof LivingEntity entity)) return;

            doAttack(entity, height.get(), silentSwitch.get(), autoHeight.get(), preDamage.get());
        }
    }

    public void doAttack(LivingEntity entity, double height, boolean silent, boolean autoHeight, boolean preDmg){
        CompletableFuture.runAsync(() -> {
            int realSlot = mc.player.getInventory().selectedSlot;

            if (silent) {
                int glowstone = InvUtils.findItemInHotbar(Items.MACE);
                if (glowstone == -1) return;
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(glowstone));
            }

            double attackHeight = height;
            if (autoHeight) attackHeight = (entity.getHealth() / 0.5) + 3;

            Vec3d pos = mc.player.getPos();

            if (preDmg) {
                moveTo(pos.add(0, 4, 0), true);
                moveTo(pos, false);
            }

            buildTpRange(10);
            moveTo(pos.add(0,attackHeight,0), false);
            moveTo(pos, false);
            mc.player.setPosition(pos);

            ignorePacket = true;
            mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(entity, mc.player.isSneaking()));
            ignorePacket = false;

            if (silent){
                mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(realSlot));
            }
        });
    }

    public void moveTo(Vec3d pos, boolean onGround) {
        if (mc.player == null) return;

        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(pos.x, pos.y, pos.z, onGround, false));

    }

    public void buildTpRange(int amount){
        for (int i = 0; i < amount; i++){
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true, false));
        }
    }
}
