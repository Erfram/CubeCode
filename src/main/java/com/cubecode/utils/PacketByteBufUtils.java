package com.cubecode.utils;

import com.cubecode.CubeCode;
import com.cubecode.api.factory.FactoryManager;
import com.cubecode.api.factory.block.BlockManager;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.io.File;
import java.util.ArrayList;

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

    public static PacketByteBuf createBlockByteBuf() {
        PacketByteBuf buf = PacketByteBufs.create();

        ArrayList<File> blocks = new ArrayList<>();
        for (String block : CubeCode.blockManager.getBlocksToRemove()) {
            blocks.add(CubeCode.blockManager.getFile(block));
        }

        PacketByteBufUtils.writeStringArray(buf, FactoryManager.getBlockStringsFromFiles(blocks));
        return buf;
    }
}