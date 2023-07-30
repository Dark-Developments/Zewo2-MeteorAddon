package com.dark.zewo2.mixin;

import com.dark.zewo2.Addon;
import meteordevelopment.meteorclient.utils.PostInit;
import meteordevelopment.meteorclient.utils.player.ChatUtils;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

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
