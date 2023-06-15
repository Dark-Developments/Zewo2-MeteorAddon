package com.nxyi.addon.Utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

public class InvUtils {
    private static MinecraftClient client = MinecraftClient.getInstance();

    public static int finditem(Item item) {
        int index = -1;
        for(int i = 0; i < 45; i++) {
            if(MinecraftClient.getInstance().player.getInventory().getStack(i).getItem() == item) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int findItemInHotbar(Item item) {
        int index = -1;
        for(int i = 0; i < 9; i++) {
            if(MinecraftClient.getInstance().player.getInventory().getStack(i).getItem() == item) {
                index = i;
                break;
            }
        }
        return index;
    }

    public static int getamount(Item item) {
        int amount = 0;
        for(int i = 0; i < 45; i++) {
            if(MinecraftClient.getInstance().player.getInventory().getStack(i).getItem() == item) {
                amount = amount + 1;
            }
        }
        return amount;
    }

    public static void movetoslot(int to, int from){
        client.interactionManager.clickSlot(0, InvUtils.getslot(from), 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, to, 0, SlotActionType.PICKUP, client.player);
        client.interactionManager.clickSlot(0, InvUtils.getslot(from), 0, SlotActionType.PICKUP, client.player);
    }

    public static int getslot(int index) {
        return index < 9 ? index + 36 : index;
    }
}
