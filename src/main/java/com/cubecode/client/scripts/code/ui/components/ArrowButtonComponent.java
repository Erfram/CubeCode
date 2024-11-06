package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

public class ArrowButtonComponent implements Component {
    String id;
    int dir;
    Runnable onClick;

    public ArrowButtonComponent(String id, int dir) {
        this.id = id;
        this.dir = dir;
        this.onClick = () -> {};
    }

    public ArrowButtonComponent dir(int dir) {
        this.dir = dir;
        return this;
    }

    public ArrowButtonComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        if (ImGui.arrowButton(this.id, this.dir)) {
            this.onClick.run();
        }
    }
}
