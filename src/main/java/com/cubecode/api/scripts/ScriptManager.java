package com.cubecode.api.scripts;

import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.api.utils.FileManager;
import org.jetbrains.annotations.Nullable;
import org.mozilla.javascript.*;
import com.cubecode.utils.CubeCodeException;
import org.mozilla.javascript.debug.DebuggableScript;

import javax.script.ScriptException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager extends DirectoryManager {
    private ConcurrentHashMap<String, Script> scripts;

    public ScriptManager(File scriptsDirectory) {
        super(scriptsDirectory);
        this.updateScripts();
    }

    public void setScript(String scriptName, String code) {
        this.getFiles().forEach(file -> {
            if (file.getName().equals(scriptName)) {
                FileManager.writeJsonToFile(file.getPath(), code);
            }
        });
    }

    public void updateScripts() {
        ConcurrentHashMap<String, Script> newScripts = new ConcurrentHashMap<>();

        this.getFiles().forEach(file -> {
            if (file.getName().endsWith(".js")) {
                newScripts.put(file.getName(), new Script(this.readFileToString(file.getName())));
            }
        });

        this.scripts = newScripts;
    }

    public void executeScript(String name, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Script script = scripts.get(name);
        if (script == null) {
            throw new CubeCodeException("Script not found: " + name, name);
        }
        script.execute(name, properties);
    }

    public static void evalCode(String code, int line, String sourceName, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Context runContext = Context.enter();
        runContext.setLanguageVersion(Context.VERSION_ES6);
        try {
            ScriptableObject scope = runContext.initStandardObjects();

            if (properties != null) {
                for (Map.Entry<String, Object> property : properties.entrySet()) {
                    ScriptableObject.putProperty(scope, property.getKey(), Context.javaToJS(property.getValue(), scope));
                }
            }

            try {
                runContext.evaluateString(scope, code, sourceName, line, null);
            } catch (EvaluatorException | EcmaError e) {
                String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
                String details = e.details().replaceFirst("TypeError: ", "");
                throw new CubeCodeException(errorType + ": " + details, sourceName);
            } catch (Exception e) {
                throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
            }
        } finally {
            Context.exit();
        }
    }

    public Map<String, Script> getScripts() {
        return new HashMap<>(this.scripts);
    }
}