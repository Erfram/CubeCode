package com.cubecode.client.views.ide.utils.node;

import com.cubecode.api.scripts.Script;

public class ScriptNode implements IdeaNode {
    private String name;
    private Script script;
    private String path;

    public ScriptNode(Script script) {
        this.name = script.getName();
        this.script = script;
        this.path = "/" + script.getName();
    }

    public ScriptNode(Script script, String path) {
        this.name = script.getName();
        this.script = script;
        this.path = path;
    }

    public ScriptNode(String name, Script script, String path) {
        this.name = name;
        this.script = script;
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
        this.script.setName(this.path.substring(1));
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
        return new ScriptNode(new Script(this.script.getUUID(), this.script.getName(), this.script.getCode(), this.script.getSide()), this.path);
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }
}