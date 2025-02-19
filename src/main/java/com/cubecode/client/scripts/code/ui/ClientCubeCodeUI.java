package com.cubecode.client.scripts.code.ui;

import com.cubecode.api.scripts.code.ScriptVector;
import com.cubecode.client.imgui.basic.View;
import com.cubecode.client.scripts.code.ui.components.*;
import com.cubecode.client.scripts.code.ui.components.draw.DrawComponent;
import com.cubecode.client.views.TestView;
import imgui.ImGui;
import imgui.flag.ImGuiDir;
import imgui.flag.ImGuiMouseCursor;
import org.lwjgl.glfw.GLFW;

/**
 * ClientCubeCodeUI is a class that implements the CubeCodeUIBuilder interface, providing a user interface (UI)
 * Object for creating various UI components
 * This class serves as a bridge between the UI components and the underlying view, allowing for the creation and management of windows, buttons, text fields, and other interactive elements.
 * It encapsulates the behavior of UI components and manages their addition to the view, while also
 * providing utility methods for handling input events and layout management.
 *
 * <pre>{@code
 * var ui = CubeCode.createUI()
 *
 * ui.window("", () => {
 *     ui.button("click")
 * }).render()
 *
 * c.player.openUI(ui)
 * }</pre>
 */
public class ClientCubeCodeUI implements CubeCodeUIBuilder {
    private final TestView view;

    public ClientCubeCodeUI() {
        this.view = new TestView();
    }

    /**
     * Creates a new window component with the specified title and callback to be executed when the window is rendered.
     */
    public WindowComponent window(String title, Runnable callback) {
        return new WindowComponent(this.view, title, callback);
    }

    /**
     * Creates a new window component with a specified title.
     */
    public WindowComponent window(String title) {
        return this.window(title, () -> {});
    }

    /**
     * Creates a new text component with the specified text.
     */
    public TextComponent text(String text) {
        TextComponent component = new TextComponent(text);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new icon component with the specified icon ID.
     * Icon ID's:
     * APPEARANCE, THEME, BLOCK, BLOCK_ENTITY, CLIPBOARD, CLIENT,
     * CUBECODE, EMPTY, ENTITY, FLAG, FABRIC, FACTORY,
     * FOLDER, MODULE, INFO, INVENTORY, ITEM, ITEM_STACK
     * LLAMA, MAGMAOUT, MATH, MINUS, NBT_COMPOUND, NBT_LIST
     * PLAYER, PLUS, QUESTION, RAY_TRACE, RESET, SAVE
     * SEARCH, SERVER, STATE, STICK, VECTOR, WORLD
     * START, BOOK, CUT, DELETE, EDIT, COPY
     * PASTE, HAMMER, JS, LUA
     */
    @Override
    public IconComponent icon(String iconId) {
        IconComponent component = new IconComponent(iconId);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new image component with the specified icon path.
     */
    @Override
    public ImageComponent image(String iconPath) {
        ImageComponent component = new ImageComponent(iconPath);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new checkbox component with the specified ID and initial active state.
     */
    @Override
    public CheckboxComponent checkbox(String id, boolean active) {
        CheckboxComponent component = new CheckboxComponent(this.view, id, active);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}
        
        return component;
    }

    /**
     * Creates a new button component with the specified label.
     */
    @Override
    public ButtonComponent button(String label) {
        ButtonComponent component = new ButtonComponent(label);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}
        
        return component;
    }

    /**
     * Creates a new radio button component with the specified ID and initial active state, and adds it to the view.
     */
    @Override
    public RadioButtonComponent radioButton(String id, boolean active) {
        RadioButtonComponent component = new RadioButtonComponent(this.view, id, active);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new arrow button component with the specified ID and direction.
     *
     * Directions:
     * None - -1
     * Left - 0
     * Right - 1
     * Up - 2
     * Down - 3
     */
    @Override
    public ArrowButtonComponent arrowButton(String id, int dir) {

        ArrowButtonComponent component = new ArrowButtonComponent(id, dir);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new input text component with the specified label, initial text, and maximum length.
     */
    @Override
    public InputTextComponent inputText(String label, String text, int maxLength) {
        InputTextComponent component = new InputTextComponent(this.view, label, text, maxLength);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new input text component with the specified label and an empty initial text.
     */
    public InputTextComponent inputText(String label, String text) {
        return this.inputText(label, text, 99999);
    }

    /**
     * Creates a new input text component with the specified label, empty initial text, and maximum length.
     */
    public InputTextComponent inputText(String label) {
        return this.inputText(label, "", 99999);
    }

    /**
     * Creates a new slider component with the specified label, initial value, and range.
     */
    @Override
    public SliderComponent slider(String label, float value, float degreesMin, float degreesMax) {
        SliderComponent component = new SliderComponent(this.view, label, value, degreesMin, degreesMax);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new child component with the specified ID.
     */
    @Override
    public ChildComponent child(String strId) {
        ChildComponent component = new ChildComponent(this.view, strId);

        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Creates a new draw component.
     */
    @Override
    public DrawComponent draw() {
        DrawComponent component = new DrawComponent();
        try {
            this.view.components.add(component);
        } catch (UnsupportedOperationException ignored) {}

        return component;
    }

    /**
     * Inserts a separator.
     */
    public void separator() {
        ImGui.separator();
    }

    /**
     * Moves the cursor to the same line for the next UI element.
     */
    public void sameLine() {
        ImGui.sameLine();
    }

    /**
     * Moves the cursor to the same line with a specified offset from the start X position.
     */
    public void sameLine(float offsetFromStartX) {
        ImGui.sameLine(offsetFromStartX);
    }

    /**
     * Moves the cursor to the same line with a specified offset from the start X position and spacing.
     */
    public void sameLine(float offsetFromStartX, float spacing) {
        ImGui.sameLine(offsetFromStartX, spacing);
    }

    /**
     * Moves the cursor to a new line
     */
    public void newLine() {
        ImGui.newLine();
    }

    /**
     * Adds spacing in the UI layout.
     */
    public void spacing() {
        ImGui.spacing();
    }

    /**
     * Indents the current position in the UI layout.
     */
    public void indent() {
        ImGui.indent();
    }

    /**
     * Indents the current position in the UI layout by a specified width.
     */
    public void indent(float indentW) {
        ImGui.indent(indentW);
    }

    /**
     * Unindents the current position in the UI layout.
     */
    public void unindent() {
        ImGui.unindent();
    }

    /**
     * Unindents the current position in the UI layout by a specified width.
     */
    public void unindent(float indentW) {
        ImGui.unindent(indentW);
    }

    /**
     * Checks if a specific key is currently pressed down.
     */
    public boolean isKeyDown(int keyCode) {
        return ImGui.isKeyDown(keyCode);
    }

    /**
     * Checks if a specific key was pressed during the current frame.
     */
    public boolean isKeyPressed(int keyCode) {
        return ImGui.isKeyPressed(keyCode);
    }

    /**
     * Checks if a specific key was pressed during the current frame, with an option to allow repeated presses.
     */
    public boolean isKeyPressed(int keyCode, boolean repeat) {
        return ImGui.isKeyPressed(keyCode, repeat);
    }

    /**
     * Checks if a specific key was released during the current frame.
     */
    public boolean isKeyReleased(int keyCode) {
        return ImGui.isKeyReleased(keyCode);
    }

    /**
     * Checks if a specific mouse button was clicked during the current frame.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseClicked(int mouse) {
        return ImGui.isMouseClicked(mouse);
    }

    /**
     * Checks if a specific mouse button was clicked during the current frame, with an option to allow repeated clicks.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseClicked(int mouse, boolean repeat) {
        return ImGui.isMouseClicked(mouse, repeat);
    }

    /**
     * Checks if a specific mouse button is currently down.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseDown(int mouse) {
        return ImGui.isMouseDown(mouse);
    }

    /**
     * Checks if a specific mouse button is currently being dragged.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseDragging(int mouse) {
        return ImGui.isMouseDragging(mouse);
    }

    /**
     * Checks if a specific mouse button is currently being dragged, with a specified lock threshold.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseDragging(int mouse, float lockThreshold) {
        return ImGui.isMouseDragging(mouse, lockThreshold);
    }

    /**
     * Checks if a specific mouse button was released during the current frame.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isMouseReleased(int mouse) {
        return ImGui.isMouseReleased(mouse);
    }

    /**
     * Checks if any mouse button is currently down.
     *
     * Left = 0
     * Right = 1
     * Middle = 2
     */
    public boolean isAnyMouseDown() {
        return ImGui.isAnyMouseDown();
    }

    /**
     * Retrieves the current mouse position as a ScriptVector.
     */
    public ScriptVector getMousePosition() {
        return new ScriptVector(ImGui.getMousePos().x, ImGui.getMousePos().y, 0);
    }

    /**
     * None: -1
     * Arrow: 0
     * TextInput: 1
     * ResizeAll: 2
     * ResizeNS: 3
     * ResizeEW: 4
     * ResizeNESW: 5
     * ResizeNWSE: 6
     * Hand: 7
     * NotAllowed: 8
     */
    public void setMouseCursor(int type) {
        ImGui.setMouseCursor(type);
    }

    public Object getVariable(String id) {
        return this.view.getVariable(id);
    }

    public View getView() {
        return view;
    }
}