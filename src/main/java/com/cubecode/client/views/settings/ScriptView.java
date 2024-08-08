package com.cubecode.client.views.settings;

import com.cubecode.client.config.CubeCodeConfig;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Button;
import com.cubecode.client.imgui.components.InputText;
import com.cubecode.client.imgui.components.Window;
import imgui.ImGui;
import imgui.type.ImString;
import net.minecraft.text.Text;

public class ScriptView extends View {
    private final int viewWidth = 500;
    private final int viewHeight = 400;

    @Override
    public String getName() {
        return String.format(Text.translatable("imgui.cubecode.settings.script.name").getString() + "##%s", uniqueID);
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
                ImGui.text(Text.translatable("imgui.cubecode.settings.script.title.title").getString());
            }).draw(
                InputText.builder().text(CubeCodeConfig.getScriptConfig().contextName).id("contextName").build(),
                Button.builder().title(Text.translatable("imgui.cubecode.settings.script.reset.title").getString()).callback(() -> {
                    CubeCodeConfig.getScriptConfig().contextName = "Context";
                    this.setVariable("contextName", "Context");

                    CubeCodeConfig.saveConfig();
                }).build(),
                Button.builder().title(Text.translatable("imgui.cubecode.settings.script.apply.title").getString()).callback(() -> {
                    CubeCodeConfig.getScriptConfig().contextName = ((ImString)this.getVariable("contextName")).get();

                    CubeCodeConfig.saveConfig();
                }).build()
            ).render(this);
    }
}
