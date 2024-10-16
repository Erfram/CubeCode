package com.cubecode.utils;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.idea.utils.Extension;
import com.cubecode.client.views.idea.utils.node.FolderNode;
import com.cubecode.client.views.idea.utils.node.IdeaNode;
import com.cubecode.client.views.idea.utils.node.NodeType;
import com.cubecode.client.views.idea.utils.node.ScriptNode;
import net.minecraft.network.PacketByteBuf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PacketByteBufUtils {
    public static void writeScript(PacketByteBuf buf, Script script) {
        buf.writeString(script.name);
        buf.writeString(script.code);
    }

    public static Script readScript(PacketByteBuf buf) {
        return new Script(buf.readString(), buf.readString());
    }

    public static void writeIdeaNode(PacketByteBuf buf, IdeaNode node) {
        buf.writeString(node.getName());
        buf.writeString(node.getPath());
        buf.writeEnumConstant(node.getType());

        if (node instanceof FolderNode) {
            FolderNode folderNode = (FolderNode) node;
            buf.writeInt(folderNode.getChildren().size());
            for (IdeaNode child : folderNode.getChildren()) {
                writeIdeaNode(buf, child);
            }
        } else if (node instanceof ScriptNode) {
            ScriptNode scriptNode = (ScriptNode) node;
            buf.writeString(scriptNode.getScript().name);
            buf.writeString(scriptNode.getScript().code);
            buf.writeEnumConstant(scriptNode.getScriptType());
        }
    }

    public static IdeaNode readIdeaNode(PacketByteBuf buf) {
        String name = buf.readString();
        String path = buf.readString();
        NodeType type = buf.readEnumConstant(NodeType.class);

        if (type == NodeType.FOLDER) {
            int childCount = buf.readInt();
            List<IdeaNode> children = new ArrayList<>();
            for (int i = 0; i < childCount; i++) {
                children.add(readIdeaNode(buf));
            }
            FolderNode folderNode = new FolderNode(name, children);
            folderNode.setPath(path);
            return folderNode;
        } else if (type == NodeType.SCRIPT) {
            String scriptName = buf.readString();
            String scriptCode = buf.readString();
            Extension scriptType = buf.readEnumConstant(Extension.class);
            Script script = new Script(scriptName, scriptCode);
            ScriptNode scriptNode = new ScriptNode(script, scriptType, path);
            return scriptNode;
        }
        return null;
    }
}