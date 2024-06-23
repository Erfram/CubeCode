package com.cubecode.api.scripts;

import com.cubecode.api.utils.DirectoryManager;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.*;
import com.cubecode.utils.CubeCodeException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ScriptManager extends DirectoryManager {
    private Map<String, Script> scripts;

    public ScriptManager(File scriptsDirectory) {
        super(scriptsDirectory);
        this.updateScripts();
    }

    public void updateScripts() {
        Map<String, Script> newScripts = new HashMap<>();

        this.getFiles().forEach(file -> {
            if (file.getName().endsWith(".js")) {
                newScripts.put(file.getName(), new Script(this.readFileString(file.getName())));
            }
        });

        this.scripts = newScripts;
    }

    public void executeScript(String name, @Nullable Map<String, Object> properties) throws CubeCodeException {
        this.scripts.get(name).execute(name, properties);
    }

    public static void evalCode(String code, int line, String sourceName, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Context runContext = Context.enter();
        runContext.setLanguageVersion(Context.VERSION_ES6);
        ScriptableObject scope = runContext.initStandardObjects();

        if (properties != null) {
            for (var property : properties.entrySet()) {
                ScriptableObject.putProperty(scope, property.getKey(), Context.javaToJS(property.getValue(), scope));
            }
        }

        try {
            runContext.evaluateString(scope, code, sourceName, line, null);
        } catch (EvaluatorException evaluatorException) {
            throw new CubeCodeException("SyntaxError: "+evaluatorException.details().replaceFirst("TypeError: ", ""), sourceName);
        } catch (EcmaError ecmaError) {
            throw new CubeCodeException("EcmaError: "+ecmaError.details().replaceFirst("TypeError: ", ""), sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getLocalizedMessage(), sourceName);
        }

        runContext.close();
    }

    public Map<String, Script> getScripts() {
        return this.scripts;
    }
}