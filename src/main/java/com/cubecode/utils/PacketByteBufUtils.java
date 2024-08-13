package com.cubecode.utils;

import com.cubecode.api.scripts.Script;
import net.minecraft.network.PacketByteBuf;

public class PacketByteBufUtils {
    public static void writeScript(PacketByteBuf buf, Script script) {
        buf.writeString(script.name);
        buf.writeString(script.code);
    }

    public static Script readScript(PacketByteBuf buf) {
        return new Script(buf.readString(), buf.readString());
    }
}
