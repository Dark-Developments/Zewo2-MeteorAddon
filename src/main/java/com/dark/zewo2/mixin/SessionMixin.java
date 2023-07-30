package com.dark.zewo2.mixin;

import com.mojang.authlib.GameProfile;
import com.dark.zewo2.gui.settings.SetSession;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Session.class)
public class SessionMixin {

    @Inject(at=@At("TAIL"), method="getSessionId", cancellable = true)

    private void getSessionId(CallbackInfoReturnable<String> cir) {
        if (SetSession.originalSession) return;
        cir.setReturnValue(SetSession.sessionid);
    }

    @Inject(at=@At("TAIL"), method="getAccessToken", cancellable = true)
    private void getAccessToken(CallbackInfoReturnable<String> cir) {
        if (SetSession.originalSession) return;
        cir.setReturnValue(SetSession.accessToken);
    }

    @Inject(at=@At("TAIL"), method="getUsername", cancellable = true)
    private void getUsername(CallbackInfoReturnable<String> cir) {
        if (SetSession.originalSession) return;
        cir.setReturnValue(SetSession.username);
    }

    @Inject(at=@At("TAIL"), method="getUuid", cancellable = true)
    private void getUuid(CallbackInfoReturnable<String> cir) {
        if (SetSession.originalSession) return;
        cir.setReturnValue(SetSession.UUID);
    }

    @Inject(at=@At("TAIL"), method="getProfile", cancellable = true)
    private void getProfile(CallbackInfoReturnable<GameProfile> cir) {
        if (SetSession.originalSession) return;
        cir.setReturnValue(SetSession.getGameProfile());
    }
}
