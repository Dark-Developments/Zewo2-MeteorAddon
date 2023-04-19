/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.example.addon.modules;

import com.example.addon.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class SoundCoordLogger extends Module {
    public SoundCoordLogger() {
        super(Addon.CATEGORY, "SoundCoordLogger", "Sends Coords of various events");
    }

    @EventHandler
    private void OnSound(PacketEvent.Receive event){
        if (event.packet instanceof PlaySoundS2CPacket){
            PlaySoundS2CPacket packet = (PlaySoundS2CPacket) event.packet;
            if (SoundEvents.BLOCK_END_PORTAL_SPAWN.equals((packet).getSound())){
                info("End Portal Activated At: " + vectorToString(new Vec3d(packet.getX(), packet.getY(), packet.getZ())));
            }
            else if (SoundEvents.ENTITY_WITHER_SPAWN.equals((packet).getSound())){
                info("Wither Spawned At: " + vectorToString(new Vec3d(packet.getX(), packet.getY(), packet.getZ())));
            }
            else if (SoundEvents.ENTITY_ENDER_DRAGON_DEATH.equals((packet).getSound())){
                info("Dragon Killed At: " + vectorToString(new Vec3d(packet.getX(), packet.getY(), packet.getZ())));
            }
        }
    }

    private String vectorToString(Vec3d vector, boolean... includeY) {
        boolean reallyIncludeY = includeY.length <= 0 || includeY[0];
        StringBuilder builder = new StringBuilder();
        builder.append('(');
        builder.append((int) Math.floor(vector.x));
        builder.append(", ");
        if(reallyIncludeY) {
            builder.append((int) Math.floor(vector.y));
            builder.append(", ");
        }
        builder.append((int) Math.floor(vector.z));
        builder.append(")");
        return builder.toString();
    }
}
