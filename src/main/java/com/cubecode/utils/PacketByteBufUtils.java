package com.cubecode.utils;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.ide.utils.node.FolderNode;
import com.cubecode.client.views.ide.utils.node.IdeaNode;
import com.cubecode.client.views.ide.utils.node.NodeType;
import com.cubecode.client.views.ide.utils.node.ScriptNode;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PacketByteBufUtils {
    public static void writeScript(PacketByteBuf buf, Script script) {
        buf.writeString(script.getName());
        buf.writeString(script.getCode());
        buf.writeCollection(script.getLibraries(), PacketByteBuf::writeString);
        buf.writeInt(script.getLastLaunchErrorLine());
        buf.writeString(script.getLastLaunchErrorMessage());
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
            writeScript(buf, scriptNode.getScript());
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
            // Script script = readScript(buf);
            //return new ScriptNode(name, script, path);
        }
        return null;
    }
}