package com.dark.zewo2.mixin;

import meteordevelopment.meteorclient.utils.misc.input.KeyBinds;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = KeyBinds.class, remap = false)

public class NoKeybindsModule {

    @Overwrite
    public static KeyBinding[] apply(KeyBinding[] binds) {
        return binds;
    }
}
