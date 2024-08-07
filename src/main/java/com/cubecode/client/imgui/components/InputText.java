package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import com.cubecode.utils.CubeCodeException;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.function.Consumer;

public class InputText implements Component {
    private final CommonProperties commonProperties;
    private final String label;
    private final String text;
    private final Consumer<String> callback;
    private final int maxLength;
    private final int flags;
    private final String id;

    public InputText(String label, String text, Consumer<String> callback, int maxLength, int flags, CommonProperties properties, String id) {
        this.label = label;
        this.text = text;
        this.callback = callback;
        this.maxLength = maxLength;
        this.flags = flags;
        this.commonProperties = properties;
        this.id = id;
    }

    public static Builder builder() {
        return new Builder();
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
            ImGui.pushItemWidth(actualWidth);
        }
        //ImGui.pushItemHeight(actualHeight);
        view.putVariable(this.id, new ImString(this.text, this.maxLength));

        ImString variable = view.getVariable(this.id);
        if (ImGui.inputText(label, variable, this.flags)) {
            this.callback.accept(variable.get());
        }

        if (this.commonProperties.width != -1 && this.commonProperties.rw != -1) {
            ImGui.popItemWidth();
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private String label = "##label";
        private String text = "";
        private Consumer<String> callback = (str) -> {};
        private int maxLength = 30;
        private int flags = 0;
        private String id = "default";

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder maxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        public Builder flags(int inputTextFlags) {
            this.flags = inputTextFlags;
            return this;
        }

        public Builder callback(Consumer<String> callback) {
            this.callback = callback;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public InputText build() {
            if (this.maxLength < 0) {
                try {
                    throw new CubeCodeException("maxLength must be greater than 0");
                } catch (CubeCodeException e) {
                    throw new RuntimeException(e);
                }
            }

            return new InputText(this.label, this.text, this.callback, this.maxLength, this.flags, this.commonProperties, this.id);
        }
    }
}