package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.type.ImBoolean;

public class CheckboxComponent extends AbstractComponent {
    View view;
    boolean active;
    String id;

    public CheckboxComponent(View view, String id, boolean active) {
        this.view = view;
        this.id = id;
        this.active = active;
    }

    @Override
    public void render() {
        this.view.putVariable(this.id, new ImBoolean(this.active));

        ImGui.checkbox("##", this.view.getVariable(this.id));
    }
}
