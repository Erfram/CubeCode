package com.cubecode.client.scripts;

import com.cubecode.api.scripts.Properties;
import com.cubecode.api.scripts.ScriptScope;
import com.cubecode.utils.CubeCodeException;
import com.cubecode.utils.Script;
import com.cubecode.utils.ScriptSide;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.EcmaError;
import dev.latvian.mods.rhino.EvaluatorException;

import static com.cubecode.CubeCodeClient.clientProjectManager;

public class ClientScript implements Script {
    public String name;
    public String code;
    public ScriptSide side;
    public Context context;
    public ScriptScope scope;

    public ClientScript(String name, String code) {
        this.name = name;
        this.code = code;
        this.side = ScriptSide.CLIENT;
    }

    public void run(String function, String sourceName, ClientProperties properties) throws CubeCodeException {
        //TODO clientProjectManager.updateScriptsFromFiles();

        try {
            this.evaluate();
            clientProjectManager.invokeFunction(context, scope, function, properties.getMap().values().toArray());
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
        clientProjectManager.evaluate(this.context, this.scope, this.code, this.name);
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSide(ScriptSide side) {
        this.side = side;
    }
}
