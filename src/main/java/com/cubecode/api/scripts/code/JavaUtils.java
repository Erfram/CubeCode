package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.ScriptScope;
import dev.latvian.mods.rhino.*;
import net.minecraft.client.option.GameOptions;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

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
