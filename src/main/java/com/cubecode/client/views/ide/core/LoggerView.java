package com.cubecode.client.views.ide.core;

import com.cubecode.CubeCodeClient;
import com.cubecode.client.imgui.CubeImGui;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.Window;
import com.cubecode.utils.TextUtils;
import imgui.ImGui;

import java.io.IOException;
import java.nio.file.Files;

public class LoggerView extends View {
    @Override
    public void init() {
        super.init();
    }

    @Override
    public String getName() {
        return "Logger##" + this.getUniqueID();
    }

    @Override
    public void render() {
        Window.create()
            .title(getName())
            .callback(() -> {
                if (ImGui.button("clear")) {
                    try {
                        Files.writeString(CubeCodeClient.loggerManager.logger.toPath(), "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                CubeImGui.beginChild("logs", 0, 0, true, () -> {
                    for (String log : CubeCodeClient.loggerManager.getLogs()) {
                        CubeImGui.textMutable(TextUtils.formatText(log));
                    }
                });
            })
            .render(this);
    }
}
