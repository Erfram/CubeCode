package com.cubecode.client.views.idea.utils.node;

import com.cubecode.api.scripts.ServerScript;
import com.cubecode.client.scripts.ClientScript;
import com.cubecode.client.views.idea.utils.Extension;
import com.cubecode.utils.Script;
import com.cubecode.utils.ScriptSide;

public class ScriptNode implements IdeaNode {
    private String name;
    private Script script;
    private Extension scriptType;
    private String path;

    public ScriptNode(Script script, Extension scriptType) {
        this.name = script.getName();
        this.script = script;
        this.scriptType = scriptType;
        this.path = "/" + script.getName();
    }

    public ScriptNode(Script script, Extension scriptType, String path) {
        this.name = script.getName();
        this.script = script;
        this.scriptType = scriptType;
        this.path = path;
    }

    public ScriptNode(String name, Script script, Extension scriptType, String path) {
        this.name = name;
        this.script = script;
        this.scriptType = scriptType;
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
        return new ScriptNode(new ServerScript(this.script.getName(), this.script.getCode(), this.script.getSide()), this.scriptType, this.path);
    }

    public Script getScript() {
        return script;
    }

    public ServerScript getServerScript() {
        return (ServerScript) this.script;
    }

    public ClientScript getClientScript() {
        return (ClientScript) this.script;
    }

    public void setScript(ServerScript script) {
        this.script = script;
    }

    public Extension getScriptType() {
        return scriptType;
    }

    public void setScriptType(Extension type) {
        this.scriptType = type;
    }
}