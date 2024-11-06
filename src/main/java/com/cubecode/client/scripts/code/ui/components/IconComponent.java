package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.utils.Icons;
import imgui.ImGui;

public class IconComponent implements Component {
    String iconId;
    boolean isClickable;
    float width;
    float height;
    Runnable onClick;

    public IconComponent(String iconId) {
        this.iconId = iconId;
        this.isClickable = false;
        this.width = 64;
        this.height = 64;
        this.onClick = () -> {};
    }

    public IconComponent wh(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public IconComponent clickable() {
        this.isClickable = true;
        return this;
    }

    /**
     * fwef
     * @param onClick
     * @return
     */
    public IconComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        Icons icon;
        try {
            icon = Icons.valueOf(iconId.toUpperCase());
        } catch (IllegalArgumentException e) {
            icon = Icons.EMPTY;
        }

        if (this.isClickable) {
            if (ImGui.imageButton(icon.getGlId(), this.width, this.height)) {
                this.onClick.run();
            }
        } else {
            ImGui.image(icon.getGlId(), this.width, this.height);

            if (ImGui.isItemClicked()) {
                this.onClick.run();
            }
        }
    }
}
