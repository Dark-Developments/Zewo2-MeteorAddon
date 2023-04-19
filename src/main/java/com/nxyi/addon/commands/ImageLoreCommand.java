/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.nxyi.addon.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import meteordevelopment.meteorclient.systems.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.text.Text;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.mojang.brigadier.Command.SINGLE_SUCCESS;

public class ImageLoreCommand extends Command {
    private static final SimpleCommandExceptionType NOT_IN_CREATIVE = new SimpleCommandExceptionType(Text.literal("You must be in creative mode to use this."));
    private final String block = "█";
    private BufferedImage imageToBuild;

    public ImageLoreCommand() {
        super("image-lore", "Adds an image to an item's lore.");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(argument("size", IntegerArgumentType.integer()).then(argument("url", StringArgumentType.greedyString()).executes(ctx -> {
            ItemStack stack = mc.player.getMainHandStack();
            if (stack.isEmpty()) {
                error("You must be holding a valid item to use this.");
                return SINGLE_SUCCESS;
            }
            StringBuilder page = new StringBuilder();
            loadImage(ctx.getArgument("url", String.class), ctx.getArgument("size", Integer.class));
            if (imageToBuild == null) return SINGLE_SUCCESS;
            for (int index = 0; index < imageToBuild.getHeight(); index++) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < imageToBuild.getWidth(); i++) sb.append("{\"text\":\"").append(block).append("\",\"color\":\"#").append(Integer.toString(imageToBuild.getRGB(i, index) & 0xFFFFFF | 0xF000000, 16).substring(1)).append("\",\"italic\":false},");
                page.append("'[").append(sb.substring(0, sb.length() - 1)).append("]'").append(",");
            }
            try {
                stack.getOrCreateNbt().copyFrom(StringNbtReader.parse("{display:{Lore:[" + page.substring(0, page.length() - 1) + "]}}"));
            } catch (Exception ignored) {
            }
            if (!mc.player.getAbilities().creativeMode) throw NOT_IN_CREATIVE.create();
            mc.interactionManager.clickCreativeStack(stack, 36 + mc.player.getInventory().selectedSlot);
            return SINGLE_SUCCESS;
        })));
    }

    private void loadImage(String url, int size) {
        try {
            URL u = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) u.openConnection();
            huc.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:88.0) Gecko/20100101 Firefox/88.0");
            huc.connect();
            InputStream is = huc.getInputStream();
            BufferedImage loadedImage = ImageIO.read(is);
            double scale = (double) loadedImage.getWidth() / (double) size;
            imageToBuild = resize(loadedImage, (int) (loadedImage.getWidth() / scale), (int) (loadedImage.getHeight() / scale));
            info("Image loaded successfully.");
            huc.disconnect();
        } catch (Exception ignored) {
            error("An error occurred while loading the image.");
        }
    }

    private BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }
}
