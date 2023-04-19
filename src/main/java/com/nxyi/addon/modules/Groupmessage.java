/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringListSetting;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

import java.util.List;

public class Groupmessage extends Module {
    final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<List<String>> players = sgGeneral.add(new StringListSetting.Builder()
        .name("players")
        .description("Players to message.")
        .build()
    );

    private final Setting<String> command = sgGeneral.add(new StringSetting.Builder()
        .name("command")
        .description("How the message command is set up on the server.")
        .defaultValue("/msg")
        .build()
    );

    public Groupmessage() {
        super(Addon.CATEGORY, "Group Message", "Message multiple players at once");
    }

    @EventHandler
    private void onmessage(SendMessageEvent event){
        for (String p : players.get()){
            mc.player.networkHandler.sendCommand(command + " " + p + " " + event.message);
        }
    }
}
