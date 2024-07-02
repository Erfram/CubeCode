package com.cubecode.client.imgui.views;

import imgui.ImGui;
import imgui.type.ImBoolean;
import net.minecraft.client.MinecraftClient;
import com.cubecode.client.editor.CodeEditor;
import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;

public class CodeEditorView extends View {

    private final ImBoolean CLOSE = new ImBoolean(true);
    private final CodeEditor EDITOR = new CodeEditor();

    @Override
    public String getName() {
        return String.format("Cube IDEA##%s", uniqueID);
    }

    @Override
    public void init() {
        int width = 500;
        int height = 400;
        try {
            int windowWidth = MinecraftClient.getInstance().getWindow().getWidth();
            int windowHeight = MinecraftClient.getInstance().getWindow().getHeight();

            ImGui.setNextWindowPos(width / 2f - windowWidth / 2f, height / 2f - windowHeight / 2f);
        } catch (Exception ignored) {}
        ImGui.setNextWindowSize(width, height);
    }

    @Override
    public void render() {
        if (ImGui.begin(getName(), CLOSE)) {
            if (!CLOSE.get()) {
                ImGuiLoader.removeView(this);
            }
            EDITOR.render();
        }
        ImGui.end();
    }
}