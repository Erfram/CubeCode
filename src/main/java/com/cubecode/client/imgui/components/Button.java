package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.ImGui;

public class Button implements Component {
    private final CommonProperties commonProperties;
    private final String title;
    private final Runnable callback;
    private final String id;

    public static Builder builder() {
        return new Builder();
    }

    public Button(CommonProperties commonProperties, String title, Runnable callback, String id) {
        this.commonProperties = commonProperties;
        this.title = title;
        this.callback = callback;
        this.id = id;
    }

    @Override
    public void render(View view) {
        float actualWidth = (this.commonProperties.rw > 0) ? ImGui.getWindowWidth() * this.commonProperties.rw : this.commonProperties.width;
        float actualHeight = (this.commonProperties.rh > 0) ? ImGui.getWindowHeight() * this.commonProperties.rh : this.commonProperties.height;

        if (this.commonProperties.x >= 0 && this.commonProperties.y >= 0) {
            ImGui.setCursorPos(this.commonProperties.x, this.commonProperties.y);
        } else if (this.commonProperties.rx >= 0 && this.commonProperties.ry >= 0) {
            float actualX = (ImGui.getWindowWidth() - this.commonProperties.width) * this.commonProperties.rx;
            float actualY = (ImGui.getWindowHeight() - this.commonProperties.height) * this.commonProperties.ry;
            ImGui.setCursorPos(actualX, actualY);
        }

        if (this.commonProperties.width != -1 && this.commonProperties.rw != -1) {
            if (ImGui.button(this.title, actualWidth, actualHeight)) {
                this.callback.run();
            }
        } else {
            if (ImGui.button(this.title)) {
                this.callback.run();
            }
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private String title = "button";
        private Runnable callback = () -> {};
        private String id = "default";

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder callback(Runnable callback) {
            this.callback = callback;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Button build() {
            return new Button(this.commonProperties, this.title, this.callback, this.id);
        }
    }
}