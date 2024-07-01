package com.cubecode.utils;

import net.minecraft.network.PacketByteBuf;

public class PacketByteBufUtils {
    public static void writeStringArray(PacketByteBuf buf, String[] strings) {
        if (strings == null) {
            buf.writeVarInt(0);
            return;
        }

        buf.writeVarInt(strings.length);
        for (String str : strings) {
            buf.writeString(str);
        }
    }

    public static String[] readStringArray(PacketByteBuf buf) {
        int length = buf.readVarInt();
        if (length == 0) {
            return new String[0];
        }

        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            strings[i] = buf.readString();
        }
        return strings;
    }
}
