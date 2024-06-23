package com.cubecode.client.imgui.basic;

import imgui.extension.texteditor.TextEditorLanguageDefinition;
import com.cubecode.client.imgui.languages.JavaScriptDefinition;
import com.cubecode.client.imgui.themes.DefaultTheme;

import java.util.UUID;

public abstract class View {

    protected static final TextEditorLanguageDefinition javaScriptSyntax = JavaScriptDefinition.build();
    protected boolean isInit;
    protected UUID uniqueID;
    protected Theme theme;

    public View() {
        uniqueID = UUID.randomUUID();
        theme = new DefaultTheme();
    }

    /**
     * @return optional parameter for rendering profiling
     */
    protected String getName() {
        return String.format("%s##%s", this.getClass().getSimpleName(), uniqueID);
    }

    /**
     * @return the styling for {@link View#render()}
     */
    public Theme getTheme() {
        return theme;
    }

    /**
     * Wrapper {@link View#init()} for runtime initialization
     */
    protected final void loop() {
        if (!isInit) {
            init();
            isInit = true;
        }
        render();
    }

    /**
     * Initialization, such as window positions and other states
     */
    public void init() {

    }

    /**
     * Stores the render implementation for the current task
     */
    public void render() {

    }

}