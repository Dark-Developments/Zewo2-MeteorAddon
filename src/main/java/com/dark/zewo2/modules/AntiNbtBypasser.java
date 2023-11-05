package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

public class AntiNbtBypasser extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> reset = sgGeneral.add(new BoolSetting.Builder().name("Overwrite").defaultValue(false).build());
    public AntiNbtBypasser() {
        super(Addon.CATEGORY, "AntiNbtBypasser", "Bypass some nbt restrictions");
    }

    @EventHandler
    private void onsent(PacketEvent.Send event){
        if (event.packet instanceof CreativeInventoryActionC2SPacket packet) {
            ItemStack spoofed = packet.getStack();
            if (spoofed.getItem() == Items.AIR) {
                return;
            }
            if (reset.get()) {
                toggle();
                mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36, new ItemStack(Items.AIR, 0)));
                toggle();
                if (mc.player.getMainHandStack().getItem() != Items.AIR) {
                    toggle();
                    mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(36 + mc.player.getInventory().selectedSlot, new ItemStack(Items.AIR, 0)));
                    toggle();
                }
            }
            spoofed.setCount(1);
            event.cancel();
            toggle();
            mc.player.networkHandler.sendPacket(new CreativeInventoryActionC2SPacket(-999, spoofed));
            toggle();
        }
    }
}
