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
import org.apache.commons.codec.binary.Base64;
import java.util.*;

public class Bookchat extends Module {

    public Bookchat() {
        super(Addon.CATEGORY, "Bookchat", "Secretly chat using books");
    }

    public String lasttext = null;
    private final SettingGroup sgGeneral = this.settings.getDefaultGroup();
    private final Setting<String> person = sgGeneral.add(new StringSetting.Builder().name("Person").description("Person your chatting with").defaultValue("").build());

    private final Setting<Boolean> encryption = sgGeneral.add(new BoolSetting.Builder()
        .name("encryption")
        .description("Encrypt the messages in base64.")
        .defaultValue(false)
        .build()
    );

    @EventHandler
    private void onchat(SendMessageEvent event){
        String msg = event.message;
        if (mc.player.getMainHandStack().getItem().equals(Items.WRITABLE_BOOK)){
            event.cancel();
            info(mc.player.getEntityName() + " : " + msg);
            String encrypted = Base64.encodeBase64String(msg.getBytes());
            mc.player.networkHandler.sendPacket(new BookUpdateC2SPacket(mc.player.getInventory().selectedSlot, Collections.singletonList((encryption.get() ? encrypted : msg)), Optional.empty()));
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
        if(mc.world.getPlayers() == null) return;
        for (PlayerEntity p : mc.world.getPlayers()){
            if (p.getEntityName().equalsIgnoreCase(person.get())){
                playerEntity = p;
            }
        }

        if (playerEntity == null) return;

        if (playerEntity.getMainHandStack().getItem().equals(Items.WRITABLE_BOOK)){
            if (playerEntity.getMainHandStack().getNbt().get("pages").toString() == null) return;
            String text = playerEntity.getMainHandStack().getNbt().get("pages").toString()
                .replaceAll("\\]", "")
                .replaceAll("\\[", "")
                .replaceAll("\"", "")
                .replaceAll("," , " ");

            byte[] decodedBytes = Base64.decodeBase64(text);
            String decodedString = new String(decodedBytes);

            if (Objects.equals(lasttext, text)) return;

            boolean isBase64 = Base64.isBase64(text.getBytes());

            lasttext = text;
            info(playerEntity.getEntityName() + " : " + (isBase64 ? decodedString : text));
        }
    }
}
