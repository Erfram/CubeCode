package com.cubecode.utils;

public class ColorUtils {

    public static int rgbaToImguiColor(int r, int g, int b, int a) {
        return (a << 24) | (b << 16) | (g << 8) | r;
    }

    public static int parseStringARGB(String color) {
        int alpha = Integer.parseInt(color.substring(0, 2), 16);
        int red = Integer.parseInt(color.substring(2, 4), 16);
        int green = Integer.parseInt(color.substring(4, 6), 16);
        int blue = Integer.parseInt(color.substring(6, 8), 16);
        return rgbaToImguiColor(red, green, blue, alpha);
    }
}
