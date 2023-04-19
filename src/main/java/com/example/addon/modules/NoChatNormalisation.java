/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoChatNormalisation extends Module {
    public NoChatNormalisation() {
        super(Addon.CATEGORY, "NoChatNormalisation", "Fuck Normal Chat");
    }

}
