package com.dark.zewo2.mixin;

import com.dark.zewo2.modules.Boykisser;
import com.mojang.authlib.GameProfile;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Final
    @Shadow
    private Supplier<SkinTextures> texturesSupplier;

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void onGetTexture(CallbackInfoReturnable<SkinTextures> cir) {
        if (Modules.get().get(Boykisser.class).isActive()) {
            cir.setReturnValue(
                new SkinTextures(
                    Boykisser.boykisser,
                    texturesSupplier.get().textureUrl(),
                    texturesSupplier.get().capeTexture(),
                    texturesSupplier.get().elytraTexture(),
                    SkinTextures.Model.SLIM,
                    texturesSupplier.get().secure()
                )
            );
        }
    }
}
