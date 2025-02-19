package com.cubecode.utils;

import com.cubecode.api.scripts.ServerScript;
import com.cubecode.client.views.ide.utils.node.FolderNode;
import com.cubecode.client.views.ide.utils.node.IdeaNode;
import com.cubecode.client.views.ide.utils.node.NodeType;
import com.cubecode.client.views.ide.utils.node.ScriptNode;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketByteBufUtils {
    public static void writeScript(PacketByteBuf buf, ServerScript script) {
        buf.writeString(script.getName());
        buf.writeString(script.getCode());
        buf.writeCollection(script.getLibraries(), PacketByteBuf::writeString);
    }

    public static ServerScript readScript(PacketByteBuf buf) {
        ServerScript serverScript = new ServerScript(buf.readString(), buf.readString());
        serverScript.setLibraries(buf.readCollection(ArrayList::new, PacketByteBuf::readString));

        return serverScript;
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
            buf.writeString(scriptNode.getScript().getName());
            buf.writeString(scriptNode.getScript().getCode());
            buf.writeCollection(scriptNode.getScript().getLibraries(), PacketByteBuf::writeString);
            buf.writeEnumConstant(scriptNode.getScript().getSide());
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
            List<String> scriptLibraries = buf.readCollection(ArrayList::new, PacketByteBuf::readString);
            ScriptType scriptType = buf.readEnumConstant(ScriptType.class);
            Script script = new ServerScript(scriptName, scriptCode, scriptType);

            script.setLibraries(scriptLibraries);

            return new ScriptNode(name, script, path);
        }
        return null;
    }
}