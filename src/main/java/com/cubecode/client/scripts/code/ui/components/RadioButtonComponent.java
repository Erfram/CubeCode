package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class RadioButtonComponent extends AbstractComponent {
    View view;
    boolean active;
    String id;
    Runnable onClick;

    public RadioButtonComponent(View view, String id, boolean active) {
        this.view = view;
        this.id = id;
        this.active = active;
        this.onClick = () -> {};
    }

    public RadioButtonComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        this.view.putVariable(this.id, new ImBoolean(this.active));

        if (ImGui.radioButton("##", this.view.getVariable(this.id))) {
            this.onClick.run();
        }
    }
}