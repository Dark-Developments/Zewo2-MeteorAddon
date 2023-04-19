/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.game.ReceiveMessageEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.text.Text;

public class NoChatFormatting extends Module {
    public NoChatFormatting() {
        super(Addon.CATEGORY, "NoChatFormatting", "Stops Chat Colour Codes");
    }

    @EventHandler
    private void onRevieve(ReceiveMessageEvent event){
        event.setMessage(Text.literal(event.getMessage().getString()));
    }
}
