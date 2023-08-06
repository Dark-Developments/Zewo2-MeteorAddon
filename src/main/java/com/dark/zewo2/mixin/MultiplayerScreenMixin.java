package com.dark.zewo2.mixin;

import com.dark.zewo2.Addon;
import com.dark.zewo2.gui.screens.accounts;
import meteordevelopment.meteorclient.gui.GuiThemes;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    protected MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        addDrawableChild(ButtonWidget.builder(Text.of(Addon.CATEGORY.toString()), button -> {
                client.setScreen(new accounts(GuiThemes.get(), (MultiplayerScreen) (Object) this));
            })
            .position(this.width - 75 -3, 512 + 3)
            .size(75, 20)
            .build());
    }
}
