package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.CubeCodeClient;
import imgui.ImGui;

public class ImageComponent extends AbstractComponent {
    String iconPath;
    boolean isClickable;
    Runnable onClick;

    public ImageComponent(String iconPath) {
        this.iconPath = iconPath;
        this.isClickable = false;
        this.width = 64f;
        this.height = 64f;
        this.onClick = () -> {};
    }

    public ImageComponent wh(float width, float height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public ImageComponent clickable() {
        this.isClickable = true;
        return this;
    }

    /**
     * fwef
     * @param onClick
     * @return
     */
    public ImageComponent onClick(Runnable onClick) {
        this.onClick = onClick;
        return this;
    }

    @Override
    public void render() {
        float width = this.rw != null ? this.rw : this.width != null ? this.width : 0;
        float height = this.rh != null ? this.rh : this.height != null ? this.height : 0;

        Integer iconGLId = CubeCodeClient.imageManager.getImage(this.iconPath);

        if (iconGLId != -1) {
            if (this.isClickable) {
                if (ImGui.imageButton(iconGLId, width, height)) {
                    this.onClick.run();
                }
            } else {
                ImGui.image(iconGLId, width, height);

                if (ImGui.isItemClicked()) {
                    this.onClick.run();
                }
            }
        }
    }
}
