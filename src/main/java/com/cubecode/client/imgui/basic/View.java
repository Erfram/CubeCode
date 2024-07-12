package com.cubecode.client.imgui.basic;

import com.cubecode.client.imgui.themes.CodeTheme;
import com.cubecode.client.views.CodeEditorView;

import java.util.HashMap;
import java.util.UUID;

public abstract class View {
    private static final HashMap<String, Object> variables = new HashMap<>();
    private boolean isInit;
    protected final UUID uniqueID;
    protected Theme theme;

    public View(Theme theme) {
        this.uniqueID = UUID.randomUUID();
        this.theme = theme;
    }

    public View() {
        this.uniqueID = UUID.randomUUID();
        this.theme = CodeTheme.parseTheme(View.class.getClassLoader().getResourceAsStream("assets/cubecode/themes/darcula.json"));
    }

    public HashMap<String, Object> getVariables() {
        return variables;
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
            this.init();
            this.isInit = true;
        }

        this.render();
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

    public void handleKeyReleased(int keyCode, int scanCode, int modifiers) {

    }

    public void handleKeyPressed(int keyCode, int scanCode, int modifiers) {

    }

    public void handleScroll(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    public void handleMouseClicked(double mouseX, double mouseY, int button) {

    }

    public void handleMouseReleased(double mouseX, double mouseY, int button) {
    }
}