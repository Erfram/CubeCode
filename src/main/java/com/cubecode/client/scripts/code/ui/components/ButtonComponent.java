package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

public class ButtonComponent extends AbstractComponent {
    String label;
    Runnable onClick;

    public ButtonComponent(String label) {
        this.label = label;
        this.width = 100f;
        this.height = 80f;
        this.onClick = () -> {};
    }

    public ButtonComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        float width = this.width;
        float height = this.height;

        if (this.rw != null) {
            width = ImGui.getWindowSizeX() * this.rw;
        }

        if (this.rh != null) {
            height = ImGui.getWindowSizeY() * this.rh;
        }

        if (ImGui.button(this.label, width, height)) {
            this.onClick.run();
        }
    }
}
