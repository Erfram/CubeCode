package com.cubecode.api.project.nodes;

public interface IDENode {
    String getName();
    void setName(String name);
    Type getType();
    String getPath();
    void setPath(String path);
    IDENode copy();

    public enum Type {
        FOLDER,
        FILE,
        SCRIPT
    }
}
