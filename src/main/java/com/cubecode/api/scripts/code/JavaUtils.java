package com.cubecode.api.scripts.code;

import com.cubecode.api.scripts.ScriptScope;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaClass;
import dev.latvian.mods.rhino.Scriptable;

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

    public Scriptable extend(Class<?> baseClass, Object methods) {
        // Создаем NativeJavaClass из Class
        NativeJavaClass nativeClass = new NativeJavaClass(cx, scope, baseClass);

        // Создаем новый объект для расширения
        Scriptable extension = cx.newObject(scope);

        // Копируем свойства базового класса
        Object[] ids = nativeClass.getIds(this.cx);
        for (Object id : ids) {
            if (id instanceof String) {
                String propertyName = (String) id;
                Object value = nativeClass.get(this.cx, propertyName, nativeClass);
                extension.put(this.cx, propertyName, extension, value);
            }
        }

        // Если methods является Scriptable, копируем его методы
        if (methods instanceof Scriptable) {
            Scriptable methodsScriptable = (Scriptable) methods;
            Object[] methodIds = methodsScriptable.getIds(this.cx);
            for (Object id : methodIds) {
                if (id instanceof String) {
                    String methodName = (String) id;
                    Object method = methodsScriptable.get(this.cx, methodName, methodsScriptable);
                    extension.put(this.cx, methodName, extension, method);
                }
            }
        }

        // Устанавливаем прототип и родительский скоп
        extension.setPrototype(nativeClass);
        extension.setParentScope(scope);

        return extension;
    }
}
