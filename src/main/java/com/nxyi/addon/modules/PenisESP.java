package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.Color;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.List;


public class PenisESP extends Module {
    public SettingGroup sgGeneral = settings.getDefaultGroup();
    public SettingGroup sgRender = settings.createGroup("Render");

    public Setting<Boolean> onlyOwn = sgGeneral.add(new BoolSetting.Builder().name("only-own").defaultValue(false).build());
    public Setting<Double> ballsSize = sgGeneral.add(new DoubleSetting.Builder().name("balls-size").defaultValue(0.1).range(0.1, 0.5).sliderRange(0.1, 0.5).build());
    public Setting<Double> penisSize = sgGeneral.add(new DoubleSetting.Builder().name("penis-size").defaultValue(0.5).range(0.1, 3.0).sliderRange(0.1, 3.0).build());
    public Setting<Double> friendsSize = sgGeneral.add(new DoubleSetting.Builder().name("friends-size").defaultValue(0.5).range(0.1, 3.0).sliderRange(0.1, 3.0).build());
    public Setting<Double> enemySize = sgGeneral.add(new DoubleSetting.Builder().name("enemy-size").defaultValue(0.3).range(0.1, 3.0).sliderRange(0.1, 3.0).build());
    public Setting<Integer> gradation = sgGeneral.add(new IntSetting.Builder().name("gradation").defaultValue(30).range(20, 100).sliderRange(20, 100).build());

    public Setting<SettingColor> penisColor = sgRender.add(new ColorSetting.Builder().name("penis-color").defaultValue(new SettingColor(170, 90, 0,255)).build());
    public Setting<SettingColor> headColor = sgRender.add(new ColorSetting.Builder().name("head-color").defaultValue(new SettingColor(240, 50, 180,75)).build());

    public PenisESP() {
        super(Addon.CATEGORY, "Penis-ESP", "Renders the player penis and balls | https://github.com/SagXD");
    }

    @EventHandler
    public void onRender(Render3DEvent event) {
        for (PlayerEntity player : mc.world.getPlayers()) {
            if (onlyOwn.get() && player != mc.player) continue;
            double size = (Friends.get().isFriend(player) ? friendsSize.get() : (player != mc.player ? enemySize.get() : penisSize.get()));

            Vec3d base = getBase(player, event.tickDelta);
            Vec3d forward = base.add(0, player.getHeight() / 2.4, 0).add(Vec3d.fromPolar(0, player.getYaw()).multiply(0.1));

            Vec3d left = forward.add(Vec3d.fromPolar(0, player.getYaw() - 90).multiply(ballsSize.get()));
            Vec3d right = forward.add(Vec3d.fromPolar(0, player.getYaw() + 90).multiply(ballsSize.get()));

            drawBall(player, event, ballsSize.get(), gradation.get(), left, penisColor.get(), 0);
            drawBall(player, event, ballsSize.get(), gradation.get(), right, penisColor.get(), 0);
            drawPenis(player, event, size, forward);
        }
    }

    public void drawBall(PlayerEntity player, Render3DEvent event, double radius, int gradation, Vec3d pos, Color color, int stage) {
        float alpha, beta;

        for (alpha = 0.0f; alpha < Math.PI; alpha += Math.PI / gradation) {
            for (beta = 0.0f; beta < 2.0 * Math.PI; beta += Math.PI / gradation) {
                double x1 = (float) (pos.getX() + (radius * Math.cos(beta) * Math.sin(alpha)));
                double y1 = (float) (pos.getY() + (radius * Math.sin(beta) * Math.sin(alpha)));
                double z1 = (float) (pos.getZ() + (radius * Math.cos(alpha)));

                double sin = Math.sin(alpha + Math.PI / gradation);
                double x2 = (float) (pos.getX() + (radius * Math.cos(beta) * sin));
                double y2 = (float) (pos.getY() + (radius * Math.sin(beta) * sin));
                double z2 = (float) (pos.getZ() + (radius * Math.cos(alpha + Math.PI / gradation)));

                Vec3d base = getBase(player, event.tickDelta);
                Vec3d forward = base.add(0, player.getHeight() / 2.4, 0).add(Vec3d.fromPolar(0, player.getYaw()).multiply(0.1));
                Vec3d vec3d = new Vec3d(x1, y1, z1);

                switch (stage) {
                    case 1 -> {
                        if (!vec3d.isInRange(forward, 0.145)) continue;
                    }
                    case 2 -> {
                        double size = (Friends.get().isFriend(player) ? friendsSize.get() : (player != mc.player ? enemySize.get() : penisSize.get()));
                        if (vec3d.isInRange(forward, size + 0.095)) continue;
                    }
                }

                event.renderer.line(x1, y1, z1, x2, y2, z2, color);
            }
        }
    }

    public void drawPenis(PlayerEntity player, Render3DEvent event, double size, Vec3d start) {
        Vec3d copy = start;
        start = start.add(Vec3d.fromPolar(0, player.getYaw()).multiply(0.1));
        Vec3d end = start.add(Vec3d.fromPolar(0, player.getYaw()).multiply(size));

        List<Vec3d> vecs = getVec3ds(start, 0.1);
        vecs.forEach(vec3d -> {
            if (!vec3d.isInRange(copy, 0.145)) return;
            if (vec3d.isInRange(copy, 0.135)) return;
            Vec3d pos = vec3d.add(Vec3d.fromPolar(0, player.getYaw()).multiply(size));
            event.renderer.line(vec3d.x, vec3d.y, vec3d.z, pos.x, pos.y, pos.z, penisColor.get());
        });

        drawBall(player, event, 0.1, gradation.get(), start, penisColor.get(), 1);
        drawBall(player, event, 0.1, gradation.get(), end, headColor.get(), 2);
    }

    public Vec3d getBase(Entity entity, double partial) {
        double x = entity.prevX + ((entity.getX() - entity.prevX) * partial);
        double y = entity.prevY + ((entity.getY() - entity.prevY) * partial);
        double z = entity.prevZ + ((entity.getZ() - entity.prevZ) * partial);

        return new Vec3d(x, y, z);
    }

    public List<Vec3d> getVec3ds(Vec3d vec3d, double radius) {
        List<Vec3d> vec3ds = new ArrayList<>();
        float alpha, beta;

        for (alpha = 0.0f; alpha < Math.PI; alpha += Math.PI / gradation.get()) {
            for (beta = 0.0f; beta < 2.01f * Math.PI; beta += Math.PI / gradation.get()) {
                double x1 = (float) (vec3d.getX() + (radius * Math.cos(beta) * Math.sin(alpha)));
                double y1 = (float) (vec3d.getY() + (radius * Math.sin(beta) * Math.sin(alpha)));
                double z1 = (float) (vec3d.getZ() + (radius * Math.cos(alpha)));

                Vec3d vec = new Vec3d(x1, y1, z1);
                vec3ds.add(vec);
            }
        }

        return vec3ds;
    }
}
