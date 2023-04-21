/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

public class phase extends Module {
    private int timer;
    private boolean prevflying;
    public phase() {super(Addon.CATEGORY, "HitboxPhase", "Phase upwards using shitty hitbox mechanics \uD83D\uDC80");}

    @Override
    public void onActivate() {
        timer = 0;
        prevflying = mc.player.getAbilities().flying;
    }

    @EventHandler
    private void ontick(TickEvent.Pre event){
        mc.player.getAbilities().flying = true;

        timer++;
        if(timer < 3)
            return;

        mc.options.sneakKey.setPressed(!mc.options.sneakKey.isPressed());
        timer = -1;
    }

    @Override
    public void onDeactivate() {
        mc.player.getAbilities().flying = prevflying;
        mc.options.sneakKey.setPressed(false);
    }
}
