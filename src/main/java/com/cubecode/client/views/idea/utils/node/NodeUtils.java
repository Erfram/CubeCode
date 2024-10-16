package com.cubecode.client.views.idea.utils.node;

import com.cubecode.api.scripts.Script;
import com.cubecode.client.views.idea.utils.Extension;

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

    public static boolean hasNodeByPath(List<IdeaNode> nodes, String nodePath) {
        if (nodes == null || nodePath == null) {
            return false;
        }

        for (IdeaNode node : nodes) {
            if (node.getPath().equalsIgnoreCase(nodePath)) {
                return true;
            }

            if (node instanceof FolderNode && hasNodeByPath(((FolderNode) node).getChildren(), nodePath)) {
                return true;
            }
        }

        return false;
    }

    public static List<IdeaNode> scriptsToIdeaNodes(List<Script> scripts) {
        List<IdeaNode> nodes = new ArrayList<>();

        for (Script script : scripts) {
            String[] pathParts = script.name.split("/");
            String scriptName = pathParts[pathParts.length - 1];

            FolderNode currentFolder = null;
            StringBuilder currentPath = new StringBuilder();

            for (int i = 0; i < pathParts.length - 1; i++) {
                String folderName = pathParts[i];
                currentPath.append("/").append(folderName);
                currentFolder = findOrCreateFolder(currentFolder == null ? nodes : currentFolder.getChildren(), folderName, currentPath.toString());
            }

            script.name = scriptName;
            ScriptNode scriptNode = new ScriptNode(script, Extension.JAVASCRIPT);
            scriptNode.setPath(currentPath + "/" + scriptName);

            if (currentFolder != null) {
                currentFolder.addChild(scriptNode);
            } else {
                nodes.add(scriptNode);
            }
        }

        return nodes;
    }

    private static FolderNode findOrCreateFolder(List<IdeaNode> nodes, String folderName, String folderPath) {
        for (IdeaNode node : nodes) {
            if (node instanceof FolderNode && node.getName().equals(folderName)) {
                return (FolderNode) node;
            }
        }

        FolderNode newFolder = new FolderNode(folderName);
        newFolder.setPath(folderPath);
        nodes.add(newFolder);

        return newFolder;
    }
}
