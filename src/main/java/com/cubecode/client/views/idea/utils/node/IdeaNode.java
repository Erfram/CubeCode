package com.cubecode.client.views.idea.utils.node;

public interface IdeaNode {
    String getName();
    void setName(String name);
    NodeType getType();
    String getPath();
    void setPath(String path);
    IdeaNode copy();
}