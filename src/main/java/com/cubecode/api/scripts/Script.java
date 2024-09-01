package com.cubecode.api.scripts;

import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import static com.cubecode.CubeCode.scriptManager;

public class Script {
    public String name;
    public String code;
    public transient Context context;
    public ScriptScope scope;

    public Script(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
        scriptManager.updateScriptsFromFiles();

        try {
            this.evaluate();
            scriptManager.invokeFunction(context, scope, function, properties.getMap().values().toArray());
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details + "\n" + "line: " + e.lineNumber() + ", column: " + e.columnNumber(), sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    public void evaluate() {
        this.context = Context.enter();
        this.scope = new ScriptScope(name, this.context);
        this.scope.setParentScope(ScriptManager.globalScope);
        scriptManager.evaluate(this.context, this.scope, code, name);
    }

    public void run(String sourceName, Properties properties) throws CubeCodeException {
        this.run("main", sourceName, properties);
    }

    public void run(String function, String sourceName) throws CubeCodeException {
        this.run(function, sourceName, Properties.create());
    }

    public void run(String sourceName) throws CubeCodeException {
        this.run("main", sourceName, Properties.create());
    }
}