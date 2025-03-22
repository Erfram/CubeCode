package com.cubecode.api.scripts;

import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.Script;
import com.cubecode.utils.ScriptType;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import java.util.ArrayList;
import java.util.List;

import static com.cubecode.CubeCode.projectManager;
import static com.cubecode.CubeCode.scriptExecutor;

public class ServerScript implements Script {
    private String name;
    private String code;
    private ScriptType side;
    private List<String> libraries;
    public Context context;
    public ScriptScope scope;

    public ServerScript(String name, String code, ScriptType side, List<String> libraries) {
        this.name = name;
        this.code = code;
        this.side = side;
        this.libraries = libraries;
    }

    public ServerScript(String name, String code, ScriptType side) {
        this.name = name;
        this.code = code;
        this.side = side;
        this.libraries = new ArrayList<>();
    }

    public ServerScript(String name, String code) {
        this.name = name;
        this.code = code;
        this.side = ScriptType.SERVER;
        this.libraries = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ScriptType getSide() {
        return side;
    }

    @Override
    public List<String> getLibraries() {
        return this.libraries;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSide(ScriptType side) {
        this.side = side;
    }

    @Override
    public void setLibraries(List<String> libraries) {
        this.libraries = libraries;
    }

    @Override
    public void addLibraryScript(String scriptName) {
        this.libraries.add(scriptName);
    }

    @Override
    public boolean hasLibraryScript(String scriptName) {
        int index = this.libraries.indexOf(scriptName);

        if (index != -1) {
            return true;
        }

        return false;
    }

    @Override
    public void removeLibraryScript(String scriptName) {
        this.libraries.remove(scriptName);
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
        this.prepare();
        try {
            this.libraries.forEach(library -> {
                ServerScript script = projectManager.getScript(library);
                if (script != null) {
                    scriptExecutor.evaluate(this.context, this.scope,  script.getCode(), sourceName);
                }
            });

            this.evaluate();
            scriptExecutor.invokeFunction(this.context, this.scope, function, properties.getMap().values().toArray());
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

    public void prepare() {
        this.context = Context.enter();
        this.scope = new ScriptScope(name, this.context);
        this.scope.setParentScope(ProjectManager.globalScope);
    }

    public void evaluate() throws CubeCodeException {
        scriptExecutor.evaluate(this.context, this.scope, code, name);
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