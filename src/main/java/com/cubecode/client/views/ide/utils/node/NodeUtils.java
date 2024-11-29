package com.cubecode.client.views.ide.utils.node;

import java.util.ArrayList;
import java.util.List;

public class NodeUtils {
    public static IdeaNode findNodeByName(List<IdeaNode> nodes, String nodeName) {
        if (nodes == null || nodeName == null)
            return null;


        for (IdeaNode node : nodes) {
            if (node.getName().equalsIgnoreCase(nodeName)) {
                return node;
            }

            if (node instanceof FolderNode) {
                IdeaNode foundInFolder = findNodeByName(((FolderNode) node).getChildren(), nodeName);
                if (foundInFolder != null) {
                    return foundInFolder;
                }
            }
        }

        return null;
    }

    public static IdeaNode findNodeByPath(List<IdeaNode> nodes, String nodePath) {
        if (nodes == null || nodePath == null)
            return null;

        for (IdeaNode node : nodes) {
            if (node.getPath().equalsIgnoreCase(nodePath)) {
                return node;
            }

            if (node instanceof FolderNode) {
                IdeaNode foundInFolder = findNodeByPath(((FolderNode) node).getChildren(), nodePath);
                if (foundInFolder != null) {
                    return foundInFolder;
                }
            }
        }

        return null;
    }

    public static boolean hasNodeByPathIgnoreCase(List<IdeaNode> nodes, String nodePath) {
        if (nodes == null || nodePath == null) {
            return false;
        }

        for (IdeaNode node : nodes) {
            if (node.getPath().equalsIgnoreCase(nodePath)) {
                return true;
            }

            if (node instanceof FolderNode && hasNodeByPathIgnoreCase(((FolderNode) node).getChildren(), nodePath)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasNodeByPath(List<IdeaNode> nodes, String nodePath) {
        if (nodes == null || nodePath == null) {
            return false;
        }

        for (IdeaNode node : nodes) {
            if (node instanceof FolderNode && hasNodeByPath(((FolderNode) node).getChildren(), nodePath)) {
                return true;
            }
        }

        return false;
    }

    public static List<ScriptNode> getAllNodes(List<IdeaNode> nodes) {
        List<ScriptNode> allNodes = new ArrayList<>();

        nodes.forEach(node -> {
            if (node.getType() == NodeType.SCRIPT) {
                allNodes.add((ScriptNode) node);
            } else if (node.getType() == NodeType.FOLDER) {
                allNodes.addAll(getAllNodes(((FolderNode) node).getChildren()));
            }
        });

        return allNodes;
    }
}
