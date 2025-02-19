package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

/**
 * button
 */
public class ButtonComponent extends AbstractComponent {
    String label;
    Runnable onClick;

    public ButtonComponent(String label) {
        this.label = label;
        this.width = 100f;
        this.height = 80f;
        this.onClick = () -> {};
    }

    /**
     * onClick
     */
    public ButtonComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    /**
     * setLabel
     */
    public ButtonComponent label(String label) {
        this.label = label;
        return this;
    }

    @Override
    public AbstractComponent w(float width) {
        this.width = width;
        return super.w(width);
    }

    @Override
    public AbstractComponent h(float height) {
        this.height = height;
        return super.h(height);
    }

    @Override
    public AbstractComponent wh(float width, float height) {
        this.width = width;
        this.height = height;
        return super.wh(width, height);
    }

    @Override
    public void render() {
        if (ImGui.button(this.label, this.getWidth(), this.getHeight())) {
            this.onClick.run();
        }
    }
}
