package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.api.utils.FileManager;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;
import dev.latvian.mods.rhino.ScriptableObject;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.jetbrains.annotations.Nullable;
import com.cubecode.utils.CubeCodeException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ScriptManager extends DirectoryManager {
    static final public Context context = Context.enter();
    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
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

    public boolean deleteScript(String scriptName) {
        try {
            Files.delete(CubeCode.scriptManager.getDirectory().toPath().resolve(scriptName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean createScript(String scriptName, String scriptContent) {
        Path scriptsPath = CubeCode.scriptManager.getDirectory().toPath();

        File scriptFile = scriptsPath.resolve(scriptName).toFile();

        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write(scriptContent);
            return true;
        } catch (IOException e) {
            return false;
        }
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
        ScriptableObject scope = context.initStandardObjects();

        if (properties != null) {
            for (Map.Entry<String, Object> property : properties.entrySet()) {
                ScriptableObject.putProperty(scope, property.getKey(), Context.javaToJS(context, property.getValue(), scope), context);
            }
        }

        try {
            context.evaluateString(scope, code, sourceName, line, null);
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details, sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    public Map<String, Script> getScripts() {
        return new HashMap<>(this.scripts);
    }
}