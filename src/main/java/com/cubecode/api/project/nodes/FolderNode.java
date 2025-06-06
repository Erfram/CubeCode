package com.cubecode.api.project.nodes;

import java.util.ArrayList;
import java.util.List;

public class FolderNode implements IDENode {
    private String name;
    private List<IDENode> children;
    private boolean isExpanded;
    private String path;

    public FolderNode(String name) {
        this.name = name;
        this.children = new ArrayList<>();
        this.path = "/" + name;
    }

    public FolderNode(String name, List<IDENode> children) {
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
    public IDENode.Type getType() {
        return IDENode.Type.FOLDER;
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
        List<IDENode> copiedChildren = new ArrayList<>();
        for (IDENode child : children) {
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
        for (IDENode child : children) {
            child.setPath(this.path + "/" + child.getName());
        }
    }

    public void addChild(IDENode node) {
        node.setPath(this.path + "/" + node.getName());

        children.add(node);
    }

    public void removeChild(IDENode node) {
        children.remove(node);
    }

    public List<IDENode> getChildren() {
        return children;
    }

    public boolean isExpanded() {
        return isExpanded;

    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}
