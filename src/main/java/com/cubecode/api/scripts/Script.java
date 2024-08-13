package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

public class Script {
    public String name;
    public String code;
    public Context context;

    public Script(String name, String code) {
        this.name = name;
        this.code = code;
        this.context = Context.enter();
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
        CubeCode.scriptManager.updateScriptsFromFiles();

        try {
            CubeCode.scriptManager.evaluate(context, code, name);
            CubeCode.scriptManager.invokeFunction(context, ScriptManager.globalScope, function, properties.get(CubeCodeConfig.getScriptConfig().contextName));
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details + "\n" + "line: " + e.lineNumber() + ", column: " + e.columnNumber(), sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    public void stateForRun(String oldCode, String function, String sourceName, Properties properties) {
        if (!code.equals(oldCode)) {
            CubeCode.scriptManager.evaluate(context, code, name);
        }

        CubeCode.scriptManager.invokeFunction(context, CubeCode.scriptManager.getScope(name), function, properties.get(CubeCodeConfig.getScriptConfig().contextName));
    }

    public void run(String sourceName, Properties properties) throws CubeCodeException {
        this.run("main", sourceName, properties);
    }
}