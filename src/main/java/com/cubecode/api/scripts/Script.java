package com.cubecode.api.scripts;

import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import static com.cubecode.CubeCode.projectManager;

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
        projectManager.updateScriptsFromFiles();

        try {
            this.evaluate();
            projectManager.invokeFunction(context, scope, function, properties.getMap().values().toArray());
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details + "\n" +
                    "Script: " + sourceName + "\n" + "Line: " + e.lineNumber() + ", Column: " + e.columnNumber() + "\n" +
                    "Code: "+ this.code.split("\n")[e.lineNumber() - 1].replace("\t", ""), sourceName
            );
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    public void evaluate() throws CubeCodeException {
        this.context = Context.enter();
        this.scope = new ScriptScope(name, this.context);
        this.scope.setParentScope(ProjectManager.globalScope);
        projectManager.evaluate(this.context, this.scope, code, name);
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