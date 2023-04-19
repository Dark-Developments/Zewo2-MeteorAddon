package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.friends.Friends;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.PlayerUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;

public class NecrophiliaPOV extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Integer> range = sgGeneral.add(new IntSetting.Builder()
        .name("range")
        .description("nax range to render")
        .defaultValue(15)
        .range(5, 30)
        .sliderMax(30)
        .build()
    );
    private final Setting<Boolean> ignoreSelf = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-self")
        .description("doesn't render you when u die")
        .defaultValue(true)
        .build()
    );
    private final Setting<Boolean> ignoreFriends = sgGeneral.add(new BoolSetting.Builder()
        .name("ignore-friends")
        .description("doesn't render friends")
        .defaultValue(false)
        .build()
    );
    private final Setting<Integer> renderTime = sgGeneral.add(new IntSetting.Builder()
        .name("render-time")
        .description("how long to render")
        .defaultValue(5)
        .range(1, 15)
        .sliderMax(15)
        .build()
    );

    public NecrophiliaPOV() {
        super(Addon.CATEGORY, "Necrophilia", "Renders cum on death entities | github.com/SplashAni");
    }

    @EventHandler
    private void onTick() {

        for (PlayerEntity player : mc.world.getPlayers()) {
            if (ignoreSelf.get() && player == mc.player) continue;
            if (Friends.get().isFriend(player) && ignoreFriends.get()) continue;
            if (mc.player.distanceTo(player) <= range.get()) {
                if(player.getHealth() == 0) {
                    Double x = player.getX();
                    Double y = player.getX();
                    Double z = player.getX();
                    for (int i = 1; i <= renderTime.get(); i++) {
                        mc.world.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0, 0);
                        mc.world.addParticle(ParticleTypes.SNEEZE, x, y, z, 0, 0, 0);
                    }
                }
            }
        }
    }
}
