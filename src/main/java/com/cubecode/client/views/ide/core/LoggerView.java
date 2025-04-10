package com.cubecode.client.views.ide.core;

import com.cubecode.CubeCode;
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
                        Files.writeString(CubeCode.loggerManager.logger.toPath(), "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                CubeImGui.beginChild("logs", 0, 0, true, () -> {
                    int color = 0xffffff;
                    for (String log : CubeCode.loggerManager.getLogs()) {
                        if (log.startsWith("&7[&")) { // Start of new log entry
                            color = getColor(log); // Error color
                        }
                        CubeImGui.textMutable(TextUtils.formatText(log).withColor(color));
                    }
                });
            })
            .render(this);
    }

    public int getColor(String log) {
        if (log.startsWith("&7[&4")) return 0xE3256B; // Error
        if (log.startsWith("&7[&c")) return 0xFAD201; // Warning
        return 0xFFFFFF;
    }
}
