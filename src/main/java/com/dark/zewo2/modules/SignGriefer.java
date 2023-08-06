package com.dark.zewo2.modules;

import com.dark.zewo2.Addon;
import com.dark.zewo2.Utils.JinxUtils;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.Rotations;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SignBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSignC2SPacket;
import net.minecraft.network.packet.s2c.play.SignEditorOpenS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SignGriefer extends Module {
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();

    private final Setting<sides> mode = sgGeneral.add(new EnumSetting.Builder<sides>()
        .name("mode")
        .description("modes")
        .defaultValue(sides.front)
        .build()
    );

    private final Setting<Double> reach = sgGeneral.add(new DoubleSetting.Builder()
        .name("range")
        .description("Maximum range to the sign.")
        .defaultValue(6)
        .min(1)
        .sliderMax(6)
        .build()
    );

    private final Setting<String> line1 = sgGeneral.add(
        new StringSetting.Builder().name("line 1").description("line 1").defaultValue("").build());

    private final Setting<String> line2 = sgGeneral.add(
        new StringSetting.Builder().name("line 2").description("line 2").defaultValue("").build());
    private final Setting<String> line3 = sgGeneral.add(
        new StringSetting.Builder().name("line 3").description("line 3").defaultValue("").build());

    private final Setting<String> line4 = sgGeneral.add(
        new StringSetting.Builder().name("line 4").description("line 4").defaultValue("").build());
    public SignGriefer() {
        super(Addon.CATEGORY, "SignGriefer", "Grief nearby signs using new editor feature");
    }

    boolean editing = false;

    @Override
    public void onActivate() {
        editing = false;
    }

    @EventHandler
    public void ontick(TickEvent.Pre event) {
        new Thread(() -> {
            int range = reach.get().intValue();
            for (int x = -range; x <= range; x++){
                for (int y = -range; y <= range; y++){
                    for (int z = -range; z <= range; z++){

                        BlockPos sign = mc.player.getBlockPos().add(x,y,z);

                        if (issign(sign)){
                            List<String> front = getNbt(sign, "front_text");
                            List<String> back = getNbt(sign, "back_text");

                            if (editing) return;
                            editing = true;

                            if (mode.get().equals(sides.front)) {

                                if (!front.get(0).equals(line1.get()) || !front.get(1).equals(line2.get()) || !front.get(2).equals(line3.get()) || !front.get(3).equals(line4.get())) {
                                    mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND ,new BlockHitResult(new Vec3d(sign.getX(), sign.getY(), sign.getZ()), mc.player.getHorizontalFacing(), sign, false), 1));

                                    mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
                                }
                            }
                            if (mode.get().equals(sides.back)) {
                                if (!back.get(0).equals(line1.get()) || !back.get(1).equals(line2.get()) || !back.get(2).equals(line3.get()) || !back.get(3).equals(line4.get())) {
                                    mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(sign.getX(), sign.getY(), sign.getZ()), mc.player.getHorizontalFacing(), sign, false), 1));

                                    mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
                                }
                            }

                            if (mode.get().equals(sides.both)){
                                if (!front.get(0).equals(line1.get()) || !front.get(1).equals(line2.get()) || !front.get(2).equals(line3.get()) || !front.get(3).equals(line4.get())
                                    || !back.get(0).equals(line1.get()) || !back.get(1).equals(line2.get()) || !back.get(2).equals(line3.get()) || !back.get(3).equals(line4.get())){
                                    mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND ,new BlockHitResult(new Vec3d(sign.getX(), sign.getY(), sign.getZ()), mc.player.getHorizontalFacing(), sign, false), 1));
                                    mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, true, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
                                    mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND ,new BlockHitResult(new Vec3d(sign.getX(), sign.getY(), sign.getZ()), mc.player.getHorizontalFacing(), sign, false), 1));
                                    mc.player.networkHandler.sendPacket(new UpdateSignC2SPacket(sign, false, isempty(line1.get()), isempty(line2.get()), isempty(line3.get()), isempty(line4.get())));
                                }
                            }
                            editing = false;
                        }
                    }
                }
            }
        }).start();
    }

    @EventHandler
    private void packet(PacketEvent.Receive event){
        if (event.packet instanceof SignEditorOpenS2CPacket packet){
            event.cancel();
        }
    }

    private String isempty(String text){
        return (text.isEmpty()) ? "" : text;
    }

    private boolean issign(BlockPos pos){
        // i know this is a bad way, but `block InstanceOf SignBlock` wasnt working
        Block block = mc.world.getBlockState(pos).getBlock();

        return block.getTranslationKey().contains("_sign");
    }

    private List<String> getNbt(BlockPos pos, String compound) {
        // get the text on the sign to see if we should edit it
        List<String> elements = new ArrayList<>();
        BlockEntity block = mc.world.getBlockEntity(pos);
        NbtCompound targetNbt = block.createNbt().getCompound(compound);
        String text1 = targetNbt.toString().split(",")[2].split("\"")[3];
        String text2 = targetNbt.toString().split(",")[3].split("\"")[3];
        String text3 = targetNbt.toString().split(",")[4].split("\"")[3];
        String text4 = targetNbt.toString().split(",")[5].split("\"")[3];

        elements.add(text1);
        elements.add(text2);
        elements.add(text3);
        elements.add(text4);

        return elements;
    }

    public enum sides{
        front,
        back,
        both
    }
}
