package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.imgui.basic.View;
import imgui.ImGui;
import imgui.type.ImBoolean;

/**
 * checkbox
 */
public class CheckboxComponent extends AbstractComponent {
    View view;
    boolean active;
    Runnable onClick;
    String id;

    public CheckboxComponent(View view, String id, boolean active) {
        this.view = view;
        this.id = id;
        this.onClick = () -> {};
        this.active = active;
    }

    /**
     * active
     */
    public CheckboxComponent active(boolean isActive) {
        this.active = isActive;
        return this;
    }

    /**
     * onClick
     */
    public CheckboxComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        this.view.putVariable(this.id, new ImBoolean(this.active));

        if (ImGui.checkbox("##", this.view.getVariable(this.id))) {
            this.onClick.run();
        }
    }
}
