/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.stream.Stream;

public class chatfilterbypass extends Module {
    public chatfilterbypass() {
        super(Addon.CATEGORY, "Chat Filter Bypass", "Bypasses Chat Filters");
    }
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("modes")
        .defaultValue(Modes.zeroSpace)
        .build()
    );

    @EventHandler
    private void OnMessage(SendMessageEvent event){
        String message = event.message;
        switch (mode.get()) {
            case zeroSpace -> {
                message = message.replaceAll("a", "a\u200C")
                    .replaceAll("e", "e\u200C")
                    .replaceAll("i", "i\u200C")
                    .replaceAll("o", "o\u200C")
                    .replaceAll("u", "u\u200C");
            }

            case RTLO -> {
                message = new StringBuilder(message).reverse().toString();
                message = "\u202E" + message;

            }
        }
        event.message = message;
    }

    public enum Modes{
        zeroSpace,
        RTLO
    }
}
