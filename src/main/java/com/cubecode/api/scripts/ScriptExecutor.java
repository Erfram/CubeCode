package com.cubecode.api.scripts;

import com.cubecode.api.scripts.code.JavaUtils;
import com.cubecode.api.scripts.code.ScriptFactory;
import com.cubecode.utils.CubeCodeException;
import dev.latvian.mods.rhino.*;
import dev.latvian.mods.rhino.mod.util.RemappingHelper;
import dev.latvian.mods.rhino.util.Remapper;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ScriptExecutor {

    public static final Remapper remapper = RemappingHelper.getMinecraftRemapper();
    public static final Context globalContext = Context.enter();
    public static final ScriptScope globalScope = new ScriptScope("CubeCode global scope", globalContext);

    public static final String DEFAULT_SCRIPT = "function server(c) {\n    c.server.send(\"Hello World!\", true)\n}";
    public static final String DEFAULT_CLIENT_SCRIPT = "function client(c) {\n\n}";


    public ScriptExecutor() {

        globalContext.setRemapper(remapper);
        globalContext.setApplicationClassLoader(ProjectManager.class.getClassLoader());
        globalContext.setMaximumInterpreterStackDepth(500);
        globalScope.setParentScope(globalContext.initStandardObjects());

        globalScope.set("CubeCode", new ScriptFactory());
        globalScope.set("Java", new JavaUtils(globalContext, globalScope));
    }

    public Object evaluate(Context context, ScriptScope scope, String code, String sourceName) {
        return context.evaluateString(scope, code, sourceName, 1, null);
    }

    public Object invokeFunction(Context context, Scriptable scope, String function, Object[] args) {
        Function functionObject = (Function) ScriptableObject.getProperty(scope, function, context);
        return functionObject.call(context, scope, scope, args);
    }

    public void evalCode(String code, String sourceName, @Nullable Map<String, Object> properties) throws CubeCodeException {
        Context context = Context.enter();
        ScriptableObject scope = context.initSafeStandardObjects();

        context.setRemapper(remapper);
        context.setApplicationClassLoader(ProjectManager.class.getClassLoader());

        if (properties != null) {
            for (Map.Entry<String, Object> property : properties.entrySet()) {
                ScriptableObject.putConstProperty(scope, property.getKey(), Context.javaToJS(context, property.getValue(), scope), context);
            }
        }

        try {
            this.evaluate(context, (ScriptScope) scope, code, sourceName);
        } catch (EvaluatorException | EcmaError e) {
            String errorType = (e instanceof EvaluatorException) ? "SyntaxError" : "EcmaError";
            String details = e.details().replaceFirst("TypeError: ", "");
            throw new CubeCodeException(errorType + ": " + details, sourceName);
        } catch (Exception e) {
            throw new CubeCodeException(e.getClass().getSimpleName() + ": " + e.getLocalizedMessage(), sourceName);
        }
    }
}
