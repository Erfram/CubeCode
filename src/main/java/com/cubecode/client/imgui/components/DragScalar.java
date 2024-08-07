package com.cubecode.client.imgui.components;

import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.imgui.components.basic.AbstractBuilder;
import com.cubecode.client.imgui.components.basic.CommonProperties;
import com.cubecode.client.imgui.components.basic.Component;
import imgui.ImGui;
import imgui.flag.ImGuiDataType;
import imgui.type.ImFloat;
import imgui.type.ImInt;

import java.util.function.Consumer;

public class DragScalar implements Component {
    private final CommonProperties commonProperties;
    private final String label;
    private final Number defaultValue;
    private final int dataType;
    private final Number speed;
    private final Number min;
    private final Number max;
    private String format;
    private final int flags;
    private final Consumer<Number> callback;
    private String id;

    public DragScalar(String label, Number defaultValue, int dataType, Number speed, Number min, Number max, String format, int flags, Consumer<Number> callback, CommonProperties commonProperties, String id) {
        this.label = label;
        this.defaultValue = defaultValue;
        this.dataType = dataType;
        this.speed = speed;
        this.min = min;
        this.max = max;
        this.format = format;
        this.flags = flags;
        this.callback = callback;
        this.commonProperties = commonProperties;
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

        ImGui.pushItemWidth(actualWidth);
        //ImGui.pushItemHeight(actualHeight);
        if (this.dataType == ImGuiDataType.Float || this.dataType == ImGuiDataType.Double) {
            view.putVariable(this.id, new ImFloat(this.defaultValue.floatValue()));

            ImFloat variable = view.getVariable(this.id);
            if (ImGui.dragScalar(
                    this.label,
                    this.dataType,
                    variable,
                    (float) this.speed,
                    (float) this.min,
                    (float) this.max,
                    this.format,
                    this.flags)) {
                this.callback.accept(variable.get());
            }
        } else if (this.dataType == ImGuiDataType.S32) {
            view.putVariable(this.id, new ImInt(this.defaultValue.intValue()));

            ImInt variable = view.getVariable(this.id);
            if (ImGui.dragScalar(
                    this.label,
                    this.dataType,
                    variable,
                    (int) this.speed,
                    (int) this.min,
                    (int) this.max,
                    this.format,
                    this.flags)) {
                this.callback.accept(variable.get());
            }
        }

        ImGui.popItemWidth();
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private String label = "dragScalar";
        private Number defaultValue = 0;
        private int dataType = ImGuiDataType.S32;
        private Number speed = 1;
        private Number min = 0;
        private Number max = 100;
        private String format = "";
        private int flags = 0;
        private Consumer<Number> callback = (scalar) -> {};
        private String id = "default";

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder defaultValue(Number defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder dataType(int dataType) {
            this.dataType = dataType;
            return this;
        }

        public Builder speed(Number speed) {
            this.speed = speed;
            return this;
        }

        public Builder min(Number min) {
            this.min = min;
            return this;
        }

        public Builder max(Number max) {
            this.max = max;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public Builder callback(Consumer<Number> callback) {
            this.callback = callback;
            return this;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public DragScalar build() {
            if (this.dataType == ImGuiDataType.Float || this.dataType == ImGuiDataType.Double) {
                if (this.format.isEmpty()) {
                    this.format = "%.1f";
                }
            }

            if (this.dataType == ImGuiDataType.S32) {
                if (this.format.isEmpty()) {
                    this.format = "%d";
                }
            }

            return new DragScalar(this.label, this.defaultValue, this.dataType, this.speed, this.min, this.max, this.format, this.flags, this.callback, this.commonProperties, this.id);
        }
    }
}
