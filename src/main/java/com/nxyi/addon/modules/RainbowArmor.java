package com.nxyi.addon.modules;

import com.nxyi.addon.Addon;
import com.nxyi.addon.Utils.JinxUtils;
import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtDouble;
import net.minecraft.util.math.MathHelper;

public class RainbowArmor extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("The mode for Rainbow Armor.")
        .defaultValue(Mode.Math)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay between armor equips.")
        .defaultValue(0)
        .min(0)
        .sliderMax(10)
        .build()
    );

    private final Setting<Boolean> excludeHelmet = sgGeneral.add(new BoolSetting.Builder()
        .name("exclude-helmet")
        .description("Does not give you a rainbow helmet.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> excludeChestplate = sgGeneral.add(new BoolSetting.Builder()
        .name("exclude-chestplate")
        .description("Does not give you a rainbow chestplate.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> excludeLeggings = sgGeneral.add(new BoolSetting.Builder()
        .name("exclude-leggings")
        .description("Does not give you rainbow leggings.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> excludeBoots = sgGeneral.add(new BoolSetting.Builder()
        .name("exclude-boots")
        .description("Does not give you rainbow boots.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build()
    );

    private int textColor;
    private int current;
    private int ticks;

    public RainbowArmor() {
        super(Addon.CATEGORY, "rainbow-armor", "Gives you rainbow leather armor.");
    }

    @Override
    public void onActivate() {
        if (!mc.player.getAbilities().creativeMode) {
            error("You must be in creative mode to use this.");
            toggle();
            return;
        }
        current = 0;
        ticks = 0;
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        if (!mc.player.getAbilities().creativeMode) {
            error("You must be in creative mode to use this.");
            toggle();
            return;
        }
        ticks++;
        if (ticks <= delay.get() && delay.get() != 0) return;
        if (mode.get() == Mode.Math) {
            float x = System.currentTimeMillis() % 2000 / 1000F;
            float red = 0.5F + 0.5F * MathHelper.sin(x * (float) Math.PI);
            float green = 0.5F + 0.5F * MathHelper.sin((x + 4F / 3F) * (float) Math.PI);
            float blue = 0.5F + 0.5F * MathHelper.sin((x + 8F / 3F) * (float) Math.PI);
            textColor = 0x04 << 16 | (int) (red * 256) << 16 | (int) (green * 256) << 8 | (int) (blue * 256);
        } else if (mode.get() == Mode.Other) {
            textColor = JinxUtils.rainbowColors(current);
            if (JinxUtils.rainbowColors(textColor) >= 18) current = 0;
            current++;
        }
        ItemStack helmet = new ItemStack(Items.LEATHER_HELMET);
        ItemStack chestplate = new ItemStack(Items.LEATHER_CHESTPLATE);
        ItemStack leggings = new ItemStack(Items.LEATHER_LEGGINGS);
        ItemStack boots = new ItemStack(Items.LEATHER_BOOTS);
        NbtCompound tag = new NbtCompound();
        tag.put("color", NbtDouble.of(textColor));
        helmet.setSubNbt("display", tag);
        chestplate.setSubNbt("display", tag);
        leggings.setSubNbt("display", tag);
        boots.setSubNbt("display", tag);
        if (!excludeHelmet.get()) mc.interactionManager.clickCreativeStack(helmet, 5);
        if (!excludeChestplate.get()) mc.interactionManager.clickCreativeStack(chestplate, 6);
        if (!excludeLeggings.get()) mc.interactionManager.clickCreativeStack(leggings, 7);
        if (!excludeBoots.get()) mc.interactionManager.clickCreativeStack(boots, 8);
        ticks = 0;
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Mode {
        Math,
        Other
    }
}
