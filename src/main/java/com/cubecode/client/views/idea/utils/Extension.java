package com.cubecode.client.views.idea.utils;

import imgui.extension.texteditor.TextEditorLanguageDefinition;

public enum Extension {
    JAVASCRIPT("js", ScriptDefinition.javaScript()),
    TYPESCRIPT("ts", ScriptDefinition.typeScript()),
    PYTHON("python", ScriptDefinition.python()),
    ANGEL_SCRIPT("as", TextEditorLanguageDefinition.angelScript()),
    CPP("cpp", TextEditorLanguageDefinition.cPlusPlus()),
    CS("cs", ScriptDefinition.cs()),
    C("C", TextEditorLanguageDefinition.c()),
    LUA("lua", TextEditorLanguageDefinition.lua()),
    GLSL("glsl", TextEditorLanguageDefinition.glsl()),
    HLSL("hlsl", TextEditorLanguageDefinition.hlsl()),
    SQL("sql", TextEditorLanguageDefinition.sql()),
    HTML("html", ScriptDefinition.html()),
    CSS("css", ScriptDefinition.css()),
    BSH("bsh", ScriptDefinition.bsh()),
    XML("xml", ScriptDefinition.xml()),
    XAML("xaml", ScriptDefinition.xaml()),
    JSON("json", ScriptDefinition.json()),
    TOML("toml", ScriptDefinition.toml()),
    TXT("txt", null),
    DOC("doc", null),
    DOCX("docx", null),
    PROPERTIES("docx", null),
    CONFIG("config", null),
    UNKNOWN("unknown", null);

    private String name;
    private TextEditorLanguageDefinition languageDefinition;

    Extension(String name, TextEditorLanguageDefinition languageDefinition) {
        this.name = name;
        this.languageDefinition = languageDefinition;
    }

    public String getName() {
        return name;
    }

    public TextEditorLanguageDefinition getDefinition() {
        return languageDefinition;
    }

    public static boolean containsName(String name) {
        if (name == null) {
            return false;
        }
        for (Extension extension : values()) {
            if (extension.name.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
