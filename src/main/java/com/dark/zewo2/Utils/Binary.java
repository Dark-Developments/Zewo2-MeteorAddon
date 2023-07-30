/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package com.dark.zewo2.Utils;

import java.util.Arrays;

public class Binary {
    public static String ToBinary(String str, int bits) {
        String result = "";
        String tmpStr;
        int tmpInt;
        char[] messChar = str.toCharArray();

        for (int i = 0; i < messChar.length; i++) {
            tmpStr = Integer.toBinaryString(messChar[i]);
            tmpInt = tmpStr.length();
            if(tmpInt != bits) {
                tmpInt = bits - tmpInt;
                if (tmpInt == bits) {
                    result += tmpStr;
                } else if (tmpInt > 0) {
                    for (int j = 0; j < tmpInt; j++) {
                        result += "0";
                    }
                    result += tmpStr;
                } else {
                    System.err.println("argument 'bits' is too small");
                }
            } else {
                result += tmpStr;
            }
            result += " "; // separator
        }

        return result;
    }

    public static String ToString(String str){
        String input = str; // Binary input as String
        StringBuilder sb = new StringBuilder(); // Some place to store the chars

        Arrays.stream( // Create a Stream
            input.split("(?<=\\G.{8})") // Splits the input string into 8-char-sections (Since a char has 8 bits = 1 byte)
        ).forEach(s -> // Go through each 8-char-section...
            sb.append((char) Integer.parseInt(s, 2)) // ...and turn it into an int and then to a char
        );

        String output = sb.toString(); // Output text (t)
        return output;
    }
}
