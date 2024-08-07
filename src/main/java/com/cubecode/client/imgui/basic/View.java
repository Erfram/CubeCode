package com.cubecode.client.imgui.basic;


import com.cubecode.client.imgui.themes.CodeTheme;
import com.cubecode.client.views.CodeEditorView;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class View {
    private final HashMap<String, Object> variables = new HashMap<>();
    private boolean isInit;
    protected final UUID uniqueID;
    protected Theme theme;

    protected final int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
    protected final int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

    public View(Theme theme) {
        this.uniqueID = UUID.randomUUID();
        this.theme = theme;
    }

    public View() {
        this.uniqueID = UUID.randomUUID();
        this.theme = CodeTheme.parseTheme(View.class.getClassLoader().getResourceAsStream("assets/cubecode/themes/darcula.json"));
    }

    public UUID getUniqueID() {
        return this.uniqueID;
    }

    public <T> void putVariable(String name, T value) {
        this.variables.putIfAbsent(name, value);
    }

    public <T> void setVariable(String name, T value) {
        this.variables.put(name, value);
    }

    public void removeVariable(String name) {
        this.variables.remove(name);
    }

    public <T> T getVariable(String name) {
        return (T) this.variables.get(name);
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