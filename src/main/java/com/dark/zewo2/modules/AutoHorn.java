package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.entity.EntityAddedEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.util.Hand;

public class AutoHorn extends Module {
    public AutoHorn() {
        super(Addon.CATEGORY, "AutoHorn", "Automatically use horn if someone enters render distance");
    }

    @EventHandler
    private void onenter(EntityAddedEvent event){
        if (event.entity instanceof PlayerEntity){
            FindItemResult hashorn = InvUtils.findInHotbar(itemStack -> itemStack.getItem() instanceof GoatHornItem);
            if (!hashorn.found()) return;
            int prevslot = mc.player.getInventory().selectedSlot;
            mc.player.getInventory().selectedSlot = hashorn.slot();
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.player.getInventory().selectedSlot = prevslot;
        }
    }
}
