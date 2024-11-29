package com.cubecode.client.views.ide.utils.node;

public interface IdeaNode {
    String getName();
    void setName(String name);
    NodeType getType();
    String getPath();
    void setPath(String path);
    IdeaNode copy();
}