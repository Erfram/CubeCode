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

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void render() {
        if (ImGui.button(this.label, this.getWidth(), this.getHeight())) {
            this.onClick.run();
        }
    }
}
