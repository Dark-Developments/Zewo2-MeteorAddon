package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.network.MeteorExecutor;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class StorageVoider extends Module {

    ScreenHandler handler;
    public StorageVoider() {
        super(Addon.CATEGORY, "Storage Voider", "Void everything in a storage block");
    }
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private int getRows(ScreenHandler handler) {
        return (handler instanceof GenericContainerScreenHandler ? ((GenericContainerScreenHandler) handler).getRows() : 3);
    }
    private void moveSlots(ScreenHandler handler, int start, int end) {
        for (int i = start; i < end; i++) {
            if (!handler.getSlot(i).hasStack()) continue;

            // Exit if user closes screen
            if (mc.currentScreen == null) break;
            mc.interactionManager.clickSlot(mc.player.currentScreenHandler.syncId, i, 50, SlotActionType.SWAP, mc.player);
        }
    }

    public void voider(ScreenHandler handler) {
        MeteorExecutor.execute(() -> moveSlots(handler, 0, getRows(handler) * 9));
    }
}
