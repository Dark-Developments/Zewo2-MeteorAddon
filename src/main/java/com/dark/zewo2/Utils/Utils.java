package com.dark.zewo2.Utils;

import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class Utils {

    public static boolean isABFree(Vec3d a, Vec3d b) {
        assert mc.player != null;
        assert mc.world != null;
        RaycastContext rc = new RaycastContext(a, b, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player);
        BlockHitResult raycast = mc.world.raycast(rc);
        return raycast.getType() == HitResult.Type.MISS;
    }
}
