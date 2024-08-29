package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.ScriptScope;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaClass;

public class JavaUtils {
    private final Context cx;
    private final ScriptScope scope;

    public JavaUtils(Context context, ScriptScope scope) {
        this.cx = context;
        this.scope = scope;
    }

    public NativeJavaClass type(String className) throws ClassNotFoundException {

        return new NativeJavaClass(cx, scope, Class.forName(className));
    }
}
