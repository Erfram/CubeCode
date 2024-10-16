package com.cubecode.client.views.idea.utils.node;

import java.util.ArrayList;
import java.util.List;

public class FolderNode implements IdeaNode {
    private String name;
    private List<IdeaNode> children;
    private boolean isExpanded;
    private String path;

    public FolderNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.path = "/" + name;
    }

    public FolderNode(String name, List<IdeaNode> children) {
        this.name = name;
        this.children = children;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;

        updatePath();
    }

    @Override
    public NodeType getType() {
        return NodeType.FOLDER;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;

        updateChildrenPaths();
    }

    @Override
    public FolderNode copy() {
        List<IdeaNode> copiedChildren = new ArrayList<>();
        for (IdeaNode child : children) {
            copiedChildren.add(child.copy());
        }

        FolderNode copy = new FolderNode(this.name, copiedChildren);

        copy.setPath(this.path);
        copy.setExpanded(this.isExpanded);

        return copy;
    }

    private void updatePath() {
        if (this.path.contains("/")) {
            this.path = this.path.substring(0, this.path.lastIndexOf("/") + 1) + this.name;
        } else {
            this.path = this.name;
        }
        updateChildrenPaths();
    }

    private void updateChildrenPaths() {
        for (IdeaNode child : children) {
            child.setPath(this.path + "/" + child.getName());
        }
    }

    public void addChild(IdeaNode node) {
        node.setPath(this.path + "/" + node.getName());

        children.add(node);
    }

    public void removeChild(IdeaNode node) {
        children.remove(node);
    }

    public List<IdeaNode> getChildren() {
        return children;
    }

    public boolean isExpanded() {
        return isExpanded;

    }
    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}