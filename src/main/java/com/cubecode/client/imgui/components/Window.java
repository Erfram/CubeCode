package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.ImGuiLoader;
import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class Window {
    private String title = "default";
    private int flags = 0;
    private Runnable callback = () -> {};
    private Runnable onExit = () -> {};

    public static Window create() {
        return new Window();
    }

    public Window title(String title) {
        this.title = title;
        return this;
    }

    public Window flags(int windowFlags) {
        this.flags = windowFlags;
        return this;
    }

    public Window callback(Runnable callback) {
        this.callback = callback;
        return this;
    }

    public Window onExit(Runnable callback) {
        this.onExit = callback;
        return this;
    }

    public void render(View view) {
        /* @DYAMO FIXME: check title validity, presence of `drawAction` and other possibly inconsistent properties.
         * This is equivalent of builder's build method that verifies consistency of the built object.
         */

        String variableClose = this.title + "_close";

        view.putVariable(variableClose, new ImBoolean(true));
        ImBoolean close = view.getVariable(variableClose);

        if (ImGui.begin(this.title, close, this.flags)) {
            if (!close.get()) {
                this.onExit.run();
                ImGuiLoader.removeView(view);
            } else {
                this.manageDocking(view);

                this.callback.run();
            }
        }

        ImGui.end();
    }

    private void manageDocking(View view) {
        boolean isDocked = ImGui.isWindowDocked();
        String uniqueID = view.getUUID().toString();
        String widthKey = "width_" + uniqueID;
        String heightKey = "height_" + uniqueID;
        String dockedKey = "docked_" + uniqueID;

        // Получаем текущее состояние и размеры окна
        boolean wasDocked = view.getVariable(dockedKey) != null ? view.getVariable(dockedKey) : false;
        float undockedWidth = view.getVariable(widthKey) != null ? view.getVariable(widthKey) : 0F;
        float undockedHeight = view.getVariable(heightKey) != null ? view.getVariable(heightKey) : 0F;

        // Если окно было закреплено и теперь откреплено, восстановить размеры и установить позицию окна к курсору
        if (wasDocked && !isDocked) {
            ImGui.setWindowSize(undockedWidth, undockedHeight);
            ImGui.setWindowPos(ImGui.getMousePosX(), ImGui.getMousePosY());
        }

        // Если окно не закреплено, сохранить его текущие размеры
        if (!isDocked) {
            view.setVariable(widthKey, ImGui.getWindowWidth());
            view.setVariable(heightKey, ImGui.getWindowHeight());
        }

        // Обновить статус закрепления
        view.setVariable(dockedKey, isDocked);
    }
}
