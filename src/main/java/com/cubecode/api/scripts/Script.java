package com.cubecode.api.scripts;

import com.cubecode.network.Dispatcher;
import com.cubecode.network.packets.all.ScriptExecutionResultPacket;
import com.cubecode.scripting.ScriptExecutionResult;
import com.cubecode.scripting.ScriptExecutor;
import com.cubecode.scripting.ScriptScope;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.ScriptType;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EvaluatorException;
import dev.latvian.mods.rhino.RhinoException;

import java.util.ArrayList;
import java.util.List;

import static com.cubecode.CubeCode.scriptExecutor;
import static com.cubecode.CubeCode.scriptManager;

public class Script {
    private String UUID;
    private String name;
    private String code;
    private ScriptType side;
    private List<String> libraries;
    private Context context;
    private ScriptScope scope;
    private int lastLaunchErrorLine = -1;
    private String lastLaunchErrorMessage = "";

    public Script(String UUID, String name, String code, ScriptType side, List<String> libraries) {
        this.setUUID(UUID);
        this.setName(name);
        this.setCode(code);
        this.setSide(side);
        this.setLibraries(libraries);
    }

    public Script(String UUID, String name, String code, ScriptType side) {
        this(UUID, name, code, side, new ArrayList<>());
    }


    //region Getter's and Setter's
    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ScriptType getSide() {
        return side;
    }

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

    public String getUUID() {
        return this.UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public ScriptScope getScope() {
        return this.scope;
    }

    public Context getContext() {
        return this.context;
    }


    public int getLastLaunchErrorLine() {
        return lastLaunchErrorLine;
    }

    public void setLastLaunchErrorLine(int lastLaunchErrorLine) {
        this.lastLaunchErrorLine = lastLaunchErrorLine;
    }

    public void setLastLaunchErrorMessage(String errorMessage) {
        this.lastLaunchErrorMessage = errorMessage;
    }

    public String getLastLaunchErrorMessage() {
        return this.lastLaunchErrorMessage;
    }

    //endregion

    public void setLibraries(List<String> libraries) {
        this.libraries = libraries;
    }

    public void addLibraryScript(String scriptName) {
        this.libraries.add(scriptName);
    }

    public boolean hasLibraryScript(String scriptName) {
        int index = this.libraries.indexOf(scriptName);

        if (index != -1) {
            return true;
        }

        return false;
    }

    public void removeLibraryScript(String scriptName) {
        this.libraries.remove(scriptName);
    }

    public ScriptExecutionResult run(String sourceName, Properties properties)  {
        return this.run(this.side == ScriptType.CLIENT ? "client" : "server", sourceName, properties);
    }

    public ScriptExecutionResult run(String function, String sourceName, Properties properties) {
        this.prepare();
        ScriptExecutionResult result = new ScriptExecutionResult("");
        try {
            this.evaluateLibraries(sourceName);
            this.evaluate();
            Object invokeResult = scriptExecutor.invokeFunction(this.context, this.scope, function, properties.getMap().values().toArray());
            result.result = invokeResult.toString();
        } catch (Exception e) {
            result = this.handleException(sourceName, e);
        } finally {
            Dispatcher.sendToServer(new ScriptExecutionResultPacket(result));
            return result;
        }
    }

    public ScriptExecutionResult handleException(String sourceName, Exception exception) {
        String errorMessage;
        int errorLine = -1;
        if (exception instanceof RhinoException e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            errorLine = e.lineNumber();
            errorMessage = errorType + ": " + details + "\n" +
                    "Script: " + sourceName + "\n" + "Line: " + e.lineNumber() + ", Column: " + e.columnNumber() + "\n" +
                    " ".repeat(Math.max(0, e.columnNumber() + 6)) + "↓\n" +
                    "Code: " + this.code.split("\n")[e.lineNumber() - 1].replace("\t", "");
        }
        else {
            errorMessage = exception.getClass().getSimpleName() + ": " + exception.getLocalizedMessage();
        }
        ScriptExecutionResult scriptExecutionResult = new ScriptExecutionResult("");
        scriptExecutionResult.setError(errorLine, errorMessage);
        scriptExecutionResult.setSource(sourceName);
        return scriptExecutionResult;
    }


    public void prepare() {
        this.context = Context.enter();
        this.scope = new ScriptScope(name, this.context);
        this.scope.setParentScope(ScriptExecutor.globalScope);
    }

    public void evaluate() throws CubeCodeException {
        this.evaluate(this.context, this.scope, this.code, this.name);
    }

    public void evaluate(Context cx, ScriptScope scope, String code, String sourceName) throws CubeCodeException {
        try {
            scriptExecutor.evaluate(cx, scope, code, sourceName);
            this.setLastLaunchErrorLine(0);
            this.setLastLaunchErrorMessage("");
        } catch (Exception e) {
            this.handleException(sourceName, e);
        }
    }

    public void evaluateLibraries(String sourceName) throws CubeCodeException {
        ScriptScope libraryScope = new ScriptScope(this.name + "_lib", this.context);
        libraryScope.setParentScope(ScriptExecutor.globalScope);
        for (String library : this.libraries) {
            Script script = scriptManager.getScript(library);
            if (script != null) {
                script.evaluate(this.context, libraryScope,  script.getCode(), sourceName);
            }
            else {
                throw new CubeCodeException("Can't find library " + library + " in " + this.name, sourceName);
            }
        }
        this.scope.setParentScope(libraryScope);
    }
}
