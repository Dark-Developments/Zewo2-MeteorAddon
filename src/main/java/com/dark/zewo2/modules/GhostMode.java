/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.modules;
import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.events.game.GameJoinedEvent;
import meteordevelopment.meteorclient.events.game.OpenScreenEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gui.screen.DeathScreen;

public class GhostMode extends Module {

    public GhostMode() {
        super(Addon.CATEGORY, "ghost-mode", "Explore the world after dying.");
    }

    @Override
    public void onDeactivate() {
        mc.player.requestRespawn();
        info("Respawn request has been sent to the server.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player.getHealth() <= 0) {
            mc.player.setHealth(20f);
            mc.player.getHungerManager().setFoodLevel(20);
        }
    }

    @EventHandler
    private void onOpenScreen(OpenScreenEvent event) {
        if (event.screen instanceof DeathScreen) {
            event.cancel();

            info("Ghost mode active. ");
        }
    }
}
