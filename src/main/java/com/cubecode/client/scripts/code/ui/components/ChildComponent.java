package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.scripts.code.ui.CubeCodeUIBuilder;
import com.cubecode.client.scripts.code.ui.components.draw.DrawComponent;
import com.cubecode.client.views.TestView;
import imgui.ImGui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * child
 */
public class ChildComponent extends AbstractComponent implements CubeCodeUIBuilder {
    TestView view;
    String strId;
    boolean border;
    Consumer<ChildComponent> childConsumer;
    List<AbstractComponent> components;

    public ChildComponent(TestView view, String strId) {
        this.view = view;
        this.strId = strId;
        this.border = false;
        this.childConsumer = (child) -> {};
        this.components = new LinkedList<>();
    }

    /**
     * border
     */
    public ChildComponent border(boolean isBorder) {
        this.border = isBorder;

        return this;
    }

    /**
     * components
     */
    public ChildComponent components(Consumer<ChildComponent> childConsumer) {
        this.childConsumer = childConsumer;

        return this;
    }

    @Override
    public void render() {
        float width = this.rw != null ? ImGui.getWindowWidth() * this.rw : this.width != null ? this.width : 0;
        float height = this.rh != null ? ImGui.getWindowHeight() * this.rh : this.height != null ? this.height : 0;

        if (ImGui.beginChild(this.strId, width, height, this.border)) {
            this.childConsumer.accept(this);
            this.components.forEach(component -> {
                component.pushTheme();
                component.pushSize();
                component.pushPosition();

                component.render();

                component.popPosition();
                component.popSize();
                component.popTheme();
            });
            ImGui.endChild();
        }

        this.components = Collections.unmodifiableList(this.components);
    }

    public TextComponent text(String text) {
        TextComponent component = new TextComponent(text);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public IconComponent icon(String iconId) {
        IconComponent component = new IconComponent(iconId);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public ImageComponent image(String iconPath) {
        ImageComponent component = new ImageComponent(iconPath);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public CheckboxComponent checkbox(String id, boolean active) {
        CheckboxComponent component = new CheckboxComponent(this.view, id, active);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public ButtonComponent button(String label) {
        ButtonComponent component = new ButtonComponent(label);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public RadioButtonComponent radioButton(String id, boolean active) {
        RadioButtonComponent component = new RadioButtonComponent(this.view, id, active);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public ArrowButtonComponent arrowButton(String id, int dir) {
        ArrowButtonComponent component = new ArrowButtonComponent(id, dir);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public InputTextComponent inputText(String label, String text, int maxLength) {
        InputTextComponent component = new InputTextComponent(this.view, label, text, maxLength);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    public InputTextComponent inputText(String label, String text) {
        return this.inputText(label, text, 99999);
    }

    public InputTextComponent inputText(String label) {
        return this.inputText(label, "", 99999);
    }

    @Override
    public SliderComponent slider(String label, float value, float degreesMin, float degreesMax) {
        SliderComponent component = new SliderComponent(this.view, label, value, degreesMin, degreesMax);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public ChildComponent child(String strId) {
        ChildComponent component = new ChildComponent(this.view, strId);

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    @Override
    public DrawComponent draw() {
        DrawComponent component = new DrawComponent();

        try {
            this.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }
}
