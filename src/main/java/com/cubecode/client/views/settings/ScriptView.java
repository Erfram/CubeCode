package com.cubecode.client.views.settings;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Button;
import com.cubecode.client.imgui.components.InputText;
import com.cubecode.client.imgui.components.Window;
import imgui.ImGui;
import imgui.type.ImString;

public class ScriptView extends View {
    private final int viewWidth = 500;
    private final int viewHeight = 400;

    @Override
    public String getName() {
        return String.format("Script Settings##%s", uniqueID);
    }

    @Override
    public void init() {
        float posX = (windowWidth - viewWidth) * 0.5f;
        float posY = (windowHeight - viewHeight) * 0.5f;

        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(viewWidth, viewHeight);
    }

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .callback(() -> {
                ImGui.text("Name Context");
            }).draw(
                InputText.builder().text(CubeCodeConfig.getScriptConfig().contextName).id("contextName").build(),
                Button.builder().title("Reset").callback(() -> {
                    CubeCodeConfig.getScriptConfig().contextName = "Context";
                    this.setVariable("contextName", "Context");

                    CubeCodeConfig.saveConfig();
                }).build(),
                Button.builder().title("Apply").callback(() -> {
                    CubeCodeConfig.getScriptConfig().contextName = ((ImString)this.getVariable("contextName")).get();

                    CubeCodeConfig.saveConfig();
                }).build()
            ).render(this);
    }
}
