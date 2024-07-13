package com.cubecode.utils;

import com.cubecode.CubeCode;
import com.cubecode.CubeCodeClient;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

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

    public static PacketByteBuf createRegistryByteBuf(FactoryType type, List<String> elements) {
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeString(type.name());
        PacketByteBufUtils.writeStringArray(buf, elements.toArray(new String[0]));

        return buf;
    }

    public static PacketByteBuf createTextureByteBuf(Set<File> files) {
        PacketByteBuf buf = PacketByteBufs.create();

        for (File file : files) {
            if (file.getName().endsWith(".png")) {
                try {
                    byte[] fileBytes = Files.readAllBytes(file.toPath());
                    CubeCode.LOGGER.info("LENGTH UNCOMPRESS = " + fileBytes.length);

                    byte[] compressedBytes = compress(fileBytes, Deflater.BEST_COMPRESSION, true);
                    System.out.println("LENGTH COMPRESS = " + compressedBytes.length);

                    buf.writeBytes(compressedBytes);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return buf;
    }

    public static byte[] compress(byte[] input, int compressionLevel,
                                  boolean GZIPFormat) {
        Deflater compressor = new Deflater(compressionLevel, GZIPFormat);
        compressor.setInput(input);
        compressor.finish();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];
        int readCount = 0;
        while (!compressor.finished()) {
            readCount = compressor.deflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }
        compressor.end();
        return bao.toByteArray();
    }

    public static byte[] decompress(byte[] input, boolean GZIPFormat) throws DataFormatException {
        Inflater decompressor = new Inflater(GZIPFormat);
        decompressor.setInput(input);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        byte[] readBuffer = new byte[1024];
        int readCount;
        while (!decompressor.finished()) {
            readCount = decompressor.inflate(readBuffer);
            if (readCount > 0) {
                bao.write(readBuffer, 0, readCount);
            }
        }
        decompressor.end();
        return bao.toByteArray();
    }
}