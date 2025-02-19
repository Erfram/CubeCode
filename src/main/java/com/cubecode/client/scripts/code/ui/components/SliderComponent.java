package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.views.TestView;
import imgui.ImGui;
import imgui.flag.ImGuiSliderFlags;

/**
 * slider
 */
public class SliderComponent extends AbstractComponent {
    TestView view;
    String label;
    float[] values;
    float degreesMin;
    float degreesMax;
    String type;
    String format;
    int flags;

    Runnable onClick;

    String id;

    public SliderComponent(TestView view, String label, float value, float degreesMin, float degreesMax) {
        this.view = view;
        this.label = label;
        this.values = new float[]{value};
        this.degreesMin = degreesMin;
        this.degreesMax = degreesMax;
        this.type = "Angle";
        this.format = "%f";
        this.onClick = () -> {};
        this.flags = 0;
        this.id = label;
    }

    /**
     * value
     */
    public SliderComponent value(float value) {
        this.values = new float[]{value};

        return this;
    }

    /**
     * degreesMin
     */
    public SliderComponent degreesMin(float degreesMin) {
        this.degreesMin = degreesMin;

        return this;
    }

    /**
     * degreesMax
     */
    public SliderComponent degreesMax(float degreesMax) {
        this.degreesMax = degreesMax;

        return this;
    }

    /**
     * type
     */
    public SliderComponent type(String type) {
        this.type = type;

        return this;
    }

    /**
     * typeFloat
     */
    public SliderComponent typeFloat() {
        this.type = "Float";

        return this;
    }

    /**
     * typeAngle
     */
    public SliderComponent typeAngle() {
        this.type = "Angle";

        return this;
    }

    /**
     * typeInt
     */
    public SliderComponent typeInt() {
        this.type = "Int";

        return this;
    }

    /**
     * format
     */
    public SliderComponent format(String format) {
        this.format = format;

        return this;
    }

    /**
     * id
     */
    public SliderComponent id(String id) {
        this.id = id;

        return this;
    }

    /**
     * noInput
     */
    public SliderComponent noInput() {
        this.flags = this.flags | ImGuiSliderFlags.NoInput;

        return this;
    }

    /**
     * alwaysClamp
     */
    public SliderComponent alwaysClamp() {
        this.flags = this.flags | ImGuiSliderFlags.AlwaysClamp;

        return this;
    }

    /**
     * logarithmic
     */
    public SliderComponent logarithmic() {
        this.flags = this.flags | ImGuiSliderFlags.Logarithmic;

        return this;
    }

    /**
     * noRoundToFormat
     */
    public SliderComponent noRoundToFormat() {
        this.flags = this.flags | ImGuiSliderFlags.NoRoundToFormat;

        return this;
    }

    @Override
    public void render() {
        this.view.putVariable(this.id, this.values);

        switch (this.type) {
            case "Angle" -> {
                if (ImGui.sliderAngle(this.label, this.view.getVariable(this.id), this.degreesMin, this.degreesMax, this.format)) {
                    this.onClick.run();
                }
            }
            case "Float" -> {
                if (ImGui.sliderFloat(this.label, this.view.getVariable(this.id), this.degreesMin, this.degreesMax, this.format, this.flags)) {
                    this.onClick.run();
                }
            }
            case "Int" -> {
                if (ImGui.sliderInt(this.label, this.view.getVariable(this.id), Integer.parseInt("" + this.degreesMin), Integer.parseInt("" + this.degreesMax), this.format)) {
                    this.onClick.run();
                }
            }
        }
    }
}
