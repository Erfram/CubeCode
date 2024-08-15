package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.api.utils.DirectoryManager;
import com.cubecode.api.utils.FileManager;
import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScriptManager extends DirectoryManager {
    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
    public static final Context globalContext = Context.enter();
    static final public ScriptScope globalScope = new ScriptScope(globalContext);

    private ConcurrentHashMap<Script, ScriptScope> scripts = new ConcurrentHashMap<>();

    public ScriptManager(File scriptsDirectory) {
        super(scriptsDirectory);

        globalContext.setRemapper(remapper);
        globalContext.setApplicationClassLoader(ScriptManager.class.getClassLoader());
        globalScope.setParentScope(globalContext.initStandardObjects());

        this.updateScriptsFromFiles();
    }

    public Object evaluate(Context context, ScriptScope scope, String code, String sourceName) {
        return context.evaluateString(scope, code, sourceName, 1, null);
    }

    public Object invokeFunction(Context context, Scriptable scope, String function, Object... args) {
        Function functionObject = (Function) ScriptableObject.getProperty(scope, function, context);
        return functionObject.call(context, scope, scope, args);
    }

    public void evalCode(String code, String sourceName, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Context context = Context.enter();
        ScriptableObject scope = context.initSafeStandardObjects();

        context.setRemapper(remapper);
        context.setApplicationClassLoader(ScriptManager.class.getClassLoader());

        if (properties != null) {
            for (Map.Entry<String, Object> property : properties.entrySet()) {
                ScriptableObject.putConstProperty(scope, property.getKey(), Context.javaToJS(context, property.getValue(), scope), context);
            }
        }

        try {
            context.evaluateString(scope, code, sourceName, 1, null);
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details, sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }

    public void saveScript(Script script) {
        this.getFiles().forEach(file -> {
            if (file.getName().equals(script.name)) {
                FileManager.writeJsonToFile(file.getPath(), script.code);
            }
        });
    }

    public void deleteScript(String scriptName) {
        try {
            Files.delete(CubeCode.scriptManager.getDirectory().toPath().resolve(scriptName));
        } catch (IOException ignored) {
        }
    }

    public void createScript(String scriptName, String scriptContent) {
        Path scriptsPath = CubeCode.scriptManager.getDirectory().toPath();

        File scriptFile = scriptsPath.resolve(scriptName).toFile();

        try (FileWriter writer = new FileWriter(scriptFile)) {
            writer.write(scriptContent);
        } catch (IOException ignored) {
        }
    }

    public void updateScriptFromFile(String scriptName) {
        this.getFiles().forEach(file -> {
            if (file.getName().equals(scriptName)) {
                this.getScript(scriptName).code = this.readFileToString(file.getName());
            }
        });
    }

    public void updateScriptsFromFiles() {
        ConcurrentHashMap<Script, ScriptScope> newScripts = new ConcurrentHashMap<>();

        for (int i = 0; i < this.getFiles().size(); i++) {
            File file = this.getFiles().stream().toList().get(i);
            if (file.getName().endsWith(".js")) {
                newScripts.put(new Script(file.getName(), this.readFileToString(file.getName())),
                        scripts.values().isEmpty() ? new ScriptScope(globalContext) : (ScriptScope) scripts.values().toArray()[i]);
            }
        }

        this.scripts = newScripts;
    }

    public Script getScript(String scriptName) {
        return this.scripts.keySet().stream().filter(script -> script.name.equals(scriptName)).findFirst().get();
    }

    public List<Script> getScripts() {
        return this.scripts.keySet().stream().toList();
    }

    public ScriptScope getScope(String scriptName) {
        return this.scripts.get(getScript(scriptName));
    }
}