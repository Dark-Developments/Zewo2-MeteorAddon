package com.dark.zewo2.mixin;

import com.dark.zewo2.modules.StorageVoider;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.InventoryTweaks;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerScreenMixin extends HandledScreen<GenericContainerScreenHandler> implements ScreenHandlerProvider<GenericContainerScreenHandler> {
    public GenericContainerScreenMixin(GenericContainerScreenHandler container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    @Override
    protected void init() {
        super.init();

        StorageVoider storageVoider = Modules.get().get(StorageVoider.class);

        if (storageVoider.isActive()) {
            addDrawableChild(
                new ButtonWidget.Builder(Text.literal("Voider"), button -> storageVoider.voider(handler))
                    .position(x + backgroundWidth - 150, y + -15)
                    .size(50, 12)
                    .build()
            );
        }
    }
}
