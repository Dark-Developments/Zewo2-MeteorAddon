/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.Utils;

public class JinxUtils {

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (Exception ignored) {
        }
    }

    public static int rainbowColors(int colors) {
        int color = 0;
        switch (colors) {
            case 0 -> color = 16711680;
            case 1 -> color = 16732675;
            case 2 -> color = 16754178;
            case 3 -> color = 16768770;
            case 4 -> color = 14155522;
            case 5 -> color = 9502464;
            case 6 -> color = 5373696;
            case 7 -> color = 65360;
            case 8 -> color = 65432;
            case 9 -> color = 65521;
            case 10 -> color = 45055;
            case 11 -> color = 15359;
            case 12 -> color = 3211519;
            case 13 -> color = 9634047;
            case 14 -> color = 13762815;
            case 15 -> color = 16711913;
            case 16 -> color = 16711859;
            case 17 -> color = 16711792;
            case 18 -> color = 16711725;
        }
        return color;
    }
}
