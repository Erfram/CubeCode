package com.cubecode.client.views.idea.utils.node;

public class FileNode implements IdeaNode {
    private String name;
    private String extension;
    private String path;

    public FileNode(String name, String extension) {
        this.name = name;
        this.extension = extension;
        this.path = "/" + name;
    }

    public FileNode(String name, String extension, String path) {
        this.name = name;
        this.extension = extension;
        this.path = path;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        this.path = this.path.substring(0, this.path.lastIndexOf("/") + 1) + this.name;
    }

    @Override
    public NodeType getType() {
        return NodeType.SCRIPT;
    }

    @Override
    public String getPath() {
        return this.path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public IdeaNode copy() {
        return new FileNode(this.name, this.extension, this.path);
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}