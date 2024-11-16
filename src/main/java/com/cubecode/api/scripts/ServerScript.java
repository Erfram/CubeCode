package com.cubecode.api.scripts;

import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.Script;
import com.cubecode.utils.ScriptSide;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import static com.cubecode.CubeCode.projectManager;

public class ServerScript implements Script {
    private String name;
    private String code;
    private ScriptSide side;
    public Context context;
    public ScriptScope scope;

    public ServerScript(String name, String code, ScriptSide side) {
        this.name = name;
        this.code = code;
        this.side = side;
    }

    public ServerScript(String name, String code) {
        this.name = name;
        this.code = code;
        this.side = ScriptSide.SERVER;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ScriptSide getSide() {
        return side;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSide(ScriptSide side) {
        this.side = side;
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
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
        this.run("server", sourceName, properties);
    }

    public void run(String function, String sourceName) throws CubeCodeException {
        this.run(function, sourceName, Properties.create());
    }

    public void run(String sourceName) throws CubeCodeException {
        this.run("server", sourceName, Properties.create());
    }
}