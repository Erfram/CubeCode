package com.cubecode.client.imgui.basic;

import com.google.gson.JsonObject;
import imgui.ImGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class View {
    private boolean isInit;

    private final Map<String, Object> variables = new HashMap<>();
    protected final UUID uuid;

    protected Window window;

    public View() {
        this.uuid = UUID.randomUUID();
    }

    public UUID getUUID() {
        return this.uuid;
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

    public String getName() {
        return String.format("%s##%s", this.getClass().getSimpleName(), this.uuid);
    }

    protected final void loop() {
        this.window = MinecraftClient.getInstance().getWindow();
        if (!isInit) {

            this.init();

            this.isInit = true;
        }

        this.render();
    }

    /**
     * Initialization, such as window positions and other states
     */
    protected void init() {
        int viewWidth = 640;
        int viewHeight = 480;

        float posX = (this.window.getWidth() - viewWidth) * 0.5f;
        float posY = (this.window.getHeight() - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    /**
     * Stores the render implementation for the current task
     */
    protected void render() {

    }

    public void onClose() {
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

    public JsonObject serialize() {
        return new JsonObject();
    }

    public void deserialize(JsonObject jsonObject) {

    }
}
