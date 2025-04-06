package com.cubecode.api.scripts.code;

import com.cubecode.scripting.ScriptScope;
import dev.latvian.mods.rhino.*;

public class JavaUtils {
    private final Context cx;
    private final ScriptScope scope;

    public JavaUtils(Context context, ScriptScope scope) {
        this.cx = context;
        this.scope = scope;
    }

    public NativeJavaClass type(String className) throws ClassNotFoundException {
        return new NativeJavaClass(this.cx, this.scope, Class.forName(className));
    }
}
