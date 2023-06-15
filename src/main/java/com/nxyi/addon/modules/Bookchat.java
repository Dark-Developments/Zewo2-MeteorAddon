package com.nxyi.addon.modules;

import com.google.common.base.Splitter;
import com.nxyi.addon.Addon;
import com.nxyi.addon.Utils.InvUtils;
import meteordevelopment.meteorclient.events.game.SendMessageEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.settings.StringSetting;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;

import java.lang.reflect.Array;
import java.util.*;

public class Bookchat extends Module {

    public Bookchat() {
        super(Addon.CATEGORY, "Bookchat", "Secretly chat using books");
    }

    public String lasttext = null;
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final Setting<String> person = sgGeneral.add(new StringSetting.Builder().name("Person").description("Person your chatting with").defaultValue("").build());

    private final Setting<Boolean> autobook = sgGeneral.add(new BoolSetting.Builder()
        .name("autoswitchbook")
        .description("Automatically hold the book on chat if not in hand")
        .defaultValue(true)
        .build()
    );

    @EventHandler
    private void onchat(SendMessageEvent event){
        String msg = mc.player.getEntityName() + " : " + event.message;
        event.cancel();
        if (mc.player.getMainHandStack().getItem().equals(Items.WRITABLE_BOOK)){
            int index = InvUtils.finditem(Items.WRITABLE_BOOK);
            if (index == -1) {
                info("you are not holding a book");
                return;
            }
            info(msg);
            mc.player.networkHandler.sendPacket(new BookUpdateC2SPacket(mc.player.getInventory().selectedSlot, Collections.singletonList(msg), Optional.empty()));
        }
    }

    @Override
    public void onActivate() {
        lasttext = "";
        if (person.get().isEmpty()){
            toggle();
            info("please put the username of the person you want to chat with in the module settings");
        }
    }

    @EventHandler
    private void ontick(TickEvent.Pre event){
        PlayerEntity playerEntity = null;

        if (person.get().isEmpty()) return;
        for (PlayerEntity p : mc.world.getPlayers()){
            if (p.getEntityName().equalsIgnoreCase(person.get())){
                playerEntity = p;
            }
        }

        if (playerEntity == null) return;

        if (playerEntity.getMainHandStack().getItem().equals(Items.WRITABLE_BOOK)){
            String text = playerEntity.getMainHandStack().getNbt().get("pages").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "")
                .replaceAll("\"", "")
                .replaceAll("," , " ");

            if (Objects.equals(lasttext, text)) return;

            lasttext = text;
            info(text);
        }
    }
}
