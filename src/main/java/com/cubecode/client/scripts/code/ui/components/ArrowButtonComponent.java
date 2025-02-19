package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

/**
 * arrowButton
 */
public class ArrowButtonComponent extends AbstractComponent {
    String id;
    int dir;
    Runnable onClick;

    public ArrowButtonComponent(String id, int dir) {
        this.id = id;
        this.dir = dir;
        this.onClick = () -> {};
    }

    /**
     * dir
     */
    public ArrowButtonComponent dir(int dir) {
        this.dir = dir;
        return this;
    }

    /**
     * onClick
     */
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
