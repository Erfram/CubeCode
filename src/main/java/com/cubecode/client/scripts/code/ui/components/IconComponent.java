package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.utils.Icons;
import imgui.ImGui;

/**
 * icon
 */
public class IconComponent extends AbstractComponent {
    String iconId;
    boolean isClickable;
    Runnable onClick;

    public IconComponent(String iconId) {
        this.iconId = iconId;
        this.isClickable = false;
        this.width = 64f;
        this.height = 64f;
        this.onClick = () -> {};
    }

    /**
     * clickable
     */
    public IconComponent clickable() {
        this.isClickable = true;
        return this;
    }

    /**
     * onClick
     */
    public IconComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        float width = this.rw != null ? this.rw : this.width != null ? this.width : 0;
        float height = this.rh != null ? this.rh : this.height != null ? this.height : 0;

        Icons icon;
        try {
            icon = Icons.valueOf(iconId.toUpperCase());
        } catch (IllegalArgumentException e) {
            icon = Icons.EMPTY;
        }

        if (this.isClickable) {
            if (ImGui.imageButton(icon.getGlId(), width, height)) {
                this.onClick.run();
            }
        } else {
            ImGui.image(icon.getGlId(), width, height);

            if (ImGui.isItemClicked()) {
                this.onClick.run();
            }
        }
    }
}
