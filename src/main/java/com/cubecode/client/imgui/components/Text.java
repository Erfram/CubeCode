package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.ImGui;

public class Text implements Component {
    private final CommonProperties commonProperties;
    private final String title;
    private final Runnable callback;
    private final String id;

    public static Builder builder() {
        return new Builder();
    }

    public Text(CommonProperties commonProperties, String title, Runnable callback, String id) {
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
            float actualX = (ImGui.getWindowWidth() - ImGui.calcTextSize(this.title).x) * this.commonProperties.rx;
            float actualY = (ImGui.getWindowHeight() - ImGui.calcTextSize(this.title).y) * this.commonProperties.ry;
            ImGui.setCursorPos(actualX, actualY);
        }

        ImGui.pushItemWidth(actualWidth);
        //ImGui.pushItemHeight(actualHeight);

        ImGui.text(this.title);
        this.callback.run();

        ImGui.popItemWidth();
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private String title = "";
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

        public Text build() {
            return new Text(this.commonProperties, this.title, this.callback, this.id);
        }
    }
}