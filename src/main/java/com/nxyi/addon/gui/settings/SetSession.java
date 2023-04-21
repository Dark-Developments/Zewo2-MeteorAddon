package com.nxyi.addon.gui.settings;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SetSession {

    public static String username = "";

    public static String accessToken = "";

    public static String UUID = "";

    public static String sessionid = "token:"+accessToken+":"+UUID;

    public static boolean originalSession = true;

    public static MinecraftClient mc = MinecraftClient.getInstance();

    @Nullable
    public static java.util.UUID getUuidOrNull() {
        try {
            return UUIDTypeAdapter.fromString(UUID);
        } catch (IllegalArgumentException var2) {
            return null;
        }
    }


    public static GameProfile getGameProfile() {
        return new GameProfile(getUuidOrNull(), username);
    }



}
