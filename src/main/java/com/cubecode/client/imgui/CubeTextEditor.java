package com.cubecode.client.imgui;

import com.cubecode.utils.Documentation;
import com.cubecode.utils.Vec2i;
import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.extension.texteditor.TextEditor;
import imgui.extension.texteditor.TextEditorLanguageDefinition;
import imgui.flag.ImGuiFocusedFlags;
import imgui.flag.ImGuiMouseButton;
import imgui.flag.ImGuiWindowFlags;

import java.util.Map;

public class CubeTextEditor {
    private final TextEditor textEditor = new TextEditor();
    private final String title;
    private final float startWidth;
    private final float startHeight;

    private float x = 0;
    private float y = 0;
    private float width = 0;
    private float height = 0;

    private int windowFlags = ImGuiWindowFlags.NoMove | ImGuiWindowFlags.HorizontalScrollbar;

    private boolean isBackground = true;
    private int colorBackground = ImGui.getColorU32(0.15f, 0.15f, 0.15f, 1f);

    private boolean isFocused = false;

    private boolean isRenderContextMenu = false;

    public CubeTextEditor(String title) {
        this(title, 0, 0);
    }

    public CubeTextEditor(String title, float startWidth, float startHeight) {
        this.title = title;
        this.startWidth = startWidth;
        this.startHeight = startHeight;

        this.textEditor.setPalette(this.textEditor.getDarkPalette());
        this.textEditor.setColorizerEnable(true);
        this.textEditor.setImGuiChildIgnored(true);
    }

    public void render() {
        this.x = ImGui.getCursorScreenPosX();
        this.y = ImGui.getCursorScreenPosY();
        this.width = ImGui.getWindowWidth() - ImGui.getStyle().getWindowPadding().x;
        this.height = ImGui.getWindowHeight();

        if (ImGui.beginChild(this.title, this.startWidth, this.startHeight, true, windowFlags)) {
            if (this.isBackground) {
                this.renderBackground();
            }

            boolean hadSelectionBeforeRender = this.textEditor.hasSelection();

            this.textEditor.render(this.title);

            ImVec2 screenPos = this.getScreenPos(this.getCursorPosition().y, this.getCursorPosition().x);

            ImGui.getWindowDrawList().addRectFilled(screenPos.x, screenPos.y, screenPos.x + ImGui.calcTextSize("A").x, screenPos.y + ImGui.calcTextSize("A").y, ImGui.getColorU32(1, 1, 1, 0.5f));

            this.handleAutoscrollOnSelectionDrag(hadSelectionBeforeRender);

            this.isFocused = ImGui.isWindowFocused(ImGuiFocusedFlags.ChildWindows);
        }
        ImGui.endChild();

        this.manageMouse();
        this.renderContextMenu();
    }

    private void manageMouse() {
        float mouseX = ImGui.getMousePosX();
        float mouseY = ImGui.getMousePosY();

        boolean clickedOutside = (mouseX >= this.x) &&
                (mouseX <= this.x + this.width) &&
                (mouseY >= this.y) &&
                (mouseY <= this.y + this.height);

        if (this.isFocused) {
            if (ImGui.isMouseReleased(ImGuiMouseButton.Right) && clickedOutside) {
                this.isRenderContextMenu = true;
            }
        }
    }

    private void renderContextMenu() {
        if (!isRenderContextMenu)
            return;

        CubeImGui.popup(this.title+"_context_menu", ImGuiWindowFlags.AlwaysAutoResize,
            () -> {
                ImGui.text("lox");
            },
            () -> {
                this.isRenderContextMenu = false;
            }
        );
    }

    private void handleAutoscrollOnSelectionDrag(boolean hadSelectionBeforeRender) {
        if (this.textEditor.hasSelection()) {
            ImVec2 mousePos = new ImVec2();
            ImGui.getMousePos(mousePos);

            boolean outsideRight = mousePos.x > this.x + ImGui.getScrollX() + this.width;
            boolean outsideLeft = mousePos.x < this.x + ImGui.getScrollX();
            boolean outsideBottom = mousePos.y > this.y + ImGui.getScrollY() + this.height;
            boolean outsideTop = mousePos.y < this.y + ImGui.getScrollY();

            if (hadSelectionBeforeRender && (outsideRight || outsideLeft || outsideBottom || outsideTop)) {
                float scrollX = ImGui.getScrollX();
                float scrollY = ImGui.getScrollY();

                if (outsideRight) {
                    scrollX += 20.0f;
                } else if (outsideLeft) {
                    scrollX -= 20.0f;
                }

                if (outsideBottom) {
                    scrollY += 20.0f;
                } else if (outsideTop) {
                    scrollY -= 20.0f;
                }

                ImGui.setScrollX(scrollX);
                ImGui.setScrollY(scrollY);
            }
        }
    }

    private void renderBackground() {
        ImGui.getWindowDrawList().addRectFilled(
                this.x,
                this.y,
                this.x + this.width,
                this.y + this.height,
                this.colorBackground,
                ImGui.getStyle().getWindowRounding()
        );
    }

    public void setColorBackground(float r, float g, float b, float a) {
        this.colorBackground = ImGui.getColorU32(r, g, b, a);
    }

    public ImVec4 getColorBackground() {
        ImVec4 rgba = new ImVec4();
        ImGui.colorConvertU32ToFloat4(this.colorBackground, rgba);
        return rgba;
    }

    public ImVec2 getScreenPos(int line, int column) {
        StringBuilder tab = new StringBuilder();
        for (int i = 0; i < this.textEditor.getTabSize(); i++) {
            tab.append(" ");
        }
        float widthLeftBar = ImGui.calcTextSize(""+this.getTextLines().length).x +
                ImGui.getFontSize() +
                ImGui.getStyle().getItemSpacingX() * 2 - ImGui.getScrollX();
        float x = this.x +
                widthLeftBar +
                ImGui.calcTextSize(this.getCurrentLineText().replaceAll("\n", tab.toString()).substring(0, column)).x;
        float y = this.y + ImGui.getStyle().getWindowPaddingY() + ImGui.calcTextSize("A").y * line - ImGui.getScrollY();

        return new ImVec2(x, y);
    }

    public Vec2i getCursorPosition() {
        return new Vec2i(
            this.textEditor.getCursorPositionColumn(),
            this.textEditor.getCursorPositionLine()
        );
    }

    public void setText(String text) {
        this.textEditor.setText(text);
    }

    public String getText() {
        return this.textEditor.getText();
    }

    public String getSelectedText() {
        return this.textEditor.getSelectedText();
    }

    public void setLanguageDefinition(TextEditorLanguageDefinition definition) {
        this.textEditor.setLanguageDefinition(definition);
    }

    public int[] getPalette() {
        return this.textEditor.getPalette();
    }

    public void setPalette(int[] palette) {
        this.textEditor.setPalette(palette);
    }

    public void setErrorMarkers(Map<Integer, String> errorMarkers) {
        this.textEditor.setErrorMarkers(errorMarkers);
    }

    public void setBreakpoints(int[] breakpoints) {
        this.textEditor.setBreakpoints(breakpoints);
    }

    public void setBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }

    public boolean isBackground() {
        return this.isBackground;
    }

    public void setTextLines(String[] lines) {
        this.textEditor.setTextLines(lines);
    }

    public String[] getTextLines() {
        return this.textEditor.getTextLines();
    }

    public String getCurrentLineText() {
        return this.textEditor.getCurrentLineText();
    }

    public int getTotalLines() {
        return textEditor.getTotalLines();
    }

    public boolean isOverwrite() {
        return this.textEditor.isOverwrite();
    }

    public void setReadOnly(boolean readOnly) {
        this.textEditor.setReadOnly(readOnly);
    }

    public boolean isReadOnly() {
        return this.textEditor.isReadOnly();
    }

    public boolean isTextChanged() {
        return this.textEditor.isTextChanged();
    }

    public boolean isCursorPositionChanged() {
        return this.textEditor.isCursorPositionChanged();
    }

    public boolean isColorizerEnabled() {
        return this.textEditor.isColorizerEnabled();
    }

    public void setColorizerEnable(boolean colorizer) {
        this.textEditor.setColorizerEnable(colorizer);
    }

    public void setCursorPosition(int line, int column) {
        this.textEditor.setCursorPosition(line, column);
    }

    public void setHandleMouseInputs(boolean handleMouseInputs) {
        this.textEditor.setHandleMouseInputs(handleMouseInputs);
    }

    public boolean isHandleMouseInputsEnabled() {
        return this.textEditor.isHandleMouseInputsEnabled();
    }

    public void setHandleKeyboardInputs(boolean handleKeyboardInputs) {
        this.textEditor.setHandleMouseInputs(handleKeyboardInputs);
    }

    public boolean isHandleKeyboardInputsEnabled() {
        return this.textEditor.isHandleKeyboardInputsEnabled();
    }

    public void setImGuiChildIgnored(boolean imGuiChildIgnored) {
        this.textEditor.setImGuiChildIgnored(imGuiChildIgnored);
    }

    public boolean isImGuiChildIgnored() {
        return this.textEditor.isImGuiChildIgnored();
    }

    public void setShowWhitespaces(boolean showWhitespaces) {
        this.textEditor.setShowWhitespaces(showWhitespaces);
    }

    public boolean isShowingWhitespaces() {
        return this.textEditor.isShowingWhitespaces();
    }

    public void setTabSize(int tabSize) {
        this.textEditor.setTabSize(tabSize);
    }

    public int getTabSize() {
        return this.textEditor.getTabSize();
    }

    public void insertText(String text) {
        this.textEditor.insertText(text);
    }

    public void moveUp(int amount, boolean select) {
        this.textEditor.moveUp(amount, select);
    }

    public void moveDown(int amount, boolean select) {
        this.textEditor.moveDown(amount, select);
    }

    public void moveLeft(int amount, boolean select, boolean wordMode) {
        this.textEditor.moveLeft(amount, select, wordMode);
    }

    public void moveRight(int amount, boolean select, boolean wordMode) {
        this.textEditor.moveRight(amount, select, wordMode);
    }

    public void moveTop(boolean select) {
        this.textEditor.moveTop(select);
    }

    public void moveBottom(boolean select) {
        this.textEditor.moveBottom(select);
    }

    public void moveHome(boolean select) {
        this.textEditor.moveHome(select);
    }

    public void moveEnd(boolean select) {
        this.textEditor.moveEnd(select);
    }

    public void setSelectionStart(int line, int column) {
        this.textEditor.setSelectionStart(line, column);
    }

    public void setSelectionEnd(int line, int column) {
        this.textEditor.setSelectionEnd(line, column);
    }

    public void setSelection(int lineStart, int columnStart, int lineEnd, int columnEnd, int selectionMode) {
        this.textEditor.setSelection(lineStart, columnStart, lineEnd, columnEnd, selectionMode);
    }

    public void selectWordUnderCursor() {
        this.textEditor.selectWordUnderCursor();
    }

    public void selectAll() {
        this.textEditor.selectAll();
    }

    public boolean hasSelection() {
        return this.textEditor.hasSelection();
    }

    public void copy() {
        this.textEditor.copy();
    }

    public void cut() {
        this.textEditor.cut();
    }

    public void paste() {
        this.textEditor.paste();
    }

    public void delete() {
        this.textEditor.delete();
    }

    public boolean canUndo() {
        return this.textEditor.canUndo();
    }

    public boolean canRedo() {
        return this.textEditor.canRedo();
    }

    public void undo(int steps) {
        this.textEditor.undo(steps);
    }

    public void redo(int steps) {
        this.textEditor.redo(steps);
    }

    public int[] getDarkPalette() {
        return this.textEditor.getDarkPalette();
    }

    public int[] getLightPalette() {
        return this.textEditor.getLightPalette();
    }

    public int[] getRetroBluePalette() {
        return this.textEditor.getRetroBluePalette();
    }
}
