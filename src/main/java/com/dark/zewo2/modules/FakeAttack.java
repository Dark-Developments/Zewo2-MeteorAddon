package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.entity.player.AttackEntityEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

public class FakeAttack extends Module {
    public FakeAttack() {
        super(Addon.CATEGORY, "FakeAttack", "Doesnt send the attack packet");
    }

    @EventHandler
    private void onattack(AttackEntityEvent event){
        event.cancel();
        mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
    }
}
