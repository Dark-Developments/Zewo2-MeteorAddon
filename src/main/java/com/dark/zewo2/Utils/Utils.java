package com.dark.zewo2.Utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;

import java.security.MessageDigest;
import java.util.ArrayList;

public class Utils {
    static MinecraftClient mc = MinecraftClient.getInstance();
    public static String getHWID() {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            StringBuilder toEncrypt = new StringBuilder();
            for (GraphicsCard graphicsCard : hal.getGraphicsCards()) {
                toEncrypt.append(graphicsCard.getVendor()).append(graphicsCard.getName());
            }
            toEncrypt.append(hal.getComputerSystem().getBaseboard().getModel());
            toEncrypt.append(hal.getComputerSystem().getHardwareUUID());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(toEncrypt.toString().getBytes());
            StringBuilder hexString = new StringBuilder();

            byte[] byteData = md.digest();

            for (byte aByteData : byteData) {
                String hex = Integer.toHexString(0xff & aByteData);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public static boolean isABFree(Vec3d a, Vec3d b) {
        assert mc.player != null;
        assert mc.world != null;
        RaycastContext rc = new RaycastContext(a, b, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player);
        BlockHitResult raycast = mc.world.raycast(rc);
        return raycast.getType() == HitResult.Type.MISS;
    }

    public static Vec3d getRotationVector(float pitch, float yaw) {
        float f = pitch * 0.017453292F;
        float g = -yaw * 0.017453292F;
        float h = MathHelper.cos(g);
        float i = MathHelper.sin(g);
        float j = MathHelper.cos(f);
        float k = MathHelper.sin(f);
        return new Vec3d(i * j, -k, h * j);
    }

    public static Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    public static BlockPos getBlockPos(Vec3d vec) {
        return new BlockPos((int) vec.getX(), (int) vec.getY(), (int) vec.getZ());
    }

    public static Vec3d getVec3(BlockPos blockPos) {
        return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }
}
