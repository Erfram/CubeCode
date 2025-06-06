package com.cubecode.api.project.nodes;

import com.cubecode.api.project.scripts.Script;

public class ScriptNode extends FileNode {
    private Script script;

    public ScriptNode(Script script) {
        super(script.getName(), "js");

        this.script = script;
    }

    public ScriptNode(Script script, String path) {
        super(script.getName(), "js", path);

        this.script = script;
    }

    public ScriptNode(String name, Script script, String path) {
        super(name, "js", path);

        this.script = script;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
        this.script.setName(this.path.substring(1));
    }

    @Override
    public IDENode.Type getType() {
        return Type.SCRIPT;
    }

    @Override
    public IDENode copy() {
        return new ScriptNode(new Script(this.script.getName(), this.script.getCode(), this.script.getSide()), this.path);
    }

    public Script getScript() {
        return script;
    }

    public void setScript(Script script) {
        this.script = script;
    }
}
