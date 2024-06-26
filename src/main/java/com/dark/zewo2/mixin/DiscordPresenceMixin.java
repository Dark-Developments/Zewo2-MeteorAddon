package com.dark.zewo2.mixin;


import meteordevelopment.discordipc.RichPresence;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(value = DiscordPresence.class, remap = false, priority = 1002)
public class DiscordPresenceMixin {
    @ModifyArg(method = "onActivate", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/DiscordIPC;start(JLjava/lang/Runnable;)Z"))
    private long modifyAppId(long appId) {
        return 1098702463646842901L;
    }

    @ModifyArgs(method = "onActivate", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setLargeImage(Ljava/lang/String;Ljava/lang/String;)V"))
    private void modifyLargeImage(Args args) {
        args.set(0, "big");
        args.set(1, "discord.gg/wRMaDcMceR");
    }

    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setState(Ljava/lang/String;)V", ordinal = 0))
    private void modifyState(RichPresence instance, String state) {
        instance.setDetails(state);
    }

    @ModifyArg(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setDetails(Ljava/lang/String;)V", ordinal = 1))
    private String modifyDetails(String details) {
        return "real";
    }

    @Redirect(method = "onTick", at = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setState(Ljava/lang/String;)V"), slice = @Slice(from = @At(value = "INVOKE", target = "Lmeteordevelopment/discordipc/RichPresence;setDetails(Ljava/lang/String;)V", ordinal = 1)))
    private void deleteMethods(RichPresence instance, String state) {}
}
