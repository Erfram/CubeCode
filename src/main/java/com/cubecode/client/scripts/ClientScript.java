package com.cubecode.client.scripts;

import com.cubecode.api.scripts.ScriptScope;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.Script;
import com.cubecode.utils.ScriptSide;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import java.util.ArrayList;
import java.util.List;

import static com.cubecode.CubeCodeClient.projectManager;

public class ClientScript implements Script {
    private String name;
    private String code;
    private ScriptSide side;
    private List<String> libraries;

    public Context context;
    public ScriptScope scope;

    public ClientScript(String name, String code) {
        this.name = name;
        this.code = code;
        this.side = ScriptSide.CLIENT;
        this.libraries = new ArrayList<>();
    }

    public void run(String function, String sourceName, ClientProperties properties) throws CubeCodeException {
        try {
            this.libraries.forEach(library -> {
                ClientScript script = projectManager.getScript(library);
                if (script != null) {
                    this.context.evaluateString(this.scope, script.getCode(), sourceName, 1, null);
                }
            });

            this.evaluate();
            projectManager.invokeFunction(this.context, this.scope, function, properties.getMap().values().toArray());
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
        this.scope.setParentScope(ClientProjectManager.globalScope);
        projectManager.evaluate(this.context, this.scope, this.code, this.name);
    }

    public void run(String sourceName, ClientProperties properties) throws CubeCodeException {
        this.run("client", sourceName, properties);
    }

    public void run(ClientProperties properties) throws CubeCodeException {
        this.run("client", this.name, properties);
    }

    public void run(String function, String sourceName) throws CubeCodeException {
        this.run(function, sourceName, ClientProperties.create());
    }

    public void run(String sourceName) throws CubeCodeException {
        this.run("client", sourceName, ClientProperties.create());
    }

    public void run() throws CubeCodeException {
        this.run("client", this.name, ClientProperties.create());
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public ScriptSide getSide() {
        return this.side;
    }

    @Override
    public List<String> getLibraries() {
        return this.libraries;
    }

    @Override
    public boolean hasLibraryScript(String scriptName) {
        int index = this.libraries.indexOf(scriptName);

        if (index != -1) {
            return true;
        }

        return false;
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

    @Override
    public void setLibraries(List<String> libraries) {
        this.libraries = libraries;
    }

    @Override
    public void addLibraryScript(String scriptName) {
        this.libraries.add(scriptName);
    }

    @Override
    public void removeLibraryScript(String scriptName) {
        this.libraries.remove(scriptName);
    }
}
