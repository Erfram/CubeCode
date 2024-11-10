package com.cubecode.client.scripts.code.ui.components;

import imgui.ImGui;

public class TextComponent extends AbstractComponent {
    String text;
    int r;
    int g;
    int b;
    int a;
    Runnable onClick;

    public TextComponent(String text) {
        this.text = text;
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.a = 255;
    }

    public TextComponent color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 255;

        return this;
    }

    public TextComponent color(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        return this;
    }

    public TextComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        ImGui.textColored(this.r, this.g, this.b, this.a, this.text);

        if (ImGui.isItemClicked()) {
            this.onClick.run();
        }
    }
}
