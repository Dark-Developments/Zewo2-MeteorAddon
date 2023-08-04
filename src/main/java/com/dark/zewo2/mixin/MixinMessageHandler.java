package com.dark.zewo2.mixin;

import com.dark.zewo2.modules.Girlboss;
import com.mojang.authlib.GameProfile;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;

@Mixin(MessageHandler.class)
public class MixinMessageHandler {

    @Inject(method = "onChatMessage", at = @At("HEAD"))
    public void onChatMessage(final SignedMessage message, final GameProfile sender, final MessageType.Parameters params, final CallbackInfo ci) {
        if (!Modules.get().get(Girlboss.class).isActive()) return;

        if (message.signature() == null) {
            return;
        }
        Girlboss.receivedMessage(message.signature().toByteBuffer());
        for (final MessageSignatureData lastSeen : message.signedBody().lastSeenMessages().entries()) {
            final ByteBuffer signatureByteBuffer = lastSeen.toByteBuffer();
            Girlboss.addSeenSignature(sender.getId(), signatureByteBuffer);
        }
    }

}
