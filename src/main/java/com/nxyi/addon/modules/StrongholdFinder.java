package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import com.nxyi.addon.Utils.JinxUtils;
import com.nxyi.addon.Utils.Line;
import meteordevelopment.meteorclient.events.world.PlaySoundEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.item.EnderEyeItem;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.reflections.vfs.Vfs;

public class StrongholdFinder extends Module {
    private boolean hasstarted =false;
    private boolean hasfinished = false;
    private double rot1 = 0;
    private double rot2 = 0;
    private Vec3d pos1 = new Vec3d(0,0,0);
    private Vec3d pos2 = new Vec3d(0,0,0);

    private int timer = 0;

    public StrongholdFinder() {
        super(Addon.CATEGORY, "StrongholdFinder", "Triangulation Stronghold location");
    }

    @Override
    public void onActivate() {
        hasstarted = false;
        hasfinished =false;
    }

    @EventHandler
    private void onthrow(TickEvent.Pre event){
        if (timer > 0){
            timer--;
        }

        if (hasfinished){
            info("3");
            hasfinished = false;

            Vec3d intersection = Line.getIntersection(pos1.x, pos1.z, rot1, pos2.x, pos2.z, rot2);
            info("Stronghold location: (highlight) %s ~ %s", (int)intersection.x, (int)intersection.z);
            toggle();
        }
    }

    @EventHandler
    private void onsound(PlaySoundEvent event){
        if (event.sound.getId().getPath().equals("entity.ender_eye.launch")){
            if (timer > 0) return;
            else timer = 20;


            if (!hasstarted) {
                new Thread(() -> {
                    info("1");
                    JinxUtils.sleep(1000);
                    Entity eye = findeye();
                    pos1 = mc.player.getPos();

                    rot1 = Rotations.getYaw(eye);

                    hasstarted = true;
                }).start();
            } else {
                new Thread(() -> {
                    JinxUtils.sleep(1000);
                    info("2");
                    Entity eye = findeye();
                    pos2 = mc.player.getPos();

                    rot2 = Rotations.getYaw(eye);

                    hasfinished = true;
                }).start();
            }
        }
    }

    private Entity findeye(){
        Entity found = null;
        for (Entity eye : mc.world.getEntities()){
            if(eye instanceof EyeOfEnderEntity){
                found = eye;
            }
        }
        return found;
    }
}
