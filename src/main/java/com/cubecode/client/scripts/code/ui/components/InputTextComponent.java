package com.cubecode.client.scripts.code.ui.components;

import com.cubecode.client.views.TestView;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.function.Consumer;

/**
 * input
 */
public class InputTextComponent extends AbstractComponent {
    TestView view;
    String label;
    String text;
    String id;

    int maxLength;

    Consumer<String> onInput;

    public InputTextComponent(TestView view, String label, String text, int maxLength) {
        this.view = view;
        this.label = label;
        this.text = text;
        this.id = label;
        this.maxLength = maxLength;
        this.onInput = (str) -> {};
    }

    /**
     * onInput
     */
    public InputTextComponent onInput(Consumer<String> callback) {
        this.onInput = callback;
        return this;
    }

    /**
     * text
     */
    public InputTextComponent text(String text) {
        this.text = text;
        return this;
    }

    /**
     * id
     */
    public InputTextComponent id(String id) {
        this.id = id;
        return this;
    }

    /**
     * maxLength
     */
    public InputTextComponent maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public void render() {
        this.view.putVariable(this.id, new ImString(this.text, this.maxLength));

        if (ImGui.inputText(this.label, this.view.getVariable(this.id))) {
            this.onInput.accept(((ImString)this.view.getVariable(this.id)).get());
        }
    }
}
