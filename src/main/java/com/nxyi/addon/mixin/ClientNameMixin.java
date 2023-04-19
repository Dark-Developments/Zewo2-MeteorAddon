package com.nxyi.addon.mixin;

import com.nxyi.addon.Addon;
import meteordevelopment.discordipc.RichPresence;
import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.systems.modules.misc.DiscordPresence;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.awt.*;

@Mixin(value = ChatUtils.class, remap = false)
public class ClientNameMixin {

    @Shadow
    private static Text PREFIX;
    /**
     * @author
     * @reason
     */
    @PostInit
    @Overwrite
    public static void init() {
        PREFIX = Text.literal("")
            .setStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
            .append("[")
            .append(Text.literal(Addon.CATEGORY.toString()).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(Color.DARK_GRAY.getRGB()))))
            .append("] ");
    }
}
