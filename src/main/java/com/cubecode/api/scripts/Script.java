package com.cubecode.api.scripts;

import com.cubecode.CubeCode;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.ScriptType;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EvaluatorException;
import dev.latvian.mods.rhino.RhinoException;

import java.util.ArrayList;
import java.util.List;

import static com.cubecode.CubeCode.projectManager;
import static com.cubecode.CubeCode.scriptExecutor;

public class Script {

    private String name;
    private String code;
    private ScriptType side;
    private List<String> libraries;
    private Context context;
    private ScriptScope scope;
    private int lastLaunchErrorLine = 0;
    private String lastLaunchErrorMessage = "";

    public Script(String name, String code, ScriptType side, List<String> libraries) {
        this.name = name;
        this.code = code;
        this.side = side;
        this.libraries = libraries;
    }

    public Script(String name, String code, ScriptType side) {
        this(name, code, side, new ArrayList<>());
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

    public void run(String sourceName, Properties properties) throws CubeCodeException {
        this.run(this.side == ScriptType.CLIENT ? "client" : "server", sourceName, properties);
    }

    public void run(String function, String sourceName, Properties properties) throws CubeCodeException {
        this.prepare();
        try {
            this.evaluateLibraries(sourceName);
            this.evaluate();
            scriptExecutor.invokeFunction(this.context, this.scope, function, properties.getMap().values().toArray());
        } catch (Exception e) {
            this.handleException(sourceName, e);
        }
    }

    public void handleException(String sourceName, Exception exception) throws CubeCodeException {
        String errorMessage = "";
        int errorLine = 0;
        if (exception instanceof RhinoException e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            errorLine = e.lineNumber();
            StringBuilder lines = new StringBuilder();
            lines.append(" ".repeat(Math.max(0, e.columnNumber())));
            errorMessage = errorType + ": " + details + "\n" +
                    "Script: " + sourceName + "\n" + "Line: " + e.lineNumber() + ", Column: " + e.columnNumber() + "\n" +
                    lines + " |\n" +
                    lines + "\\/\n" +
                    //"Code: " + this.code.split("\n")[e.lineNumber() - 1].replace("\t", "");
                    "Code: " + e.lineSource();
        }
        else {
            errorMessage = exception.getClass().getSimpleName() + ": " + exception.getLocalizedMessage();
        }
        this.setLastLaunchErrorLine(errorLine);
        this.setLastLaunchErrorMessage(errorMessage);
        //TODO: Предлагаю тут сохранять в settings.json
        CubeCode.loggerManager.error(this.name, errorMessage.replaceAll("\\n", "\n&c"));
        throw new CubeCodeException(errorMessage, sourceName);
    }

    public void setLastLaunchErrorMessage(String errorMessage) {
        this.lastLaunchErrorMessage = errorMessage;
    }

    public String getLastLaunchErrorMessage() {
        return this.lastLaunchErrorMessage;
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
            Script script = projectManager.getScript(library);
            if (script != null) {
                script.evaluate(this.context, libraryScope,  script.getCode(), sourceName);
            }
            else {
                throw new CubeCodeException("Can't find library " + library + " in " + this.name, sourceName);
            }
        }
        this.scope.setParentScope(libraryScope);
    }

    public ScriptScope getScope() {
        return this.scope;
    }

    public Context getContext() {
        return this.context;
    }

    public void setScope(ScriptScope scope) {
        this.scope = scope;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public int getLastLaunchErrorLine() {
        return lastLaunchErrorLine;
    }

    public void setLastLaunchErrorLine(int lastLaunchErrorLine) {
        this.lastLaunchErrorLine = lastLaunchErrorLine;
    }
}
