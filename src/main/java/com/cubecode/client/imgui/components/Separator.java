package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.ImGui;

public class Separator implements Component {
    private final CommonProperties commonProperties;

    public static Builder builder() {
        return new Builder();
    }

    public Separator(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
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

        ImGui.pushItemWidth(actualWidth);
        //ImGui.pushItemHeight(actualHeight);

        ImGui.separator();

        ImGui.popItemWidth();
    }

    public static class Builder extends AbstractBuilder<Builder>  {
        public Separator build() {
            return new Separator(this.commonProperties);
        }
    }
}
