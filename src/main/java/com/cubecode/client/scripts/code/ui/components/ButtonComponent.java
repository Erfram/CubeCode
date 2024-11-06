package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

public class ButtonComponent implements Component {
    String label;
    float width;
    float height;
    Runnable onClick;

    public ButtonComponent(String label) {
        this.label = label;
        this.width = 100;
        this.height = 80;
        this.onClick = () -> {};
    }

    public ButtonComponent wh(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ButtonComponent w(float width) {
        this.width = width;
        return this;
    }

    public ButtonComponent h(float height) {
        this.height = height;
        return this;
    }

    public ButtonComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        if (ImGui.button(this.label, this.width, this.height)) {
            this.onClick.run();
        }
    }
}
