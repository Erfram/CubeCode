package com.cubecode.api.project.scripts;

import com.cubecode.CubeCode;
import com.cubecode.exceptions.CubeCodeException;
import dev.latvian.mods.rhino.Context;

public class Script {
    private String name;
    private String code;
    private Type side;

    private Context context;
    private ScriptScope scope;

    public Script(String name, String code, Type side) {
        this.name = name;
        this.code = code;
        this.side = side;
    }

    public void run(String sourceName, Properties properties) throws CubeCodeException {
        this.run(this.side == Script.Type.CLIENT ? "client" : "server", sourceName, properties);
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
        this.prepare();
        try {
            this.evaluate();
            CubeCode.projectManager.executor.invokeFunction(this.context, this.scope, function, properties.getMap().values().toArray());
        } catch (Exception e) {
        }
    }

    public void prepare() {
        this.context = Context.enter();
        this.scope = new ScriptScope(name, this.context);
        this.scope.setParentScope(ScriptExecutor.globalScope);
    }

    public void evaluate() throws CubeCodeException {
        this.evaluate(this.context, this.scope, this.code, this.name);
    }

    public void evaluate(Context cx, ScriptScope scope, String code, String sourceName) throws CubeCodeException {
        try {
            CubeCode.projectManager.executor.evaluate(cx, scope, code, sourceName);
        } catch (Exception e) {

        }
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public Type getSide() {
        return side;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSide(Type side) {
        this.side = side;
    }


    public enum Type {
        SERVER,
        CLIENT,
        LIBRARY
    }
}
