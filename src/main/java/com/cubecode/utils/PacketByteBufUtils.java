package com.cubecode.utils;

import com.cubecode.api.project.nodes.FolderNode;
import com.cubecode.api.project.nodes.IDENode;
import com.cubecode.api.project.nodes.ScriptNode;
import com.cubecode.api.project.scripts.Script;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketByteBufUtils {
    public static void writeScript(PacketByteBuf buf, Script script) {
        buf.writeString(script.getName());
        buf.writeString(script.getCode());
    }

    public static Script readScript(PacketByteBuf buf) {
        return new Script(buf.readString(), buf.readString(), Script.Type.SERVER);
    }

    public static void writeIdeaNode(PacketByteBuf buf, IDENode node) {
        buf.writeString(node.getName());
        buf.writeString(node.getPath());
        buf.writeEnumConstant(node.getType());

        if (node instanceof FolderNode folderNode) {
            buf.writeInt(folderNode.getChildren().size());
            for (IDENode child : folderNode.getChildren()) {
                writeIdeaNode(buf, child);
            }
        } else if (node instanceof ScriptNode scriptNode) {
            writeScript(buf, scriptNode.getScript());
        }
    }

    public static IDENode readIdeaNode(PacketByteBuf buf) {
        String name = buf.readString();
        String path = buf.readString();
        IDENode.Type type = buf.readEnumConstant(IDENode.Type.class);

        if (type == IDENode.Type.FOLDER) {
            int childCount = buf.readInt();
            List<IDENode> children = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                children.add(readIdeaNode(buf));
            }
            FolderNode folderNode = new FolderNode(name, children);
            folderNode.setPath(path);
            return folderNode;
        } else if (type == IDENode.Type.SCRIPT) {
            Script script = readScript(buf);
            return new ScriptNode(name, script, path);
        }
        return null;
    }
}
