package com.cubecode.api.project.nodes;

import net.minecraft.nbt.NbtCompound;

public class FileNode implements IDENode {
    protected String name;
    protected String extension;
    protected String path;

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
    public IDENode.Type getType() {
        return Type.FILE;
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
    public IDENode copy() {
        return new FileNode(this.name, this.extension, this.path);
    }

    public String getExtension() {
        return this.extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
