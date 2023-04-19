/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.Utils;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NetworkHandler {
    public static void NetworkHandler(ClientPlayNetworkHandler networkHandler, PlayerMoveC2SPacket packet){
        networkHandler.sendPacket(packet);
    }
}
