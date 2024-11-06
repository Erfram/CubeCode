package com.cubecode.client.scripts.code.ui;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.scripts.code.ui.components.*;
import com.cubecode.client.views.TestView;
import imgui.ImGui;

public class ClientCubeCodeUI {
    private final TestView view;

    public ClientCubeCodeUI() {
        this.view = new TestView();
    }

    public WindowComponent window(String title, Runnable callback) {
        return new WindowComponent(this.view, title, callback);
    }

    public WindowComponent window(String title) {
        return this.window(title, () -> {});
    }

    public TextComponent text(String text) {
        TextComponent component = new TextComponent(text);

        this.view.components.add(component);

        return component;
    }

    public IconComponent icon(String iconId) {
        IconComponent component = new IconComponent(iconId);

        this.view.components.add(component);

        return component;
    }

    public CheckboxComponent checkbox(String id, boolean active) {
        CheckboxComponent component = new CheckboxComponent(this.view, id, active);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}
        
        return component;
    }

    public ButtonComponent button(String label) {
        ButtonComponent component = new ButtonComponent(label);

        this.view.components.add(component);
        
        return component;
    }

    public RadioButtonComponent radioButton(String id, boolean active) {
        RadioButtonComponent component = new RadioButtonComponent(this.view, id, active);

        this.view.components.add(component);

        return component;
    }

    public ArrowButtonComponent arrowButton(String id, int dir) {
        ArrowButtonComponent component = new ArrowButtonComponent(id, dir);

        this.view.components.add(component);

        return component;
    }

    public InputTextComponent inputText(String label, String text, int maxlength) {
        InputTextComponent component = new InputTextComponent(this.view, label, text, maxlength);

        this.view.components.add(component);

        return component;
    }

    public InputTextComponent inputText(String label, String text) {
        return this.inputText(label, text, 99999);
    }

    public InputTextComponent inputText(String label) {
        return this.inputText(label, "", 99999);
    }

    public void separator() {
        ImGui.separator();
    }

    public void sameLine() {
        ImGui.sameLine();
    }

    public void sameLine(float offsetFromStartX) {
        ImGui.sameLine(offsetFromStartX);
    }

    public void sameLine(float offsetFromStartX, float spacing) {
        ImGui.sameLine(offsetFromStartX, spacing);
    }

    public void newLine() {
        ImGui.newLine();
    }

    public void spacing() {
        ImGui.spacing();
    }

    public void indent() {
        ImGui.indent();
    }

    public void indent(float indentW) {
        ImGui.indent(indentW);
    }

    public void unindent() {
        ImGui.unindent();
    }

    public void unindent(float indentW) {
        ImGui.unindent(indentW);
    }

    public boolean isKeyDown(int keyCode) {
        return ImGui.isKeyDown(keyCode);
    }

    public boolean isKeyPressed(int keyCode) {
        return ImGui.isKeyPressed(keyCode);
    }

    public boolean isKeyPressed(int keyCode, boolean repeat) {
        return ImGui.isKeyPressed(keyCode, repeat);
    }

    public boolean isKeyReleased(int keyCode) {
        return ImGui.isKeyReleased(keyCode);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseClicked(int mouse) {
        return ImGui.isMouseClicked(mouse);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseClicked(int mouse, boolean repeat) {
        return ImGui.isMouseClicked(mouse, repeat);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseDown(int mouse) {
        return ImGui.isMouseDown(mouse);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseDragging(int mouse) {
        return ImGui.isMouseDragging(mouse);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseDragging(int mouse, float lockThreshold) {
        return ImGui.isMouseDragging(mouse, lockThreshold);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isMouseReleased(int mouse) {
        return ImGui.isMouseReleased(mouse);
    }

    /**
     *  Left = 0 |
     *  Right = 1 |
     *  Middle = 2 |
     *  COUNT = 5
     */
    public boolean isAnyMouseDown() {
        return ImGui.isAnyMouseDown();
    }

    public ScriptVector getMousePosition() {
        return new ScriptVector(ImGui.getMousePos().x, ImGui.getMousePos().y, 0);
    }

    public Object getVariable(String id) {
        return this.view.getVariable(id);
    }

    public View getView() {
        return view;
    }
}